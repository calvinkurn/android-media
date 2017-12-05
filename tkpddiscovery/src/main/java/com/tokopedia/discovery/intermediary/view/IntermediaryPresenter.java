package com.tokopedia.discovery.intermediary.view;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.interactor.GetCategoryHeaderUseCase;
import com.tokopedia.discovery.intermediary.domain.interactor.GetIntermediaryCategoryUseCase;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import retrofit2.Response;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryPresenter extends BaseDaggerPresenter<IntermediaryContract.View>
        implements  IntermediaryContract.Presenter{

    private final GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase;
    private final GetCategoryHeaderUseCase getCategoryHeaderUseCase;

    public IntermediaryPresenter(GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase,
                                 GetCategoryHeaderUseCase getCategoryHeaderUseCase) {
        this.getIntermediaryCategoryUseCase = getIntermediaryCategoryUseCase;
        this.getCategoryHeaderUseCase = getCategoryHeaderUseCase;
    }

    @Override
    public void getIntermediaryCategory(String categoryId) {
        getIntermediaryCategoryUseCase.setCategoryId(categoryId);
        getCategoryHeaderUseCase.setCategoryId(categoryId);
        getView().showLoading();
       /* getIntermediaryCategoryUseCase.execute( RequestParams.EMPTY,
                new IntermediarySubscirber());*/
       getCategoryHeaderUseCase.execute(RequestParams.EMPTY, new CategoryHeaderSubscirber());
    }

    @Override
    public void addFavoriteShop(String categoryId) {

    }

    private class CategoryHeaderSubscirber extends DefaultSubscriber<Response<CategoryHadesModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().skipIntermediaryPage();
            getView().hideLoading();
            e.printStackTrace();
        }

        @Override
        public void onNext(Response<CategoryHadesModel> categoryHadesModelResponse) {
            if (categoryHadesModelResponse.body().getData()!=null &&
                    categoryHadesModelResponse.body().getData().getIntermediary() &&
                    categoryHadesModelResponse.body().getData().getTemplate().equals(IntermediaryCategoryDomainModel.LIFESTYLE_TEMPLATE)) {
                    getIntermediaryCategoryUseCase.setCategoryId(categoryHadesModelResponse.body().getData().getId());
                    getIntermediaryCategoryUseCase.setCategoryHadesModel(categoryHadesModelResponse.body());
                    getIntermediaryCategoryUseCase.execute(RequestParams.EMPTY, new IntermediarySubscirber(categoryHadesModelResponse.body()));
            } else {
                getView().skipIntermediaryPage(categoryHadesModelResponse.body());
            }
        }

    }

    private class IntermediarySubscirber extends DefaultSubscriber<IntermediaryCategoryDomainModel> {

        private final CategoryHadesModel categoryHadesModel;

        private IntermediarySubscirber(CategoryHadesModel categoryHadesModel) {
            this.categoryHadesModel = categoryHadesModel;
        }

        @Override
        public void onCompleted() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            getView().skipIntermediaryPage();
            e.printStackTrace();
        }

        @Override
        public void onNext(IntermediaryCategoryDomainModel domainModel) {
            if (isViewAttached()) {
                getView().renderTopAds();
                getView().renderCuratedProducts(domainModel.getCuratedSectionModelList());
                if (domainModel.getHotListModelList().size() > 0) {
                    getView().renderHotList(domainModel.getHotListModelList());
                }

                getView().updateDepartementId(domainModel.getDepartementId());
                if (domainModel.getVideoModel() != null && domainModel.getVideoModel().getVideoUrl() != null) {
                    getView().renderVideo(domainModel.getVideoModel());
                }
                if (domainModel.getBrandModelList() != null && domainModel.getBrandModelList().size() > 0) {
                    getView().renderBrands(domainModel.getBrandModelList());
                }
                getView().renderCategoryChildren(domainModel.getChildCategoryModelList());
                getView().renderHeader(domainModel.getHeaderModel());
                if (domainModel.getBannerModelList().size() > 0) {
                    getView().renderBanner(domainModel.getBannerModelList());
                }
                getView().backToTop();
            }
        }

    }
}
