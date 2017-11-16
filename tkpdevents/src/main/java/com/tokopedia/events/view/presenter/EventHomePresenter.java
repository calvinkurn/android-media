package com.tokopedia.events.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.model.CategoryEntity;
import com.tokopedia.events.domain.model.ItemResponseEntity;
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

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View> implements EventsContract.Presenter  {

    public GetEventsListRequestUseCase getEventsListRequestUsecase;

    @Inject
    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    public void getEventsList(){

        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<List<CategoryEntity>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<CategoryEntity> categoryEntities) {

                getView().renderCategoryList(convertIntoCategoryListVeiwModel(categoryEntities));
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    private List<CategoryViewModel> convertIntoCategoryListVeiwModel(List<CategoryEntity> categoryList){
        List<CategoryViewModel> categoryViewModels=new ArrayList<>();
        if(categoryList!=null){
            for (CategoryEntity categoryEntity:categoryList
                 ) {
                categoryViewModels.add(new CategoryViewModel(categoryEntity.getTitle(),categoryEntity.getName(),convertIntoCategoryListItemsVeiwModel(categoryEntity.getItems())));

            }
        }
        return categoryViewModels;
    }

    public  List<CategoryItemsViewModel> convertIntoCategoryListItemsVeiwModel(List<ItemResponseEntity> categoryResponseItemsList){
        List<CategoryItemsViewModel> categoryItemsViewModelList=new ArrayList<>();
        if(categoryResponseItemsList!=null){
            for (ItemResponseEntity categoryEntity:categoryResponseItemsList
                    ) {
                categoryItemsViewModelList.add(new CategoryItemsViewModel(categoryEntity.getId(),categoryEntity.getCategoryId(),categoryEntity.getDisplayName(),
                        categoryEntity.getTitle(),categoryEntity.getImageApp(),categoryEntity.getSalesPrice()));
            }
        }
        return categoryItemsViewModelList;
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels){
        ArrayList<String> imagesList=new ArrayList<>();
        if(categoryItemsViewModels!=null){
            for (CategoryItemsViewModel categoryItemsViewModel: categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageApp());
            }
        }
        return imagesList;
    }
}
