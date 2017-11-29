package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModel implements Parcelable {
    private OfficialStoreBannerModel officialStoreBannerModel;
    private List<ProductItem> productList;
    private boolean hasCatalog;
    private String query;
    private String shareUrl;
    private String additionalParams;
    private SuggestionModel suggestionModel;
    private int totalData;
    private SearchParameter searchParameter;
    private boolean forceSearch;

    public ProductViewModel() {
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public OfficialStoreBannerModel getOfficialStoreBannerModel() {
        return officialStoreBannerModel;
    }

    public void setOfficialStoreBannerModel(OfficialStoreBannerModel officialStoreBannerModel) {
        this.officialStoreBannerModel = officialStoreBannerModel;
    }


    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public SuggestionModel getSuggestionModel() {
        return suggestionModel;
    }

    public void setSuggestionModel(SuggestionModel suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    public boolean isForceSearch() {
        return forceSearch;
    }

    public void setForceSearch(boolean forceSearch) {
        this.forceSearch = forceSearch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.officialStoreBannerModel, flags);
        dest.writeTypedList(this.productList);
        dest.writeByte(this.hasCatalog ? (byte) 1 : (byte) 0);
        dest.writeString(this.query);
        dest.writeString(this.shareUrl);
        dest.writeString(this.additionalParams);
        dest.writeParcelable(this.suggestionModel, flags);
        dest.writeInt(this.totalData);
        dest.writeParcelable(this.searchParameter, flags);
        dest.writeByte(this.forceSearch ? (byte) 1 : (byte) 0);
    }

    protected ProductViewModel(Parcel in) {
        this.officialStoreBannerModel = in.readParcelable(OfficialStoreBannerModel.class.getClassLoader());
        this.productList = in.createTypedArrayList(ProductItem.CREATOR);
        this.hasCatalog = in.readByte() != 0;
        this.query = in.readString();
        this.shareUrl = in.readString();
        this.additionalParams = in.readString();
        this.suggestionModel = in.readParcelable(SuggestionModel.class.getClassLoader());
        this.totalData = in.readInt();
        this.searchParameter = in.readParcelable(SearchParameter.class.getClassLoader());
        this.forceSearch = in.readByte() != 0;
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel source) {
            return new ProductViewModel(source);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };
}
