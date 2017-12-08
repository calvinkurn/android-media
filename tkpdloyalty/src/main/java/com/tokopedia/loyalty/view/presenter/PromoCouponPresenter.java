package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponPresenter implements IPromoCouponPresenter {

    private final IPromoCouponInteractor promoCouponInteractor;
    private final IPromoCouponView view;

    @Inject
    public PromoCouponPresenter(IPromoCouponView view, IPromoCouponInteractor promoCouponInteractor) {
        this.view = view;
        this.promoCouponInteractor = promoCouponInteractor;
    }

    @Override
    public void processGetCouponList(String platform) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        //param.put("user_id", SessionHandler.getLoginID(view.getContext()));
        param.put("type", platform);
        param.put("page", "1");
        param.put("page_size", "10");

        //TODO Revert Later
        promoCouponInteractor.getCouponList(
                //AuthUtil.generateParamsNetwork(view.getContext(), param),
                AuthUtil.generateDummyParamsNetwork(view.getContext(), param),
                new Subscriber<List<CouponData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CouponData> couponData) {
                        if (couponData.size() < 1) {
                            view.couponDataNoResult();
                        } else {
                            view.renderCouponListDataResult(couponData);
                        }
                    }
                });
    }

    @Override
    public void processPostCouponValidateRedeem() {

    }

    @Override
    public void processPostCouponRedeem() {

    }

    @Override
    public void processGetPointRecentHistory() {

    }

    @Override
    public void processGetCatalogList() {

    }

    @Override
    public void processGetCatalogDetail() {

    }

    @Override
    public void processGetCatalogFilterCategory() {

    }

    @Override
    public void submitVoucher(final CouponData couponData) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(VOUCHER_CODE, couponData.getCode());
        promoCouponInteractor.submitVoucher(
                couponData.getTitle(),
                couponData.getCode(),
                AuthUtil.generateParamsNetwork(view.getContext(), param
                ), makeCouponSubscriber(couponData));

    }

    @Override
    public void submitDigitalVoucher(CouponData couponData, String categoryId) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(VOUCHER_CODE, couponData.getCode());
        param.put(CATEGORY_ID, categoryId);
        promoCouponInteractor.submitDigitalVoucher(
                couponData.getTitle(),
                couponData.getCode(),
                AuthUtil.generateParamsNetwork(view.getContext(), param
                ), makeCouponSubscriber(couponData));
    }

    private Subscriber<CouponViewModel> makeCouponSubscriber(final CouponData couponData) {
        return new Subscriber<CouponViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                couponData.setErrorMessage(e.getMessage());
                view.hideProgressLoading();
                view.couponError();
            }

            @Override
            public void onNext(CouponViewModel couponViewModel) {
                view.receiveResult(couponViewModel);
                view.hideProgressLoading();
            }
        };
    }
}
