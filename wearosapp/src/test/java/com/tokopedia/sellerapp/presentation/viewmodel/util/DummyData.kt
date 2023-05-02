package com.tokopedia.sellerapp.presentation.viewmodel.util

import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.TITLE_CHAT
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER

object DummyData {
    val listSummaryData = listOf(
        SummaryModel(
            title = TITLE_CHAT,
            dataKey = "123",
            counter = "2",
            description = "description 1"
        ),
        SummaryModel(
            title = "title 2",
            dataKey = "111",
            counter = "3",
            description = "description 2"
        ),
        SummaryModel(
            title = TITLE_NEW_ORDER,
            dataKey = "222",
            counter = "1",
            description = "description 3"
        )
    )

    val listOrderData = listOf(
        OrderModel(
            orderId = "10",
            orderStatusId = "1",
            orderTotalPrice = "1000",
            orderDate = "sunday",
            deadLineText = "immediately",
            courierName = "JPG",
            destinationProvince = "North Sumatra",
            products = listOf()
        ),
        OrderModel(
            orderId = "13",
            orderStatusId = "220",
            orderTotalPrice = "10000",
            orderDate = "monday",
            deadLineText = "immediately",
            courierName = "PNG",
            destinationProvince = "DKI Jakarta",
            products = listOf()
        ),
        OrderModel(
            orderId = "20",
            orderStatusId = "0",
            orderTotalPrice = "4000",
            orderDate = "tuesday",
            deadLineText = "immediately",
            courierName = "JPEG",
            destinationProvince = "England",
            products = listOf()
        ),
    )

    val listNotificationData = listOf(
        NotificationModel(
            notificationId = "122112",
            readStatus = 1,
            status = 2,
            title = "hello this is a new notification",
            shortDescription = "this notification has new message",
            createTimeUnix = 0L,
            infoThumbnailUrl = "http://www.tokopedia.com"
        ),
        NotificationModel(
            notificationId = "122113",
            readStatus = 2,
            status = 1,
            title = "hello this is second notification",
            shortDescription = "this notification has new message",
            createTimeUnix = 0L,
            infoThumbnailUrl = "http://www.tokopedia.com"
        )
    )
}
