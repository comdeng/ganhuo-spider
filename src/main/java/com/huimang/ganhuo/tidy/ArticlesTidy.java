package com.huimang.ganhuo.tidy;

import com.github.kevinsawicki.http.HttpRequest;
import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.util.Const;
import com.huimang.ganhuo.util.Md5;
import com.huimang.ganhuo.util.Mysql;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.CRC32;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 12:04
 */
public class ArticlesTidy extends BaseTidy {
    private String urlExt = "/cn/articles/";
    private int pageSize = 12;
    private int artiType = ArticleEntity.TYPE_ARTICLE;
    private String selector = ".news_type1,.news_type2";

    @Override
    public String getUrlExt() {
        return this.urlExt;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getArtiType() {
        return this.artiType;
    }

    @Override
    public String getSelector() {
        return this.selector;
    }
}
