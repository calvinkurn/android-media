package com.tokopedia.inbox.rescenter.discussion.view.viewmodel;

/**
 * Created by nisie on 3/31/17.
 */

public class AttachmentViewModel {

    String imgThumb;
    String url;
    private String fileLoc;

    public String getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(String imgThumb) {
        this.imgThumb = imgThumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    public void setFileLoc(String fileLoc) {
        this.fileLoc = fileLoc;
    }
}
