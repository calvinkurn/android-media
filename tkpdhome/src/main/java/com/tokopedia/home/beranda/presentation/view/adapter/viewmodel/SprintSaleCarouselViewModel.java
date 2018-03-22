package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * Created by errysuprayogi on 3/22/18.
 */

public class SprintSaleCarouselViewModel implements Visitable<HomeTypeFactory>{

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
