package com.tokopedia.discovery.newdiscovery.hotlist.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */
@SuppressWarnings("unused")
public class PojoHotlistAttribute {

    @SerializedName("header")
    private Header header;
    @SerializedName("data")
    private Data data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Header {
        @SerializedName("total_data")
        private int totalData;
        @SerializedName("process_time")
        private double processTime;
        @SerializedName("additional_params")
        private String additionalParams;

        public int getTotalData() {
            return totalData;
        }

        public void setTotalData(int totalData) {
            this.totalData = totalData;
        }

        public double getProcessTime() {
            return processTime;
        }

        public void setProcessTime(double processTime) {
            this.processTime = processTime;
        }

        public String getAdditionalParams() {
            return additionalParams;
        }

        public void setAdditionalParams(String additionalParams) {
            this.additionalParams = additionalParams;
        }
    }

    public static class Data {

        @SerializedName("hashtag")
        private List<Hashtag> hashtag;
        @SerializedName("breadcrumb")
        private List<Breadcrumb> breadcrumb;

        public List<Hashtag> getHashtag() {
            return hashtag;
        }

        public void setHashtag(List<Hashtag> hashtag) {
            this.hashtag = hashtag;
        }

        public List<Breadcrumb> getBreadcrumb() {
            return breadcrumb;
        }

        public void setBreadcrumb(List<Breadcrumb> breadcrumb) {
            this.breadcrumb = breadcrumb;
        }

        public static class Hashtag {
            @SerializedName("name")
            private String name;
            @SerializedName("url")
            private String url;
            @SerializedName("department_id")
            private String departmentId;

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

            public String getDepartmentId() {
                return departmentId;
            }

            public void setDepartmentId(String departmentId) {
                this.departmentId = departmentId;
            }
        }

        public static class Breadcrumb {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("total_data")
            private String totalData;
            @SerializedName("parent_id")
            private String parentId;
            @SerializedName("identifier")
            private String identifier;
            @SerializedName("tree")
            private int tree;
            @SerializedName("href")
            private String href;
            @SerializedName("name_without_total")
            private String nameWithoutTotal;
            @SerializedName("child")
            private List<Breadcrumb> child;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTotalData() {
                return totalData;
            }

            public void setTotalData(String totalData) {
                this.totalData = totalData;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getIdentifier() {
                return identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }

            public int getTree() {
                return tree;
            }

            public void setTree(int tree) {
                this.tree = tree;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public String getNameWithoutTotal() {
                return nameWithoutTotal;
            }

            public void setNameWithoutTotal(String nameWithoutTotal) {
                this.nameWithoutTotal = nameWithoutTotal;
            }

            public List<Breadcrumb> getChild() {
                return child;
            }

            public void setChild(List<Breadcrumb> child) {
                this.child = child;
            }

        }
    }
}
