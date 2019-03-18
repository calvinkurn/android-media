package com.tokopedia.discovery.newdynamicfilter.controller

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.LevelThreeCategory
import com.tokopedia.core.discovery.model.LevelTwoCategory
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import org.junit.Test

class FilterControllerTest {

    @Test
    fun testInsertAndGetFilterParameter() {
        val givenFilterParameter = mutableMapOf<String, String>()
        givenFilterParameter[SearchApiConst.Q] = "samsung"
        givenFilterParameter[SearchApiConst.OFFICIAL] = "true"

        val filterController = FilterController()
        filterController.initFilterController(givenFilterParameter)

        val expectedFilterParameter = givenFilterParameter.toMap()

        assertFilterParameter(filterController.getFilterParameter(), expectedFilterParameter)
    }

    @Test
    fun testInsertAndGetFilterParameterWithMultipleValues() {
        val givenFilterParameter = mutableMapOf<String, String>()
        givenFilterParameter[SearchApiConst.Q] = "samsung"
        givenFilterParameter[SearchApiConst.FCITY] = "145,146"

        val filterController = FilterController()
        filterController.initFilterController(givenFilterParameter)

        val expectedFilterParameter = mutableMapOf<String, String>()
        expectedFilterParameter[SearchApiConst.Q] = "samsung"
        expectedFilterParameter[SearchApiConst.FCITY] = "146,145"

        assertFilterParameter(filterController.getFilterParameter(), expectedFilterParameter)
    }

    @Test
    fun testFilterParameterRemoveDuplicateValues() {
        val givenFilterParameter = mutableMapOf<String, String>()
        givenFilterParameter[SearchApiConst.Q] = "samsung"
        givenFilterParameter[SearchApiConst.FCITY] = "145,146,145,145,146"

        val filterController = FilterController()
        filterController.initFilterController(givenFilterParameter)

        val expectedFilterParameter = mutableMapOf<String, String>()
        expectedFilterParameter[SearchApiConst.Q] = "samsung"
        expectedFilterParameter[SearchApiConst.FCITY] = "146,145"

        assertFilterParameter(filterController.getFilterParameter(), expectedFilterParameter)
    }

    private fun createRegularSearchParameter() : Map<String, String> {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = "sepatu"
        searchParameter[SearchApiConst.OFFICIAL] = "true"
        searchParameter[SearchApiConst.SC] = "1800"
        searchParameter[SearchApiConst.FCITY] = "144,146,150,151,167,168,171,174,175,176,177,178,463,165"

        return searchParameter
    }

    private fun createSearchParameterWithDuplicateValues() : Map<String, String> {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = "sepatu"
        searchParameter[SearchApiConst.OFFICIAL] = "true"
        searchParameter[SearchApiConst.SC] = "1800"
        searchParameter[SearchApiConst.FCITY] = "144,146,150,151,167,168,171,174,175,176,177,178,463,165,174,175,176,177,178"

        return searchParameter
    }

    private fun createRespondFromDetailPage() : List<Option> {
        val response = mutableListOf<Option>()

        val locationOptions = createLocationOptions()
        for(locationOption in locationOptions) {
            locationOption.inputState =
                (locationOption.value == "144,146,150,151,167,168,171,174,175,176,177,178,463"
                    || locationOption.value == "174,175,176,177,178"
                    || locationOption.value == "165")
                    .toString()
        }

        response.addAll(locationOptions)

        return response
    }

    private fun createFilterList() : List<Filter> {
        val filterList = mutableListOf<Filter>()

        val filterToko = Filter()
        filterToko.title = "Toko"
        filterToko.options = createTokoOptions()
        filterList.add(filterToko)

        val filterCategory = Filter()
        filterCategory.title = "Kategori"
        filterCategory.options = createCategoryOptions()
        filterList.add(filterCategory)

        val filterLocation = Filter()
        filterLocation.title = "Lokasi"
        filterLocation.options = createLocationOptions()
        filterList.add(filterLocation)

        return filterList
    }

    private fun createTokoOptions() : List<Option> {
        val tokoOptionList = mutableListOf<Option>()

        val goldMerchantOption = createOption("goldmerchant", "Power Badge", "true")
        tokoOptionList.add(goldMerchantOption)

        val officialStoreOption = createOption("official", "Official Store", "true")
        tokoOptionList.add(officialStoreOption)

        return tokoOptionList
    }

