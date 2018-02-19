package com.tokopedia.shop.address.view.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoAddress;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class ShopAddressViewModelMapper {

    @Inject
    public ShopAddressViewModelMapper() {
    }

    public List<Visitable> transform(ShopInfo shopInfo) {
        List<Visitable> visitableList = new ArrayList<>();
        for (ShopInfoAddress shopInfoAddress : shopInfo.getAddress()) {
            ShopAddressViewModel shopAddressViewModel = new ShopAddressViewModel();
            shopAddressViewModel.setId(shopInfoAddress.getLocationAddressId());
            shopAddressViewModel.setName(shopInfoAddress.getLocationAddressName());
            shopAddressViewModel.setContent(shopInfoAddress.getLocationAddress());
            shopAddressViewModel.setArea(shopInfoAddress.getLocationArea());
            shopAddressViewModel.setEmail(shopInfoAddress.getLocationEmail());
            shopAddressViewModel.setPhone(shopInfoAddress.getLocationPhone());
            shopAddressViewModel.setFax(shopInfoAddress.getLocationFax());
            visitableList.add(shopAddressViewModel);
        }
        return visitableList;
    }
}
