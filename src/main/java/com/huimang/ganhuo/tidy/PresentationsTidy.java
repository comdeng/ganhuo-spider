package com.huimang.ganhuo.tidy;

import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.util.Mysql;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:46
 */
public class PresentationsTidy extends BaseTidy {
    public String urlExt = "/cn/presentations/";
    public int pageSize = 12;
    public int artiType = ArticleEntity.TYPE_PRESENTATION;
    public String selector = "#content .news_type_video";

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

    /**
     * 载入文章列表，写入数据库
     *
     * @param page
     */
    public void loadLists(int page) throws SQLException {
        String html = getListPage(page);
        Document doc = Jsoup.parse(html);

        Elements links = doc.select(this.getSelector());
        List<ArticleEntity> list = new ArrayList<ArticleEntity>();
        for (int i = 0, l = links.size(); i < l; i++) {
            Element movie = links.get(i);
            ArticleEntity arti = new ArticleEntity(ArticleEntity.TYPE_PRESENTATION);
            Element link = movie.select(">a").first();
            arti.setOriginalUrl(link.attr("href"));
            arti.setTitle(link.attr("title"));

            arti.setCover(movie.select(".movie img").first().attr("src"));

            Element author = movie.select(".author").first();
            Elements authorLinks = author.getElementsByTag("a");
            if (authorLinks.size() == 1) {
                arti.setAuthor(authorLinks.first().html());
            } else if (authorLinks.size() == 2) {
                // 是否有评论
                if (authorLinks.get(1).select(".comments_counts").size() > 0) {
                    arti.setAuthor(authorLinks.first().html());
                    arti.setCommentNum(Integer.valueOf(authorLinks.get(1).text()));
                } else {
                    arti.setAuthor(authorLinks.first().html());
                    arti.setTranslator(authorLinks.get(1).html());
                }
            } else if (authorLinks.size() == 3) {
                arti.setAuthor(authorLinks.first().html());
                arti.setTranslator(authorLinks.get(1).html());
                arti.setCommentNum(Integer.valueOf(authorLinks.get(2).text()));
            }

            String text = author.text().trim();
            int pos = text.indexOf("发布于");
            if (pos > -1) {
                String publishDay = text.substring(pos + 3).replaceAll("^( |\\t|\\r|\\n|\\s)*", "");
                SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
                try {
                    Date publishDate = format.parse(publishDay);
                    arti.setPublishDay(publishDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            arti.setSummary(movie.getElementsByTag("p").first().html());
            list.add(arti);
        }

        Connection conn = Mysql.getConnection();
        for (ArticleEntity arti : list) {
            if (ArticleTidy.isArtiExisted(conn, arti.getOriginalId())) {
                this.logger.info("arti existed:" + arti.getOriginalUrl());
                continue;
            }
            System.out.print(arti.getTranslator());
            this.writeArticle(conn, arti);
        }
    }
}
