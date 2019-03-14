package com.tokopedia.discovery.newdynamicfilter.controller

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import java.util.*

class FilterController() : Parcelable {

    private val searchParameter = mutableMapOf<String, String>()
    private val flagFilterHelper = mutableMapOf<String, Boolean>()
    private val filterList = mutableListOf<Filter>()
    private val activeFilterKeyList = mutableSetOf<String>()
    private val shownInMainState = mutableMapOf<String, Boolean>()

    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    constructor(parcel: Parcel) : this() {
        resetStatesBeforeLoad()

        parcel.readMap(searchParameter, String::class.java.classLoader)
        parcel.readMap(flagFilterHelper, Boolean::class.java.classLoader)
        parcel.readList(filterList, Filter::class.java.classLoader)
        parcel.readMap(shownInMainState, Boolean::class.java.classLoader)
        pressedSliderMinValueState = parcel.readInt()
        pressedSliderMaxValueState = parcel.readInt()

        loadActiveFilter()
    }

    fun initFilterController(searchParameter: Map<String, String> = mapOf(),
                             filterList: List<Filter> = listOf()) {
        resetStatesBeforeLoad()

        loadSearchParameter(searchParameter)
        loadFilterList(filterList)
        loadFlagFilterHelper()
        loadActiveFilter()
    }

    private fun resetStatesBeforeLoad() {
        searchParameter.clear()
        flagFilterHelper.clear()
        filterList.clear()
        activeFilterKeyList.clear()
        shownInMainState.clear()
        pressedSliderMaxValueState = -1
        pressedSliderMaxValueState = -1
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
        val optionsForFlagFilterHelper = mutableListOf<Option>()

        loopOptionsInFilterList { option ->
            if(isOptionSelected(option))
                addOrCombineOptions(optionsForFlagFilterHelper, option)
        }

        for(option in optionsForFlagFilterHelper) {
            flagFilterHelper[option.uniqueId] = true
        }
    }

    private fun isOptionSelected(option: Option) : Boolean {
        val key = option.key

        if(searchParameter.containsKey(key)) {
            val optionValues = option.value.split(Option.VALUE_SEPARATOR)
            val searchParameterValues = getFilterValue(key).split(Option.VALUE_SEPARATOR)

            return searchParameterValues.containsAll(optionValues)
        }

        return false
    }

    private fun addOrCombineOptions(optionsForFlagFilterHelper: MutableList<Option>, option: Option) {
        val optionsWithSameKey = optionsForFlagFilterHelper.filter { it.key == option.key }.toMutableList()

        if (optionsWithSameKey.isEmpty()) {
            optionsForFlagFilterHelper.add(option)
        } else {
            combineOptionsToFlagFilterHelper(optionsForFlagFilterHelper, option)
        }
    }

    private fun combineOptionsToFlagFilterHelper(optionsForFlagFilterHelper: MutableList<Option>, option: Option) {
        val iterator = optionsForFlagFilterHelper.listIterator()
        while (iterator.hasNext()) {
            val existingOption = iterator.next()

            if(existingOption.key == option.key) {
                val existingOptionValueList = existingOption.value.split(Option.VALUE_SEPARATOR).toList()
                val currentOptionValueList = option.value.split(Option.VALUE_SEPARATOR).toList()

                if (currentOptionValueList.containsAll(existingOptionValueList)) {
                    iterator.set(option)
                } else if (!existingOptionValueList.containsAll(currentOptionValueList)
                    && existingOptionValueList != currentOptionValueList
                ) {
                    iterator.add(option)
                }
            }
        }
    }

    private fun loadActiveFilter() {
        loopOptionsInFilterList { option ->
            if(searchParameter.containsKey(option.key)) {
                activeFilterKeyList.add(option.key)
            }
        }
    }

