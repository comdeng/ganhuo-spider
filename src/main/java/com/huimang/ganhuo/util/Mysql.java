package com.huimang.ganhuo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/19 8:11
 */
public class Mysql {
    public static Connection getConnection() {
        String url = "jdbc:mysql://192.168.56.101:3306/ganhuo?useUnicode=true&amp;characterEncoding=UTF-8";
        try {
            Connection conn = DriverManager.getConnection(url, "root", "huimang");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
