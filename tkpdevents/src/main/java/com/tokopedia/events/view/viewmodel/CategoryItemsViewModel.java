package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class CategoryItemsViewModel implements Parcelable {

    private Integer id;
    private Integer parentId;
    private Integer categoryId;
    private Integer providerId;
    private String childCategoryIds;
    private String cityIds;
    private String providerProductId;
    private String providerProductCode;
    private String providerProductName;
    private String displayName;
    private String title;
    private String actionText;
    private String censor;
    private String genre;
    private String duration;
    private String url;
    private String seoUrl;
    private String imageWeb;
    private String thumbnailWeb;
    private String imageApp;
    private String thumbnailApp;
    private String tnc;
    private String offerText;
    private String shortDesc;
    private String longRichDesc;
    private String salientFeatures;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String searchTags;
    private String displayTags;
    private String promotionText;
    private String autocode;
    private Integer convenienceFee;
    private Integer mrp;
    private Integer salesPrice;
    private Integer seatChartTypeId;
    private Integer hasSeatLayout;
    private Integer form;
    private Integer priority;
    private Integer quantity;
    private Integer soldQuantity;
    private Integer sellRate;
    private Integer thumbsUp;
    private Integer thumbsDown;
    private Integer rating;
    private Integer isFeatured;
    private Integer isPromo;
    private Integer isFoodAvailable;
    private Integer isSearchable;
    private Integer isTop;
    private Integer usePdf;
    private Integer status;
    private Integer redirect;
    private Integer minStartDate;
    private Integer maxEndDate;
    private Integer saleStartDate;
    private Integer saleEndDate;
    private String customLabels;
    private String minStartTime;
    private String maxEndTime;
    private String saleStartTime;
    private String saleEndTime;
    private Boolean dateRange;
    private String cityName;
    private Object schedules;
    private Object forms;

    public CategoryItemsViewModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getChildCategoryIds() {
        return childCategoryIds;
    }

    public void setChildCategoryIds(String childCategoryIds) {
        this.childCategoryIds = childCategoryIds;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String getProviderProductId() {
        return providerProductId;
    }

    public void setProviderProductId(String providerProductId) {
        this.providerProductId = providerProductId;
    }

    public String getProviderProductCode() {
        return providerProductCode;
    }

    public void setProviderProductCode(String providerProductCode) {
        this.providerProductCode = providerProductCode;
    }

    public String getProviderProductName() {
        return providerProductName;
    }

    public void setProviderProductName(String providerProductName) {
        this.providerProductName = providerProductName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getCensor() {
        return censor;
    }

    public void setCensor(String censor) {
        this.censor = censor;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getImageWeb() {
        return imageWeb;
    }

    public void setImageWeb(String imageWeb) {
        this.imageWeb = imageWeb;
    }

    public String getThumbnailWeb() {
        return thumbnailWeb;
    }

    public void setThumbnailWeb(String thumbnailWeb) {
        this.thumbnailWeb = thumbnailWeb;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public String getThumbnailApp() {
        return thumbnailApp;
    }

    public void setThumbnailApp(String thumbnailApp) {
        this.thumbnailApp = thumbnailApp;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongRichDesc() {
        return longRichDesc;
    }

    public void setLongRichDesc(String longRichDesc) {
        this.longRichDesc = longRichDesc;
    }

    public String getSalientFeatures() {
        return salientFeatures;
    }

    public void setSalientFeatures(String salientFeatures) {
        this.salientFeatures = salientFeatures;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getSearchTags() {
        return searchTags;
    }

    public void setSearchTags(String searchTags) {
        this.searchTags = searchTags;
    }

    public String getDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(String displayTags) {
        this.displayTags = displayTags;
    }

    public String getPromotionText() {
        return promotionText;
    }

    public void setPromotionText(String promotionText) {
        this.promotionText = promotionText;
    }

    public String getAutocode() {
        return autocode;
    }

    public void setAutocode(String autocode) {
        this.autocode = autocode;
    }

    public Integer getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Integer convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Integer getSeatChartTypeId() {
        return seatChartTypeId;
    }

    public void setSeatChartTypeId(Integer seatChartTypeId) {
        this.seatChartTypeId = seatChartTypeId;
    }

    public Integer getHasSeatLayout() {
        return hasSeatLayout;
    }

    public void setHasSeatLayout(Integer hasSeatLayout) {
        this.hasSeatLayout = hasSeatLayout;
    }

    public Integer getForm() {
        return form;
    }

    public void setForm(Integer form) {
        this.form = form;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getSellRate() {
        return sellRate;
    }

    public void setSellRate(Integer sellRate) {
        this.sellRate = sellRate;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public Integer getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(Integer thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(Integer isPromo) {
        this.isPromo = isPromo;
    }

    public Integer getIsFoodAvailable() {
        return isFoodAvailable;
    }

    public void setIsFoodAvailable(Integer isFoodAvailable) {
        this.isFoodAvailable = isFoodAvailable;
    }

    public Integer getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(Integer isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getUsePdf() {
        return usePdf;
    }

    public void setUsePdf(Integer usePdf) {
        this.usePdf = usePdf;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRedirect() {
        return redirect;
    }

    public void setRedirect(Integer redirect) {
        this.redirect = redirect;
    }

    public Integer getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(Integer minStartDate) {
        this.minStartDate = minStartDate;
    }

    public Integer getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(Integer maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public Integer getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(Integer saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public Integer getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(Integer saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public String getCustomLabels() {
        return customLabels;
    }

    public void setCustomLabels(String customLabels) {
        this.customLabels = customLabels;
    }

    public String getMinStartTime() {
        return minStartTime;
    }

    public void setMinStartTime(String minStartTime) {
        this.minStartTime = minStartTime;
    }

    public String getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(String maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    public String getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(String saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public String getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(String saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Boolean getDateRange() {
        return dateRange;
    }

    public void setDateRange(Boolean dateRange) {
        this.dateRange = dateRange;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Object getSchedules() {
        return schedules;
    }

    public void setSchedules(Object schedules) {
        this.schedules = schedules;
    }

    public Object getForms() {
        return forms;
    }

    public void setForms(Object forms) {
        this.forms = forms;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.parentId);
        dest.writeValue(this.categoryId);
        dest.writeValue(this.providerId);
        dest.writeString(this.childCategoryIds);
        dest.writeString(this.cityIds);
        dest.writeString(this.providerProductId);
        dest.writeString(this.providerProductCode);
        dest.writeString(this.providerProductName);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeString(this.actionText);
        dest.writeString(this.censor);
        dest.writeString(this.genre);
        dest.writeString(this.duration);
        dest.writeString(this.url);
        dest.writeString(this.seoUrl);
        dest.writeString(this.imageWeb);
        dest.writeString(this.thumbnailWeb);
        dest.writeString(this.imageApp);
        dest.writeString(this.thumbnailApp);
        dest.writeString(this.tnc);
        dest.writeString(this.offerText);
        dest.writeString(this.shortDesc);
        dest.writeString(this.longRichDesc);
        dest.writeString(this.salientFeatures);
        dest.writeString(this.metaTitle);
        dest.writeString(this.metaDescription);
        dest.writeString(this.metaKeywords);
        dest.writeString(this.searchTags);
        dest.writeString(this.displayTags);
        dest.writeString(this.promotionText);
        dest.writeString(this.autocode);
        dest.writeValue(this.convenienceFee);
        dest.writeValue(this.mrp);
        dest.writeValue(this.salesPrice);
        dest.writeValue(this.seatChartTypeId);
        dest.writeValue(this.hasSeatLayout);
        dest.writeValue(this.form);
        dest.writeValue(this.priority);
        dest.writeValue(this.quantity);
        dest.writeValue(this.soldQuantity);
        dest.writeValue(this.sellRate);
        dest.writeValue(this.thumbsUp);
        dest.writeValue(this.thumbsDown);
        dest.writeValue(this.rating);
        dest.writeValue(this.isFeatured);
        dest.writeValue(this.isPromo);
        dest.writeValue(this.isFoodAvailable);
        dest.writeValue(this.isSearchable);
        dest.writeValue(this.isTop);
        dest.writeValue(this.usePdf);
        dest.writeValue(this.status);
        dest.writeValue(this.redirect);
        dest.writeValue(this.minStartDate);
        dest.writeValue(this.maxEndDate);
        dest.writeValue(this.saleStartDate);
        dest.writeValue(this.saleEndDate);
        dest.writeString(this.customLabels);
        dest.writeString(this.minStartTime);
        dest.writeString(this.maxEndTime);
        dest.writeString(this.saleStartTime);
        dest.writeString(this.saleEndTime);
        dest.writeValue(this.dateRange);
        dest.writeString(this.cityName);
        //dest.writeParcelable(this.schedules, flags);
        //dest.writeParcelable(this.forms, flags);
    }

    protected CategoryItemsViewModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.parentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.categoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.providerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.childCategoryIds = in.readString();
        this.cityIds = in.readString();
        this.providerProductId = in.readString();
        this.providerProductCode = in.readString();
        this.providerProductName = in.readString();
        this.displayName = in.readString();
        this.title = in.readString();
        this.actionText = in.readString();
        this.censor = in.readString();
        this.genre = in.readString();
        this.duration = in.readString();
        this.url = in.readString();
        this.seoUrl = in.readString();
        this.imageWeb = in.readString();
        this.thumbnailWeb = in.readString();
        this.imageApp = in.readString();
        this.thumbnailApp = in.readString();
        this.tnc = in.readString();
        this.offerText = in.readString();
        this.shortDesc = in.readString();
        this.longRichDesc = in.readString();
        this.salientFeatures = in.readString();
        this.metaTitle = in.readString();
        this.metaDescription = in.readString();
        this.metaKeywords = in.readString();
        this.searchTags = in.readString();
        this.displayTags = in.readString();
        this.promotionText = in.readString();
        this.autocode = in.readString();
        this.convenienceFee = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mrp = (Integer) in.readValue(Integer.class.getClassLoader());
        this.salesPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.seatChartTypeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.hasSeatLayout = (Integer) in.readValue(Integer.class.getClassLoader());
        this.form = (Integer) in.readValue(Integer.class.getClassLoader());
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.soldQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sellRate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.thumbsUp = (Integer) in.readValue(Integer.class.getClassLoader());
        this.thumbsDown = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rating = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isFeatured = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isPromo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isFoodAvailable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isSearchable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isTop = (Integer) in.readValue(Integer.class.getClassLoader());
        this.usePdf = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.redirect = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minStartDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxEndDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleStartDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleEndDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.customLabels = in.readString();
        this.minStartTime = in.readString();
        this.maxEndTime = in.readString();
        this.saleStartTime = in.readString();
        this.saleEndTime = in.readString();
        this.dateRange = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.cityName = in.readString();
       // this.schedules = in.readParcelable(Object.class.getClassLoader());
       // this.forms = in.readParcelable(Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryItemsViewModel> CREATOR = new Parcelable.Creator<CategoryItemsViewModel>() {
        @Override
        public CategoryItemsViewModel createFromParcel(Parcel source) {
            return new CategoryItemsViewModel(source);
        }

        @Override
        public CategoryItemsViewModel[] newArray(int size) {
            return new CategoryItemsViewModel[size];
        }
    };
}
