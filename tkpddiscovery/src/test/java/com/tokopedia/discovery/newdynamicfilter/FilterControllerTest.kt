package com.tokopedia.discovery.newdynamicfilter

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView
import org.junit.Test
import org.mockito.Mockito

class FilterControllerTest {

    private val dynamicFilterView = Mockito.mock(DynamicFilterView::class.java)

    @Test
    fun testCreateFilterController() {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = "samsung"
        searchParameter[SearchApiConst.OFFICIAL] = "true"
        searchParameter[SearchApiConst.SC] = "65"

        val filterList = mutableListOf<Filter>()

        val filterController = FilterController(
            searchParameter,
            filterList,
            dynamicFilterView
        )

        assert(filterController.getFilterValue(SearchApiConst.Q) == "samsung")
        assert(filterController.getFilterValue(SearchApiConst.OFFICIAL).toBoolean())
        assert(filterController.getFilterValue(SearchApiConst.SC) == "65")
    }
}