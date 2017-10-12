package com.tokopedia.session.register.view.viewmodel;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverItemViewModel {

    private String id;
    private String name;
    private String url;
    private String image;
    private String color;

    public DiscoverItemViewModel(String id, String name, String url,
                                 String image, String color) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }
}
