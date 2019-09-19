package com.tokopedia.discovery.newdiscovery.helper

import org.junit.Test

class UrlParamHelperTest {

    @Test
    fun `test generate url param string`() {
        val inputMap = hashMapOf<String?, String?>(
                "q" to "Samsung",
                "official" to "true"
        )

        executeAndValidate(inputMap, "q=Samsung&official=true")
    }

    private fun executeAndValidate(inputMap: Map<String?, String?>?, expected: String) {
        val actual = UrlParamHelper.generateUrlParamString(inputMap)

        assert(actual == expected) {
            "Test failed. Expected: $expected, Actual: $actual"
        }
    }

    @Test
    fun `test generate url param with null inputs`() {
        val inputMap = null
        executeAndValidate(inputMap, "")
    }

    @Test
    fun `test generate url param with empty map`() {
        val inputMap = hashMapOf<String?, String?>()
        executeAndValidate(inputMap, "")
    }

    @Test
    fun `test generate url param string with some null values`() {
        val inputMap = hashMapOf<String?, String?>(
                "q" to "Samsung",
                "official" to null
        )

        executeAndValidate(inputMap, "q=Samsung")
    }

    @Test
    fun `test generate url param string with null keys`() {
        val inputMap = hashMapOf<String?, String?>(
                null to "Samsung",
                "official" to null,
                "sc" to "32"
        )

        executeAndValidate(inputMap, "sc=32")
    }

    @Test
    fun `test generate url param string with spaces`() {
        val inputMap = hashMapOf<String?, String?>(
                "q" to "samsung galaxy s8",
                "official" to "true"
        )

        executeAndValidate(inputMap, "q=samsung+galaxy+s8&official=true")
    }
}