package com.tokopedia.home.explore.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListViewModel implements Visitable<TypeFactory> {

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
