package com.tokopedia.discovery.newdynamicfilter.helper;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.LevelThreeCategory;
import com.tokopedia.core.discovery.model.LevelTwoCategory;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 11/13/17.
 */

public class FilterHelper {
    public static void populateWithSelectedCategory(List<Filter> filters,
                                                    FilterFlagSelectedModel selectedModel,
                                                    String categoryId) {

        Filter categoryFilter = findCategoryFilter(filters);

        if (categoryFilter == null || categoryId == null) {
            return;
        }

        List<Option> rootCategoryList = categoryFilter.getOptions();

        if (rootCategoryList != null) {
            for (Option rootCategory : rootCategoryList) {
                if (categoryId.equals(rootCategory.getValue())) {
                    selectedModel.setCategoryId(categoryId);
                    selectedModel.setSelectedCategoryName(rootCategory.getName());
                    selectedModel.setSelectedCategoryRootId(rootCategory.getValue());
                    return;
                } else {
                    List<LevelTwoCategory> levelTwoCategoryList = rootCategory.getLevelTwoCategoryList();
                    if (levelTwoCategoryList != null) {
                        for (LevelTwoCategory levelTwoCategory : levelTwoCategoryList) {
                            if (categoryId.equals(levelTwoCategory.getValue())) {
                                selectedModel.setCategoryId(categoryId);
                                selectedModel.setSelectedCategoryName(levelTwoCategory.getName());
                                selectedModel.setSelectedCategoryRootId(rootCategory.getValue());
                                return;
                            } else {
                                List<LevelThreeCategory> levelThreeCategoryList = levelTwoCategory.getLevelThreeCategoryList();
                                if (levelThreeCategoryList != null) {
                                    for (LevelThreeCategory levelThreeCategory : levelThreeCategoryList) {
                                        if (categoryId.equals(levelThreeCategory.getValue())) {
                                            selectedModel.setCategoryId(categoryId);
                                            selectedModel.setSelectedCategoryName(levelThreeCategory.getName());
                                            selectedModel.setSelectedCategoryRootId(rootCategory.getValue());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Filter findCategoryFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isCategoryFilter()) {
                return filter;
            }
        }
        return null;
    }

    public static void addPreFilteredIsOfficial(List<Filter> filters, FilterFlagSelectedModel selectedModel) {
        for (Filter filter : filters) {
            for (Option option : filter.getOptions()) {
                if (Option.KEY_OFFICIAL.equals(option.getKey())) {
                    selectedModel.getSavedCheckedState().put(option.getUniqueId(), true);
                }
            }
        }
    }
}
