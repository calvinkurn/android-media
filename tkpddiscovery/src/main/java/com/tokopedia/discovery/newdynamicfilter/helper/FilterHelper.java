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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL;

/**
 * Created by henrypriyono on 11/13/17.
 */

public class FilterHelper {

    @Nullable
    public static Category getSelectedCategoryDetailsFromFilterList(List<Filter> filterList, String categoryId) {
        return getSelectedCategoryDetails(getCategoryFilterFromList(filterList), categoryId);
    }

    @Nullable
    public static Filter getCategoryFilterFromList(List<Filter> filterList) {
        for(Filter filter : filterList) {
            if(filter.isCategoryFilter()) return filter;
        }

        return null;
    }

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

    public static List<Filter> initializeFilterList(List<Filter> filterList) {
        List<Filter> list = new ArrayList<>(filterList);

        removeFiltersWithEmptyOption(list);
        mergeSizeFilterOptionsWithSameValue(list);
        removeBrandFilterOptionsWithSameValue(list);
        removeValueFromOptionWithInputTypeTextBox(list);

        return list;
    }

    private static void removeFiltersWithEmptyOption(List<Filter> filterList) {
        Iterator<Filter> iterator = filterList.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            if (filter.getOptions().isEmpty() && !filter.isSeparator()) {
                iterator.remove();
            }
        }
    }

    private static void mergeSizeFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter sizeFilter = getSizeFilter(filterList);
        if (sizeFilter == null) {
            return;
        }

        List<Option> sizeFilterOptions = sizeFilter.getOptions();
        Iterator<Option> iterator = sizeFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                existingOption.setName(existingOption.getName() + " / " + getFormattedSizeName(option));
                iterator.remove();
            } else {
                option.setName(getFormattedSizeName(option));
                option.setMetric("");
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private static Filter getSizeFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isSizeFilter()) return filter;
        }
        return null;
    }

    private static String getFormattedSizeName(Option option) {
        if (METRIC_INTERNATIONAL.equals(option.getMetric())) {
            return option.getName();
        } else {
            return option.getName() + " " + option.getMetric();
        }
    }

    private static void removeBrandFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter brandFilter = getBrandFilter(filterList);
        if (brandFilter == null) {
            return;
        }

        List<Option> brandFilterOptions = brandFilter.getOptions();
        Iterator<Option> iterator = brandFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                iterator.remove();
            } else {
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private static Filter getBrandFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isBrandFilter()) return filter;
        }
        return null;
    }

    private static void removeValueFromOptionWithInputTypeTextBox(List<Filter> filterList) {
        for (Filter filter : filterList) {
            for(Option option : filter.getOptions()) {
                if(option.getInputType().equals(Option.INPUT_TYPE_TEXTBOX)) {
                    option.setValue("");
                }
            }
        }
    }
}
