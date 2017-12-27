package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksItemViewModel {

    private final String name;
    private String image;
    private String url;

    public ToppicksItemViewModel(String name, String image, String url) {
        this.name = name;
        this.image = image;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