    private fun createCategoryOptions() : List<Option> {
        val categoryOptionList = mutableListOf<Option>()

        val fashionPriaOption = createOption(SearchApiConst.SC, "Fashion Pria", "1759")
        categoryOptionList.add(fashionPriaOption)

        val fashionPriaLevelTwoList = mutableListOf<LevelTwoCategory>()
        val sepatuPriaCategory = createLevelTwoCategory(SearchApiConst.SC, "Sepatu", "1800")
        fashionPriaLevelTwoList.add(sepatuPriaCategory)
        fashionPriaOption.levelTwoCategoryList = fashionPriaLevelTwoList

        val fashionPriaLevelThreeList = mutableListOf<LevelThreeCategory>()
        val sneakersSepatuPriaCategory = createLevelThreeCategory(SearchApiConst.SC, "Sneakers", "1845")
        fashionPriaLevelThreeList.add(sneakersSepatuPriaCategory)
        sepatuPriaCategory.levelThreeCategoryList = fashionPriaLevelThreeList

        return categoryOptionList
    }

    private fun createLevelTwoCategory(key: String, name: String, value: String) : LevelTwoCategory {
        val levelTwoCategory = LevelTwoCategory()
        levelTwoCategory.key = key
        levelTwoCategory.name = name
        levelTwoCategory.value = value

        return levelTwoCategory
    }

    private fun createLevelThreeCategory(key: String, name: String, value: String) : LevelThreeCategory {
        val levelThreeCategory = LevelThreeCategory()
        levelThreeCategory.key = key
        levelThreeCategory.name = name
        levelThreeCategory.value = value

        return levelThreeCategory
    }

    private fun createLocationOptions() : List<Option> {
        val locationOptions = mutableListOf<Option>()

        val jabodetabekOption = createOption(SearchApiConst.FCITY, "Jabodetabek", "144,146,150,151,167,168,171,174,175,176,177,178,463")
        locationOptions.add(jabodetabekOption)

        val dkiJakartaOption = createOption(SearchApiConst.FCITY, "DKI Jakarta", "174,175,176,177,178")
        locationOptions.add(dkiJakartaOption)

        val bandungOption = createOption(SearchApiConst.FCITY, "Bandung", "165")
        locationOptions.add(bandungOption)

        val kabTangerangOption = createOption(SearchApiConst.FCITY, "Kab. Tangerang", "144")
        locationOptions.add(kabTangerangOption)

        return locationOptions
    }

    private fun createOption(key: String, name: String, value: String) : Option {
        val option = Option()
        option.key = key
        option.name = name
        option.value = value

        return option
    }

    private fun assertFilterParameter(actualFilterParameter: Map<String, String>, expectedFilterParameter: Map<String, String>) {
        assertFilterParameterSize(actualFilterParameter, expectedFilterParameter)

        assertFilterParameterContents(actualFilterParameter, expectedFilterParameter)
    }

    private fun assertFilterParameterSize(actualFilterParameter: Map<String, String>, expectedFilterParameter: Map<String, String>) {
        assert(actualFilterParameter.size == expectedFilterParameter.size)
    }

    private fun assertFilterParameterContents(actualFilterParameter: Map<String, String>, expectedFilterParameter: Map<String, String>) {
        for(expectedFilterSet in expectedFilterParameter) {
            val actualFilterValue = actualFilterParameter[expectedFilterSet.key] ?: ""
            val expectedFilterValue = expectedFilterSet.value

            assertFilterValue(actualFilterValue, expectedFilterValue)
        }
    }

    private fun assertFilterValue(actualFilterValue: String, expectedFilterValue: String) {
        val actualFilterValueList = actualFilterValue.split(Option.VALUE_SEPARATOR).toList()
        val expectedFilterValueList = expectedFilterValue.split(Option.VALUE_SEPARATOR).toList()

        assertFilterValueSize(actualFilterValueList, expectedFilterValueList)

        assertFilterValueContents(actualFilterValueList, expectedFilterValueList)
    }

    private fun assertFilterValueSize(actualFilterValueList: List<String>, expectedFilterValueList: List<String>) {
        assert(actualFilterValueList.size == expectedFilterValueList.size)
    }

    private fun assertFilterValueContents(actualFilterValueList: List<String>, expectedFilterValueList: List<String>) {
        assert(actualFilterValueList.containsAll(expectedFilterValueList))
        assert(expectedFilterValueList.containsAll(actualFilterValueList))
    }

    private fun assertFilterListCorrect(filterController: FilterController, filterList: List<Filter>) {
        assert(filterController.getFilterList().none { filter -> filter.options.isEmpty() })
    }

    private fun assertFlagFilterHelperCorrect(filterController: FilterController, isOptionShown: (Option) -> Boolean) {
        for(filter in filterController.getFilterList()) {
            for(option in filter.options) {
                val actualShown = filterController.getFlagFilterHelperValue(option.uniqueId)
                val expectedShown = isOptionShown(option)

                assert(actualShown == expectedShown) {
                    "Testing option: ${option.uniqueId}, expected: $expectedShown, got: $actualShown"
                }
            }
        }
    }
}