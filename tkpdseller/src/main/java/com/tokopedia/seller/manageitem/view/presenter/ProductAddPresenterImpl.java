package com.tokopedia.seller.manageitem.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.manageitem.common.listener.ProductAddView;
import com.tokopedia.seller.manageitem.common.util.AddEditPageType;
import com.tokopedia.seller.manageitem.common.util.ViewUtils;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;
import com.tokopedia.seller.manageitem.domain.usecase.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import rx.Subscriber;
import timber.log.Timber;

import static com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.PRODUCT_ADD_SOURCE;
import static com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.PRODUCT_EDIT_SOURCE;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl<T extends ProductAddView> extends BaseDaggerPresenter<T> implements ProductAddPresenter<T> {

    private final GQLGetShopInfoUseCase gqlGetShopInfoUseCase;
    private UserSessionInterface userSession;

    public ProductAddPresenterImpl(GQLGetShopInfoUseCase gqlGetShopInfoUseCase,
                                   UserSessionInterface userSession) {
        this.gqlGetShopInfoUseCase = gqlGetShopInfoUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getShopInfo(AddEditPageType addEditPageType) {
        ArrayList<Integer> shopIds = new ArrayList<>();
        try {
            shopIds.add(Integer.parseInt(userSession.getShopId()));
        }
        catch (NumberFormatException exception) {
            Timber.d("Failed to convert shop ID to integer");
        }
        String gqlShopInfoSoure = "";
        if (addEditPageType == AddEditPageType.ADD) {
            gqlShopInfoSoure = PRODUCT_ADD_SOURCE;
        } else if (addEditPageType == AddEditPageType.EDIT) {
            gqlShopInfoSoure = PRODUCT_EDIT_SOURCE;
        }
        gqlGetShopInfoUseCase.setParams(GQLGetShopInfoUseCase.createParams(
                shopIds,
                null ,
                GQLGetShopInfoUseCase.getDefaultShopFields(),
                gqlShopInfoSoure
        ));
        gqlGetShopInfoUseCase.execute(
                shopInfo -> {
                    if (isViewAttached()) {
                        getView().onSuccessLoadShopInfo(
                                shopInfo.getGoldOS().isGold() == 1,
                                false,
                                shopInfo.getGoldOS().isOfficial() == 1
                        );
                    }
                    return Unit.INSTANCE;
                },
                throwable -> {
                    if (isViewAttached()) {
                        getView().onErrorLoadShopInfo(ViewUtils.getErrorMessage(throwable));
                    }
                    return Unit.INSTANCE;
                }
        );
    }

    public void detachView() {
        super.detachView();
        gqlGetShopInfoUseCase.cancelJobs();
    }
}
