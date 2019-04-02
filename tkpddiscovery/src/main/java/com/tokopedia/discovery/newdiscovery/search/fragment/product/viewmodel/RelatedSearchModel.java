package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;

import java.util.List;

public class RelatedSearchModel implements Parcelable, Visitable<ProductListTypeFactory> {

    private String relatedKeyword;
    private List<OtherRelated> otherRelated;

    public String getRelatedKeyword() {
        return relatedKeyword;
    }

    public void setRelatedKeyword(String relatedKeyword) {
        this.relatedKeyword = relatedKeyword;
    }

    public List<OtherRelated> getOtherRelated() {
        return otherRelated;
    }

    public void setOtherRelated(List<OtherRelated> otherRelated) {
        this.otherRelated = otherRelated;
    }

    public static class OtherRelated implements Parcelable {
        private String keyword;
        private String url;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.keyword);
            dest.writeString(this.url);
        }

        public OtherRelated() {
        }

        protected OtherRelated(Parcel in) {
            this.keyword = in.readString();
            this.url = in.readString();
        }

        public static final Parcelable.Creator<OtherRelated> CREATOR = new Parcelable.Creator<OtherRelated>() {
            @Override
            public OtherRelated createFromParcel(Parcel source) {
                return new OtherRelated(source);
            }

            @Override
            public OtherRelated[] newArray(int size) {
                return new OtherRelated[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.relatedKeyword);
        dest.writeTypedList(this.otherRelated);
    }

    public RelatedSearchModel() {
    }

    protected RelatedSearchModel(Parcel in) {
        this.relatedKeyword = in.readString();
        this.otherRelated = in.createTypedArrayList(OtherRelated.CREATOR);
    }

    public static final Parcelable.Creator<RelatedSearchModel> CREATOR = new Parcelable.Creator<RelatedSearchModel>() {
        @Override
        public RelatedSearchModel createFromParcel(Parcel source) {
            return new RelatedSearchModel(source);
        }

        @Override
        public RelatedSearchModel[] newArray(int size) {
            return new RelatedSearchModel[size];
        }
    };

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
