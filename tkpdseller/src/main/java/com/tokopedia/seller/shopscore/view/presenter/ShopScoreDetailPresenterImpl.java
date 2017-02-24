package com.tokopedia.seller.shopscore.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.view.fragment.ShopScoreDetailView;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailPresenterImpl extends BaseDaggerPresenter<ShopScoreDetailView> implements ShopScoreDetailPresenter {
    private final GetShopScoreDetailUseCase getShopScoreDetailUseCase;

    public ShopScoreDetailPresenterImpl(GetShopScoreDetailUseCase getShopScoreDetailUseCase) {
        this.getShopScoreDetailUseCase = getShopScoreDetailUseCase;
    }

    @Override
    public void getShopScoreDetail() {
        getShopScoreDetailUseCase.execute(
                RequestParams.EMPTY,
                new GetShopScoreDetailSubscriber()
        );
    }

    private class GetShopScoreDetailSubscriber extends rx.Subscriber<com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ShopScoreDetailDomainModel shopScoreDetailDomainModel) {

        }
    }
}
