/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.initialize;

import java.util.Properties;

/**
 *
 * @author muslumoncel
 */
public class InitProperties {

    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "mail.hackengine-er.com");
        properties.put("mail.smtp.port", "8889");
        return properties;
    }

    public static String getUSERNAME() {
        return "postmaster@hackengine-er.com";
    }

    public static String getPASS() {
        return "Muslum_10cel";
    }
}
