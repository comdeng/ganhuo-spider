package com.huimang.ganhuo.tidy;

import com.github.kevinsawicki.http.HttpRequest;
import com.huimang.ganhuo.util.Const;
import com.huimang.ganhuo.util.Mysql;
import com.huimang.ganhuo.entity.ArticleEntity;
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
import java.util.zip.CRC32;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:46
 */
public class NewsTidy {

    public static void loadLists() throws SQLException
    {
        loadLists(1);
    }
    /**
     * 载入文章列表，写入数据库
     *
     * @param page
     */
    public static void loadLists(int page) throws SQLException {
        Connection conn = Mysql.getConnection();
        String url;
        if (page == 1) {
            url = Const.INFOQ_HOST + "/news/";
        } else {
            url = Const.INFOQ_HOST + "/news/" + (page * 15);
        }
        String body = HttpRequest.get(url).body();
        String[] urls = tidy(body);

        PreparedStatement stmt;
        String sql;

        for (String originalUrl : urls) {
            CRC32 crc32 = new CRC32();
            crc32.update(originalUrl.getBytes());
            long originalId = crc32.getValue();

            // 检查是否已经存在对应的文章
            sql = "select 1 from article where original_id=" + originalId;
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    System.out.println("article existed:" + originalUrl);
                    stmt.close();
                    continue;
                }
            }
            System.out.println("article found:" + originalUrl);

            ArticleEntity arti = ArticleTidy.loadArticle(originalUrl);

            sql = "insert into article(" +
                    "`type`," +
                    "`title`," +
                    "`original_url`," +
                    "`original_id`," +
                    "`create_time`," +
                    "`publish_time`," +
                    "`author`," +
                    "`translator`," +
                    "`keywords`," +
                    "`summary`," +
                    "`content`," +
                    "`md5`," +
                    "`cover`" +
                    ") values(?, ?, ?, ?, ?, ?," +
                    ", '', '', '', '', '', '', ''" +
                    ")";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ArticleEntity.TYPE_NEWS);
            stmt.setString(2, arti.getTitle());
            stmt.setString(3, arti.getUrl());
            stmt.setLong(4, originalId);
            stmt.setLong(5, System.currentTimeMillis() / 1000);
            stmt.set

            stmt.execute();
            stmt.close();

        }
        conn.close();
    }

    /**
     * 过滤内容
     *
     * @param html
     * @return
     */
    public static String[] tidy(String html) {
        Document doc = Jsoup.parse(html);
        List<String> list = new ArrayList<String>();

        Elements links = doc.getElementById("content").select(".news_type_block>h2>a");
        for (int i = 0, l = links.size(); i < l; i++) {
            list.add(links.get(i).attr("href"));
        }
        return list.toArray(new String[]{});
    }
}
