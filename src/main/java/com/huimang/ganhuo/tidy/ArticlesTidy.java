package com.huimang.ganhuo.tidy;

import com.huimang.ganhuo.entity.ArticleEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 12:04
 */
public class ArticlesTidy {
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
        Elements links = doc.getElementById("content").select(".news_type1>h2>a,.news_type2>h2>a");
        for (int i = 0, l = links.size(); i < l; i++) {
            Element link = links.get(i);
            ArticleEntity arti = new ArticleEntity(ArticleEntity.TYPE_ARTICLE);
            arti.setUrl(link.attr("href"));
            arti.setTitle(link.html());
            list.add(arti);
        }
        return list;
    }
}
