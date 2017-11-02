package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentProductViewModel implements Visitable<KolTypeFactory> {

    private final String imageUrl;
    private final String name;
    private final String price;
    private final boolean isWishlisted;

    public KolCommentProductViewModel(String imageUrl, String name, String price, boolean
            isWishlisted) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.isWishlisted = isWishlisted;
    }

    @Override
    public int type(KolTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


}
