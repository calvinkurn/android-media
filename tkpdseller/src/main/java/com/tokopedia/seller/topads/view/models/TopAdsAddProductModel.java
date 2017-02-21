package com.tokopedia.seller.topads.view.models;

import com.tokopedia.seller.topads.domain.model.ProductDomain;

/**
 * Created by normansyahputa on 2/13/17.
 */

public class TopAdsAddProductModel extends StateTypeBasedModel {
    public static final int TYPE = 1292394;

    public String imageUrl;
    public String description;
    public String snippet;
    public ProductDomain productDomain;

    private TopAdsAddProductModel() {
        super(TYPE);
    }

    public TopAdsAddProductModel(String imageUrl, String description, String snippet) {
        this();
        this.imageUrl = imageUrl;
        this.description = description;
        this.snippet = snippet;
    }

    public TopAdsAddProductModel(String imageUrl, String description, String snippet,
                                 ProductDomain productDomain) {
        this(imageUrl, description, snippet);
        this.productDomain = productDomain;
    }
}
