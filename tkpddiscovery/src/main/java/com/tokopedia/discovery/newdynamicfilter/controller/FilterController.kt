package com.tokopedia.discovery.newdynamicfilter.controller

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.LevelThreeCategory
import com.tokopedia.core.discovery.model.LevelTwoCategory
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import java.util.*

class FilterController() : Parcelable {

    private val filterParameter = mutableMapOf<String, String>()
    private val filterViewState = mutableSetOf<String>()
    private val filterList = mutableListOf<Filter>()

    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    constructor(parcel: Parcel) : this() {
        resetStatesBeforeLoad()

        val filterViewStateList = mutableListOf<String>()

        parcel.readMap(filterParameter, String::class.java.classLoader)
        parcel.readList(filterViewStateList, String::class.java.classLoader)
        parcel.readList(filterList, Filter::class.java.classLoader)
        pressedSliderMinValueState = parcel.readInt()
        pressedSliderMaxValueState = parcel.readInt()

        loadFilterViewStateFromParcel(filterViewStateList)
    }

    private fun loadFilterViewStateFromParcel(filterViewStateList: List<String>) {
        for(viewState in filterViewStateList) {
            filterViewState.add(viewState)
        }
    }

    fun initFilterController(filterParameter: Map<String, String>? = mapOf(),
                             filterList: List<Filter>? = listOf()) {
        resetStatesBeforeLoad()

        loadFilterParameter(filterParameter)
        loadFilterList(filterList)
        loadFilterViewState()
    }

    private fun resetStatesBeforeLoad() {
        filterParameter.clear()
        filterViewState.clear()
        filterList.clear()
        pressedSliderMaxValueState = -1
        pressedSliderMaxValueState = -1
    }

    private fun loadFilterParameter(filterParameter: Map<String, String>?) {
        if(filterParameter == null) return

        this.filterParameter.putAll(filterParameter)
        filterParameterRemoveDuplicateValues()
    }

    private fun filterParameterRemoveDuplicateValues() {
        for(entrySet in filterParameter.entries) {
            val valueSet = entrySet.value.split(OptionHelper.VALUE_SEPARATOR).toSet()
            entrySet.setValue(valueSet.joinToString(separator = OptionHelper.VALUE_SEPARATOR))
        }
    }

    private fun loadFilterList(filterList: List<Filter>?) {
        if(filterList == null) return

        this.filterList.addAll(filterList)
    }

    private fun loadFilterViewState() {
        val optionsForFilterViewState = mutableListOf<Option>()

        loopSelectedOptionsInFilterList { _, option ->
            addOrCombineOptions(optionsForFilterViewState, option)
        }

        for(option in optionsForFilterViewState) {
            if(option.value == "") option.value = getFilterValue(option.key)
            filterViewState.add(option.uniqueId)
        }
    }

    private fun addOrCombineOptions(optionsForFilterViewState: MutableList<Option>, option: Option) {
        val optionsWithSameKey = optionsForFilterViewState.filter { it.key == option.key }

        if (optionsWithSameKey.isEmpty()) {
            optionsForFilterViewState.add(option)
        } else {
            setBundledOptionsForFilterViewState(optionsForFilterViewState, option)
        }
    }

    private fun setBundledOptionsForFilterViewState(optionsForFilterViewState: MutableList<Option>, option: Option) {
        val iterator = optionsForFilterViewState.listIterator()
        var optionHasBeenAddedOrReplaced = false

        while (iterator.hasNext()
            && !optionHasBeenAddedOrReplaced) {
            val existingOption = iterator.next()

            if(existingOption.key == option.key) {
                optionHasBeenAddedOrReplaced = addOrReplaceOptionWithSameKey(iterator, existingOption, option)
            }
        }

        if(!optionHasBeenAddedOrReplaced) {
            optionsForFilterViewState.add(option)
        }
    }

