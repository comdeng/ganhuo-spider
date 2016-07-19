package com.huimang.ganhuo.tidy;

import com.huimang.ganhuo.entity.ArticleEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
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
public class PresentationsTidy {
    /**
     * 过滤内容
     *
     * @param file
     * @return
     */
    public static List<ArticleEntity> tidy(File file) {
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ArticleEntity> list = new ArrayList<ArticleEntity>();

        Elements links = doc.getElementById("content").select(".news_type_video");
        for (int i = 0, l = links.size(); i < l; i++) {
            Element movie = links.get(i);
            ArticleEntity arti = new ArticleEntity(ArticleEntity.TYPE_PRESENTATION);
            Element link = movie.select(">a").first();
            arti.setUrl(link.attr("href"));
            arti.setTitle(link.attr("title"));

            arti.setCover(movie.select(".movie img").first().attr("src"));

            Element author = movie.select(".author").first();
            Elements authorLinks = author.getElementsByTag("a");
            if (authorLinks.size() == 1) {
                arti.setAuthor(authorLinks.first().html());
            } else if(authorLinks.size() > 1) {
                arti.setAuthor(authorLinks.first().html());
                arti.setTranslator(authorLinks.get(i).html());
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
        return list;
    }
}
