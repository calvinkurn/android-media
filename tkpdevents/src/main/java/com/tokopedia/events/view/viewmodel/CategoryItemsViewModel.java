package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class CategoryItemsViewModel implements Parcelable {

    private int id;
    private int parentId;
    private int categoryId;
    private int providerId;
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
    private int convenienceFee;
    private int mrp;
    private int salesPrice;
    private int seatChartTypeId;
    private int hasSeatLayout;
    private int form;
    private int priority;
    private int quantity;
    private int soldQuantity;
    private int sellRate;
    private int thumbsUp;
    private int thumbsDown;
    private int rating;
    private int isFeatured;
    private int isPromo;
    private int isFoodAvailable;
    private int isSearchable;
    private int isTop;
    private int usePdf;
    private int status;
    private int redirect;
    private int minStartDate;
    private int maxEndDate;
    private int saleStartDate;
    private int saleEndDate;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
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

    public int getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(int convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getSeatChartTypeId() {
        return seatChartTypeId;
    }

    public void setSeatChartTypeId(int seatChartTypeId) {
        this.seatChartTypeId = seatChartTypeId;
    }

    public int getHasSeatLayout() {
        return hasSeatLayout;
    }

    public void setHasSeatLayout(int hasSeatLayout) {
        this.hasSeatLayout = hasSeatLayout;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getSellRate() {
        return sellRate;
    }

    public void setSellRate(int sellRate) {
        this.sellRate = sellRate;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public int getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(int isPromo) {
        this.isPromo = isPromo;
    }

    public int getIsFoodAvailable() {
        return isFoodAvailable;
    }

    public void setIsFoodAvailable(int isFoodAvailable) {
        this.isFoodAvailable = isFoodAvailable;
    }

    public int getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(int isSearchable) {
        this.isSearchable = isSearchable;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getUsePdf() {
        return usePdf;
    }

    public void setUsePdf(int usePdf) {
        this.usePdf = usePdf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRedirect() {
        return redirect;
    }

    public void setRedirect(int redirect) {
        this.redirect = redirect;
    }

    public int getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(int minStartDate) {
        this.minStartDate = minStartDate;
    }

    public int getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(int maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public int getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(int saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public int getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(int saleEndDate) {
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
        dest.writeInt(this.id);
        dest.writeInt(this.parentId);
        dest.writeInt(this.categoryId);
        dest.writeInt(this.providerId);
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
        dest.writeInt(this.convenienceFee);
        dest.writeInt(this.mrp);
        dest.writeInt(this.salesPrice);
        dest.writeInt(this.seatChartTypeId);
        dest.writeInt(this.hasSeatLayout);
        dest.writeInt(this.form);
        dest.writeInt(this.priority);
        dest.writeInt(this.quantity);
        dest.writeInt(this.soldQuantity);
        dest.writeInt(this.sellRate);
        dest.writeInt(this.thumbsUp);
        dest.writeInt(this.thumbsDown);
        dest.writeInt(this.rating);
        dest.writeInt(this.isFeatured);
        dest.writeInt(this.isPromo);
        dest.writeInt(this.isFoodAvailable);
        dest.writeInt(this.isSearchable);
        dest.writeInt(this.isTop);
        dest.writeInt(this.usePdf);
        dest.writeInt(this.status);
        dest.writeInt(this.redirect);
        dest.writeInt(this.minStartDate);
        dest.writeInt(this.maxEndDate);
        dest.writeInt(this.saleStartDate);
        dest.writeInt(this.saleEndDate);
        dest.writeString(this.customLabels);
        dest.writeString(this.minStartTime);
        dest.writeString(this.maxEndTime);
        dest.writeString(this.saleStartTime);
        dest.writeString(this.saleEndTime);
        dest.writeValue(this.dateRange);
        dest.writeString(this.cityName);
    }

    protected CategoryItemsViewModel(Parcel in) {
        this.id = in.readInt();
        this.parentId = in.readInt();
        this.categoryId = in.readInt();
        this.providerId = in.readInt();
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
        this.convenienceFee = in.readInt();
        this.mrp = in.readInt();
        this.salesPrice = in.readInt();
        this.seatChartTypeId = in.readInt();
        this.hasSeatLayout = in.readInt();
        this.form = in.readInt();
        this.priority = in.readInt();
        this.quantity = in.readInt();
        this.soldQuantity = in.readInt();
        this.sellRate = in.readInt();
        this.thumbsUp = in.readInt();
        this.thumbsDown = in.readInt();
        this.rating = in.readInt();
        this.isFeatured = in.readInt();
        this.isPromo = in.readInt();
        this.isFoodAvailable = in.readInt();
        this.isSearchable = in.readInt();
        this.isTop = in.readInt();
        this.usePdf = in.readInt();
        this.status = in.readInt();
        this.redirect = in.readInt();
        this.minStartDate = in.readInt();
        this.maxEndDate = in.readInt();
        this.saleStartDate = in.readInt();
        this.saleEndDate = in.readInt();
        this.customLabels = in.readString();
        this.minStartTime = in.readString();
        this.maxEndTime = in.readString();
        this.saleStartTime = in.readString();
        this.saleEndTime = in.readString();
        this.dateRange = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.cityName = in.readString();
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
