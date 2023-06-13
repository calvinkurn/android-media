package com.tokopedia.sellerapp.presentation.viewmodel.data

import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node

class FakeCapabilityInfo(
    private val nodes: MutableSet<Node>
): CapabilityInfo {
    override fun getName(): String = "I am capable"

    override fun getNodes(): MutableSet<Node> = nodes
}

