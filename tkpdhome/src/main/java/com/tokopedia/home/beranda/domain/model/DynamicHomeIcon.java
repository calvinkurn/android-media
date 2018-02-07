package com.tokopedia.home.beranda.domain.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by henrypriyono on 30/01/18.
 */

public class DynamicHomeIcon {

    @Expose
    private List<UseCaseIcon> useCaseIcon;

    @Expose
    private List<DynamicIcon> dynamicIcon;

    public List<UseCaseIcon> getUseCaseIcon() {
        return useCaseIcon;
    }

    public void setUseCaseIcon(List<UseCaseIcon> useCaseIcon) {
        this.useCaseIcon = useCaseIcon;
    }

    public List<DynamicIcon> getDynamicIcon() {
        return dynamicIcon;
    }

    public void setDynamicIcon(List<DynamicIcon> dynamicIcon) {
        this.dynamicIcon = dynamicIcon;
    }

    public class DynamicIcon {
        @Expose
        private String id;

        @Expose
        private String applinks;

        @Expose
        private String imageUrl;

        @Expose
        private String name;

        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class UseCaseIcon {
        @Expose
        private String id;

        @Expose
        private String applinks;

        @Expose
        private String imageUrl;

        @Expose
        private String name;

        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
