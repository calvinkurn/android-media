package com.tokopedia.sellerapp.presentation.viewmodel.data

import com.google.android.gms.wearable.Node

class FakeNode(
    private val id: String,
    private val displayName: String,
    private val isNearby: Boolean
): Node {
    override fun getId(): String = id

    override fun getDisplayName(): String = displayName

    override fun isNearby(): Boolean = isNearby
}