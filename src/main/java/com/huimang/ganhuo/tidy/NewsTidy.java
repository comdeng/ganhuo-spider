package com.huimang.ganhuo.tidy;

import com.github.kevinsawicki.http.HttpRequest;
import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.util.Const;
import com.huimang.ganhuo.util.Md5;
import com.huimang.ganhuo.util.Mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.zip.CRC32;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:46
 */
public class NewsTidy extends BaseTidy {
    private String urlExt = "/cn/news/";
    private int pageSize = 15;
    private int artiType = ArticleEntity.TYPE_NEWS;
    private String selector = ".news_type_block";

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