    private fun loopOptionsInFilterList(action: (option: Option) -> Unit) {
        for(filter in filterList)
            for(option in filter.options)
                action(option)
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun isSliderValueHasChanged(minValue: Int, maxValue: Int): Boolean {
        return minValue != pressedSliderMinValueState || maxValue != pressedSliderMaxValueState
    }

    fun setFilterValueFromDetailActivity(optionList: List<Option>) {
        val tempHashMapSearchParameter = mutableMapOf<String, String>()

        for(option in optionList) {
            val isFilterApplied = isFilterApplied(option.inputState)
            setOrRemoveShownInMainState(option.uniqueId, isFilterApplied)
            setOrRemoveFlagFilterHelper(option.uniqueId, isFilterApplied)

            if(isFilterApplied) {
                insertToTempHashMap(tempHashMapSearchParameter, option)
            }
        }

        searchParameter.putAll(tempHashMapSearchParameter)
    }

    private fun insertToTempHashMap(tempHashMap: MutableMap<String, String>, option: Option) {
        val currentValueInHashMap = tempHashMap[option.key] ?: ""

        tempHashMap[option.key] =
            if(!TextUtils.isEmpty(currentValueInHashMap))
                currentValueInHashMap + Option.VALUE_SEPARATOR + option.value
            else
                option.value
    }

    fun setFilterValueExpandableItem(option: Option, value: Boolean) {
        setOrRemoveFlagFilterHelper(option.uniqueId, value)

        setFilterValue(option, getNewFilterValue(value, option.key, option.value))
    }

    private fun setOrRemoveFlagFilterHelper(key: String, value: Boolean) {
        if(value) flagFilterHelper[key] = true
        else flagFilterHelper.remove(key)
    }

    private fun getNewFilterValue(isFilterAdded: Boolean, key: String, value: String) : String {
        return if(isFilterAdded) {
            appendFilterValue(key, value)
        } else {
            removeFilterValue(key, value)
        }
    }

    private fun appendFilterValue(key: String, value: String) : String {
        val currentValue = getFilterValue(key)

        return if(!TextUtils.isEmpty(currentValue)) {
            currentValue + Option.VALUE_SEPARATOR + value
        }
        else {
            value
        }
    }

    private fun removeFilterValue(key: String, value: String) : String {
        val currentValueList = getFilterValue(key).split(Option.VALUE_SEPARATOR).toMutableList()
        val newValueList = value.split(Option.VALUE_SEPARATOR)

        for(newValue in newValueList) {
            currentValueList.remove(newValue)
        }

        return currentValueList.joinToString(separator = Option.VALUE_SEPARATOR)
    }

    fun setFilterValue(option: Option, value: String) {
        val isFilterApplied = isFilterApplied(value)

        setOrRemoveFilterValue(isFilterApplied, option.key, value)
    }

    private fun isFilterApplied(value: String) : Boolean {
        return if(value.toBoolean()) true
        else isValueNotEmptyAndNotFalse(value)
    }

    private fun isValueNotEmptyAndNotFalse(value: String) : Boolean {
        return !TextUtils.isEmpty(value) && value != java.lang.Boolean.FALSE.toString()
    }

    private fun setOrRemoveShownInMainState(key: String, value: Boolean) {
        if(value) shownInMainState[key] = true
        else shownInMainState.remove(key)
    }

    fun resetAllFilters() {
        removeActiveFiltersFromSearchParameter()

        flagFilterHelper.clear()

        resetSliderStates()
    }

    private fun removeActiveFiltersFromSearchParameter() {
        val activeFiltersForReset = mutableListOf<String>()
        activeFiltersForReset.addAll(activeFilterKeyList)

        for(activeFilterKey in activeFiltersForReset) {
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

    private fun resetSliderStates() {
        pressedSliderMinValueState = -1
        pressedSliderMaxValueState = -1
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
        return java.lang.Boolean.TRUE == getFlagFilterHelperValue(option.uniqueId)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeMap(searchParameter)
        parcel.writeMap(flagFilterHelper)
        parcel.writeList(filterList)
        parcel.writeMap(shownInMainState)
        parcel.writeInt(pressedSliderMinValueState)
        parcel.writeInt(pressedSliderMaxValueState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterController> {
        override fun createFromParcel(parcel: Parcel): FilterController {
            return FilterController(parcel)
        }

        override fun newArray(size: Int): Array<FilterController?> {
            return arrayOfNulls(size)
        }
    }
}