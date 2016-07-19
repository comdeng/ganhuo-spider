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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ronnie on 2016/7/19.
 */
public abstract class BaseTidy {

    public Logger logger;


    public BaseTidy() {
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public abstract String getUrlExt();

    public abstract int getPageSize();

    public abstract int getArtiType();

    public abstract String getSelector();

    /**
     * 过滤内容
     *
     * @param html
     * @return
     */
    public List<ArticleEntity> tidy(String html) {
        Document doc = Jsoup.parse(html);
        List<ArticleEntity> list = new ArrayList<ArticleEntity>();

        Elements blocks = doc.getElementById("content").select(this.getSelector());
        for (int i = 0, l = blocks.size(); i < l; i++) {
            ArticleEntity arti = new ArticleEntity();
            Element block = blocks.get(i);
            Element link = block.select(">h2>a").first();
            arti.setOriginalUrl(link.attr("href"));

            Elements pics = block.select(">p>a>img");
            if (pics.size() > 0) {
                arti.setCover(pics.first().attr("src"));
            }
            list.add(arti);
        }
        return list;
    }

    public void loadLists() throws SQLException {
        this.loadLists(1);
    }

    /**
     * 获取列表页面的html代码
     *
     * @param page
     * @return
     */
    public String getListPage(int page) {
        String url;
        if (page == 1) {
            url = Const.INFOQ_HOST + this.getUrlExt();
        } else {
            url = Const.INFOQ_HOST + this.getUrlExt() + ( (page - 1) * this.getPageSize());
        }
        Logger.getLogger("news").info("request url:" + url);
        String html = HttpRequest.get(url).body();
        Logger.getLogger("news").info("request finished");
        return html;
    }

    /**
     * 检查文章是否存在
     * @param conn
     * @param originalId
     * @return
     */
    public boolean isArtiExisted(Connection conn, long originalId) {
        // 检查是否已经存在对应的文章
        String sql = "select 1 from article where original_id=" + originalId;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 1) {
                rs.close();
                stmt.close();
                return true;
            } else {
                rs.close();
                stmt.close();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 载入文章列表，写入数据库
     *
     * @param page
     */
    public void loadLists(int page) throws SQLException {
        String html = getListPage(page);

        List<ArticleEntity> list = tidy(html);

        Connection conn = Mysql.getConnection();

        for (ArticleEntity _arti:list) {
            long originalId = _arti.getOriginalId();

            if (this.isArtiExisted(conn, originalId)) {
                this.logger.info("article existed:" + _arti.getOriginalUrl());
                continue;
            }
            this.logger.info("article found:" + _arti.getOriginalUrl());

            ArticleEntity arti = ArticleTidy.loadArticle(_arti.getOriginalUrl());
            if (!_arti.getCover().equals("") && arti.getCover().equals("")) {
                arti.setCover(_arti.getCover());
            }
            writeArticle(conn, arti);
        }
    }

    public void writeArticle(Connection conn, ArticleEntity arti) throws SQLException {

        String md5 = "";
        if (!arti.getContent().equals("")) {
            md5 = Md5.getMD5(arti.getContent());
        }

        String sql = "insert into article(" +
                "`type`," +         // 1
                "`original_url`," + // 2
                "`original_id`," +  // 3
                "`title`," +        // 4
                "`author`," +       // 5
                "`translator`," +   // 6
                "`keywords`," +     // 7
                "`summary`," +      // 8
                "`content`," +      // 9
                "`md5`," +          // 10
                "`create_time`," +  // 11
                "`publish_time`," + // 12
                "`comment_num`," +  // 13
                "`cover`" +         // 14
                ") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, this.getArtiType());
        stmt.setString(2, arti.getOriginalUrl());
        stmt.setLong(3, arti.getOriginalId());
        stmt.setString(4, arti.getTitle());
        stmt.setString(5, arti.getAuthor());
        stmt.setString(6, arti.getTranslator());
        if (arti.getKeywords() == null) {
            stmt.setString(7, "");
        } else {
            stmt.setString(7, String.join(",", arti.getKeywords()));
        }
        stmt.setString(8, arti.getSummary());
        stmt.setString(9, arti.getContent());
        stmt.setString(10, md5);
        stmt.setLong(11, System.currentTimeMillis() / 1000);
        stmt.setLong(12, arti.getPublishDay().getTime() / 1000);
        stmt.setInt(13, arti.getCommentNum());
        stmt.setString(14, arti.getCover());

        stmt.execute();
        stmt.close();

        this.logger.info("arti writed:" + arti.getOriginalUrl());
    }
}
