package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksItemViewModel {

    private String image;
    private String url;

    public ToppicksItemViewModel(String image, String url) {
        this.image = image;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
}
