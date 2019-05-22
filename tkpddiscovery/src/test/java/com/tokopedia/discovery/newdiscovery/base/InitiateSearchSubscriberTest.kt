package com.tokopedia.discovery.newdiscovery.base

import org.junit.Test
import org.mockito.Mockito
import rx.Observable

// InitiateSearchSubscriber is currently untestable due to GraphqlResponse.getData() method is final
class InitiateSearchSubscriberTest {

    val initiateSearchListener = Mockito.mock(InitiateSearchListener::class.java)
    val initiateSearchSubscriber = InitiateSearchSubscriber(initiateSearchListener)

    private fun verifyResponseSearch(wantedNumberOfInvocations: Int = 1, expectedHasCatalog: Boolean? = null) {
        Mockito.verify(initiateSearchListener, Mockito.times(wantedNumberOfInvocations)).onHandleResponseSearch(
            expectedHasCatalog ?: Mockito.anyBoolean()
        )
    }

    private fun verifyResponseApplink(wantedNumberOfInvocations: Int = 1, expectedApplink: String? = null) {
        Mockito.verify(initiateSearchListener, Mockito.times(wantedNumberOfInvocations)).onHandleApplink(
            expectedApplink ?: Mockito.anyString()
        )
    }

    private fun verifyResponseUnknown(wantedNumberOfInvocations: Int = 1) {
        Mockito.verify(initiateSearchListener, Mockito.times(wantedNumberOfInvocations)).onHandleResponseUnknown()
    }

    private fun verifyResponseError(wantedNumberOfInvocations: Int = 1) {
        Mockito.verify(initiateSearchListener, Mockito.times(wantedNumberOfInvocations)).onHandleResponseError()
    }

    @Test
    fun test_OnNext_GqlResponseNull_ShouldHandleResponseError() {
        Observable.just(null).subscribe(initiateSearchSubscriber)

        verifyResponseError(1)
        verifyResponseSearch(0)
        verifyResponseApplink(0)
        verifyResponseUnknown(0)
    }
}