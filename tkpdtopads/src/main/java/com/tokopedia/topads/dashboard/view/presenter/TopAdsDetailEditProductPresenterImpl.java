package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditProductPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenterImpl<T> implements TopAdsDetailEditProductPresenter<T> {

    private TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase;
    private TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase;
    private TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase;
    private TopAdsEditPromoFragmentListener listener;

    public TopAdsDetailEditProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                TopAdsProductListUseCase topAdsProductListUseCase,
                                                TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        super(topAdsProductListUseCase);
        this.topAdsGetSuggestionUseCase = topAdsGetSuggestionUseCase;
        this.topAdsGetDetailProductUseCase = topAdsGetDetailProductUseCase;
        this.topAdsSaveDetailProductUseCase = topAdsSaveDetailProductUseCase;
    }



    @Override
    public void saveAd(TopAdsDetailProductViewModel adViewModel) {
        topAdsSaveDetailProductUseCase.execute(TopAdsSaveDetailProductUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(adViewModel)), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailProductUseCase.execute(TopAdsGetDetailProductUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetDetailProductUseCase.unsubscribe();
        topAdsSaveDetailProductUseCase.unsubscribe();
    }

    @Override
    public void getSuggestionBid(List<String> ids, String source) {
        GetSuggestionBody getSuggestionBody = new GetSuggestionBody();
        getSuggestionBody.setRounding(true);
        getSuggestionBody.setSource(source);
        getSuggestionBody.setDataType(TopAdsNetworkConstant.SUGGESTION_DATA_TYPE_SUMMARY);
        getSuggestionBody.setSuggestionType(TopAdsNetworkConstant.SUGGESTION_TYPE_DEPARTMENT_ID);
        List<Long> ids_ = new ArrayList<>();
        for (String id : ids) {
            ids_.add(Long.valueOf(id));
        }
        getSuggestionBody.setIds(ids_);

        topAdsGetSuggestionUseCase.execute(TopAdsGetSuggestionUseCase.createRequestParams(getSuggestionBody), new Subscriber<GetSuggestionResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onSuggestionError(e);
                }
            }

            @Override
            public void onNext(GetSuggestionResponse s) {
                if(isViewAttached()) {
                    getView().onSuggestionSuccess(s);
                }
            }
        });
    }
}