package com.tokopedia.discovery.newdynamicfilter.controller

import android.text.TextUtils
import com.tokopedia.discovery.common.data.Filter
import com.tokopedia.discovery.common.data.LevelThreeCategory
import com.tokopedia.discovery.common.data.LevelTwoCategory
import com.tokopedia.discovery.common.data.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import java.util.*

open class FilterController {

    private val nonFilterParameter = mutableMapOf<String, String>()
    private val filterList = mutableListOf<Filter>()
    private val filterViewState = mutableSetOf<String>()
    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1

    fun initFilterController(parameter: Map<String, String>? = mapOf(),
                             filterList: List<Filter>? = listOf()) {
        resetStatesBeforeLoad()

        loadFilterList(filterList)
        loadParameter(parameter)
        loadFilterViewState(parameter)
    }

    private fun resetStatesBeforeLoad() {
        nonFilterParameter.clear()
        filterList.clear()
        filterViewState.clear()
        pressedSliderMaxValueState = -1
        pressedSliderMaxValueState = -1
    }

    private fun loadFilterList(filterList: List<Filter>?) {
        if(filterList == null) return

        this.filterList.addAll(filterList)
    }

    private fun loadParameter(parameter: Map<String, String>?) {
        if(parameter == null) return

        generateNonFilterParameter(parameter)
    }

    private fun generateNonFilterParameter(parameter: Map<String, String>) {
        val toNonFilterParameter = mutableMapOf<String, String>()

        for(entry in parameter.entries) {
            if(!getIsFilterParameter(entry)) {
                toNonFilterParameter[entry.key] = entry.value
            }
        }

        nonFilterParameter.putAll(toNonFilterParameter)
    }

    private fun getIsFilterParameter(parameterEntry: Map.Entry<String, String>) : Boolean {
        var isFilterParameter = false

        loopOptionsInFilterList { _, option ->
            if(option.key == parameterEntry.key) {
                isFilterParameter = true
                return@loopOptionsInFilterList
            }
        }

        return isFilterParameter
    }

    private fun loadFilterViewState(parameter: Map<String, String>?) {
        if(parameter == null) return

        val optionsForFilterViewState = mutableListOf<Option>()

        loopOptionsInFilterList { _, option ->
            if(isOptionSelected(parameter, option)) {
                optionsForFilterViewState.add(option)
            }
            else {
                addOptionsFromDeeperLevelCategory(parameter, option, optionsForFilterViewState)
            }
        }

        iterateOptionAndCheckForBundledOption(optionsForFilterViewState)

        for(option in optionsForFilterViewState) {
            if(option.value == "") option.value = parameter[option.key]
            filterViewState.add(option.uniqueId)
        }
    }

    private fun isOptionSelected(parameter: Map<String, String>, option: Option) : Boolean {
        return if(parameter.containsKey(option.key))
            return when {
                option.value == "" -> true
                isOptionValuesExistsInFilterParameter(parameter, option) -> true
                else -> false
            }
        else false
    }

    private fun isOptionValuesExistsInFilterParameter(parameter: Map<String, String>, option: Option) : Boolean {
        val optionValues = option.value.split(OptionHelper.VALUE_SEPARATOR)
        val filterParameterValues = parameter[option.key]?.split(OptionHelper.VALUE_SEPARATOR) ?: listOf<Option>()

        return filterParameterValues.containsAll(optionValues)
    }

    private fun addOptionsFromDeeperLevelCategory(parameter: Map<String, String>, option: Option, optionsForFilterViewState: MutableList<Option>) {
        if(!listIsNullOrEmpty(option.levelTwoCategoryList)) {
            val categoryAsOption = getSelectedOptionLevelTwoCategoryList(parameter, option.levelTwoCategoryList)
            if(categoryAsOption != null) {
                optionsForFilterViewState.add(categoryAsOption)
            }
        }
    }

    private fun <T: Any> listIsNullOrEmpty(list : List<T>?) : Boolean {
        return list == null || list.isEmpty()
    }

