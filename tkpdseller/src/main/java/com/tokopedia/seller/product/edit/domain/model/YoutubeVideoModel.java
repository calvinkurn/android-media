package com.tokopedia.seller.product.edit.domain.model;

/**
 * @author normansyahputa on 4/11/17.
 */

public class YoutubeVideoModel {
    private String snippetTitle;
    private String snippetDescription;
    private String thumbnailUrl;
    private int width, height;

    public static YoutubeVideoModel invalidYoutubeModel() {
        YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
        youtubeVideoModel.setWidth(-1);
        youtubeVideoModel.setHeight(-1);
        return youtubeVideoModel;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YoutubeVideoModel that = (YoutubeVideoModel) o;

        if (width != that.width) {
            return false;
        }
        if (height != that.height) {
            return false;
        }
        if (snippetTitle != null ? !snippetTitle.equals(that.snippetTitle) : that.snippetTitle != null){
            return false;
        }
        if (snippetDescription != null ? !snippetDescription.equals(that.snippetDescription) : that.snippetDescription != null){
            return false;
        }
        return thumbnailUrl != null ? thumbnailUrl.equals(that.thumbnailUrl) : that.thumbnailUrl == null;

    }

    @Override
    public int hashCode() {
        int result = snippetTitle != null ? snippetTitle.hashCode() : 0;
        result = 31 * result + (snippetDescription != null ? snippetDescription.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