    private fun addOrReplaceOptionWithSameKey(iterator: MutableListIterator<Option>, existingOption: Option, currentOption: Option) : Boolean {
        val existingOptionValueList = existingOption.value.split(OptionHelper.VALUE_SEPARATOR).toList()
        val currentOptionValueList = currentOption.value.split(OptionHelper.VALUE_SEPARATOR).toList()

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

    private fun loopSelectedOptionsInFilterList(action: (filter: Filter, option: Option) -> Unit) {
        loopOptionsInFilterList { filter, option ->
            if(isOptionSelected(option)) {
                action(filter, option)
            }
            else {
                if(!listIsNullOrEmpty(option.levelTwoCategoryList)) {
                    val categoryAsOption = getSelectedOptionLevelTwoCategoryList(option.levelTwoCategoryList)
                    if(categoryAsOption != null) {
                        action(filter, categoryAsOption)
                    }
                }
            }
        }
    }

    private fun loopOptionsInFilterList(action: (filter: Filter, option: Option) -> Unit) {
        for(filter in filterList)
            for(option in filter.options)
                action(filter, option)
    }

    private fun isOptionSelected(option: Option) : Boolean {
        return if(filterParameter.containsKey(option.key))
            return when {
                option.value == "" -> true
                isOptionValuesExistsInFilterParameter(option) -> true
                else -> false
            }
        else false
    }

    private fun <T: Any> listIsNullOrEmpty(list : List<T>?) : Boolean {
        return list == null || list.isEmpty()
    }

    private fun getSelectedOptionLevelTwoCategoryList(levelTwoCategoryList: List<LevelTwoCategory>) : Option? {
        for(levelTwoCategory in levelTwoCategoryList) {
            if(getFilterValue(levelTwoCategory.key) == levelTwoCategory.value) {
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelTwoCategory.key, levelTwoCategory.value, levelTwoCategory.name)
                )
            }
            else if(!listIsNullOrEmpty(levelTwoCategory.levelThreeCategoryList)) {
                val levelThreeCategoryAsOption = getSelectedOptionLevelThreeCategoryList(levelTwoCategory.levelThreeCategoryList)
                if(levelThreeCategoryAsOption != null)
                    return levelThreeCategoryAsOption
            }
        }

