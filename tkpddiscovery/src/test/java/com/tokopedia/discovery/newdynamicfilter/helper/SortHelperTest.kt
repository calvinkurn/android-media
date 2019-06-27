package com.tokopedia.discovery.newdynamicfilter.helper

import com.tokopedia.discovery.common.data.Sort
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import org.junit.Test

class SortHelperTest {

    companion object {
        private const val QUERY_FOR_TEST_SAMSUNG = "samsung"

        private const val PALING_SESUAI_VALUE = "23"
        private const val ULASAN_VALUE = "5"
        private const val TERBARU_VALUE = "9"
        private const val HARGA_TERTINGGI_VALUE = "4"
        private const val HARGA_TERENDAH_VALUE = "3"

        private val palingSesuaiSort = createSort(SearchApiConst.OB, PALING_SESUAI_VALUE, "Paling Sesuai")
        private val ulasanSort = createSort(SearchApiConst.OB, ULASAN_VALUE, "Ulasan")
        private val terbaruSort = createSort(SearchApiConst.OB, TERBARU_VALUE, "Terbaru")
        private val hargaTertinggiSort = createSort(SearchApiConst.OB, HARGA_TERTINGGI_VALUE, "Harga Tertinggi")
        private val hargaTerendahSort = createSort(SearchApiConst.OB, HARGA_TERENDAH_VALUE, "Harga Terendah")

        private fun createSort(key: String, value: String, name: String) : Sort {
            val sort = Sort()
            sort.key = key
            sort.value = value
            sort.name = name

            return sort
        }
    }

    @Test
    fun testGetSelectedSortFromSearchParameter() {
        val searchParameter = createSearchParameter()
        val sortList = createSortList()

        val selectedSort = SortHelper.getSelectedSortFromSearchParameter(searchParameter, sortList)

        assertGetSelectedSortFromSearchParameter(selectedSort)
    }

    private fun createSearchParameter() : Map<String, String> {
        val searchParameter = mutableMapOf<String, String>()
        searchParameter[SearchApiConst.Q] = QUERY_FOR_TEST_SAMSUNG
        searchParameter[hargaTerendahSort.key] = hargaTerendahSort.value

        return searchParameter
    }

    private fun createSortList() : List<Sort> {
        val sortList = mutableListOf<Sort>()

        sortList.add(palingSesuaiSort)
        sortList.add(ulasanSort)
        sortList.add(terbaruSort)
        sortList.add(hargaTertinggiSort)
        sortList.add(hargaTerendahSort)

        return sortList
    }

    private fun assertGetSelectedSortFromSearchParameter(actualSelectedSort: Map<String, String>) {
        val expectedSelectedSort = mutableMapOf<String, String>()
        expectedSelectedSort[hargaTerendahSort.key] = hargaTerendahSort.value

        assert(actualSelectedSort.size == expectedSelectedSort.size) {
            "Testing get selected sort from search parameter, expected size: ${expectedSelectedSort.size}, actual size: ${actualSelectedSort.size}"
        }

        for(expectedSort in expectedSelectedSort.entries) {
            val actualValue = actualSelectedSort[expectedSort.key]
            assert(actualValue == expectedSort.value) {
                "Testing get selected sort from search parameter key: ${expectedSort.key}, expected value: ${expectedSort.value}, actual value: $actualValue"
            }
        }
    }
}