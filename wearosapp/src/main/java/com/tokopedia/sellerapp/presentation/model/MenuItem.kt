package com.tokopedia.sellerapp.presentation.model

import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_READY_TO_SHIP
import com.tokopedia.iconunify.R.drawable as iconR

data class MenuItem(
    val title: String = "",
    val dataKey: String = "",
    val unreadCount: Int = 0,
    val icon: Int = -1,
)

const val TITLE_NOTIF = "Notifikasi"
const val TITLE_CHAT = "Chat"
const val TITLE_NEW_ORDER = "Pesanan Baru"
const val TITLE_READY_TO_SHIP = "Siap Dikirim"
val ICON_NOTIF = iconR.iconunify_bell
val ICON_CHAT = iconR.iconunify_chat
val ICON_NEW_ORDER = iconR.iconunify_product
val ICON_READY_TO_SHIP = iconR.iconunify_product_move

fun generateInitialMenu() = listOf(
    /**
     * FOR NEXT DEVELOPMENT
     */
    MenuItem(
        title = TITLE_NOTIF,
        icon = ICON_NOTIF,
    ),
//    MenuItem(
//        title = TITLE_CHAT,
//        icon = ICON_CHAT,
//        dataKey = DATAKEY_UNREAD_CHAT
//    ),
    MenuItem(
        title = TITLE_NEW_ORDER,
        icon = ICON_NEW_ORDER,
        dataKey = DATAKEY_NEW_ORDER
    ),
    MenuItem(
        title = TITLE_READY_TO_SHIP,
        icon = ICON_READY_TO_SHIP,
        dataKey = DATAKEY_READY_TO_SHIP
    )
)
