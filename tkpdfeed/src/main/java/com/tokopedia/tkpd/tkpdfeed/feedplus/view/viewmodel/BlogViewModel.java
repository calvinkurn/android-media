package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

/**
 * @author by nisie on 5/19/17.
 */

public class BlogViewModel implements Visitable<FeedPlusTypeFactory> {

    private String imageUrl;
    private String videoUrl;
    private String url;
    private String title;
    private String content;

    public BlogViewModel() {
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public BlogViewModel(String imageUrl, String title) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.videoUrl = "";
        this.url = "https://tokopedia.com";
        this.content = "Berani tentukan Mimpi - #Lanjutkan Mimpi atau #Ubah Mimipi";
    }

    public BlogViewModel(String videoUrl) {
        this.title = "Video post";
        this.imageUrl = "";
        this.videoUrl = videoUrl;
        this.url = "https://tokopedia.com";
        this.content = "Ini isinya video loh";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
