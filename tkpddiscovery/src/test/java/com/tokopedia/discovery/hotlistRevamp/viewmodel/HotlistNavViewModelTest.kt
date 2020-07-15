package com.tokopedia.discovery.hotlistRevamp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.categoryrevamp.domain.usecase.SendTopAdsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HotlistNavViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var trackUrl: String = "test"
    private var sendTopAdsUseCase: SendTopAdsUseCase = mockk(relaxed = true)
    private var hotlistNavViewModel: HotlistNavViewModel = spyk(HotlistNavViewModel(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            sendTopAdsUseCase
    ))

    @Test
    fun testSendTopAds() {
        var url = ""
        val slotUrl = slot<String>()
        every { sendTopAdsUseCase.executeOnBackground(capture(slotUrl)) } answers {
            url = slotUrl.captured
        }

        hotlistNavViewModel.sendTopAds(trackUrl)

        assertEquals(trackUrl, url)
    }
}