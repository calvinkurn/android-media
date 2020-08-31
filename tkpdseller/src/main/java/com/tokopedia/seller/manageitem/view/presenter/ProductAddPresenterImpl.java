package com.tokopedia.seller.manageitem.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.manageitem.common.listener.ProductAddView;
import com.tokopedia.seller.manageitem.common.util.AddEditPageType;
import com.tokopedia.seller.manageitem.common.util.ViewUtils;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;
import com.tokopedia.seller.manageitem.domain.usecase.FetchProductVariantByCatUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.SaveDraftProductUseCase;
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

    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final GQLGetShopInfoUseCase gqlGetShopInfoUseCase;
    protected final FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase;
    private UserSessionInterface userSession;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   GQLGetShopInfoUseCase gqlGetShopInfoUseCase,
                                   UserSessionInterface userSession,
                                   FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.gqlGetShopInfoUseCase = gqlGetShopInfoUseCase;
        this.userSession = userSession;
        this.fetchProductVariantByCatUseCase = fetchProductVariantByCatUseCase;
    }

    @Override
    public void saveDraft(ProductViewModel viewModel, boolean isUploading) {
        saveDraftProductUseCase.execute(generateRequestParamAddDraft(viewModel, isUploading),
                new SaveDraftSubscriber(isUploading));
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

    @Override
    public void fetchProductVariantByCat(long categoryId) {
        com.tokopedia.core.base.domain.RequestParams requestParam = FetchProductVariantByCatUseCase.generateParam(categoryId);
        fetchProductVariantByCatUseCase.execute(requestParam, getProductVariantSubscriber());
    }

    private Subscriber<List<ProductVariantByCatModel>> getProductVariantSubscriber() {
        return new Subscriber<List<ProductVariantByCatModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorGetProductVariantByCat(e);
            }

            @Override
            public void onNext(List<ProductVariantByCatModel> s) {
                getView().onSuccessGetProductVariantCat(s);
            }
        };
    }


    private class SaveDraftSubscriber extends Subscriber<Long> {

        boolean isUploading;

        SaveDraftSubscriber(boolean isUploading) {
            this.isUploading = isUploading;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            if (isUploading) {
                getView().onErrorStoreProductToDraftWhenUpload(ViewUtils.getErrorMessage(e));
            } else {
                getView().onErrorStoreProductToDraftWhenBackPressed(ViewUtils.getErrorMessage(e));
            }
        }

        @Override
        public void onNext(Long productId) {
            checkViewAttached();
            getView().onSuccessStoreProductToDraft(productId, isUploading);
        }
    }

    private RequestParams generateRequestParamAddDraft(ProductViewModel viewModel, boolean isUploading) {
        return SaveDraftProductUseCase.generateUploadProductParam(viewModel, getProductDraftId(),
                isUploading);
    }

    private long getProductDraftId() {
        return getView().getProductDraftId();
    }

    public void detachView() {
        super.detachView();
        gqlGetShopInfoUseCase.cancelJobs();
        saveDraftProductUseCase.unsubscribe();
        fetchProductVariantByCatUseCase.unsubscribe();
    }
}
