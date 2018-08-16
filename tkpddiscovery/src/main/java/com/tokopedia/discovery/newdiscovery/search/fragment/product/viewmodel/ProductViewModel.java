package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

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
    private boolean imageSearch;
    private DynamicFilterModel dynamicFilterModel;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;

    protected ProductViewModel(Parcel in) {
        officialStoreBannerModel = in.readParcelable(OfficialStoreBannerModel.class.getClassLoader());
        productList = in.createTypedArrayList(ProductItem.CREATOR);
        hasCatalog = in.readByte() != 0;
        query = in.readString();
        shareUrl = in.readString();
        additionalParams = in.readString();
        suggestionModel = in.readParcelable(SuggestionModel.class.getClassLoader());
        totalData = in.readInt();
        searchParameter = in.readParcelable(SearchParameter.class.getClassLoader());
        forceSearch = in.readByte() != 0;
        imageSearch = in.readByte() != 0;
        dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(officialStoreBannerModel, flags);
        dest.writeTypedList(productList);
        dest.writeByte((byte) (hasCatalog ? 1 : 0));
        dest.writeString(query);
        dest.writeString(shareUrl);
        dest.writeString(additionalParams);
        dest.writeParcelable(suggestionModel, flags);
        dest.writeInt(totalData);
        dest.writeParcelable(searchParameter, flags);
        dest.writeByte((byte) (forceSearch ? 1 : 0));
        dest.writeByte((byte) (imageSearch ? 1 : 0));
        dest.writeParcelable(dynamicFilterModel, flags);
        dest.writeParcelable(adsModel, flags);
        dest.writeParcelable(cpmModel, flags);
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel in) {
            return new ProductViewModel(in);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };

    public TopAdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(TopAdsModel adsModel) {
        this.adsModel = adsModel;
    }

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }

    public boolean isImageSearch() {
        return imageSearch;
    }

    public void setImageSearch(boolean imageSearch) {
        this.imageSearch = imageSearch;
    }

    public DynamicFilterModel getDynamicFilterModel() {
        return dynamicFilterModel;
    }

    public void setDynamicFilterModel(DynamicFilterModel dynamicFilterModel) {
        this.dynamicFilterModel = dynamicFilterModel;
    }

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


}
