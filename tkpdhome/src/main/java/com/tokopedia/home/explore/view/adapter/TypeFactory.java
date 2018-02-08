package com.tokopedia.home.explore.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryFavoriteViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public interface TypeFactory extends AdapterTypeFactory {

    int type(CategoryGridListViewModel viewModel);

    int type(SellViewModel viewModel);

    int type(CategoryFavoriteViewModel viewModel);

    int type(MyShopViewModel viewModel);

}
