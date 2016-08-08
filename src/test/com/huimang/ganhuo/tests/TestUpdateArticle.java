package com.huimang.ganhuo.tests;

import com.huimang.ganhuo.tidy.ArticleTidy;

import java.sql.SQLException;

/**
 * Created by ronnie on 2016/8/8.
 */
public class TestUpdateArticle {

    public static void main(String[] args) {
        String url = "/cn/articles/micro-service-and-devops";
        try {
            ArticleTidy.updateArticle(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
