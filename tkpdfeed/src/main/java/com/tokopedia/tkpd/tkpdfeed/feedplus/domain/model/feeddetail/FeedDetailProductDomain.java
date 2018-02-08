package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailProductDomain {
    private final
    @Nullable
    Integer id;

    private final
    @Nullable
    String name;

    private final
    @Nullable
    String price;

    private final
    @Nullable
    String image;

    private final
    @Nullable
    List<FeedDetailWholesaleDomain> wholesale;

    private final
    @Nullable
    Boolean freereturns;

    private final
    @Nullable
    Boolean preorder;

    private final
    @Nullable
    String cashback;

    private final
    @Nullable
    Object url;

    private final
    @Nullable
    String productLink;

    private final
    @Nullable
    Boolean wishlist;

    private final
    @Nullable
    double rating;

    public FeedDetailProductDomain(Integer id,
                                   String name,
                                   String price,
                                   String image,
                                   List<FeedDetailWholesaleDomain> wholesale,
                                   Boolean freereturns,
                                   Boolean preorder,
                                   String cashback,
                                   Object url,
                                   String productLink,
                                   Boolean wishlist,
                                   double rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.wholesale = wholesale;
        this.freereturns = freereturns;
        this.preorder = preorder;
        this.cashback = cashback;
        this.url = url;
        this.productLink = productLink;
        this.wishlist = wishlist;
        this.rating = rating;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @Nullable
    public List<FeedDetailWholesaleDomain> getWholesale() {
        return wholesale;
    }

    @Nullable
    public Boolean getFreereturns() {
        return freereturns;
    }

    @Nullable
    public Boolean getPreorder() {
        return preorder;
    }

    @Nullable
    public String getCashback() {
        return cashback;
    }

    @Nullable
    public Object getUrl() {
        return url;
    }

    @Nullable
    public String getProductLink() {
        return productLink;
    }

    @Nullable
    public Boolean getWishlist() {
        return wishlist;
    }

    @Nullable
    public double getRating() {
        return rating;
    }
}
