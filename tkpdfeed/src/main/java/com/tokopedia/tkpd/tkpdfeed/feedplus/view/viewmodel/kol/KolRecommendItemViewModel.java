package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendItemViewModel {

    private String name;
    private String imageUrl;
    private String url;
    private String label;
    private String id;

    public KolRecommendItemViewModel(String id, String name, String imageUrl, String url, String
            label) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.url = url;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
