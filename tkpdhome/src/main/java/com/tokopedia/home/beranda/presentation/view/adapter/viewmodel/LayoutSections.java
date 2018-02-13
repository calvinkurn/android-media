package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class LayoutSections {
    public static final int ICON_USE_CASE = 1;
    public static final int ICON_DYNAMIC_CASE = 2;
    String title;
    String icon;
    String applink;
    String url;
    int typeCase;

    public LayoutSections(int typeCase, String title, String icon, String applink, String url) {
        this.title = title;
        this.icon = icon;
        this.applink = applink;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTypeCase() {
        return typeCase;
    }

}
