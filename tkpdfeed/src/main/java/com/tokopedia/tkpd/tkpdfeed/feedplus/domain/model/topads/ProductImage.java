package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/3/17.
 * Copied to feed by milhamj 1/18/17.
 */
public class ProductImage {
    private String m_url;
    private String s_url;
    private String xs_url;
    private String m_ecs;
    private String s_ecs;
    private String xs_ecs;

    public ProductImage(String m_url, String s_url, String xs_url, String m_ecs, String s_ecs,
                        String xs_ecs) {
        this.m_url = m_url;
        this.s_url = s_url;
        this.xs_url = xs_url;
        this.m_ecs = m_ecs;
        this.s_ecs = s_ecs;
        this.xs_ecs = xs_ecs;
    }

    public String getM_url() {
        return m_url;
    }

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }

    public String getS_url() {
        return s_url;
    }

    public void setS_url(String s_url) {
        this.s_url = s_url;
    }

    public String getXs_url() {
        return xs_url;
    }

    public void setXs_url(String xs_url) {
        this.xs_url = xs_url;
    }

    public String getM_ecs() {
        return m_ecs;
    }

    public void setM_ecs(String m_ecs) {
        this.m_ecs = m_ecs;
    }

    public String getS_ecs() {
        return s_ecs;
    }

    public void setS_ecs(String s_ecs) {
        this.s_ecs = s_ecs;
    }

    public String getXs_ecs() {
        return xs_ecs;
    }

    public void setXs_ecs(String xs_ecs) {
        this.xs_ecs = xs_ecs;
    }
}
