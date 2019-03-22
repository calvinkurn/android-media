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

    private val filterParameter = mutableMapOf<String, String>()
    private val filterViewState = mutableMapOf<String, Boolean>()
    private val filterList = mutableListOf<Filter>()
    private val activeFilterKeyList = mutableSetOf<String>()

    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    constructor(parcel: Parcel) : this() {
        resetStatesBeforeLoad()

        parcel.readMap(filterParameter, String::class.java.classLoader)
        parcel.readMap(filterViewState, Boolean::class.java.classLoader)
        parcel.readList(filterList, Filter::class.java.classLoader)
        pressedSliderMinValueState = parcel.readInt()
        pressedSliderMaxValueState = parcel.readInt()

        loadActiveFilter()
    }

    fun initFilterController(filterParameter: Map<String, String> = mapOf(),
                             filterList: List<Filter> = listOf()) {
        resetStatesBeforeLoad()

        loadFilterParameter(filterParameter)
        loadFilterList(filterList)
        loadFilterViewState()
        loadActiveFilter()
    }

    private fun resetStatesBeforeLoad() {
        filterParameter.clear()
        filterViewState.clear()
        filterList.clear()
        activeFilterKeyList.clear()
        pressedSliderMaxValueState = -1
        pressedSliderMaxValueState = -1
    }

    private fun loadFilterParameter(filterParameter: Map<String, String>) {
        this.filterParameter.putAll(filterParameter)
        filterParameterRemoveDuplicateValues()
    }

    private fun filterParameterRemoveDuplicateValues() {
        for(entrySet in filterParameter.entries) {
            val valueSet = entrySet.value.split(Option.VALUE_SEPARATOR).toSet()
            entrySet.setValue(valueSet.joinToString(separator = Option.VALUE_SEPARATOR))
        }
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

    private fun loadFilterViewState() {
        val optionsForFilterViewState = mutableListOf<Option>()

        loopOptionsInFilterList { _, option ->
            if(isOptionSelected(option))
                addOrCombineOptions(optionsForFilterViewState, option)
        }

        for(option in optionsForFilterViewState) {
            filterViewState[option.uniqueId] = true
        }
    }

    private fun canAddToFilterViewState(filter: Filter, option: Option) : Boolean {
        return filter.isExpandableFilter && !filter.isCategoryFilter && isOptionSelected(option)
    }

    private fun isOptionSelected(option: Option) : Boolean {
        val key = option.key

        if(filterParameter.containsKey(key)) {
            val optionValues = option.value.split(Option.VALUE_SEPARATOR)
            val filterParameterValues = getFilterValue(key).split(Option.VALUE_SEPARATOR)

            return filterParameterValues.containsAll(optionValues)
        }

        return false
    }

    private fun addOrCombineOptions(optionsForFilterViewState: MutableList<Option>, option: Option) {
        val optionsWithSameKey = optionsForFilterViewState.filter { it.key == option.key }

        if (optionsWithSameKey.isEmpty()) {
            optionsForFilterViewState.add(option)
        } else {
            setBundledOptionsForFlagFilterHelper(optionsForFilterViewState, option)
        }
    }

    private fun setBundledOptionsForFlagFilterHelper(optionsForFilterViewState: MutableList<Option>, option: Option) {
        val iterator = optionsForFilterViewState.listIterator()
        var optionHasBeenAddedOrReplaced = false

        while (iterator.hasNext()
            && !optionHasBeenAddedOrReplaced) {
            val existingOption = iterator.next()

            if(existingOption.key == option.key) {
                optionHasBeenAddedOrReplaced = replaceOrAddOptionWithSameKey(iterator, existingOption, option)
            }
        }

        if(!optionHasBeenAddedOrReplaced) {
            optionsForFilterViewState.add(option)
        }
    }

    private fun replaceOrAddOptionWithSameKey(iterator: MutableListIterator<Option>, existingOption: Option, currentOption: Option) : Boolean {
        val existingOptionValueList = existingOption.value.split(Option.VALUE_SEPARATOR).toList()
        val currentOptionValueList = currentOption.value.split(Option.VALUE_SEPARATOR).toList()

        return when {
            shouldReplaceExistingOptionWithCurrentOption(currentOptionValueList, existingOptionValueList)-> {
                iterator.set(currentOption)
                true
            }
            existingOptionAlreadyContainsCurrentOption(currentOptionValueList, existingOptionValueList) -> true
            else -> false
        }
    }

    private fun shouldReplaceExistingOptionWithCurrentOption(currentOptionValueList: List<String>, existingOptionValueList: List<String>) : Boolean {
        return currentOptionValueList.containsAll(existingOptionValueList)
    }

    private fun existingOptionAlreadyContainsCurrentOption(currentOptionValueList: List<String>, existingOptionValueList: List<String>) : Boolean {
        return existingOptionValueList.containsAll(currentOptionValueList)
    }

    private fun loadActiveFilter() {
        loopOptionsInFilterList { _, option ->
            if(filterParameter.containsKey(option.key)) {
                activeFilterKeyList.add(option.key)
            }
        }
    }

    private fun loopOptionsInFilterList(action: (filter: Filter, option: Option) -> Unit) {
        for(filter in filterList)
            for(option in filter.options)
                action(filter, option)
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun isSliderValueHasChanged(minValue: Int, maxValue: Int): Boolean {
        return minValue != pressedSliderMinValueState || maxValue != pressedSliderMaxValueState
    }

    fun setFilterValueFromDetailActivity(optionList: List<Option>) {
        val tempHashMapFilterParameter = mutableMapOf<String, String>()

        for(option in optionList) {
            filterParameter.remove(option.key)
            activeFilterKeyList.remove(option.key)

            val isFilterApplied = isFilterApplied(option.inputState)
            setOrRemoveFlagFilterHelper(option.uniqueId, isFilterApplied)

            if(isFilterApplied) {
                insertToTempHashMap(tempHashMapFilterParameter, option)
            }
        }

        filterParameter.putAll(tempHashMapFilterParameter)
        for(entryKey in tempHashMapFilterParameter.keys) {
            activeFilterKeyList.add(entryKey)
        }
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
        if(value) filterViewState[key] = true
        else filterViewState.remove(key)
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

    fun resetAllFilters() {
        removeActiveFiltersFromFilterParameter()

        filterViewState.clear()

        resetSliderStates()
    }

    private fun removeActiveFiltersFromFilterParameter() {
        val activeFiltersForReset = mutableListOf<String>()
        activeFiltersForReset.addAll(activeFilterKeyList)

        for(activeFilterKey in activeFiltersForReset) {
            setOrRemoveFilterValue(false, activeFilterKey, "")
        }
    }

    private fun setOrRemoveFilterValue(isFilterApplied: Boolean, key: String, value: String) {
        if(isFilterApplied) {
            filterParameter[key] = value
            activeFilterKeyList.add(key)
        }
        else {
            filterParameter.remove(key)
            activeFilterKeyList.remove(key)
        }
    }

    private fun resetSliderStates() {
        pressedSliderMinValueState = -1
        pressedSliderMaxValueState = -1
    }

    fun getSelectedOptions(filter: Filter): List<Option> {
        val selectedOptionList = ArrayList<Option>()
        val emptyPopularList = listOf<Option>()

        putSelectedOptionsToList(selectedOptionList, filter, emptyPopularList)

        return selectedOptionList
    }

    fun getSelectedAndPopularOptions(filter: Filter): List<Option> {
        val selectedOptionList = ArrayList<Option>()
        val popularOptionList: List<Option> = getPopularOptionList(filter)

        putSelectedOptionsToList(selectedOptionList, filter, popularOptionList)

        return selectedOptionList
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

    private fun putSelectedOptionsToList(selectedOptionList: MutableList<Option>, filter: Filter, popularOptionList: List<Option>) {
        if (isAddCategoryFilter(filter, popularOptionList)) {
            selectedOptionList.add(getSelectedCategoryAsOption(filter))
        } else {
            selectedOptionList.addAll(
                getCustomSelectedOptionList(filter) { option ->
                isCustomOptionDisplayed(option) && isIncludePopularOption(popularOptionList, option)
            })
        }

        selectedOptionList.addAll(popularOptionList)
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

    private fun getCustomSelectedOptionList(filter: Filter, isDisplayed:(Option) -> Boolean): List<Option> {
        val checkedOptions = ArrayList<Option>()

        for (option in filter.options) {
            if(isDisplayed(option)) {
                checkedOptions.add(option)
            }
        }
        return checkedOptions
    }

    private fun isCustomOptionDisplayed(option: Option) : Boolean {
        return java.lang.Boolean.TRUE == getFilterViewStateValue(option.uniqueId)
    }

    private fun isIncludePopularOption(popularOptionList: List<Option>, option: Option) : Boolean {
        return (popularOptionList.isEmpty() || !option.isPopular)
    }

    fun isFilterActive() : Boolean {
        return activeFilterKeyList.size > 0
    }

    fun getFilterValue(key: String): String {
        return filterParameter[key] ?: ""
    }

    fun getFilterViewStateValue(key: String) : Boolean {
        return filterViewState[key] ?: false
    }

    fun getFilterParameter() : Map<String, String> {
        return this.filterParameter
    }

    fun getFilterList() : List<Filter> {
        return filterList
    }

    fun getActiveFilterOptionList() : List<Option> {
        val activeFilterOptionList = mutableListOf<Option>()

        loopOptionsInFilterList { filter, option ->
            if(activeFilterKeyList.contains(option.key)) {
                when {
                    filter.isPriceFilter -> addIntoActiveFilterOptionListFromFilterParameter(activeFilterOptionList, option)
                    filter.isExpandableFilter -> addintoActiveFilterListExpandableFilter(filter, activeFilterOptionList, option)
                    else -> addIntoActiveFilterOptionListFromFilterViewState(activeFilterOptionList, option)
                }
            }
        }

        return activeFilterOptionList
    }

    private fun addintoActiveFilterListExpandableFilter(filter: Filter, activeFilterOptionList: MutableList<Option>, option: Option) {
        if(filter.isCategoryFilter) {
            addIntoActiveFilterOptionListFromFilterParameter(activeFilterOptionList, option)
        }
        else {
            addIntoActiveFilterOptionListFromFilterViewState(activeFilterOptionList, option)
        }
    }

    private fun addIntoActiveFilterOptionListFromFilterParameter(activeFilterOptionList: MutableList<Option>, option: Option) {
        if(filterParameter.containsKey(option.key)) {
            activeFilterOptionList.add(option)
        }
    }

    private fun addIntoActiveFilterOptionListFromFilterViewState(activeFilterOptionList: MutableList<Option>, option: Option) {
        if(filterViewState.containsKey(option.key)) {
            activeFilterOptionList.add(option)
        }
    }

    // New functionality
    // TODO:: remove previous functions to set and get filter values / state
    fun setFilter(optionList: List<Option>) {
        val tempHashMapFilterParameter = mutableMapOf<String, String>()

        for(option in optionList) {
            filterParameter.remove(option.key)

            val isFilterApplied = isFilterApplied(option.inputState)

            if(isFilterApplied) {
                filterViewState[option.uniqueId] = true

                val currentValueInTempHashMap = tempHashMapFilterParameter[option.key] ?: ""
                val currentValueInTempHashMapList = currentValueInTempHashMap.split(Option.VALUE_SEPARATOR).toMutableList()
                currentValueInTempHashMapList.addAll(option.value.split(Option.VALUE_SEPARATOR).toList())
                currentValueInTempHashMapList.removeAll { it == "" }

                tempHashMapFilterParameter[option.key] = currentValueInTempHashMapList.joinToString(separator = Option.VALUE_SEPARATOR)
            }
            else {
                filterViewState.remove(option.uniqueId)
            }
        }

        filterParameter.putAll(tempHashMapFilterParameter)
    }

    @JvmOverloads
    fun setFilter(option: Option, isFilterApplied: Boolean, isCleanUpExistingFilterWithSameKey: Boolean = false) {
        if(isCleanUpExistingFilterWithSameKey) {
            filterParameter.remove(option.key)

            val filterViewStateIterator = filterViewState.entries.iterator()
            while(filterViewStateIterator.hasNext()) {
                val entrySet = filterViewStateIterator.next()
                val optionKey = OptionHelper.parseKeyFromUniqueId(entrySet.key)
                if(optionKey == option.key) {
                    filterViewStateIterator.remove()
                }
            }
        }

        val existingValue = filterParameter[option.key] ?: ""
        val existingValueList = existingValue.split(Option.VALUE_SEPARATOR).toMutableList()

        if(isFilterApplied) {
            if(!getFilterViewState(option)) {
                existingValueList.addAll(option.value.split(Option.VALUE_SEPARATOR).toList())
                existingValueList.removeAll { it == "" }

                filterParameter[option.key] =
                    existingValueList.joinToString(separator = Option.VALUE_SEPARATOR)
                filterViewState[option.uniqueId] = true
            }
        }
        else {
            if(getFilterViewState(option)) {
                val newValueList = option.value.split(Option.VALUE_SEPARATOR).toList()
                for (newValue in newValueList) {
                    existingValueList.remove(newValue)
                }

                filterViewState.remove(option.uniqueId)
            }
        }
    }

    fun getFilterValue(option: Option) : String {
        return filterParameter[option.key] ?: ""
    }

    fun getFilterViewState(option: Option) : Boolean {
        return filterViewState[option.uniqueId] ?: false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeMap(filterParameter)
        parcel.writeMap(filterViewState)
        parcel.writeList(filterList)
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