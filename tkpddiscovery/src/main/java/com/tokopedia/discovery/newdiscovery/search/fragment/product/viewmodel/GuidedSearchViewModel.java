package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;

import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchViewModel implements Visitable<ProductListTypeFactory>,Parcelable {
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public static class Item implements Parcelable {
        private String keyword;
        private String url;
        private String currentPage;
        private String previousKey;

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

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setPreviousKey(String previousKey) {
            this.previousKey = previousKey;
        }

        public String getPreviousKey() {
            return previousKey;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.keyword);
            dest.writeString(this.url);
            dest.writeString(this.currentPage);
            dest.writeString(this.previousKey);
        }

        public Item() {
        }

        protected Item(Parcel in) {
            this.keyword = in.readString();
            this.url = in.readString();
            this.currentPage = in.readString();
            this.previousKey = in.readString();
        }

        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.itemList);
    }

    public GuidedSearchViewModel() {
    }

    protected GuidedSearchViewModel(Parcel in) {
        this.itemList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Parcelable.Creator<GuidedSearchViewModel> CREATOR = new Parcelable.Creator<GuidedSearchViewModel>() {
        @Override
        public GuidedSearchViewModel createFromParcel(Parcel source) {
            return new GuidedSearchViewModel(source);
        }

        @Override
        public GuidedSearchViewModel[] newArray(int size) {
            return new GuidedSearchViewModel[size];
        }
    };
}
