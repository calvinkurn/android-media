package com.tokopedia.discovery.imagesearch.domain.usecase;

import com.aliyuncs.RoaAcsRequest;
import com.aliyuncs.http.MethodType;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class RoaSearchRequest extends RoaAcsRequest<RoaSearchResponse> {

    public RoaSearchRequest() {
        super("ImageSearch", "2018-01-20", "SearchItem");
        setUriPattern("/item/search");
        setMethod(MethodType.POST);
    }

    private String cat_id;

    private String instanceName;

    private String app;

    private Integer s;

    private String product_id;

    private String pic_name;

    private Integer n;

    private String vid;

    public String getCatd() {
        return this.cat_id;
    }

    public void setCatId(String cat_id) {
        this.cat_id = cat_id;
        if (cat_id != null) {
            putQueryParameter("cat_id", cat_id);
        }
    }

    public String getApp() {
        return this.app;
    }

    public void setApp(String app) {
        this.app = app;
        if (app != null) {
            putQueryParameter("app", app);
        }
    }

    public Integer getS() {
        return this.s;
    }

    public void setS(Integer s) {
        this.s = s;
        if (s != null) {
            putQueryParameter("s", s.toString());
        }
    }

    public String getProductId() {
        return this.product_id;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
        if (product_id != null) {
            putQueryParameter("product_id", product_id);
        }
    }

    public String getPicName() {
        return this.pic_name;
    }

    public void setPicName(String pic_name) {
        this.pic_name = pic_name;
        if (pic_name != null) {
            putQueryParameter("pic_name", pic_name);
        }
    }

    public Integer getN() {
        return this.n;
    }

    public void setN(Integer n) {
        this.n = n;
        if (n != null) {
            putQueryParameter("n", n.toString());
        }
    }

    public String getVid() {
        return this.vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
        if (vid != null) {
            putQueryParameter("vid", vid);
        }
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;

    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        if (instanceName != null) {
            putQueryParameter("instanceName", instanceName);
        }
    }

    @Override
    public Class<RoaSearchResponse> getResponseClass() {
        return RoaSearchResponse.class;
    }

}
