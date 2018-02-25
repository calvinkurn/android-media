package com.tokopedia.shop.product.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopParamApiConstant;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.mapper.ShopNoteViewModelMapper;
import com.tokopedia.shop.product.data.source.cloud.model.Badge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<BaseListViewListener<ShopProductViewModel>> {

    private static final String BADGGE_FREE_RETURN = "Free Return";

    private final GetShopProductListUseCase getShopProductListUseCase;

    @Inject
    public ShopProductListPresenter(GetShopProductListUseCase getShopProductListUseCase) {
        this.getShopProductListUseCase = getShopProductListUseCase;
    }

    public void getShopPageList(
            String shopId,
            String keyword,
            String etalaseId,
            int wholesale,
            int page,
            int orderBy) {
        ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(shopId, keyword, etalaseId, wholesale, page, orderBy);
        getShopProductListUseCase.execute(GetShopProductListUseCase.createRequestParam(shopProductRequestModel), new Subscriber<ShopProductList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopProductList shopProductList) {
                if (!isViewAttached())
                    return;

                getView().renderList(convert(shopProductList.getList()), checkNextPage(shopProductList));
            }
        });
    }

    @Deprecated
    public void getShopPageList(String shopId) {
        ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(shopId);
        getShopProductListUseCase.execute(GetShopProductListUseCase.createRequestParam(shopProductRequestModel), new Subscriber<ShopProductList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopProductList shopProductList) {
                if (!isViewAttached())
                    return;

                getView().renderList(convert(shopProductList.getList()));
            }
        });
    }

    @NonNull
    protected ShopProductRequestModel getShopProductRequestModel(String shopId) {
        return getShopProductRequestModel(shopId, null, null, 0, 1, -1);
    }

    @NonNull
    public static ShopProductRequestModel getShopProductRequestModel(
            String shopId,
            String keyword,
            String etalaseId,
            int wholesale,
            int page,
            int orderBy) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setPage(page);

        if (etalaseId != null)
            shopProductRequestModel.setEtalaseId(etalaseId);

        if (keyword != null)
            shopProductRequestModel.setKeyword(keyword);

        if (wholesale > 0)
            shopProductRequestModel.setWholesale(wholesale);

        if (orderBy > 0)
            shopProductRequestModel.setOrderBy(orderBy);

        return shopProductRequestModel;
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductListUseCase.unsubscribe();
    }

    public static List<ShopProductViewModel> convert(List<ShopProduct> shopProducts) {
        List<ShopProductViewModel> shopProductViewModels = new ArrayList<>();
        for (ShopProduct shopProduct : shopProducts) {
            shopProductViewModels.add(convert(shopProduct));
        }
        return shopProductViewModels;
    }

    public static ShopProductViewModel convert(ShopProduct shopProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(shopProduct.getProductId());
        shopProductViewModel.setName(shopProduct.getProductName());
        shopProductViewModel.setPrice(shopProduct.getProductPrice());
        shopProductViewModel.setImageUrl(shopProduct.getProductImage());

        shopProductViewModel.setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        shopProductViewModel.setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (Badge badge : shopProduct.getBadges()) {
                if (badge.getTitle().equalsIgnoreCase(BADGGE_FREE_RETURN)){
                    shopProductViewModel.setFreeReturn(true);
                    break;
                }
            }
        }
        return shopProductViewModel;
    }

    private boolean checkNextPage(ShopProductList shopProductList) {
        if (shopProductList.getPaging() != null &&
                shopProductList.getPaging().getUriNext() != null &&
                !shopProductList.getPaging().getUriNext().isEmpty() &&
                !shopProductList.getPaging().getUriNext().equals("0")) {
            return true;
        } else {
            return true;
        }
    }
}