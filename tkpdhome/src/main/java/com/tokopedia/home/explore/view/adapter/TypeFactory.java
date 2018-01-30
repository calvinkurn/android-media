package com.tokopedia.home.explore.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public interface TypeFactory extends AdapterTypeFactory {

    int type(CategoryGridListViewModel viewModel);

}
