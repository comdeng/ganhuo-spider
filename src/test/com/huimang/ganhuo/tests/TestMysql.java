package com.huimang.ganhuo.tests;

import com.huimang.ganhuo.util.Mysql;

import java.sql.Connection;

/**
 * Created by ronnie on 2016/7/20.
 */
public class TestMysql {
    public static void main(String[] args) {
        Connection conn = Mysql.getConnection();
    }
}
