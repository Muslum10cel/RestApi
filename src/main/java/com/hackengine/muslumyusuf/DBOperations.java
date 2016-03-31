/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.muslumyusuf;

import com.hackengine.initialize.GenerateVerificationCode;
import com.hackengine.initialize.Configuration;
import com.hackengine.models.Baby;
import com.hackengine.models.Comment;
import com.hackengine.models.Image;
import com.hackengine.models.User;
import com.hackengine.models.Vaccine;
import com.hackengine.models.VaccineDateResponse;
import com.hackengine.models.VaccineStatusResponse;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author muslumoncel
 * @version 1.0
 */
public class DBOperations {

    private static final MysqlDataSource dataSource = new MysqlDataSource();

    static {
        dataSource.setURL(Configuration.getDB_URL());
        dataSource.setUser(Configuration.getUSERNAME());
        dataSource.setPassword(Configuration.getPASSWORD());
    }

    //private static final List<Vaccine> vaccines = new ArrayList<>();
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static CallableStatement callableStatement = null;
    private static ResultSet resultSet = null;

    private final Integer[] HEPATIT_B_DATES = {0, 30, 180};
    private final Integer[] BCG_DATES = {60};
    private final Integer[] DaBT_IPA_HIB_DATES = {60, 120, 180, 540};
    private final Integer[] OPA_DATES = {180, 540};
    private final Integer[] KPA_DATES = {60, 120, 180, 360};
    private final Integer[] KKK_DATES = {360};
    private final Integer[] VARICELLA_DATES = {360};
    private final Integer[] HEPATIT_A_DATES = {540, 720};
    private final Integer[] RVA_DATES = {60, 120, 180};
    private final SendMail sendMail = new SendMail();

