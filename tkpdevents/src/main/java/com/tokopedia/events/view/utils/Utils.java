package com.tokopedia.events.view.utils;

import android.util.Log;

import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class Utils {
    private static Utils singleInstance;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
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
            for (EventsItemDomain categoryEntity : categoryResponseItemsList) {
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
                CategoryItemsViewModel.setIsTop(categoryEntity.getIsTop());
                CategoryItemsViewModel.setHasSeatLayout(categoryEntity.getHasSeatLayout());
                CategoryItemsViewModel.setUrl(categoryEntity.getUrl());
                categoryItemsViewModelList.add(CategoryItemsViewModel);
            }
        }
        return categoryItemsViewModelList;
    }

    public ArrayList<SearchViewModel> convertIntoSearchViewModel(List<CategoryViewModel> source) {
        ArrayList<SearchViewModel> searchViewModels = new ArrayList<>();
        if (source != null) {
            SearchViewModel searchModelItem;
            for (CategoryViewModel item : source) {
                List<CategoryItemsViewModel> sourceModels = item.getItems();
                for (CategoryItemsViewModel sourceItem : sourceModels) {
                    if (sourceItem.getIsTop() == 1) {
                        searchModelItem = new SearchViewModel();
                        searchModelItem.setCityName(sourceItem.getCityName());
                        searchModelItem.setDisplayName(sourceItem.getDisplayName());
                        searchModelItem.setImageApp(sourceItem.getImageApp());
                        searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                        searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                        searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                        searchModelItem.setTitle(sourceItem.getTitle());
                        searchModelItem.setUrl(sourceItem.getUrl());
                        searchViewModels.add(searchModelItem);
                    }
                }

            }
        }
        return searchViewModels;
    }

    public List<SearchViewModel> convertSearchResultsToModel(List<CategoryItemsViewModel> categoryItemsViewModels) {
        List<SearchViewModel> searchResults = null;
        if (categoryItemsViewModels != null && !categoryItemsViewModels.isEmpty()) {
            SearchViewModel searchModelItem;
            searchResults = new ArrayList<>();
            for (CategoryItemsViewModel sourceItem : categoryItemsViewModels) {
                searchModelItem = new SearchViewModel();
                searchModelItem.setCityName(sourceItem.getCityName());
                searchModelItem.setDisplayName(sourceItem.getDisplayName());
                searchModelItem.setImageApp(sourceItem.getImageApp());
                searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                searchModelItem.setTitle(sourceItem.getTitle());
                searchModelItem.setUrl(sourceItem.getUrl());
                searchResults.add(searchModelItem);
            }
        }
        return searchResults;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
