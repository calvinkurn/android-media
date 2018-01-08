package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View> implements EventsContract.Presenter {

    public GetEventsListRequestUseCase getEventsListRequestUsecase;
    public GetEventsListByLocationRequestUseCase getEventsListByLocationRequestUseCase;
    public GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;
    CategoryViewModel carousel;
    TouchViewPager mTouchViewPager;
    int currentPage, totalPages;

    @Inject
    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase, GetEventsListByLocationRequestUseCase getEventsListByLocationRequestUseCase, GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
        this.getEventsListByLocationRequestUseCase = getEventsListByLocationRequestUseCase;
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;

    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                    }
                });
    }

    public void getEventsList() {
        getView().showProgressBar();
        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getEventsList();
                    }
                });
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {
                getView().hideProgressBar();
                getView().renderCategoryList(convertIntoCategoryListVeiwModel(categoryEntities));
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public void getEventsListByLocation(String location) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getEventsListByLocationRequestUseCase.LOCATION, location);
        getEventsListByLocationRequestUseCase.execute(requestParams, new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {

                getView().renderCategoryList(convertIntoCategoryListVeiwModel(categoryEntities));
            }
        });
    }

    public void getEventsListBySearch(String searchText) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchEventsListRequestUseCase.TAG, searchText);

        getSearchEventsListRequestUseCase.execute(requestParams, new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {

                getView().renderCategoryList(convertIntoCategoryListVeiwModel(categoryEntities));
                CommonUtils.dumper("enter onNext");
            }
        });
    }


    private List<CategoryViewModel> convertIntoCategoryListVeiwModel(List<EventsCategoryDomain> categoryList) {
        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (EventsCategoryDomain eventsCategoryDomain : categoryList
                    ) {
                if ("top".equalsIgnoreCase(eventsCategoryDomain.getName())) {
                    categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(), eventsCategoryDomain.getName(), convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                } else {
                    categoryViewModels.add(new CategoryViewModel(eventsCategoryDomain.getTitle(), eventsCategoryDomain.getName(), convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                }

            }
        }
        for (CategoryViewModel model : categoryViewModels) {
            if (model.getTitle().equals("Carousel")) {
                carousel = model;
                break;
            }
        }
        return categoryViewModels;
    }

    public List<CategoryItemsViewModel> convertIntoCategoryListItemsVeiwModel(List<EventsItemDomain> categoryResponseItemsList) {
        List<CategoryItemsViewModel> categoryItemsViewModelList = new ArrayList<>();
        if (categoryResponseItemsList != null) {
            CategoryItemsViewModel CategoryItemsViewModel;
            for (EventsItemDomain categoryEntity : categoryResponseItemsList
                    ) {
                CategoryItemsViewModel = new CategoryItemsViewModel();
                CategoryItemsViewModel.setId(categoryEntity.getId());
                CategoryItemsViewModel.setCategoryId(categoryEntity.getCategoryId());
                CategoryItemsViewModel.setDisplayName(categoryEntity.getDisplayName());
                CategoryItemsViewModel.setTitle(categoryEntity.getTitle());
                CategoryItemsViewModel.setImageApp(categoryEntity.getImageApp());
                CategoryItemsViewModel.setSalesPrice(categoryEntity.getSalesPrice());
                CategoryItemsViewModel.setMinStartTime(categoryEntity.getMinStartTime());
                CategoryItemsViewModel.setCityName(categoryEntity.getCityName());
                CategoryItemsViewModel.setMinStartDate(categoryEntity.getMinStartDate());
                CategoryItemsViewModel.setMaxEndDate(categoryEntity.getMaxEndDate());
                CategoryItemsViewModel.setLongRichDesc(categoryEntity.getLongRichDesc());
                CategoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
                CategoryItemsViewModel.setTnc(categoryEntity.getTnc());
                CategoryItemsViewModel.setHasSeatLayout(categoryEntity.getHasSeatLayout());
                CategoryItemsViewModel.setUrl(categoryEntity.getUrl());
                categoryItemsViewModelList.add(CategoryItemsViewModel);
            }
        }
        return categoryItemsViewModelList;
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ArrayList<String> imagesList = new ArrayList<>();
        if (categoryItemsViewModels != null) {
            for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageApp());
            }
        }
        return imagesList;
    }

    public void onClickBanner() {
        CategoryItemsViewModel categoryItemsViewModel = carousel.getItems().get(getView().getBannerPosition());
        if (!categoryItemsViewModel.getUrl().contains("www.tokopedia.com")) {
            Intent intent = new Intent(getView().getActivity(), EventDetailsActivity.class);
            intent.putExtra("homedata", categoryItemsViewModel);
            getView().getActivity().startActivity(intent);
        } else {
            if (getView().getActivity().getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) getView().getActivity().getApplication())
                        .actionOpenGeneralWebView(getView().getActivity(), categoryItemsViewModel.getUrl());
            }
        }
    }
}
