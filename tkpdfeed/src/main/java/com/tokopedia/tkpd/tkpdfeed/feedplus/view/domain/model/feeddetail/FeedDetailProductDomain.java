package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail;

import com.tkpdfeed.feeds.FeedDetail;

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
    Integer rating;

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
                                   Integer rating) {
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
}
