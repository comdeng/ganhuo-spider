package com.huimang.ganhuo.tests;

import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.tidy.ArticleTidy;

import java.io.File;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 10:54
 */
public class TestArticleTidy {
    public static void main(String[] args) {
        String url = "/cn/articles/let-react-components-become-responsive";
        ArticleEntity article = ArticleTidy.loadArticle(url);
        System.out.print(article.getContent());
    }
}
