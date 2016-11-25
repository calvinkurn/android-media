package com.tokopedia.discovery.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by noiz354 on 3/23/16.
 */
@Parcel
public class HotListBannerModel {
    @SerializedName("query")
    public Query query;

    @SerializedName("info")
    public Info info;

    @Parcel
    public static class Query {
        @SerializedName("ob")
        public String ob;

        @SerializedName("q")
        public String q;

        @SerializedName("terms")
        public String terms;

        @SerializedName("sc")
        public String sc;

        @SerializedName("negative_keyword")
        public String negative_keyword;

        @SerializedName("pmin")
        public String pmin;

        @SerializedName("fshop")
        public String fshop;

        @SerializedName("type")
        public String type;

        @SerializedName("pmax")
        public String pmax;

        @SerializedName("hot_id")
        public String hot_id;

        @SerializedName("shop_id")
        public String shop_id;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Parcel
    public static class Info {
        @SerializedName("title")
        public String title;

        @SerializedName("meta_description")
        public String metaDescription;

        @SerializedName("share_file_path")
        public String shareFilePath;

        @SerializedName("meta_title")
        public String metaTitle;

        @SerializedName("file_name")
        public String fileName;

        @SerializedName("cover_img")
        public String coverImg;

        @SerializedName("alias_key")
        public String aliasKey;

        @SerializedName("hotlist_description")
        public String hotlistDescription;

    }

    public static final class HotListBannerContainer implements ObjContainer<HotListBannerModel>{
        HotListBannerModel hotListBannerModel;

        public HotListBannerContainer(HotListBannerModel hotListBannerModel) {
            this.hotListBannerModel = hotListBannerModel;
        }

        @Override
        public HotListBannerModel body() {
            return hotListBannerModel;
        }
    }
}
