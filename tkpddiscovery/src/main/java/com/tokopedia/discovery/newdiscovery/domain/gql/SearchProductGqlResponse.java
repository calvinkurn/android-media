package com.tokopedia.discovery.newdiscovery.domain.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.entity.discovery.GuidedSearchResponse;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.List;

public class SearchProductGqlResponse {

    @SerializedName("searchProduct")
    @Expose
    private SearchProduct searchProduct = new SearchProduct();

    @SerializedName("search_filter_product")
    @Expose
    private DynamicFilterModel dynamicFilterModel = new DynamicFilterModel();

    @SerializedName("ace_guide")
    @Expose
    private GuidedSearchResponse guidedSearchResponse = new GuidedSearchResponse();

    @SerializedName("quick_filter")
    @Expose
    private DataValue quickFilterModel = new DataValue();

    @SerializedName("productAds")
    @Expose
    private TopAdsModel topAdsModel = new TopAdsModel();

    @SerializedName("headlineAds")
    @Expose
    private CpmModel cpmModel = new CpmModel();

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }

    public SearchProduct getSearchProduct() {
        return searchProduct;
    }

    public DynamicFilterModel getDynamicFilterModel() {
        return dynamicFilterModel;
    }

    public GuidedSearchResponse getGuidedSearchResponse() {
        return guidedSearchResponse;
    }

    public DataValue getQuickFilterModel() {
        return quickFilterModel;
    }

    public TopAdsModel getTopAdsModel() {
        return topAdsModel;
    }

    public void setTopAdsModel(TopAdsModel topAdsModel) {
        this.topAdsModel = topAdsModel;
    }

    public static class SearchProduct {

        @SerializedName("query")
        @Expose
        private String query;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("shareUrl")
        @Expose
        private String shareUrl;
        @SerializedName("isFilter")
        @Expose
        private boolean isFilter;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("count_text")
        @Expose
        private String countText;
        @SerializedName("additional_params")
        @Expose
        private String additionalParams;
        @SerializedName("redirection")
        @Expose
        private Redirection redirection = new Redirection();
        @SerializedName("suggestion")
        @Expose
        private Suggestion suggestion = new Suggestion();
        @SerializedName("related")
        @Expose
        private Related related = new Related();
        @SerializedName("products")
        @Expose
        private List<Product> products = null;
        @SerializedName("catalogs")
        @Expose
        private List<Catalog> catalogs = null;

        public String getQuery() {
            return query;
        }

        public String getSource() {
            return source;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public boolean isFilter() {
            return isFilter;
        }

        public int getCount() {
            return count;
        }

        public String getCountText() {
            return countText;
        }

        public String getAdditionalParams() {
            return additionalParams;
        }

        public Redirection getRedirection() {
            return redirection;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public List<Product> getProducts() {
            return products;
        }

        public List<Catalog> getCatalogs() {
            return catalogs;
        }

        public Related getRelated() {
            return related;
        }
    }

    public static class Related {
        @SerializedName("related_keyword")
        @Expose
        private String relatedKeyword;

        @SerializedName("other_related")
        @Expose
        private List<OtherRelated> otherRelated;

        public String getRelatedKeyword() {
            return relatedKeyword;
        }

        public List<OtherRelated> getOtherRelated() {
            return otherRelated;
        }
    }

    public static class OtherRelated {
        @SerializedName("keyword")
        private String keyword;
        @SerializedName("url")
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
    }

    public static class Catalog {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("price_raw")
        @Expose
        private String rawPrice;
        @SerializedName("price_min")
        @Expose
        private String minPrice;
        @SerializedName("price_max")
        @Expose
        private String maxPrice;
        @SerializedName("price_min_raw")
        @Expose
        private String rawMinPrice;
        @SerializedName("price_max_raw")
        @Expose
        private String rawMaxPrice;
        @SerializedName("count_product")
        @Expose
        private int count;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("department_id")
        @Expose
        private int departmentId;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getRawPrice() {
            return rawPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public String getRawMinPrice() {
            return rawMinPrice;
        }

        public String getRawMaxPrice() {
            return rawMaxPrice;
        }

        public int getCount() {
            return count;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUrl() {
            return url;
        }

        public int getDepartmentId() {
            return departmentId;
        }
    }

    public static class Suggestion {

        @SerializedName("currentKeyword")
        @Expose
        private String currentKeyword;
        @SerializedName("suggestion")
        @Expose
        private String suggestion;
        @SerializedName("suggestionCount")
        @Expose
        private int suggestionCount;
        @SerializedName("instead")
        @Expose
        private String instead;
        @SerializedName("insteadCount")
        @Expose
        private int insteadCount;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("query")
        @Expose
        private String query;

        public String getCurrentKeyword() {
            return currentKeyword;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public int getSuggestionCount() {
            return suggestionCount;
        }

        public String getInstead() {
            return instead;
        }

        public int getInsteadCount() {
            return insteadCount;
        }

        public String getText() {
            return text;
        }

        public String getQuery() {
            return query;
        }
    }

    public static class Product {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("childs")
        @Expose
        private Object childs = new Object();
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("image_url_700")
        @Expose
        private String imageUrlLarge;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("price_range")
        @Expose
        private String priceRange;
        @SerializedName("top_label")
        @Expose
        private List<String> topLabel;
        @SerializedName("bottom_label")
        @Expose
        private List<String> bottomLabel;
        @SerializedName("whole_sale_price")
        @Expose
        private List<WholeSalePrice> wholeSalePrice = null;
        @SerializedName("courier_count")
        @Expose
        private int courierCount;
        @SerializedName("condition")
        @Expose
        private int condition;
        @SerializedName("category_id")
        @Expose
        private int categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_breadcrumb")
        @Expose
        private String categoryBreadcrumb;
        @SerializedName("department_id")
        @Expose
        private int departmentId;
        @SerializedName("department_name")
        @Expose
        private String departmentName;
        @SerializedName("labels")
        @Expose
        private List<Label> labels = null;
        @SerializedName("badges")
        @Expose
        private List<Badge> badges = null;
        @SerializedName("is_featured")
        @Expose
        private int isFeatured;
        @SerializedName("rating")
        @Expose
        private int rating;
        @SerializedName("count_review")
        @Expose
        private int countReview;
        @SerializedName("original_price")
        @Expose
        private String originalPrice;
        @SerializedName("discount_expired_time")
        @Expose
        private String discountExpiredTime;
        @SerializedName("discount_start_time")
        @Expose
        private String discountStartTime;
        @SerializedName("discount_percentage")
        @Expose
        private int discountPercentage;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("stock")
        @Expose
        private int stock;
        @SerializedName("ga_key")
        @Expose
        private String gaKey;
        @SerializedName("is_preorder")
        @Expose
        private boolean preorder;
        @SerializedName("wishlist")
        @Expose
        private boolean wishlist;
        @SerializedName("shop")
        @Expose
        private Shop shop = new Shop();

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Object getChilds() {
            return childs;
        }

        public String getUrl() {
            return url;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageUrlLarge() {
            return imageUrlLarge;
        }

        public String getPrice() {
            return price;
        }

        public List<WholeSalePrice> getWholeSalePrice() {
            return wholeSalePrice;
        }

        public int getCourierCount() {
            return courierCount;
        }

        public int getCondition() {
            return condition;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCategoryBreadcrumb() {
            return categoryBreadcrumb;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public List<Label> getLabels() {
            return labels;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public int getIsFeatured() {
            return isFeatured;
        }

        public int getRating() {
            return rating;
        }

        public int getCountReview() {
            return countReview;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public String getDiscountExpiredTime() {
            return discountExpiredTime;
        }

        public String getDiscountStartTime() {
            return discountStartTime;
        }

        public int getDiscountPercentage() {
            return discountPercentage;
        }

        public String getSku() {
            return sku;
        }

        public int getStock() {
            return stock;
        }

        public String getGaKey() {
            return gaKey;
        }

        public boolean isPreorder() {
            return preorder;
        }

        public boolean isWishlist() {
            return wishlist;
        }

        public String getPriceRange() {
            return priceRange;
        }

        public List<String> getTopLabel() {
            return topLabel;
        }

        public List<String> getBottomLabel() {
            return bottomLabel;
        }

        public Shop getShop() {
            return shop;
        }
    }

    public static class WholeSalePrice {
        @SerializedName("quantity_min")
        @Expose
        private int quantityMin;
        @SerializedName("quantity_max")
        @Expose
        private int quantityMax;
        @SerializedName("price")
        @Expose
        private String price;

        public int getQuantityMin() {
            return quantityMin;
        }

        public int getQuantityMax() {
            return quantityMax;
        }

        public String getPrice() {
            return price;
        }
    }

    public static class Label {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("color")
        @Expose
        private String color;

        public String getTitle() {
            return title;
        }

        public String getColor() {
            return color;
        }
    }

    public static class Badge {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("show")
        @Expose
        private boolean isShown;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public boolean isShown() {
            return isShown;
        }
    }

    public static class Redirection {

        @SerializedName("redirect_url")
        @Expose
        private String redirectUrl;
        @SerializedName("department_id")
        @Expose
        private String departmentId;
        @SerializedName("redirect_applink")
        @Expose
        private String redirectApplink;

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public String getRedirectApplink() {
            return redirectApplink;
        }
    }

    public static class Shop {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("reputation")
        @Expose
        private String reputation;
        @SerializedName("clover")
        @Expose
        private String clover;
        @SerializedName("is_gold_shop")
        @Expose
        private boolean goldmerchant;
        @SerializedName("is_official")
        @Expose
        private boolean official;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getLocation() {
            return location;
        }

        public String getCity() {
            return city;
        }

        public String getReputation() {
            return reputation;
        }

        public String getClover() {
            return clover;
        }

        public boolean isGoldmerchant() {
            return goldmerchant;
        }

        public boolean isOfficial() {
            return official;
        }
    }
}
