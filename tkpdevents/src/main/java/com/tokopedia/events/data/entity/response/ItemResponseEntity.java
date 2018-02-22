package com.tokopedia.events.data.entity.response;

/**
 * Created by ashwanityagi on 15/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemResponseEntity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
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
    private Integer convenienceFee;
    @SerializedName("mrp")
    @Expose
    private Integer mrp;
    @SerializedName("sales_price")
    @Expose
    private Integer salesPrice;
    @SerializedName("SeatChartTypeId")
    @Expose
    private Integer seatChartTypeId;
    @SerializedName("has_seat_layout")
    @Expose
    private Integer hasSeatLayout;
    @SerializedName("form")
    @Expose
    private Integer form;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("sold_quantity")
    @Expose
    private Integer soldQuantity;
    @SerializedName("sell_rate")
    @Expose
    private Integer sellRate;
    @SerializedName("thumbs_up")
    @Expose
    private Integer thumbsUp;
    @SerializedName("thumbs_down")
    @Expose
    private Integer thumbsDown;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("is_featured")
    @Expose
    private Integer isFeatured;
    @SerializedName("is_promo")
    @Expose
    private Integer isPromo;
    @SerializedName("is_food_available")
    @Expose
    private Integer isFoodAvailable;
    @SerializedName("is_searchable")
    @Expose
    private Integer isSearchable;
    @SerializedName("is_top")
    @Expose
    private Integer isTop;
    @SerializedName("use_pdf")
    @Expose
    private Integer usePdf;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("redirect")
    @Expose
    private Integer redirect;
    @SerializedName("min_start_date")
    @Expose
    private Integer minStartDate;
    @SerializedName("max_end_date")
    @Expose
    private Integer maxEndDate;
    @SerializedName("sale_start_date")
    @Expose
    private Integer saleStartDate;
    @SerializedName("sale_end_date")
    @Expose
    private Integer saleEndDate;
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
    @SerializedName("schedules")
    @Expose
    private Object schedules;
    @SerializedName("forms")
    @Expose
    private Object forms;

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

}