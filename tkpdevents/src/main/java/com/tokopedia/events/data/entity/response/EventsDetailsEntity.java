
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsDetailsEntity {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("parent_id")
    @Expose
    private int parentId;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("child_category_ids")
    @Expose
    private String childCategoryIds;
    @SerializedName("city_ids")
    @Expose
    private String cityIds;
    @SerializedName("provider_product_id")
    @Expose
    private String providerProductId;
    @SerializedName("provider_product_code")
    @Expose
    private String providerProductCode;
    @SerializedName("provider_product_name")
    @Expose
    private String providerProductName;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("action_text")
    @Expose
    private String actionText;
    @SerializedName("censor")
    @Expose
    private String censor;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("seo_url")
    @Expose
    private String seoUrl;
    @SerializedName("image_web")
    @Expose
    private String imageWeb;
    @SerializedName("thumbnail_web")
    @Expose
    private String thumbnailWeb;
    @SerializedName("image_app")
    @Expose
    private String imageApp;
    @SerializedName("thumbnail_app")
    @Expose
    private String thumbnailApp;
    @SerializedName("tnc")
    @Expose
    private String tnc;
    @SerializedName("offer_text")
    @Expose
    private String offerText;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("long_rich_desc")
    @Expose
    private String longRichDesc;
    @SerializedName("salient_features")
    @Expose
    private String salientFeatures;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("meta_keywords")
    @Expose
    private String metaKeywords;
    @SerializedName("search_tags")
    @Expose
    private String searchTags;
    @SerializedName("display_tags")
    @Expose
    private String displayTags;
    @SerializedName("promotion_text")
    @Expose
    private String promotionText;
    @SerializedName("autocode")
    @Expose
    private String autocode;
    @SerializedName("convenience_fee")
    @Expose
    private int convenienceFee;
    @SerializedName("mrp")
    @Expose
    private int mrp;
    @SerializedName("sales_price")
    @Expose
    private int salesPrice;
    @SerializedName("SeatChartTypeId")
    @Expose
    private int seatChartTypeId;
    @SerializedName("has_seat_layout")
    @Expose
    private int hasSeatLayout;
    @SerializedName("form")
    @Expose
    private int form;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("sold_quantity")
    @Expose
    private int soldQuantity;
    @SerializedName("sell_rate")
    @Expose
    private int sellRate;
    @SerializedName("thumbs_up")
    @Expose
    private int thumbsUp;
    @SerializedName("thumbs_down")
    @Expose
    private int thumbsDown;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("is_featured")
    @Expose
    private int isFeatured;
    @SerializedName("is_promo")
    @Expose
    private int isPromo;
    @SerializedName("is_food_available")
    @Expose
    private int isFoodAvailable;
    @SerializedName("is_searchable")
    @Expose
    private int isSearchable;
    @SerializedName("is_top")
    @Expose
    private int isTop;
    @SerializedName("use_pdf")
    @Expose
    private int usePdf;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("redirect")
    @Expose
    private int redirect;
    @SerializedName("min_start_date")
    @Expose
    private int minStartDate;
    @SerializedName("max_end_date")
    @Expose
    private int maxEndDate;
    @SerializedName("sale_start_date")
    @Expose
    private int saleStartDate;
    @SerializedName("sale_end_date")
    @Expose
    private int saleEndDate;
    @SerializedName("custom_labels")
    @Expose
    private String customLabels;
    @SerializedName("min_start_time")
    @Expose
    private String minStartTime;
    @SerializedName("max_end_time")
    @Expose
    private String maxEndTime;
    @SerializedName("sale_start_time")
    @Expose
    private String saleStartTime;
    @SerializedName("sale_end_time")
    @Expose
    private String saleEndTime;
    @SerializedName("date_range")
    @Expose
    private Boolean dateRange;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("seatmap_image")
    @Expose
    private String seatmapImage;
    @SerializedName("schedules")
    @Expose
    private List<Schedule> schedules = null;
    @SerializedName("forms")
    @Expose
    private List<Form> forms = null;

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

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public String getSeatmapImage() {
        return seatmapImage;
    }

    public void setSeatmapImage(String seatmapImage) {
        this.seatmapImage = seatmapImage;
    }
}