    private fun getSelectedOptionLevelTwoCategoryList(parameter: Map<String, String>, levelTwoCategoryList: List<LevelTwoCategory>) : Option? {
        for(levelTwoCategory in levelTwoCategoryList) {
            if(parameter[levelTwoCategory.key] == levelTwoCategory.value) {
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelTwoCategory.key, levelTwoCategory.value, levelTwoCategory.name)
                )
            }
            else if(!listIsNullOrEmpty(levelTwoCategory.levelThreeCategoryList)) {
                val levelThreeCategoryAsOption = getSelectedOptionLevelThreeCategoryList(parameter, levelTwoCategory.levelThreeCategoryList)
                if(levelThreeCategoryAsOption != null)
                    return levelThreeCategoryAsOption
            }
        }

        return null
    }

    private fun getSelectedOptionLevelThreeCategoryList(parameter: Map<String, String>, levelThreeCategoryList: List<LevelThreeCategory>) : Option? {
        for(levelThreeCategory in levelThreeCategoryList)
            if(parameter[levelThreeCategory.key] == levelThreeCategory.value)
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelThreeCategory.key, levelThreeCategory.value, levelThreeCategory.name)
                )

        return null
    }

    private fun iterateOptionAndCheckForBundledOption(optionsForFilterViewState: MutableList<Option>) {
        val currentIterator = optionsForFilterViewState.listIterator()

        while(currentIterator.hasNext()) {
            val currentOption = currentIterator.next()
            val bundledOptionList = optionsForFilterViewState.filter { it.key == currentOption.key }

            for(bundledOption in bundledOptionList) {
                if(isOptionAlreadyBundled(currentOption.value, bundledOption.value)) {
                    currentIterator.remove()
                    break
                }
            }
        }
    }

    private fun isOptionAlreadyBundled(optionValue: String, bundledOptionValue: String) : Boolean {
        val optionValueList = optionValue.split(OptionHelper.VALUE_SEPARATOR).toList()
        val bundledOptionValueList = bundledOptionValue.split(OptionHelper.VALUE_SEPARATOR).toList()

        return optionValue.length < bundledOptionValue.length
                && bundledOptionValueList.containsAll(optionValueList)
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

    fun resetAllFilters() {
        filterViewState.clear()
        resetSliderStates()
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
                    if (popularOptionList.isEmpty()) {
                        getFilterViewState(option)
                    } else {
                        getFilterViewState(option) && !option.isPopular
                    }
                }
            )
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

    fun isFilterActive() : Boolean {
        return filterViewState.isNotEmpty()
    }

    fun getFilterValue(key: String): String {
        val optionValues = mutableSetOf<String>()

        filterViewState.forEach { optionUniqueId ->
            if (OptionHelper.parseKeyFromUniqueId(optionUniqueId) == key) {
                val optionValue = OptionHelper.parseValueFromUniqueId(optionUniqueId)
                optionValues += optionValue.split(OptionHelper.VALUE_SEPARATOR).toSet()
            }
        }

        return optionValues.joinToString(separator = OptionHelper.VALUE_SEPARATOR)
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

    fun getParameter() : Map<String, String> {
        return getActiveFilterMap() + nonFilterParameter
    }

    fun getActiveFilterMap() : Map<String, String> {
        val filterParameter = mutableMapOf<String, String>()

        for(optionUniqueId in filterViewState) {
            val optionKey = OptionHelper.parseKeyFromUniqueId(optionUniqueId)
            val currentOptionValue = filterParameter[optionKey]
            val addedOptionValue = OptionHelper.parseValueFromUniqueId(optionUniqueId)

            filterParameter[optionKey] = getAppendedFilterValues(currentOptionValue, addedOptionValue)
        }

        return filterParameter
    }

    private fun getAppendedFilterValues(currentFilterValue: String?, addedFilterValue: String) : String {
        val currentFilterValueList = currentFilterValue?.split(OptionHelper.VALUE_SEPARATOR)?.toSet() ?: setOf()
        val addedFilterValueList = addedFilterValue.split(OptionHelper.VALUE_SEPARATOR).toSet()

        return (currentFilterValueList + addedFilterValueList).joinToString(separator = OptionHelper.VALUE_SEPARATOR)
    }

    fun getActiveFilterOptionList() : List<Option> {
        val activeFilterOptionList = mutableListOf<Option>()

        filterViewState.forEach {
            activeFilterOptionList.add(OptionHelper.generateOptionFromUniqueId(it))
        }

        return activeFilterOptionList
    }

    @JvmOverloads
    fun setFilter(option: Option?, isFilterApplied: Boolean, isCleanUpExistingFilterWithSameKey: Boolean = false) {
        if(option == null) return

        if(isCleanUpExistingFilterWithSameKey) {
            cleanUpExistingFilterWithSameKey(option.key)
        }

        saveFilterViewState(option.uniqueId, isFilterApplied)
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

        for(option in optionList) {
            val isFilterApplied = option.inputState?.toBoolean() ?: false
            saveFilterViewState(option.uniqueId, isFilterApplied)
        }
    }
}