        return null
    }

    private fun getSelectedOptionLevelThreeCategoryList(levelThreeCategoryList: List<LevelThreeCategory>) : Option? {
        for(levelThreeCategory in levelThreeCategoryList)
            if(getFilterValue(levelThreeCategory.key) == levelThreeCategory.value)
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelThreeCategory.key, levelThreeCategory.value, levelThreeCategory.name)
                )

        return null
    }

    private fun isOptionValuesExistsInFilterParameter(option: Option) : Boolean {
        val optionValues = option.value.split(OptionHelper.VALUE_SEPARATOR)
        val filterParameterValues = getFilterValue(option.key).split(OptionHelper.VALUE_SEPARATOR)

        return filterParameterValues.containsAll(optionValues)
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun isSliderValueHasChanged(minValue: Int, maxValue: Int): Boolean {
        return minValue != pressedSliderMinValueState || maxValue != pressedSliderMaxValueState
    }

    fun resetAllFilters() {
        removeActiveFiltersFromFilterParameter()

        filterViewState.clear()

        resetSliderStates()
    }

    private fun removeActiveFiltersFromFilterParameter() {
        val activeFilterKeySet = mutableSetOf<String>()

        for(filterViewStateUniqueId in filterViewState) {
            val optionKey = OptionHelper.parseKeyFromUniqueId(filterViewStateUniqueId)
            activeFilterKeySet.add(optionKey)
        }

        for(activeFilterKey in activeFilterKeySet) {
            filterParameter.remove(activeFilterKey)
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
                getFilterViewState(option) && isIncludePopularOption(popularOptionList, option)
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

    private fun isIncludePopularOption(popularOptionList: List<Option>, option: Option) : Boolean {
        return (popularOptionList.isEmpty() || !option.isPopular)
    }

    fun isFilterActive() : Boolean {
        return filterViewState.isNotEmpty()
    }

    fun getFilterValue(key: String): String {
        return filterParameter[key] ?: ""
    }

    fun getFilterViewState(option: Option) : Boolean {
        return getFilterViewState(option.uniqueId)
    }

    fun getFilterViewState(uniqueId: String) : Boolean {
        return filterViewState.contains(uniqueId)
    }

    fun getFilterViewStateSize() : Int {
        return filterViewState.size
    }

    fun getFilterParameter() : Map<String, String> {
        return this.filterParameter
    }

    fun getActiveFilterMap() : Map<String, String> {
        val activeFilter = mutableMapOf<String, String>()

        loopOptionsInFilterList { _, option ->
            if (filterParameter.contains(option.key)) {
                activeFilter[option.key] = getFilterValue(option.key)
            }
        }

        return activeFilter
    }

    fun getActiveFilterOptionList() : List<Option> {
        val activeFilterOptionList = mutableListOf<Option>()

        loopSelectedOptionsInFilterList { _, option ->
            if (isOptionConsideredActiveFilter(option)) {
                activeFilterOptionList.add(option)
            }
        }

        return activeFilterOptionList
    }

    private fun isOptionConsideredActiveFilter(option: Option) : Boolean {
        return (option.value == "" && filterParameter.contains(option.key))
                || getFilterViewState(option)
    }

    @JvmOverloads
    fun setFilter(option: Option?, isFilterApplied: Boolean, isCleanUpExistingFilterWithSameKey: Boolean = false) {
        if(option == null) return

        if(isCleanUpExistingFilterWithSameKey) {
            cleanUpExistingFilterWithSameKey(option.key)
        }

        saveFilterViewState(option.uniqueId, isFilterApplied)

        populateFilterViewStateToFilterParameter(option.key)
    }

    private fun cleanUpExistingFilterWithSameKey(newOptionKey: String) {
        val filterViewStateIterator = filterViewState.iterator()
        while(filterViewStateIterator.hasNext()) {
            val filterViewStateUniqueId = filterViewStateIterator.next()
            val existingOptionKey = OptionHelper.parseKeyFromUniqueId(filterViewStateUniqueId)
            if(existingOptionKey == newOptionKey) {
                filterViewStateIterator.remove()
            }
        }
    }

    private fun saveFilterViewState(uniqueId: String, isFilterApplied: Boolean) {
        if(isFilterApplied) {
            filterViewState.add(uniqueId)
        }
        else {
            filterViewState.remove(uniqueId)
        }
    }

    fun setFilter(optionList: List<Option>?) {
        if(optionList == null || optionList.isEmpty() ) return

        val updatedOptionsKey = mutableSetOf<String>()

        for(option in optionList) {
            updatedOptionsKey.add(option.key)

            val isFilterApplied = option.inputState?.toBoolean() ?: false
            saveFilterViewState(option.uniqueId, isFilterApplied)
        }

        populateFilterViewStateToFilterParameter(updatedOptionsKey)
    }

    private fun populateFilterViewStateToFilterParameter(keyList: Set<String>) {
        for(key in keyList) {
            populateFilterViewStateToFilterParameter(key)
        }
    }

    private fun populateFilterViewStateToFilterParameter(key: String) {
        val newFilterParameterValueList = mutableSetOf<String>()

        for(filterViewStateUniqueId in filterViewState) {
            val optionKeyInViewState = OptionHelper.parseKeyFromUniqueId(filterViewStateUniqueId)
            if(optionKeyInViewState == key) {
                val optionValueInViewState = OptionHelper.parseValueFromUniqueId(filterViewStateUniqueId)
                newFilterParameterValueList.add(optionValueInViewState)
            }
        }

        if(!newFilterParameterValueList.isEmpty()) {
            filterParameter[key] =
                newFilterParameterValueList.joinToString(separator = OptionHelper.VALUE_SEPARATOR)
        } else {
            filterParameter.remove(key)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeMap(filterParameter)
        parcel.writeList(filterViewState.toList())
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