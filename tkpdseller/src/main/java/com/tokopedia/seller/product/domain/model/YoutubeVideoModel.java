package com.tokopedia.seller.product.domain.model;

/**
 * @author normansyahputa on 4/11/17.
 */

public class YoutubeVideoModel {
    private String snippetTitle;
    private String snippetDescription;
    private String thumbnailUrl;
    private int width, height;

    public String getSnippetTitle() {
        return snippetTitle;
    }

    public void setSnippetTitle(String snippetTitle) {
        this.snippetTitle = snippetTitle;
    }

    public String getSnippetDescription() {
        return snippetDescription;
    }

    public void setSnippetDescription(String snippetDescription) {
        this.snippetDescription = snippetDescription;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
