package com.tokopedia.discovery.newdynamicfilter.controller

import android.text.TextUtils
import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import java.io.Serializable
import java.util.*

class FilterController : Serializable {

    private val searchParameter = mutableMapOf<String, String>()
    private val flagFilterHelper = mutableMapOf<String, Boolean>()
    private val filterList = mutableListOf<Filter>()
    private val activeFilterKeyList = mutableSetOf<String>()
    private val shownInMainState = mutableMapOf<String, Boolean>()

    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    fun initFilterController(searchParameter: Map<String, String> = mapOf(),
                             filterList: List<Filter> = listOf()) {
        loadSearchParameter(searchParameter)
        loadFilterList(filterList)
        loadFlagFilterHelper()
        loadActiveFilter()

        shownInMainState.clear()
    }

    private fun loadSearchParameter(searchParameter: Map<String, String>) {
        this.searchParameter.putAll(searchParameter)
    }

    private fun loadFilterList(filterList: List<Filter>) {
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

    private fun loadActiveFilter() {
        for(filter in filterList) {
            for(option in filter.options) {
                if(searchParameter.containsKey(option.key)) activeFilterKeyList.add(option.key)
            }
        }
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun isSliderValueHasChanged(minValue: Int, maxValue: Int): Boolean {
        return minValue != pressedSliderMinValueState || maxValue != pressedSliderMaxValueState
    }

    fun setFlagFilterHelper(option: Option, value: Boolean) {
        setOrRemoveFlagFilterHelper(option.uniqueId, value)

        setFilterValue(option, value.toString())
    }

    private fun setOrRemoveFlagFilterHelper(key: String, value: Boolean) {
        if(value) flagFilterHelper[key] = true
        else flagFilterHelper.remove(key)
    }

    fun setFilterValue(option: Option, value: String) {
        val isFilterApplied = isFilterApplied(value)

        setOrRemoveFilterValue(isFilterApplied, option.key, value)
    }

    fun setFilterValue(optionList: List<Option>) {
        for(option in optionList) {
            val isFilterApplied = isFilterApplied(option.inputState)
            setOrRemoveShownInMainState(isFilterApplied, option.uniqueId, option.inputState?.toBoolean() ?: false)
            setOrRemoveFilterValue(isFilterApplied, option.key, option.inputState)
        }
    }

    private fun isFilterApplied(value: String) : Boolean {
        return if(value.toBoolean()) true
        else isValueNotEmptyAndNotFalse(value)
    }

    private fun isValueNotEmptyAndNotFalse(value: String) : Boolean {
        return !TextUtils.isEmpty(value) && value != java.lang.Boolean.FALSE.toString()
    }

    private fun setOrRemoveShownInMainState(isFilterAdded: Boolean, key: String, value: Boolean) {
        if(isFilterAdded) shownInMainState[key] = value
        else shownInMainState.remove(key)
    }

    fun resetAllFilters() {
        for(activeFilterKey in activeFilterKeyList) {
            setOrRemoveFilterValue(false, activeFilterKey, "")
        }
    }

    private fun setOrRemoveFilterValue(isFilterApplied: Boolean, key: String, value: String) {
        if(isFilterApplied) {
            searchParameter[key] = value
            activeFilterKeyList.add(key)
        }
        else {
            searchParameter.remove(key)
            activeFilterKeyList.remove(key)
        }
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

    fun isFilterActive() : Boolean {
        return activeFilterKeyList.size > 0
    }

    fun getFilterValue(key: String): String {
        return searchParameter[key] ?: ""
    }

    fun getFlagFilterHelperValue(key: String) : Boolean {
        return flagFilterHelper[key] ?: false
    }

    fun getSearchParameter() : Map<String, String> {
        return this.searchParameter
    }

    fun getFilterList() : List<Filter> {
        return filterList
    }
}