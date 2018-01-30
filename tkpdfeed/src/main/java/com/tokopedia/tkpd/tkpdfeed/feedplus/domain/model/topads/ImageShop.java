package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */
public class ImageShop {
    private String cover;
    private String sUrl;
    private String xsUrl;
    private String coverEcs;
    private String sEcs;
    private String xsEcs;

    public ImageShop(String cover, String sUrl, String xsUrl, String coverEcs, String sEcs,
                     String xsEcs) {
        this.cover = cover;
        this.sUrl = sUrl;
        this.xsUrl = xsUrl;
        this.coverEcs = coverEcs;
        this.sEcs = sEcs;
        this.xsEcs = xsEcs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getXsUrl() {
        return xsUrl;
    }

    public void setXsUrl(String xsUrl) {
        this.xsUrl = xsUrl;
    }

    public String getCoverEcs() {
        return coverEcs;
    }

    public void setCoverEcs(String coverEcs) {
        this.coverEcs = coverEcs;
    }

    public String getsEcs() {
        return sEcs;
    }

    public void setsEcs(String sEcs) {
        this.sEcs = sEcs;
    }

    public String getXsEcs() {
        return xsEcs;
    }

    public void setXsEcs(String xsEcs) {
        this.xsEcs = xsEcs;
    }
}
