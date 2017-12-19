package com.tokopedia.events.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View> implements EventsContract.Presenter {

    public GetEventsListRequestUseCase getEventsListRequestUsecase;
    public GetEventsListByLocationRequestUseCase getEventsListByLocationRequestUseCase;
    public GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;

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

    public void getEventsList() {

        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<List<EventsCategoryDomain>>() {
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
}
