package com.tokopedia.tkpdpdp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAdsPdpAffiliateResponse {

    @SerializedName("topAdsPDPAffiliate")
    private TopAdsPdpAffiliate topAdsPDPAffiliate;

    public TopAdsPdpAffiliate getTopAdsPDPAffiliate() {
        return topAdsPDPAffiliate;
    }

    public void setTopAdsPDPAffiliate(TopAdsPdpAffiliate topAdsPDPAffiliate) {
        this.topAdsPDPAffiliate = topAdsPDPAffiliate;
    }

    public static class TopAdsPdpAffiliate {
        @SerializedName("data")
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public static class Data {
            @SerializedName("affiliate")
            private List<PdpAffiliate> affiliate;

            public List<PdpAffiliate> getAffiliate() {
                return affiliate;
            }

            public void setAffiliate(List<PdpAffiliate> affiliate) {
                this.affiliate = affiliate;
            }

            public static class PdpAffiliate {
                @SerializedName("AdId")
                private int AdId;
                @SerializedName("ProductId")
                private int ProductId;
                @SerializedName("AffiliatedByUser")
                private boolean AffiliatedByUser;
                @SerializedName("CommissionPercent")
                private int CommissionPercent;
                @SerializedName("CommissionPercentDispay")
                private String CommissionPercentDispay;
                @SerializedName("CommissionValue")
                private int CommissionValue;
                @SerializedName("CommissionValueDisplay")
                private String CommissionValueDisplay;
                @SerializedName("UniqueURL")
                private String UniqueURL;

                public int getAdId() {
                    return AdId;
                }

                public void setAdId(int AdId) {
                    this.AdId = AdId;
                }

                public int getProductId() {
                    return ProductId;
                }

                public void setProductId(int ProductId) {
                    this.ProductId = ProductId;
                }

                public boolean isAffiliatedByUser() {
                    return AffiliatedByUser;
                }

                public void setAffiliatedByUser(boolean AffiliatedByUser) {
                    this.AffiliatedByUser = AffiliatedByUser;
                }

                public int getCommissionPercent() {
                    return CommissionPercent;
                }

                public void setCommissionPercent(int CommissionPercent) {
                    this.CommissionPercent = CommissionPercent;
                }

                public String getCommissionPercentDispay() {
                    return CommissionPercentDispay;
                }

                public void setCommissionPercentDispay(String CommissionPercentDispay) {
                    this.CommissionPercentDispay = CommissionPercentDispay;
                }

                public int getCommissionValue() {
                    return CommissionValue;
                }

                public void setCommissionValue(int CommissionValue) {
                    this.CommissionValue = CommissionValue;
                }

                public String getCommissionValueDisplay() {
                    return CommissionValueDisplay;
                }

                public void setCommissionValueDisplay(String CommissionValueDisplay) {
                    this.CommissionValueDisplay = CommissionValueDisplay;
                }

                public String getUniqueURL() {
                    return UniqueURL;
                }

                public void setUniqueURL(String UniqueURL) {
                    this.UniqueURL = UniqueURL;
                }
            }
        }
    }
}
