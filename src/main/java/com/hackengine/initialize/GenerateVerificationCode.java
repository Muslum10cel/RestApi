/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.initialize;

import com.hackengine.muslumyusuf.DBOperations;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muslumoncel
 */
public class GenerateVerificationCode {

    public static String getVerificationCode() {
        Random rand = new Random();
        String temp = String.valueOf(rand.nextInt(9999)), hash;
        char ch1, ch2, ch3, ch4;
        int cha1, cha2, cha3, cha4;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(temp.getBytes("UTF-8"));
            byte[] digest = md.digest();
            hash = String.format("%064x", new java.math.BigInteger(1, digest));
            cha1 = rand.nextInt(hash.length());
            ch1 = hash.charAt(cha1);
            cha2 = rand.nextInt(hash.length());
            ch2 = hash.charAt(cha2);
            cha3 = rand.nextInt(hash.length());
            ch3 = hash.charAt(cha3);
            cha4 = rand.nextInt(hash.length());
            ch4 = hash.charAt(cha4);
            return Character.toString(ch1) + Character.toString(ch2) + Character.toString(ch3) + Character.toString(ch4);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
