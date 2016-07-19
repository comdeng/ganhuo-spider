package com.huimang.ganhuo.tidy;

import com.github.kevinsawicki.http.HttpRequest;
import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.util.Const;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
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

    /**
     * 载入文章
     * @param article
     */
    public static ArticleEntity loadArticle(String url)
    {
        url = Const.INFOQ_HOST + url;
        String html = HttpRequest.get(url).body();
        return tidy(html);
    }
    /**
     * 过滤内容
     * @param html
     * @return
     */
    public static ArticleEntity tidy(String html) {
        Document doc = Jsoup.parse(html);

        ArticleEntity arti = new ArticleEntity();

        Element header = doc.head();
        arti.setTitle(header.getElementsByTag("title").first().text());
        Elements metas = header.getElementsByTag("meta");

        for(int i = 0, l = metas.size(); i < l;i++) {
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

        // 找到div.clear的标签，并把之后出现的元素都删除掉
        int foundIndex = contentElem.select("div.clear").first().elementSiblingIndex();
        int totalSize = contentElem.children().size();
        for (int i = totalSize - 1; i >= foundIndex; i--) {
            Element elem = contentElem.child(i);
            elem.remove();
        }

        // 查找作者
        Element extraElem = doc.body().select(".author_general").first();
        Elements authorElems = extraElem.getElementsByTag("a");
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
