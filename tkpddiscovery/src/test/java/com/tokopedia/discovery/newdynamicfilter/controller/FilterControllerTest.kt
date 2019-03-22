package com.tokopedia.discovery.newdynamicfilter.controller

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper
import org.junit.Before
import org.junit.Test

class FilterControllerTest {

    private val filterController = FilterController()

    private val officialOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", SearchApiConst.OFFICIAL))
    private val jabodetabekOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, "1,2,3", "Jabodetabek"))
    private val jakartaOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, "1,2", "Jakarta"))
    private val jakartaBaratOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, "1", "Jakarta Barat"))
    private val tangerangOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, "3", "Tangerang"))
    private val bandungOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, "4", "Bandung"))
    private val handphoneOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, "1", "Handphone"))
    private val tvOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, "2", "TV"))
    private val minPriceOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Harga Minimum"))
    private val maxPriceOption = OptionHelper.createOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMAX, "", "Harga Maximum"))

    private val tokoOptions = mutableListOf<Option>()
    private val locationOptions = mutableListOf<Option>()
    private val categoryOptions = mutableListOf<Option>()
    private val priceOptions = mutableListOf<Option>()

    @Before
    fun initFilterController() {
        val filterParameter = mutableMapOf<String, String>()
        filterParameter["q"] = "samsung"
        filterParameter["official"] = "true"
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

        filterController.initFilterController(filterParameter, filterList)
    }

    @Test
    fun testInitFilter() {
        assertGetFilterValueExists(tokoOptions)
        assertGetFilterValueNotExists(locationOptions + categoryOptions + priceOptions)
    }

    @Test
    fun testApplyFilterNoCleanUp() {
        filterController.setFilter(jabodetabekOption, true)

        val expectedOptionsExist = mutableListOf<Option>()
        expectedOptionsExist.add(jabodetabekOption)
        expectedOptionsExist.addAll(tokoOptions)

        val expectedOptionsNotExists = mutableListOf<Option>()
        expectedOptionsNotExists.add(jakartaOption)
        expectedOptionsNotExists.add(jakartaBaratOption)
        expectedOptionsNotExists.add(tangerangOption)
        expectedOptionsNotExists.add(bandungOption)
        expectedOptionsNotExists.addAll(categoryOptions)
        expectedOptionsNotExists.addAll(priceOptions)

        assertGetFilterValueExists(expectedOptionsExist)
        assertGetFilterValueNotExists(expectedOptionsNotExists)

        filterController.setFilter(jakartaOption, true)

        expectedOptionsExist.clear()
        expectedOptionsExist.add(jabodetabekOption)
        expectedOptionsExist.add(jakartaOption)
        expectedOptionsExist.addAll(tokoOptions)

        expectedOptionsNotExists.clear()
        expectedOptionsNotExists.add(jakartaBaratOption)
        expectedOptionsNotExists.add(tangerangOption)
        expectedOptionsNotExists.add(bandungOption)
        expectedOptionsNotExists.addAll(categoryOptions)
        expectedOptionsNotExists.addAll(priceOptions)

        assertGetFilterValueExists(expectedOptionsExist)
        assertGetFilterValueNotExists(expectedOptionsNotExists)
    }

    @Test
    fun testApplyFilterWithCleanUp() {
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)

        val expectedOptionsExist = mutableListOf<Option>()
        expectedOptionsExist.add(handphoneOption)
        expectedOptionsExist.addAll(tokoOptions)

        val expectedOptionsNotExists = mutableListOf<Option>()
        expectedOptionsNotExists.add(tvOption)
        expectedOptionsNotExists.addAll(locationOptions)
        expectedOptionsNotExists.addAll(priceOptions)

        assertGetFilterValueExists(expectedOptionsExist)
        assertGetFilterValueNotExists(expectedOptionsNotExists)

        filterController.setFilter(tvOption, true, isCleanUpExistingFilterWithSameKey = true)

        expectedOptionsExist.clear()
        expectedOptionsExist.add(tvOption)
        expectedOptionsExist.addAll(tokoOptions)

        expectedOptionsNotExists.clear()
        expectedOptionsNotExists.add(handphoneOption)
        expectedOptionsNotExists.addAll(locationOptions)
        expectedOptionsNotExists.addAll(priceOptions)

        assertGetFilterValueExists(expectedOptionsExist)
        assertGetFilterValueNotExists(expectedOptionsNotExists)
    }

    private fun createFilterWithOptions(optionList: List<Option>) : Filter {
        val filter = Filter()
        filter.options = optionList
        return filter
    }

    private fun assertGetFilterValueExists(optionList: List<Option>) {
        for(option in optionList) {
            val expectedFilterValue = option.value
            val actualFilterValue = filterController.getFilterValue(option)
            assert(isActualContainsExpected(actualFilterValue, expectedFilterValue)) {
                getAssertFilterValueMessage(option.uniqueId, expectedFilterValue, actualFilterValue)
            }

            val actualFilterViewState = filterController.getFilterViewState(option)
            assert(actualFilterViewState) {
                getAssertFilterViewStateMessage(option.uniqueId, true, actualFilterViewState)
            }
        }
    }

    private fun assertGetFilterValueNotExists(optionList: List<Option>) {
        for(option in optionList) {
            val actualFilterValue = filterController.getFilterValue(option)
            val notExpectedFilterValue = option.value
            assert(!isActualContainsExpected(actualFilterValue, notExpectedFilterValue)) {
                getAssertFilterValueMessage(option.uniqueId, "empty or not $notExpectedFilterValue", actualFilterValue)
            }

            val actualFilterViewState = filterController.getFilterViewState(option)
            assert(!actualFilterViewState) {
                getAssertFilterViewStateMessage(option.uniqueId, false, actualFilterViewState)
            }
        }
    }

    private fun isActualContainsExpected(actualFilterValue: String, expectedFilterValue: String) : Boolean {
        val actualFilterValueList = actualFilterValue.split(Option.VALUE_SEPARATOR).toList()
        val expectedFilterValueList = expectedFilterValue.split(Option.VALUE_SEPARATOR).toList()

        return actualFilterValueList.containsAll(expectedFilterValueList)
    }

    private fun getAssertFilterValueMessage(uniqueId: String, expectedFilterValue: String, actualFilterValue: String) : String {
        return "Testing filter value option $uniqueId: expected: $expectedFilterValue, actual: $actualFilterValue"
    }

    private fun getAssertFilterViewStateMessage(uniqueId: String, expectedFilterViewState: Boolean, actualFilterViewState: Boolean) : String {
        return "Testing filter view state option $uniqueId: expected: $expectedFilterViewState, actual: $actualFilterViewState"
    }
}