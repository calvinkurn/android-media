package com.tokopedia.discovery.newdynamicfilter.controller

import android.text.TextUtils
import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView
import java.io.Serializable
import java.util.*

class FilterController(private val dynamicFilterView : DynamicFilterView) : Serializable {

    private val searchParameter = mutableMapOf<String, String>()
    private val flagFilterHelper = mutableMapOf<String, Boolean>()
    private val filterList = mutableListOf<Filter>()
    private val shownInMainState = mutableMapOf<String, Boolean>()

    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    fun initFilterController(searchParameter: Map<String, String> = mapOf(),
                             filterList: List<Filter> = listOf()) {
        loadSearchParameter(searchParameter)
        loadFilterData(filterList)
        loadFlagFilterHelper()

        shownInMainState.clear()
    }

    private fun loadSearchParameter(searchParameter: Map<String, String>) {
        this.searchParameter.putAll(searchParameter)
    }

    private fun loadFilterData(filterList: List<Filter>) {
        this.filterList.addAll(filterList)

        removeFiltersWithEmptyOption()
        mergeSizeFilterOptionsWithSameValue()
        removeBrandFilterOptionsWithSameValue()
    }

    private fun removeFiltersWithEmptyOption() {
        val iterator = filterList.iterator()
        while (iterator.hasNext()) {
            val filter = iterator.next()
            if (filter.options.isEmpty() && !filter.isSeparator) {
                iterator.remove()
            }
        }
    }

    private fun mergeSizeFilterOptionsWithSameValue() {
        val sizeFilter = getSizeFilter() ?: return

        val sizeFilterOptions = sizeFilter.options
        val iterator = sizeFilterOptions.iterator()
        val optionMap = mutableMapOf<String, Option>()

        while (iterator.hasNext()) {
            val option = iterator.next()
            val existingOption = optionMap[option.value]
            if (existingOption != null) {
                existingOption.name = existingOption.name + " / " + getFormattedSizeName(option)
                iterator.remove()
            } else {
                option.name = getFormattedSizeName(option)
                option.metric = ""
                optionMap[option.value] = option
            }
        }
    }

    private fun getSizeFilter(): Filter? {
        for (filter in filterList) {
            if (filter.isSizeFilter) return filter
        }
        return null
    }

    private fun getFormattedSizeName(option: Option): String {
        return if (METRIC_INTERNATIONAL == option.metric) {
            option.name
        } else {
            option.name + " " + option.metric
        }
    }

    private fun removeBrandFilterOptionsWithSameValue() {
        val brandFilter = getBrandFilter() ?: return

        val brandFilterOptions = brandFilter.options
        val iterator = brandFilterOptions.iterator()
        val optionMap = mutableMapOf<String, Option>()

        while (iterator.hasNext()) {
            val option = iterator.next()
            val existingOption = optionMap[option.value]
            if (existingOption != null) {
                iterator.remove()
            } else {
                optionMap[option.value] = option
            }
        }
    }

    private fun getBrandFilter(): Filter? {
        for (filter in filterList) {
            if (filter.isBrandFilter) return filter
        }
        return null
    }

