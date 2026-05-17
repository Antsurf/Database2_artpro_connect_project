package com.project.artconnect.config;

/**
 * Database configuration constants.
 */
public class DatabaseConfig {
    public static  String URL = "jdbc:mysql://127.0.0.1:3306/ARTPROJECT";
    public static  String USER = "project";
    public static  String PASSWORD = "projectPW";

    public static void setURL(String URL) {
        DatabaseConfig.URL = URL;
    }

    public static void setUSER(String USER) {
        DatabaseConfig.USER = USER;
    }

    public static void setPASSWORD(String PASSWORD) {
        DatabaseConfig.PASSWORD = PASSWORD;
    }

    public static String getURL() {
        return URL;
    }

    public static String getUSER() {
        return USER;
    }

}
