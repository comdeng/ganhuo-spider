package com.huimang.ganhuo.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/19 8:11
 */
public class Mysql {
    private static Connection connection;
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            return connection = DriverManager.getConnection(Const.getProperty("url"), Const.getProperty("user"), Const.getProperty("pass"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
