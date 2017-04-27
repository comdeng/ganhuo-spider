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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文章过滤器
 *
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 10:44
 */
public class ArticleTidy {
    public static void updateArticle(String url) throws SQLException {
        ArticleEntity arti = new ArticleEntity();
        arti.setOriginalUrl(url);

        Connection conn = Mysql.getConnection();
        if (isArtiExisted(conn, arti.getOriginalId())) {
            // 更新文章内容及md5
            arti = loadArticle(url);

            String sql = "update article set `content`=?,`md5`=? where original_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, arti.getContent());
            stmt.setString(2, Md5.getMD5(arti.getContent()));
            stmt.setLong(3, arti.getOriginalId());
            stmt.execute();
            stmt.close();
        }
    }

    /**
     * 检查文章是否存在
     *
     * @param conn
     * @param originalId
     * @return
     */
    public static boolean isArtiExisted(Connection conn, long originalId) {
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
     * 载入文章
     *
     * @param url
     */
    public static ArticleEntity loadArticle(String url) {
        String html = HttpRequest.get(Const.INFOQ_HOST + url).body();
        ArticleEntity arti = tidy(html);
        arti.setOriginalUrl(url);
        return arti;
    }

    /**
     * 过滤内容
     *
     * @param html
     * @return
     */
    public static ArticleEntity tidy(String html) {
        html = html.replace("</p><ol>\r\n<p>", "</p><p>");
        Document doc = Jsoup.parse(html);

        ArticleEntity arti = new ArticleEntity();
        Element header = doc.head();
        arti.setTitle(header.getElementsByTag("title").first().text());
        Elements metas = header.getElementsByTag("meta");

        for (int i = 0, l = metas.size(); i < l; i++) {
            Element elem = metas.get(i);
            if (elem.attr("name").equals("keywords")) {
                arti.setKeywords(elem.attr("content").split(","));
            } else if (elem.attr("name").equals("description")) {
                arti.setSummary(elem.attr("content"));
            }
        }

        Element contentElem = doc.body().select(".text_content_container > .text_info").first();
        // 删除script标签
        contentElem.getElementsByTag("script").remove();
        contentElem.select(".related_sponsors").remove();


        // 找到div.clear的标签，并把之后出现的元素都删除掉
        int foundIndex = contentElem.select(".random_links").first().siblingIndex() - 1;
        int totalSize = contentElem.children().size();
        for (int i = totalSize - 1; i >= foundIndex; i--) {
            Element elem = contentElem.child(i);
            elem.remove();
        }

        // 查找作者
        Element extraElem = doc.body().select(".author_general").first();
        Elements authorElems = extraElem.select("a.editorlink");
        int authorSize = authorElems.size();
        if (authorSize == 1) {
            arti.setAuthor(authorElems.first().html());
        } else if (authorSize > 1) {
            arti.setAuthor(authorElems.first().html());
            arti.setTranslator(authorElems.get(1).html());
        }

        String text = extraElem.text().trim();
        int pos = text.indexOf("发布于");
        if (pos > -1) {
            String publishDay = text.substring(pos + 3);
            publishDay = publishDay.substring(0, publishDay.length() - 1).trim();
            SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
            try {
                Date publishDate = format.parse(publishDay);
                arti.setPublishDay(publishDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        arti.setContent(contentElem.html());
        return arti;
    }
}