    /**
     * Establish connection to database
     */
    private static void establishConnection() {
        try {
            connection = (Connection) dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns an integer value. This method returns when user wants to
     * register. Username, full name and password must be specified.
     *
     * @param registerUser
     * @return Registration is successful or failed
     */
    public synchronized int register(User registerUser) {
        int userAvailable = 0, registered = -1;
        try {
            establishConnection();
            preparedStatement = connection.prepareStatement(DbFunctions.CHECK_USER_FUNCTION);
            preparedStatement.setString(1, registerUser.getUsername());
            resultSet = preparedStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    userAvailable = resultSet.getInt(1);
                }
            }
            //!Objects.equals(userAvailable,1)
            if (!Objects.equals(userAvailable, 1)) {
                preparedStatement = connection.prepareStatement(DbFunctions.REGISTER);
                preparedStatement.setString(1, registerUser.getUsername());
                preparedStatement.setString(2, passToHash(registerUser.getPassword()));
                preparedStatement.setString(3, registerUser.getFullname());
                preparedStatement.setInt(4, Flags.USER_FLAG);
                preparedStatement.setString(5, registerUser.getEmail());
                preparedStatement.executeQuery();
                registered = 2;
            } else {
                return userAvailable;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return registered;
    }

    /**
     * Returns an integer value. This method returns when user log-in to system.
     * Username and password must be specified.
     *
     * @param logUser
     * @return Log-in successful or failed
     */
    public synchronized byte logIn(User logUser) {
        int userAvailable = 0;
        try {
            establishConnection();
            preparedStatement = connection.prepareStatement(DbFunctions.CHECK_USER_FUNCTION);
            preparedStatement.setString(1, logUser.getUsername());
            resultSet = preparedStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    userAvailable = resultSet.getInt(1);
                }
            }
            if (Objects.equals(userAvailable, 1)) {
                preparedStatement = connection.prepareStatement(DbFunctions.LOG_IN);
                preparedStatement.setString(1, logUser.getUsername());
                preparedStatement.setString(2, logUser.getPassword());
                resultSet = preparedStatement.executeQuery();
                if (!Objects.equals(resultSet, null)) {
                    while (resultSet.next()) {
                        return resultSet.getByte(1);
                    }
                }
            } else {
                return -2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Returns an integer value.
     *
     * @param baby
     * @return 1 for successfully
     *
     */
    public synchronized int addBaby(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.ADD_BABY);
            callableStatement.setString(1, baby.getUsername());
            callableStatement.setString(2, baby.getBaby_name());
            callableStatement.setString(3, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = connection.prepareCall(DbStoredProcedures.ADD_VACCINES);
            callableStatement.setString(1, calculateBcg(baby.getDate_of_birth()));
            callableStatement.setString(2, calculateVaricella(baby.getDate_of_birth()));
            callableStatement.executeUpdate();

            callableStatement = calculateDaBT_IPA_HIB(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateHepatit_A(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateHepatit_B(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateKKK(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateKPA(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateOPA(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = calculateRVA(connection, baby.getDate_of_birth());
            callableStatement.executeUpdate();

            callableStatement = connection.prepareCall(DbStoredProcedures.SET_FALSE_ALL_VACCINES_STATUS);
            return callableStatement.executeUpdate();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates DABT IPA HIB vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_DaBT_IPA_HIB(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_DaBT_IPA_HIB);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                case 3:
                    callableStatement.setInt(2, Flags.THREE_FLAG);
                    break;
                case 4:
                    callableStatement.setInt(2, Flags.FOUR_FLAG);
                    break;
                case 5:
                    callableStatement.setInt(2, Flags.FIVE_FLAG);
                    break;
                case 6:
                    callableStatement.setInt(2, Flags.SIX_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }

        return -1;
    }

    /**
     * Updates HEPATITIS A vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_Hepatit_A(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_HEPATIT_A);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates HEPATITIS B vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_Hepatit_B(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_HEPATIT_B);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                case 3:
                    callableStatement.setInt(2, Flags.THREE_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates KKK vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_KKK(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_KKK);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates KPA vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_KPA(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_KPA);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                case 3:
                    callableStatement.setInt(2, Flags.THREE_FLAG);
                    break;
                case 4:
                    callableStatement.setInt(2, Flags.FOUR_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates OPA vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_OPA(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_OPA);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates RVA vaccine of baby
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_RVA(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_RVA);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                case 3:
                    callableStatement.setInt(2, Flags.THREE_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Updates baby vaccine such as BCG, VARICELLA
     *
     * @param baby
     * @return 1 updated, 0 not updated, -2 flag is not correct, -1 if catches
     * SQLException
     */
    public synchronized int update_Vaccines(Baby baby) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_VACCINES);
            callableStatement.setInt(1, baby.getBabyID());
            switch (baby.getVaccineFlag()) {
                case 1:
                    callableStatement.setInt(2, Flags.ONE_FLAG);
                    break;
                case 2:
                    callableStatement.setInt(2, Flags.TWO_FLAG);
                    break;
                case 3:
                    callableStatement.setInt(2, Flags.THREE_FLAG);
                    break;
                case 4:
                    callableStatement.setInt(2, Flags.FOUR_FLAG);
                    break;
                case 5:
                    callableStatement.setInt(2, Flags.FIVE_FLAG);
                    break;
                case 6:
                    callableStatement.setInt(2, Flags.SIX_FLAG);
                    break;
                default:
                    return -2;
            }
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Adds comment written by user
     *
     * @param username of wrote comment
     * @param vaccine_name commented vaccine name
     * @param comment written comment
     * @return 1 updated, 0 not updated, -1 if catches SQLException
     */
    public synchronized int addComment(String username, String vaccine_name, String comment) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.ADD_COMMENT);
            callableStatement.setString(1, username.trim());
            callableStatement.setString(2, vaccine_name.trim());
            callableStatement.setString(3, comment);
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger((DBOperations.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Gets vaccine completion details of desired baby
     *
     * @param baby_id of desired baby
     * @return an object includes completion details of baby or null if catches
     * SQLException or JSONException
     */
    public synchronized List<VaccineStatusResponse> completedAndIncompletedVaccines(int baby_id) {
        List<VaccineStatusResponse> statusResponses = new ArrayList<>();
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.GET_COMPLETED_VACCINES);
            callableStatement.setInt(1, baby_id);
            resultSet = callableStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    statusResponses.add(new VaccineStatusResponse(Tags.BCG, resultSet.getInt(1)));
                    statusResponses.add(new VaccineStatusResponse(Tags.DaBT_IPA, resultSet.getInt(2)));
                    statusResponses.add(new VaccineStatusResponse(Tags.VARICELLA, resultSet.getInt(3)));
                    statusResponses.add(new VaccineStatusResponse(Tags.KMA4, resultSet.getInt(4)));
                    statusResponses.add(new VaccineStatusResponse(Tags.HPA, resultSet.getInt(5)));
                    statusResponses.add(new VaccineStatusResponse(Tags.INFLUENZA, resultSet.getInt(6)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_RVA, resultSet.getInt(7)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_RVA, resultSet.getInt(8)));
                    statusResponses.add(new VaccineStatusResponse(Tags.THIRD_RVA, resultSet.getInt(9)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_OPA, resultSet.getInt(10)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_OPA, resultSet.getInt(11)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_HEPATIT_A, resultSet.getInt(12)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_HEPATIT_A, resultSet.getInt(13)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_HEPATIT_B, resultSet.getInt(14)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_HEPATIT_B, resultSet.getInt(15)));
                    statusResponses.add(new VaccineStatusResponse(Tags.THIRD_HEPATIT_B, resultSet.getInt(16)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_KKK, resultSet.getInt(17)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_KKK, resultSet.getInt(18)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_KPA, resultSet.getInt(19)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_KPA, resultSet.getInt(20)));
                    statusResponses.add(new VaccineStatusResponse(Tags.THIRD_KPA, resultSet.getInt(21)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FOURTH_KPA, resultSet.getInt(22)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIRST_DaBT_IPA_HIB, resultSet.getInt(23)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SECOND_DaBT_IPA_HIB, resultSet.getInt(24)));
                    statusResponses.add(new VaccineStatusResponse(Tags.THIRD_DaBT_IPA_HIB, resultSet.getInt(25)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FOURTH_DaBT_IPA_HIB, resultSet.getInt(26)));
                    statusResponses.add(new VaccineStatusResponse(Tags.FIFTH_DaBT_IPA_HIB, resultSet.getInt(27)));
                    statusResponses.add(new VaccineStatusResponse(Tags.SIXTH_DaBT_IPA_HIB, resultSet.getInt(28)));
                }
                return statusResponses;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return null;
    }

    /**
     * Updates password of user
     *
     * @param user
     * @return 1 updated, 0 not updated, -1 if catches SQLException, -2 if user
     * not available
     */
    public synchronized int forgottenPassword(User user) {
        try {
            int userAvailable = -2;
            establishConnection();
            preparedStatement = connection.prepareStatement(DbFunctions.CHECK_USER_FUNCTION);
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    userAvailable = resultSet.getInt(1);
                }
            }
            if (Objects.equals(userAvailable, 1)) {
                callableStatement = connection.prepareCall(DbStoredProcedures.FORGOTTEN_PASSWORD);
                callableStatement.setString(1, user.getUsername());
                callableStatement.setString(2, passToHash(user.getPassword()));
                return callableStatement.executeUpdate();
            } else {
                return userAvailable;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Gets comments written by users
     *
     * @param vaccine
     * @return an object includes comments or null if catches SQLException or
     * JSONException
     */
    public synchronized List<Comment> getComments(Vaccine vaccine) {
        List<Comment> comments = new ArrayList<>();
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.GET_COMMENTS);
            callableStatement.setString(1, vaccine.getVaccine_name());
            callableStatement.setInt(2, vaccine.getBegin());
            callableStatement.setInt(3, vaccine.getLast());
            resultSet = callableStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    comments.add(new Comment(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4)));
                }
            }
            return comments;
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return null;
    }

    /**
     * Gets babies of logged in user
     *
     * @param user
     * @return an object includes baby names of null if catches SQLException or
     * JSONException
     */
    public synchronized List<Baby> getBabies(User user) {
        List<Baby> babies = new ArrayList<>();
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.GET_BABIES);
            callableStatement.setString(1, user.getUsername());
            resultSet = callableStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    babies.add(new Baby(resultSet.getInt(1), resultSet.getString(2)));
                }
            }
            return babies;
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return null;
    }

    /**
     * Closes necessary objects
     */
    private void closeEverything() {
        try {
            if (!Objects.equals(connection, null)) {
                connection.close();
            }
            if (!Objects.equals(resultSet, null)) {
                resultSet.close();
            }
            if (!Objects.equals(preparedStatement, null)) {
                preparedStatement.close();
            }
            if (!Objects.equals(callableStatement, null)) {
                callableStatement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns hashed string
     *
     * @param password user's password
     * @return SHA-512 encrypted password or null if catches
     * NoSuchAlgorithmException or UnsupportedEncodingException
     */
    private String passToHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates VARICELLA vaccines dates
     *
     * @param date_of_birth of baby
     * @return date of VARICELLA
     * @throws ParseException
     */
    private String calculateVaricella(String dateTemp) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(dateTemp)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, VARICELLA_DATES[0]);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Calculates BCG vaccines dates
     *
     * @param date_of_birth of baby
     * @return date of BCG
     * @throws ParseException
     */
    private String calculateBcg(String dateTemp) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(dateTemp)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, BCG_DATES[0]);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Calculates DABT IPA HIB vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateDaBT_IPA_HIB(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_DaBT_IPA_HIB);
            for (int i = 0; i < DaBT_IPA_HIB_DATES.length; i++) {
                calendar.add(Calendar.DATE, DaBT_IPA_HIB_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates HEPATITIS A vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateHepatit_A(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_HEPATIT_A_VACCINES);
            for (int i = 0; i < HEPATIT_A_DATES.length; i++) {
                calendar.add(Calendar.DATE, HEPATIT_A_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates HEPATITIS B vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateHepatit_B(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_HEPATIT_B_VACCINES);
            for (int i = 0; i < HEPATIT_B_DATES.length; i++) {
                calendar.add(Calendar.DATE, HEPATIT_B_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates KKK vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateKKK(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_KKK_VACCINES);
            for (int i = 0; i < KKK_DATES.length; i++) {
                calendar.add(Calendar.DATE, KKK_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates KPA vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateKPA(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_KPA_VACCINES);
            for (int i = 0; i < KPA_DATES.length; i++) {
                calendar.add(Calendar.DATE, KPA_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates OPA vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateOPA(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_OPA_VACCINES);
            for (int i = 0; i < OPA_DATES.length; i++) {
                calendar.add(Calendar.DATE, OPA_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Calculates RVA vaccines dates and create a callable statement
     *
     * @param connection current connection
     * @param date_of_birth of baby
     * @return a callableStatement included necessary informations or null if
     * catches SQLException or ParseException
     */
    private CallableStatement calculateRVA(Connection connection, String date_of_birth) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateFormat.format(dateFormat.parse(date_of_birth)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CallableStatement tempCall = connection.prepareCall(DbStoredProcedures.ADD_RVA_VACCINES);
            for (int i = 0; i < RVA_DATES.length; i++) {
                calendar.add(Calendar.DATE, RVA_DATES[i]);
                tempCall.setString(i + 1, dateFormat.format(calendar.getTime()));
                calendar.setTime(date);
            }
            return tempCall;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Gets all vaccine names recorded to the database
     *
     * @return an object added vaccine names, null if catches SQLException or
     * JSONException
     */
    public List<Vaccine> getAllVaccineNames() {
        final List<Vaccine> vaccines = new ArrayList<>();
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.GET_ALL_VACCINE_NAMES);
            resultSet = callableStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    vaccines.add(new Vaccine(resultSet.getString(1)));
                }
            }
            return vaccines;
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return null;
    }

    /**
     * Gets vaccine details of desired baby
     *
     * @param baby_id of desired baby for vaccination details
     * @return an object added baby's vaccine details, null if catches
     * SQLException or JSONException
     */
    public List<VaccineDateResponse> getVaccinesDateDetailsOfBaby(int baby_id) {
        final List<VaccineDateResponse> dateResponses = new ArrayList<>();
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.GET_BABY_VACCINES);
            callableStatement.setInt(1, baby_id);
            resultSet = callableStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {

                    dateResponses.add(new VaccineDateResponse(Tags.BCG, resultSet.getString(2)));
                    dateResponses.add(new VaccineDateResponse(Tags.VARICELLA, resultSet.getString(3)));
                    dateResponses.add(new VaccineDateResponse(Tags.HPA, resultSet.getString(4)));
                    dateResponses.add(new VaccineDateResponse(Tags.KMA4, resultSet.getString(5)));
                    dateResponses.add(new VaccineDateResponse(Tags.DaBT_IPA, resultSet.getString(6)));
                    dateResponses.add(new VaccineDateResponse(Tags.INFLUENZA, resultSet.getString(7)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_DaBT_IPA_HIB, resultSet.getString(8)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_DaBT_IPA_HIB, resultSet.getString(9)));
                    dateResponses.add(new VaccineDateResponse(Tags.THIRD_DaBT_IPA_HIB, resultSet.getString(10)));
                    dateResponses.add(new VaccineDateResponse(Tags.FOURTH_DaBT_IPA_HIB, resultSet.getString(11)));
                    dateResponses.add(new VaccineDateResponse(Tags.FIFTH_DaBT_IPA_HIB, resultSet.getString(12)));
                    dateResponses.add(new VaccineDateResponse(Tags.SIXTH_DaBT_IPA_HIB, resultSet.getString(13)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_HEPATIT_B, resultSet.getString(14)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_HEPATIT_B, resultSet.getString(15)));
                    dateResponses.add(new VaccineDateResponse(Tags.THIRD_HEPATIT_B, resultSet.getString(16)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_KPA, resultSet.getString(17)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_KPA, resultSet.getString(18)));
                    dateResponses.add(new VaccineDateResponse(Tags.THIRD_KPA, resultSet.getString(19)));
                    dateResponses.add(new VaccineDateResponse(Tags.FOURTH_KPA, resultSet.getString(20)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_KKK, resultSet.getString(21)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_KKK, resultSet.getString(22)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_RVA, resultSet.getString(23)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_RVA, resultSet.getString(24)));
                    dateResponses.add(new VaccineDateResponse(Tags.THIRD_RVA, resultSet.getString(25)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_HEPATIT_A, resultSet.getString(26)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_HEPATIT_A, resultSet.getString(27)));

                    dateResponses.add(new VaccineDateResponse(Tags.FIRST_OPA, resultSet.getString(28)));
                    dateResponses.add(new VaccineDateResponse(Tags.SECOND_OPA, resultSet.getString(29)));
                }
                return dateResponses;
            }
        } catch (SQLException e) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeEverything();
        }
        return null;
    }

    /**
     * If user forgets his/her password this method will sent a verification
     * code to the user's e-mail for changing password
     *
     * @param user
     * @return 10 sent successfully, -2 if catches MessagingException
     */
    public synchronized int sendMailToUser(User user) {
        String verificationCode = GenerateVerificationCode.getVerificationCode();
        if (!Objects.equals(updateVerificationCodeInDB(user.getEmail(), verificationCode), 0)) {
            return sendMail.sendMailTo(user.getEmail(), verificationCode);
        }
        return -2;
    }

    /**
     * After generating verification code with this method code will be inserted
     * generated code to related database column of user
     *
     * @param e_mail E-mail address of user
     * @param code Generated code
     * @return 1 updated, 0 not updated, -1 if catches SQLException
     */
    private int updateVerificationCodeInDB(String e_mail, String code) {
        try {
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_VERIFICATION_CODE);
            callableStatement.setString(1, e_mail);
            callableStatement.setString(2, code);
            return callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeEverything();
        }
        return -1;
    }

    /**
     * Checks verification code is valid or not sent by system to user's e-mail
     * address
     *
     * @param user
     * @return 1 validated code, 0 not validated code, -2 if catches
     * SQLException
     */
    public synchronized int checkVerificationCode(User user) {
        try {
            establishConnection();
            preparedStatement = connection.prepareStatement(DbFunctions.VALIDATE_VERIFICATION_CODE);
            preparedStatement.setString(1, String.valueOf(user.getVerificationCode()));
            preparedStatement.setString(2, user.getEmail());
            resultSet = preparedStatement.executeQuery();
            if (!Objects.equals(resultSet, null)) {
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeEverything();
        }
        return -2;
    }

    /**
     *
     * Upload image to ftp and insert it's URL to database
     *
     * @param image
     * @return
     */
    public synchronized int uploadImage(Image image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(image.getBase64());
            if (!uploadToFTP(image.getUsername(), image.getFilename(), new ByteArrayInputStream(imageBytes))) {
                return -2;
            }
            establishConnection();
            callableStatement = connection.prepareCall(DbStoredProcedures.UPDATE_IMAGE);
            callableStatement.setString(1, Configuration.imageFTPPath() + image.getUsername() + Tags.IMAGE_PREFIX + image.getFilename());
            callableStatement.setString(2, image.getUsername());
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeEverything();
        }
        return -1;
    }

    private boolean uploadToFTP(String username, String fileName, InputStream inputStream) {
        FTPClient client = new FTPClient();
        try {
            client.connect(Configuration.FTPClient());
            if (client.login(Configuration.FTPUsername(), Configuration.getPASSWORD())) {
                client.setDefaultTimeout(10000);
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (client.storeFile(Tags.SITE + username + "-" + fileName, inputStream)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (client.isConnected()) {
                try {
                    client.logout();
                    client.disconnect();
                } catch (IOException ex) {
                    Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
}
