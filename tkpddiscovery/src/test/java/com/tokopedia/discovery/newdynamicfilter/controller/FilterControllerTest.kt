package com.tokopedia.discovery.newdynamicfilter.controller

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import org.junit.Test

class FilterControllerTest {

    companion object {
        private const val QUERY_FOR_TEST_SAMSUNG = "samsung"

        private const val TRUE_VALUE = true.toString()

        private const val JABODETABEK_VALUE = "1,2,3,4,5,6,7,8,9"
        private const val JAKARTA_VALUE = "1,2,3,4,5"
        private const val JAKARTA_BARAT_VALUE = "1"
        private const val TANGERANG_VALUE = "8"
        private const val BANDUNG_VALUE = "10"

        private const val HANDPHONE_VALUE = "1"
        private const val TV_VALUE = "2"
    }

    private val filterController = FilterController()

    private val officialOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, TRUE_VALUE, SearchApiConst.OFFICIAL))

    private val jabodetabekOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JABODETABEK_VALUE, "Jabodetabek"))
    private val jakartaOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JAKARTA_VALUE, "Jakarta"))
    private val jakartaBaratOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JAKARTA_BARAT_VALUE, "Jakarta Barat"))
    private val tangerangOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, TANGERANG_VALUE, "Tangerang"))
    private val bandungOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, BANDUNG_VALUE, "Bandung"))
    private val handphoneOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, HANDPHONE_VALUE, "Handphone"))
    private val tvOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, TV_VALUE, "TV"))
    private val minPriceOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Harga Minimum"))
    private val maxPriceOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMAX, "", "Harga Maximum"))

    private val tokoOptions = mutableListOf<Option>()
    private val locationOptions = mutableListOf<Option>()
    private val categoryOptions = mutableListOf<Option>()
    private val priceOptions = mutableListOf<Option>()

    @Test
    fun testInitFilterWithNullsOrEmptyShouldNotCrash() {
        filterController.initFilterController(null, null)
        filterController.initFilterController(null, listOf())
        filterController.initFilterController(mapOf(), null)
        filterController.initFilterController()
    }

    @Test
    fun testSetFilterWithNullOptionShouldNotCrash() {
        filterController.initFilterController()

        filterController.setFilter(null, true)
        filterController.setFilter(null)
        filterController.setFilter(listOf())
    }

    @Test
    fun testFilterControllerInitializedProperly() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateCorrect(tokoOptions)
        assertFilterViewStateSizeCorrect(tokoOptions.size)
    }

    private fun createFilterParameter() : Map<String, String> {
        val filterParameter = mutableMapOf<String, String>()
        filterParameter[SearchApiConst.Q] = QUERY_FOR_TEST_SAMSUNG
        filterParameter[SearchApiConst.OFFICIAL] = TRUE_VALUE

        return filterParameter
    }

    private fun createFilterList() : List<Filter> {
        val filterList = mutableListOf<Filter>()

        tokoOptions.add(officialOption)
        filterList.add(createFilterWithOptions(tokoOptions))

        locationOptions.add(jabodetabekOption)
        locationOptions.add(jakartaOption)
        locationOptions.add(jakartaOption)
        locationOptions.add(jakartaBaratOption)
        locationOptions.add(tangerangOption)
        locationOptions.add(bandungOption)
        filterList.add(createFilterWithOptions(locationOptions))

        categoryOptions.add(handphoneOption)
        categoryOptions.add(tvOption)
        filterList.add(createFilterWithOptions(categoryOptions))

        priceOptions.add(minPriceOption)
        priceOptions.add(maxPriceOption)
        filterList.add(createFilterWithOptions(priceOptions))

        return filterList
    }

    private fun createFilterWithOptions(optionList: List<Option>) : Filter {
        val filter = Filter()
        filter.options = optionList
        return filter
    }

    @Test
    fun testSetFilterNoCleanUp() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(jabodetabekOption, true)
        filterController.setFilter(jakartaOption, true)

        assertFilterViewStatesAppended()
    }

    private fun assertFilterViewStatesAppended() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testSetFilterWithCleanUp() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(tvOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 900), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 1000), true, isCleanUpExistingFilterWithSameKey = true)

        assertFilterViewStateReplaced()
    }

    private fun createPriceOptionWithValue(priceOption: Option, priceOptionValue: Int) : Option {
        val createdPriceOption = OptionHelper.generateOptionFromUniqueId(priceOption.uniqueId)
        createdPriceOption.value = priceOptionValue.toString()

        return priceOption
    }

    private fun assertFilterViewStateReplaced() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(tvOption)
        expectedOptions.add(officialOption)
        expectedOptions.add(createPriceOptionWithValue(minPriceOption, 1000))

        assertFilterValueCorrect(expectedOptions)
        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testRemoveFilter() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(officialOption, false)

        assertFilterViewStateRemoved()
    }

    private fun assertFilterViewStateRemoved() {
        assert(filterController.getFilterValue(SearchApiConst.OFFICIAL) == "")
        assertFilterViewStateCorrect(listOf())
        assertFilterViewStateSizeCorrect(0)
    }

    @Test
    fun testSetFilterMultipleOptions() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        val optionsWithInputState = createMultipleOptionsWithInputState()
        filterController.setFilter(optionsWithInputState)

        assertFilterViewStateMultipleOptions()
    }

    private fun createMultipleOptionsWithInputState() : List<Option> {
        val optionList = mutableListOf<Option>()

        val jabodetabekOptionSelected = OptionHelper.generateOptionFromUniqueId(jabodetabekOption.uniqueId)
        jabodetabekOptionSelected.inputState = TRUE_VALUE
        optionList.add(jabodetabekOptionSelected)

        val jakartaOptionSelected = OptionHelper.generateOptionFromUniqueId(jakartaOption.uniqueId)
        jakartaOptionSelected.inputState = TRUE_VALUE
        optionList.add(jakartaOptionSelected)

        val jakartaBaratOptionUnSelected = OptionHelper.generateOptionFromUniqueId(jakartaBaratOption.uniqueId)
        jakartaBaratOptionUnSelected.inputState = "Some random string should NOT make this option selected"
        optionList.add(jakartaBaratOptionUnSelected)

        return optionList
    }

    private fun assertFilterViewStateMultipleOptions() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaBaratOption, true)

        val optionsWithInputState = createMultipleOptionsWithInputState()
        filterController.setFilter(optionsWithInputState)

        assertSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey()
    }

    private fun assertSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)
        expectedOptions.add(handphoneOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testGetActiveFilterAsOptionList() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 1000), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaOption, true)

        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(minPriceOption)
        expectedOptionList.add(jakartaOption)

        assertActiveFilterOptionList(expectedOptionList)
    }

    @Test
    fun testGetActiveFilterAsMap() {
        val filterParameter = createFilterParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(createPriceOptionWithValue(maxPriceOption, 3000000), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaOption, true)

        val expectedMap = mutableMapOf<String, String>()
        expectedMap[officialOption.key] = officialOption.value
        expectedMap[maxPriceOption.key] = 3000000.toString()
        expectedMap[jakartaOption.key] = jakartaOption.value

        assertActiveFilterMap(expectedMap)
    }

    private fun assertFilterValueCorrect(optionList: List<Option>) {
        for(option in optionList) {
            val actualFilterValue = filterController.getFilterValue(option.key)
            assert(actualFilterValue == option.value) {
                getAssertFilterValueMessage(option.uniqueId, option.value, actualFilterValue)
            }
        }
    }

    private fun getAssertFilterValueMessage(uniqueId: String, expectedValue: String, actualValue: String) : String {
        return "Testing filter value, option $uniqueId: expected: $expectedValue, actual: $actualValue"
    }

    private fun assertFilterViewStateCorrect(optionList: List<Option>) {
        for(option in optionList) {
            val actualFilterViewState = filterController.getFilterViewState(option)
            assert(actualFilterViewState) {
                getAssertFilterViewStateMessage(option.uniqueId, true, actualFilterViewState)
            }
        }
    }

    private fun getAssertFilterViewStateMessage(uniqueId: String, expectedFilterViewState: Boolean, actualFilterViewState: Boolean) : String {
        return "Testing filter view state, option $uniqueId: expected: $expectedFilterViewState, actual: $actualFilterViewState"
    }

    private fun assertFilterViewStateSizeCorrect(expectedSize: Int) {
        val actualSize = filterController.getFilterViewStateSize()

        assert(filterController.getFilterViewStateSize() == expectedSize) {
            getAssertFilterViewStateSizeMessage(expectedSize, actualSize)
        }
    }

    private fun getAssertFilterViewStateSizeMessage(expectedFilterViewStateSize: Int, actualFitlerViewStateSize: Int) : String {
        return "Testing filter view state size, expected: $expectedFilterViewStateSize, actual $actualFitlerViewStateSize"
    }

    private fun assertActiveFilterOptionList(expectedOptionList: List<Option>) {
        val actualOptionList = filterController.getActiveFilterOptionList()

        assert(actualOptionList.size == expectedOptionList.size) {
            "Testing get active filter option list, expected size: ${expectedOptionList.size} actual size: ${actualOptionList.size}"
        }

        for(actualFilterOption in actualOptionList) {
            val isExpectedContainsActual = expectedOptionList.filter {
                it.key == actualFilterOption.key
                        && it.name == actualFilterOption.name
            }.isNotEmpty()

            assert(isExpectedContainsActual) {
                "Testing get active filter option list, option ${actualFilterOption.key} is expected."
            }
        }
    }

    private fun assertActiveFilterMap(expectedMap : Map<String, String>) {
        val actualMap = filterController.getActiveFilterParameter()

        assert(actualMap.size == expectedMap.size) {
            "Testing get active filter map, expected size: ${expectedMap.size} actual size: ${actualMap.size}"
        }

        for(expectedMapEntry in expectedMap.entries) {
            assert(actualMap.contains(expectedMapEntry.key)) {
                "Testing get active filter map, expected Map key ${expectedMapEntry.key} not found in actual Map"
            }

            assert(actualMap[expectedMapEntry.key] == expectedMapEntry.value) {
                "Testing get active filter map, comparing value for key ${expectedMapEntry.key}. Expected value: ${expectedMapEntry.value}, actual value: ${actualMap[expectedMapEntry.key]}"
            }
        }
    }
}