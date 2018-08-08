package com.tokopedia.discovery.newdiscovery.data.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.discovery.SearchProductResponse;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopMapper implements Func1<Response<BrowseShopModel>, ShopViewModel> {

    private final Gson gson;

    public ShopMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public ShopViewModel call(Response<BrowseShopModel> response) {
        if (response.isSuccessful()) {
            BrowseShopModel browseShopModel = response.body();
            if (browseShopModel != null) {
                return mapPojoIntoDomain(browseShopModel);
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private ShopViewModel mapPojoIntoDomain(BrowseShopModel browseShopModel) {
        ShopViewModel viewModel = new ShopViewModel();

        List<ShopViewModel.ShopItem> shopItemList = new ArrayList<>();
        for (BrowseShopModel.Shops shop : browseShopModel.result.shops) {
            shopItemList.add(convertToShopItem(shop));
        }

        viewModel.setShopItemList(shopItemList);
        viewModel.setHasNextPage(!TextUtils.isEmpty(browseShopModel.result.paging.getUriNext()));
        viewModel.setQuery(browseShopModel.result.query);
        return viewModel;
    }

    private ShopViewModel.ShopItem convertToShopItem(BrowseShopModel.Shops shop) {
        ShopViewModel.ShopItem shopItem = new ShopViewModel.ShopItem();
        shopItem.setShopGoldShop(shop.shopGoldShop);
        shopItem.setShopDescription(shop.shopDescription);
        shopItem.setShopLucky(shop.shopLucky);
        shopItem.setShopId(shop.shopId);
        shopItem.setReputationScore(shop.reputationScore);
        shopItem.setShopDomain(shop.shopDomain);
        shopItem.setShopImage(shop.shopImage);
        shopItem.setShopTotalFavorite(shop.shopTotalFavorite);
        shopItem.setShopTotalTransaction(shop.shopTotalTransaction);
        shopItem.setShopRateAccuracy(shop.shopRateAccuracy);
        shopItem.setShopImage300(shop.shopImage300);
        shopItem.setShopName(shop.shopName);
        shopItem.setShopIsImg(shop.shopIsImg);
        shopItem.setShopLocation(shop.shopLocation);
        shopItem.setReputationImageUri(shop.reputationImageUri);
        shopItem.setShopRateService(shop.shopRateService);
        shopItem.setShopIsOwner(shop.shopIsOwner);
        shopItem.setShopUrl(shop.shopUrl);
        shopItem.setShopRateSpeed(shop.shopRateSpeed);
        shopItem.setShopTagLine(shop.shopTagLine);
        shopItem.setShopStatus(shop.shopStatus);
        shopItem.setOfficial(shop.isOfficial);

        List<String> productImages = new ArrayList<>();
        productImages.addAll(shop.productImages);
        shopItem.setProductImages(productImages);

        return shopItem;
    }
}
