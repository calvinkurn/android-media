package com.tokopedia.topads.sdk.data;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 4/14/17.
 */

public class ModelConverter {

    public static ProductGridViewModel convertToProductGridViewModel(Data data) {
        ProductGridViewModel viewModel = new ProductGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ProductListViewModel convertToProductListViewModel(Data data) {
        ProductListViewModel viewModel = new ProductListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopGridViewModel convertToShopGridViewModel(Data data) {
        ShopGridViewModel viewModel = new ShopGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopListViewModel convertToShopListViewModel(Data data) {
        ShopListViewModel viewModel = new ShopListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static void convertList(List<Item> list, int displayMode){
        for (int i = 0; i < list.size(); i++) {
            Item visitable = list.get(i);
            if (displayMode == DisplayMode.GRID && visitable instanceof ProductListViewModel) {
                list.set(i, ModelConverter.convertToProductGridViewModel(((ProductListViewModel) list.get(i)).getData()));
            } else if (displayMode == DisplayMode.GRID && visitable instanceof ShopListViewModel) {
                list.set(i, ModelConverter.convertToShopGridViewModel(((ShopListViewModel) list.get(i)).getData()));
            } else if (displayMode == DisplayMode.LIST && visitable instanceof ProductGridViewModel) {
                list.set(i, ModelConverter.convertToProductListViewModel(((ProductGridViewModel) list.get(i)).getData()));
            } else if (displayMode == DisplayMode.LIST && visitable instanceof ShopGridViewModel) {
                list.set(i, ModelConverter.convertToShopListViewModel(((ShopGridViewModel) list.get(i)).getData()));
            }
        }
    }
}
