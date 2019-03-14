package com.tokopedia.discovery.newdynamicfilter.controller

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.core.discovery.model.LevelThreeCategory
import com.tokopedia.core.discovery.model.LevelTwoCategory
import com.tokopedia.core.discovery.model.Option
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import org.junit.Test

class FilterControllerTest {

    private fun createFilterList() : List<Filter> {
        val filterList = mutableListOf<Filter>()

        val filterToko = Filter()
        filterToko.title = "Toko"
        filterToko.options = createTokoOptions()

        val filterCategory = Filter()
        filterCategory.title = "Kategori"
        filterCategory.options = createCategoryOptions()

        val filterLocation = Filter()
        filterLocation.title = "Lokasi"
        filterLocation.options = createLocationOptions()

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

        return locationOptions
    }

    private fun createOption(key: String, name: String, value: String) : Option {
        val option = Option()
        option.key = key
        option.name = name
        option.value = value

        return option
    }

    @Test
    fun testInitFilterController() {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = "sepatu"
        searchParameter[SearchApiConst.OFFICIAL] = "true"
        searchParameter[SearchApiConst.SC] = "1800"
        searchParameter[SearchApiConst.FCITY] = "144,146,150,151,167,168,171,174,175,176,177,178,463"

        val filterList = createFilterList()

        val filterController = FilterController()
        filterController.initFilterController(searchParameter, filterList)

        assertSearchParameterCorrect(filterController)
        assertFlagFilterHelperCorrect(filterController)
    }

    private fun assertSearchParameterCorrect(filterController: FilterController) {
        assert(filterController.getFilterValue(SearchApiConst.Q) == "sepatu")
        assert(filterController.getFilterValue(SearchApiConst.OFFICIAL).toBoolean())
        assert(filterController.getFilterValue(SearchApiConst.SC) == "1800")
        assert(filterController.getFilterValue(SearchApiConst.FCITY) == "144,146,150,151,167,168,171,174,175,176,177,178,463")
    }

    private fun assertFlagFilterHelperCorrect(filterController: FilterController) {
        for(filter in filterController.getFilterList()) {
            for(option in filter.options) {
                val optionIsShown = filterController.getFlagFilterHelperValue(option.uniqueId)

                if(option.value == "144,146,150,151,167,168,171,174,175,176,177,178,463") {
                    assert(optionIsShown)
                }
                else {
                    assert(!optionIsShown)
                }
            }
        }
    }
}