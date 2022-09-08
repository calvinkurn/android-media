package com.tokopedia.sellerapp.domain

import com.tokopedia.sellerapp.data.uimodel.OrderListItemUiModel
import javax.inject.Inject

class GetNewOrderListUseCase @Inject constructor() {

    private val mockProductImageUrl =
        "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/8/16/a21caa18-a4bb-4f50-81af-f00e4ec82a04.png.webp?ect=4g"

    fun execute(): List<OrderListItemUiModel> {
        //MOCK DATA
        return listOf(
            OrderListItemUiModel(
                mockProductImageUrl,
                "Product 1",
                "13 Sep; 14:55"
            ),
            OrderListItemUiModel(
                mockProductImageUrl,
                "Product 2 ellipsize ellipsize",
                "13 Sep; 14:55"
            ),
            OrderListItemUiModel(
                mockProductImageUrl,
                "Product 1",
                "13 Sep; 14:55"
            ),
            OrderListItemUiModel(
                mockProductImageUrl,
                "Product 1",
                "13 Sep; 14:55"
            ),
            OrderListItemUiModel(
                mockProductImageUrl,
                "Product 1",
                "13 Sep; 14:55"
            )
        )
    }

}