package com.tokopedia.events.view.utils;

import android.util.Log;

import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class Utils {
    private static Utils singleInstance;

    synchronized public static Utils getSingletonInstance(){
        if(singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils(){
        Log.d("UTILS","Utils Instance created");
    }

    public List<CategoryViewModel> convertIntoCategoryListVeiwModel(List<EventsCategoryDomain> categoryList) {
        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (EventsCategoryDomain eventsCategoryDomain : categoryList
                    ) {
                if ("top".equalsIgnoreCase(eventsCategoryDomain.getName())) {
                    categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                } else {
                    categoryViewModels.add(new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

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
}
