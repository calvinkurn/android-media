package com.tokopedia.discovery.newdynamicfilter

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView
import java.io.Serializable

class FilterParameter(searchParameter: Map<String, String> = mapOf(),
                      filterList: List<Filter> = listOf(),
                      private val dynamicFilterView : DynamicFilterView) : Serializable {

    private val searchParameter = mutableMapOf<String, String>()
    private val flagFilterHelper = mutableMapOf<String, Boolean>()
    private val filterList = mutableListOf<Filter>()
    private val shownInMainState = mutableMapOf<String, Boolean>()

    init {
        loadSearchParameter(searchParameter)
        loadFilterData(filterList)
        parseFilterHelper()

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

    private fun parseFilterHelper() {
        for (filter in filterList) {
            for (option in filter.options) {
                if (searchParameter.containsKey(option.key)) {
                    flagFilterHelper[option.uniqueId] = true
                }
            }
        }
    }

    fun getFilterValue(key: String): String {
        return searchParameter[key] ?: ""
    }

    fun getSearchParameter() : Map<String, String> {
        return searchParameter
    }

    fun getFilterList() : List<Filter> {
        return filterList
    }
}