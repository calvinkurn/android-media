package com.tokopedia.discovery.newdynamicfilter.helper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.LevelThreeCategory;
import com.tokopedia.core.discovery.model.LevelTwoCategory;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.model.Category;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 11/13/17.
 */

public class FilterHelper {

    @Nullable
    public static Category getSelectedCategoryDetails(Filter categoryFilter, String categoryId) {
        if (categoryFilter == null || TextUtils.isEmpty(categoryId)) {
            return null;
        }

        return findInRootCategoryList(categoryFilter, categoryId);
    }

    @Nullable
    private static Category findInRootCategoryList(Filter categoryFilter, String categoryId) {
        List<Option> rootCategoryList = categoryFilter.getOptions();

        if (rootCategoryList != null) {
            for (Option rootCategory : rootCategoryList) {
                if (categoryId.equals(rootCategory.getValue())) {
                    return new Category(categoryId, rootCategory.getValue(), rootCategory.getName());
                }

                Category category = findInLevelTwoCategoryList(rootCategory, categoryId);
                if (category != null) return category;
            }
        }

        return null;
    }

    @Nullable
    private static Category findInLevelTwoCategoryList(Option rootCategory, String categoryId) {
        List<LevelTwoCategory> levelTwoCategoryList = rootCategory.getLevelTwoCategoryList();

        if (levelTwoCategoryList != null) {
            for (LevelTwoCategory levelTwoCategory : levelTwoCategoryList) {
                if (categoryId.equals(levelTwoCategory.getValue())) {
                    return new Category(categoryId, rootCategory.getValue(), levelTwoCategory.getName());
                }

                Category category = findInLevelThreeCategoryList(rootCategory, levelTwoCategory, categoryId);
                if (category != null) return category;
            }
        }

        return null;
    }

    @Nullable
    private static Category findInLevelThreeCategoryList(Option rootCategory, LevelTwoCategory levelTwoCategory, String categoryId) {
        List<LevelThreeCategory> levelThreeCategoryList = levelTwoCategory.getLevelThreeCategoryList();

        if (levelThreeCategoryList != null) {
            for (LevelThreeCategory levelThreeCategory : levelThreeCategoryList) {
                if (categoryId.equals(levelThreeCategory.getValue())) {
                    return new Category(categoryId, rootCategory.getValue(), levelThreeCategory.getName());
                }
            }
        }

        return null;
    }

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
                if (SearchApiConst.OFFICIAL.equals(option.getKey())) {
                    selectedModel.getSavedCheckedState().put(option.getUniqueId(), true);
                }
            }
        }
    }
}
