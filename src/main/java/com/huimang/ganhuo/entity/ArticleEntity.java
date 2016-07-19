package com.huimang.ganhuo.entity;

import java.util.Date;
import java.util.zip.CRC32;

/**
 * 文章
 *
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 10:44
 */
public class ArticleEntity {
    public static final int TYPE_NEWS = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_PRESENTATION = 4;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        this.originalId = getOriginId(originalUrl);
    }

    public static long getOriginId(String url)
    {
        CRC32 crc32 = new CRC32();
        crc32.update(url.getBytes());
        return crc32.getValue();
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public Date getPublishDay() {
        return publishDay;
    }

    public void setPublishDay(Date publishDay) {
        this.publishDay = publishDay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    private String title;
    private String originalUrl;
    private String[] keywords;
    private String summary = "";
    private String content = "";
    private String author = "";
    private String translator = "";
    private Date publishDay;
    private String cover = "";

    public long getOriginalId() {
        return originalId;
    }

    private long originalId;

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    private int commentNum = 0;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ArticleEntity() {

    }

    public ArticleEntity(int type) {
        this.setType(type);
    }

}
