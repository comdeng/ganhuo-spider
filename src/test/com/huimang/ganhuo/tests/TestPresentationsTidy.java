package com.huimang.ganhuo.tests;

import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.tidy.PresentationsTidy;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:49
 */
public class TestPresentationsTidy {
    public static void main(String[] args) {
        PresentationsTidy tidy = new PresentationsTidy();
        try {
            tidy.loadLists(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
