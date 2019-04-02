package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 6/19/17.
 */

public class NewReplyDiscussionEntity {


    @SerializedName("resolution")
    private Resolution resolution;
    @SerializedName("conversations")
    private List<Conversations> conversations;
    @SerializedName("cache_key")
    private String cacheKey;

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public List<Conversations> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversations> conversations) {
        this.conversations = conversations;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public static class Resolution {
        @SerializedName("id")
        private String id;
        @SerializedName("status")
        private Status status;
        @SerializedName("createBy")
        private CreateBy createBy;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("updateBy")
        private UpdateBy updateBy;
        @SerializedName("updateTime")
        private String updateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public CreateBy getCreateBy() {
            return createBy;
        }

        public void setCreateBy(CreateBy createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public UpdateBy getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(UpdateBy updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public static class Status {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

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
        }

        public static class CreateBy {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("picture")
            private Picture picture;

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

            public Picture getPicture() {
                return picture;
            }

            public void setPicture(Picture picture) {
                this.picture = picture;
            }

            public static class Picture {
                @SerializedName("fullUrl")
                private String fullUrl;
                @SerializedName("thumbnail")
                private String thumbnail;

                public String getFullUrl() {
                    return fullUrl;
                }

                public void setFullUrl(String fullUrl) {
                    this.fullUrl = fullUrl;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }
            }
        }

        public static class UpdateBy {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("picture")
            private PictureX picture;

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

            public PictureX getPicture() {
                return picture;
            }

            public void setPicture(PictureX picture) {
                this.picture = picture;
            }

            public static class PictureX {
                @SerializedName("fullUrl")
                private String fullUrl;
                @SerializedName("thumbnail")
                private String thumbnail;

                public String getFullUrl() {
                    return fullUrl;
                }

                public void setFullUrl(String fullUrl) {
                    this.fullUrl = fullUrl;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }
            }
        }
    }

    public static class Conversations {
        @SerializedName("id")
        private String id;
        @SerializedName("message")
        private String message;
        @SerializedName("actionBy")
        private int actionBy;
        @SerializedName("createBy")
        private CreateByX createBy;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("createTimeStr")
        private String createTimeStr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getActionBy() {
            return actionBy;
        }

        public void setActionBy(int actionBy) {
            this.actionBy = actionBy;
        }

        public CreateByX getCreateBy() {
            return createBy;
        }

        public void setCreateBy(CreateByX createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(String createTimeStr) {
            this.createTimeStr = createTimeStr;
        }

        public static class CreateByX {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("picture")
            private PictureXX picture;

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

            public PictureXX getPicture() {
                return picture;
            }

            public void setPicture(PictureXX picture) {
                this.picture = picture;
            }

            public static class PictureXX {
                @SerializedName("fullUrl")
                private String fullUrl;
                @SerializedName("thumbnail")
                private String thumbnail;

                public String getFullUrl() {
                    return fullUrl;
                }

                public void setFullUrl(String fullUrl) {
                    this.fullUrl = fullUrl;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }
            }
        }
    }
}
