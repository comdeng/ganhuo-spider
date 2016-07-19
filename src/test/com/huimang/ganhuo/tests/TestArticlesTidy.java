package com.huimang.ganhuo.tests;

import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.tidy.ArticlesTidy;
import com.huimang.ganhuo.tidy.NewsTidy;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:49
 */
public class TestArticlesTidy {
    public static void main(String[] args) {
        ArticlesTidy tidy = new ArticlesTidy();
        try {
            tidy.loadLists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
