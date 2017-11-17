package com.tokopedia.seller.common.featuredproduct;

import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductDomainModel {
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public static class Label {

        private String title;
        private String color;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

    }

    public static class WholesaleDetail {

        private String qty;
        private String price;

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }

    public static class Datum {

        private long productId;

        private String name;

        private String uri;

        private String price;

        private String imageUri;

        private boolean preorder;

        private boolean returnable;

        private boolean wholesale;

        private List<WholesaleDetail> wholesaleDetail = null;

        private boolean cashback;

        private List<Label> labels = null;

        private CashbackDetail cashbackDetail;

        private List<Badge> badges = null;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

        public boolean isPreorder() {
            return preorder;
        }

        public void setPreorder(boolean preorder) {
            this.preorder = preorder;
        }

        public boolean isReturnable() {
            return returnable;
        }

        public void setReturnable(boolean returnable) {
            this.returnable = returnable;
        }

        public boolean isWholesale() {
            return wholesale;
        }

        public void setWholesale(boolean wholesale) {
            this.wholesale = wholesale;
        }

        public List<WholesaleDetail> getWholesaleDetail() {
            return wholesaleDetail;
        }

        public void setWholesaleDetail(List<WholesaleDetail> wholesaleDetail) {
            this.wholesaleDetail = wholesaleDetail;
        }

        public boolean isCashback() {
            return cashback;
        }

        public void setCashback(boolean cashback) {
            this.cashback = cashback;
        }

        public List<Label> getLabels() {
            return labels;
        }

        public void setLabels(List<Label> labels) {
            this.labels = labels;
        }

        public CashbackDetail getCashbackDetail() {
            return cashbackDetail;
        }

        public void setCashbackDetail(CashbackDetail cashbackDetail) {
            this.cashbackDetail = cashbackDetail;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

    }

    public static class Badge {

        private String title;

        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }

    public static class CashbackDetail {

        private long cashbackStatus;

        private long cashbackPercent;

        private long isCashbackExpired;

        private long cashbackValue;

        public long getCashbackStatus() {
            return cashbackStatus;
        }

        public void setCashbackStatus(long cashbackStatus) {
            this.cashbackStatus = cashbackStatus;
        }

        public long getCashbackPercent() {
            return cashbackPercent;
        }

        public void setCashbackPercent(long cashbackPercent) {
            this.cashbackPercent = cashbackPercent;
        }

        public long getIsCashbackExpired() {
            return isCashbackExpired;
        }

        public void setIsCashbackExpired(long isCashbackExpired) {
            this.isCashbackExpired = isCashbackExpired;
        }

        public long getCashbackValue() {
            return cashbackValue;
        }

        public void setCashbackValue(long cashbackValue) {
            this.cashbackValue = cashbackValue;
        }

    }
}
