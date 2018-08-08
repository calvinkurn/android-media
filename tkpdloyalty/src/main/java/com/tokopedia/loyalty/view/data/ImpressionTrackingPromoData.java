package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 12/01/18.
 */

public class ImpressionTrackingPromoData implements Parcelable {

    private Ecommerce ecommerce;

    public ImpressionTrackingPromoData(Ecommerce ecommerce) {
        this.ecommerce = ecommerce;
    }

    public Ecommerce getEcommerce() {
        return ecommerce;
    }

    public void setEcommerce(Ecommerce ecommerce) {
        this.ecommerce = ecommerce;
    }

    public static class Ecommerce implements Parcelable {
        public Ecommerce(PromoView promoView) {
            this.promoView = promoView;
        }

        public PromoView getPromoView() {
            return promoView;
        }

        public void setPromoView(PromoView promoView) {
            this.promoView = promoView;
        }

        private PromoView promoView;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.promoView, flags);
        }

        protected Ecommerce(Parcel in) {
            this.promoView = in.readParcelable(PromoView.class.getClassLoader());
        }

        public static final Parcelable.Creator<Ecommerce> CREATOR = new Parcelable.Creator<Ecommerce>() {
            @Override
            public Ecommerce createFromParcel(Parcel source) {
                return new Ecommerce(source);
            }

            @Override
            public Ecommerce[] newArray(int size) {
                return new Ecommerce[size];
            }
        };
    }

    public static class PromoView implements Parcelable {
        public PromoView(List<Promotion> promotionList) {
            this.promotionList = promotionList;
        }

        public List<Promotion> getPromotionList() {
            return promotionList;
        }

        public void setPromotionList(List<Promotion> promotionList) {
            this.promotionList = promotionList;
        }

        private List<Promotion> promotionList = new ArrayList<>();

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.promotionList);
        }

        protected PromoView(Parcel in) {
            this.promotionList = in.createTypedArrayList(Promotion.CREATOR);
        }

        public static final Parcelable.Creator<PromoView> CREATOR = new Parcelable.Creator<PromoView>() {
            @Override
            public PromoView createFromParcel(Parcel source) {
                return new PromoView(source);
            }

            @Override
            public PromoView[] newArray(int size) {
                return new PromoView[size];
            }
        };
    }

    public static class Promotion implements Parcelable {

        private String id;
        private String name;
        private String creative;
        private String position;
        private String promoId;
        private String promoCode;
        private List<String> promoCodeList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreative() {
            return creative;
        }

        public String getPosition() {
            return position;
        }


        public String getPromoId() {
            return promoId;
        }

        public String getPromoCode() {
            return promoCode;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCreative(String creative) {
            this.creative = creative;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setPromoId(String promoId) {
            this.promoId = promoId;
        }

        public void setPromoCode(String promoCode) {
            this.promoCode = promoCode;
        }

        public Promotion() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.creative);
            dest.writeString(this.position);
            dest.writeString(this.promoId);
            dest.writeString(this.promoCode);
            dest.writeStringList(this.promoCodeList);
        }

        protected Promotion(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.creative = in.readString();
            this.position = in.readString();
            this.promoId = in.readString();
            this.promoCode = in.readString();
            this.promoCodeList = in.createStringArrayList();
        }

        public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {
            @Override
            public Promotion createFromParcel(Parcel source) {
                return new Promotion(source);
            }

            @Override
            public Promotion[] newArray(int size) {
                return new Promotion[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ecommerce, flags);
    }

    protected ImpressionTrackingPromoData(Parcel in) {
        this.ecommerce = in.readParcelable(Ecommerce.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImpressionTrackingPromoData> CREATOR =
            new Parcelable.Creator<ImpressionTrackingPromoData>() {
                @Override
                public ImpressionTrackingPromoData createFromParcel(Parcel source) {
                    return new ImpressionTrackingPromoData(source);
                }

                @Override
                public ImpressionTrackingPromoData[] newArray(int size) {
                    return new ImpressionTrackingPromoData[size];
                }
            };
}
