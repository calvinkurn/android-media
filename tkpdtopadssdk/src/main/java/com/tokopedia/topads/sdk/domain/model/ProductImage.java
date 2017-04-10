package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/3/17.
 */
public class ProductImage {

    private static final String KEY_M_URL = "m_url";
    private static final String KEY_S_URL = "s_url";
    private static final String KEY_XS_URL = "xs_url";
    private static final String KEY_M_ECS = "m_ecs";
    private static final String KEY_S_ECS = "s_ecs";
    private static final String KEY_XS_ECS = "xs_ecs";

    private String m_url;
    private String s_url;
    private String xs_url;
    private String m_ecs;
    private String s_ecs;
    private String xs_ecs;

    public ProductImage(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_M_URL)){
            setM_url(object.getString(KEY_M_URL));
        }
        if(!object.isNull(KEY_S_URL)){
            setS_url(object.getString(KEY_S_URL));
        }
        if(!object.isNull(KEY_XS_URL)){
            setXs_url(object.getString(KEY_XS_URL));
        }
        if(!object.isNull(KEY_M_ECS)){
            setM_ecs(object.getString(KEY_M_ECS));
        }
        if(!object.isNull(KEY_S_ECS)){
            setS_ecs(object.getString(KEY_S_ECS));
        }
        if(!object.isNull(KEY_XS_ECS)){
            setXs_ecs(object.getString(KEY_XS_ECS));
        }
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
