package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModel implements Parcelable {
    private OfficialStoreBannerModel officialStoreBannerModel;
    private List<ProductItem> productList = new ArrayList<>();
    private boolean hasCatalog;
    private String query;
    private String shareUrl;
    private String additionalParams;
    private SuggestionModel suggestionModel;
    private int totalData;
    private int totalItem;
    private boolean forceSearch;
    private boolean imageSearch;
    private DynamicFilterModel dynamicFilterModel;
    private GuidedSearchViewModel guidedSearchViewModel;
    private DataValue quickFilterModel;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;
    private RelatedSearchModel relatedSearchModel;
    private SearchParameter searchParameter;

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

    public GuidedSearchViewModel getGuidedSearchViewModel() {
        return guidedSearchViewModel;
    }

    public void setGuidedSearchViewModel(GuidedSearchViewModel guidedSearchViewModel) {
        this.guidedSearchViewModel = guidedSearchViewModel;
    }

    public DataValue getQuickFilterModel() {
        return quickFilterModel;
    }

    public void setQuickFilterModel(DataValue quickFilterModel) {
        this.quickFilterModel = quickFilterModel;
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

    public RelatedSearchModel getRelatedSearchModel() {
        return relatedSearchModel;
    }

    public void setRelatedSearchModel(RelatedSearchModel relatedSearchModel) {
        this.relatedSearchModel = relatedSearchModel;
    }

    public int getTotalItem() {
        return getProductList().size() + getAdsModel().getData().size();
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
        dest.writeInt(this.totalItem);
        dest.writeParcelable(this.searchParameter, flags);
        dest.writeByte(this.forceSearch ? (byte) 1 : (byte) 0);
        dest.writeByte(this.imageSearch ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.dynamicFilterModel, flags);
        dest.writeParcelable(this.guidedSearchViewModel, flags);
        dest.writeParcelable(this.quickFilterModel, flags);
        dest.writeParcelable(this.adsModel, flags);
        dest.writeParcelable(this.cpmModel, flags);
        dest.writeParcelable(this.relatedSearchModel, flags);
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
        this.totalItem = in.readInt();
        this.searchParameter = in.readParcelable(SearchParameter.class.getClassLoader());
        this.forceSearch = in.readByte() != 0;
        this.imageSearch = in.readByte() != 0;
        this.dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        this.guidedSearchViewModel = in.readParcelable(GuidedSearchViewModel.class.getClassLoader());
        this.quickFilterModel = in.readParcelable(DataValue.class.getClassLoader());
        this.adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        this.cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
        this.relatedSearchModel = in.readParcelable(RelatedSearchModel.class.getClassLoader());
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
