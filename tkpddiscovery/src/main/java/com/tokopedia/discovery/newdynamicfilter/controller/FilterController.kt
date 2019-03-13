package com.tokopedia.discovery.newdynamicfilter.controller

import android.text.TextUtils
import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView
import java.io.Serializable

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

    fun applyFilter() {
        loadFlagFilterHelperToSearchParameter()
        dynamicFilterView.applyFilter(searchParameter)
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

    fun getFilterValue(key: String): String {
        return searchParameter[key] ?: ""
    }

    fun getFilterList() : List<Filter> {
        return filterList
    }
}