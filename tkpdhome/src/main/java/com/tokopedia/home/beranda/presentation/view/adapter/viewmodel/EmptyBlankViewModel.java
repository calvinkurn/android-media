package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * Created by meta on 22/03/18.
 */

public class EmptyBlankViewModel implements Visitable<HomeTypeFactory> {

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
