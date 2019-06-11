package com.tokopedia.discovery.newdynamicfilter.helper

import com.tokopedia.discovery.common.data.Sort

class SortHelper {

    companion object {
        fun getSelectedSortFromSearchParameter(
            searchParameter: Map<String, String>,
            sortList: List<Sort>
        ): Map<String, String> {
            val sortParameter = mutableMapOf<String, String>()

            for (sort in sortList) {
                if (searchParameter.containsKey(sort.key)) {
                    sortParameter[sort.key] = searchParameter.getValue(sort.key)
                }
            }

            return sortParameter
        }
    }
}