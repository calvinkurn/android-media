package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_gmt")
    @Expose
    private String dateGmt;
    @SerializedName("guid")
    @Expose
    private Guid guid;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("modified_gmt")
    @Expose
    private String modifiedGmt;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("excerpt")
    @Expose
    private Excerpt excerpt;
    @SerializedName("author")
    @Expose
    private int author;
    @SerializedName("featured_media")
    @Expose
    private int featuredMedia;
    @SerializedName("comment_status")
    @Expose
    private String commentStatus;
    @SerializedName("ping_status")
    @Expose
    private String pingStatus;
    @SerializedName("sticky")
    @Expose
    private boolean sticky;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("categories")
    @Expose
    private List<Integer> categories = new ArrayList<>();
    @SerializedName("tags")
    @Expose
    private List<Integer> tags = new ArrayList<>();
    @SerializedName("cta_text")
    @Expose
    private String ctaText;
    @SerializedName("acf")
    @Expose
    private Acf acf;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public Guid getGuid() {
        return guid;
    }

    public String getModified() {
        return modified;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public String getSlug() {
        return slug;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public int getAuthor() {
        return author;
    }

    public int getFeaturedMedia() {
        return featuredMedia;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public boolean isSticky() {
        return sticky;
    }

    public String getTemplate() {
        return template;
    }

    public String getFormat() {
        return format;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public String getCtaText() {
        return ctaText;
    }

    public Acf getAcf() {
        return acf;
    }
}