    private fun loadFlagFilterHelper() {
        for (filter in filterList) {
            for (option in filter.options) {
                if (searchParameter.containsKey(option.key)) {
                    flagFilterHelper[option.uniqueId] = true
                }
            }
        }
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun checkSliderStateAndApplyFilter(minValue: Int, maxValue: Int) {
         if (!isPriceRangeValueSameAsSliderPressedState(minValue, maxValue)) {
             applyFilter()
         }
    }

    private fun isPriceRangeValueSameAsSliderPressedState(minValue: Int, maxValue: Int): Boolean {
        return minValue == pressedSliderMinValueState && maxValue == pressedSliderMaxValueState
    }

    private fun loadFlagFilterHelperToSearchParameter() {
        for ((key, value) in flagFilterHelper) {
            if (value) appendToMap(key)
        }
    }

    private fun appendToMap(uniqueId: String) {
        val key = OptionHelper.parseKeyFromUniqueId(uniqueId)
        val value = OptionHelper.parseValueFromUniqueId(uniqueId)
        val appendedMapValue = appendMapValue(key, value)

        searchParameter[key] = appendedMapValue
    }

    private fun appendMapValue(key: String, previousValue: String): String {
        var value = searchParameter[key] ?: ""

        if (TextUtils.isEmpty(value))
            value = previousValue
        else
            value += ",$previousValue"

        return value
    }

    fun setAndApplyFilter(option: Option, value: String) {
        setFilterValue(option, value)
        applyFilter()
    }

    fun applyFilter() {
        loadFlagFilterHelperToSearchParameter()
        dynamicFilterView.applyFilter(searchParameter)
    }

    fun setFilterValue(option: Option, value: String) {
        val isFilterAdded = isFilterAdded(value)
        dynamicFilterView.trackSearch(option.name, value, isFilterAdded)

        setOrRemoveFilterValue(isFilterAdded, option.key, value)

        dynamicFilterView.updateResetButtonVisibility()
    }

    private fun isFilterAdded(value: String) : Boolean {
        return if(value.toBoolean()) true
        else isValueNotEmptyAndNotFalse(value)
    }

    private fun isValueNotEmptyAndNotFalse(value: String) : Boolean {
        return !TextUtils.isEmpty(value) && value != java.lang.Boolean.FALSE.toString()
    }

    private fun setOrRemoveFilterValue(isFilterAdded: Boolean, key: String, value: String) {
        if(isFilterAdded) searchParameter[key] = value
        else searchParameter.remove(key)
    }

    fun getFilterValue(key: String): String {
        return searchParameter[key] ?: ""
    }

    fun getSelectedOptions(filter: Filter): List<Option> {
        val selectedOptions = ArrayList<Option>()
        val popularOptionList = getPopularOptionList(filter)

        if (isAddCategoryFilter(filter, popularOptionList)) {
            selectedOptions.add(getSelectedCategoryAsOption(filter))
        } else {
            selectedOptions.addAll(getCustomSelectedOptionList(filter))
        }

        selectedOptions.addAll(popularOptionList)
        return selectedOptions
    }

    private fun getPopularOptionList(filter: Filter): List<Option> {
        val checkedOptions = ArrayList<Option>()

        for (option in filter.options) {
            if (option.isPopular) {
                checkedOptions.add(option)
            }
        }
        return checkedOptions
    }

    private fun isAddCategoryFilter(filter: Filter, popularOptionList: List<Option>) : Boolean {
        return filter.isCategoryFilter && isCategorySelected() && !isSelectedCategoryInList(popularOptionList)
    }

    private fun isCategorySelected(): Boolean {
        return !TextUtils.isEmpty(getFilterValue(SearchApiConst.SC))
    }

    private fun isSelectedCategoryInList(optionList: List<Option>): Boolean {
        val selectedCategoryId = getFilterValue(SearchApiConst.SC)

        if (TextUtils.isEmpty(selectedCategoryId)) {
            return false
        }

        for (option in optionList) {
            if (selectedCategoryId == option.value) {
                return true
            }
        }

        return false
    }

    private fun getSelectedCategoryAsOption(filter: Filter): Option {
        val selectedCategoryId = getFilterValue(SearchApiConst.SC)
        val category = FilterHelper.getSelectedCategoryDetails(filter, selectedCategoryId)
        val selectedCategoryName = category?.categoryName ?: ""

        return OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName)
    }

    private fun getCustomSelectedOptionList(filter: Filter): List<Option> {
        val checkedOptions = ArrayList<Option>()

        for (option in filter.options) {
            val isDisplayed = isCustomOptionDisplayed(option)

            if (isDisplayed && !option.isPopular) {
                checkedOptions.add(option)
            }
        }
        return checkedOptions
    }

    private fun isCustomOptionDisplayed(option: Option) : Boolean {
        return java.lang.Boolean.TRUE == getFlagFilterHelperValue(option.key)
                || java.lang.Boolean.TRUE == shownInMainState[option.uniqueId]
    }

    fun getFlagFilterHelperValue(key: String) : Boolean {
        return flagFilterHelper[key] ?: false
    }

    fun getFilterList() : List<Filter> {
        return filterList
    }
}