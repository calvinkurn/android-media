package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendItemViewModel {

    private boolean followed;
    private String name;
    private String imageUrl;
    private String url;
    private String label;
    private int id;

    public KolRecommendItemViewModel(int id, String name, String imageUrl, String url, String
            label, boolean followed) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.url = url;
        this.label = label;
        this.followed = followed;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
