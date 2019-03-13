package com.tokopedia.discovery.newdynamicfilter

import com.tokopedia.core.discovery.model.Filter
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView
import org.junit.Test
import org.mockito.Mockito

class FilterParameterTest {

    private val dynamicFilterView = Mockito.mock(DynamicFilterView::class.java)

    @Test
    fun testCreateFilterParameter() {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = "samsung"
        searchParameter[SearchApiConst.OFFICIAL] = "true"
        searchParameter[SearchApiConst.SC] = "65"

        val filterList = mutableListOf<Filter>()

        val filterParameter = FilterParameter(searchParameter, filterList, dynamicFilterView)

        assert(filterParameter.getFilterValue(SearchApiConst.Q) == "samsung")
        assert(filterParameter.getFilterValue(SearchApiConst.OFFICIAL).toBoolean())
        assert(filterParameter.getFilterValue(SearchApiConst.SC) == "65")
    }
}