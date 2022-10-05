package com.tokopedia.sellerapp.presentation.screen

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.*
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextYellowColor
import com.tokopedia.sellerapp.presentation.theme.Text_DADCE0_Color
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.OrderModelHelper.getOrderType
import com.tokopedia.sellerapp.util.OrderType.NEW_ORDER_TYPE
import com.tokopedia.sellerapp.util.UiState
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


private const val MAXIMUM_ORDER_DISPLAYED = 8

@Composable
fun NewOrderListScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
    dataKey: String
) {
    getNewOrderListData(sharedViewModel, dataKey)
    CreateScreenScaffold(sharedViewModel, screenNavigation)
}

private fun getNewOrderListData(
    sharedViewModel: SharedViewModel,
    dataKey: String
) {
    sharedViewModel.getOrderList(dataKey)
}

@Composable
fun CreateScreenScaffold(sharedViewModel: SharedViewModel, screenNavigation: ScreenNavigation) {
    Scaffold {
        val orderList by sharedViewModel.orderList.collectAsState()
        val orderType = orderList.data?.getOrderType().orEmpty()
        when (orderList) {
            is UiState.Success -> {
                CreateListNewOrder(orderList.data, orderType, screenNavigation)
            }
            else -> {}
        }
    }
}

@Composable
fun CreateListNewOrder(
    orderList: List<OrderModel>?,
    orderType: String,
    screenNavigation: ScreenNavigation
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = null,
        contentPadding = PaddingValues(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
            bottom = 40.dp
        )
    ) {
        createItemTotalNewOrder(this, orderType, orderList)
        createItemsNewOrderList(this, orderType, orderList, screenNavigation)
        if (orderList?.size.orZero() > MAXIMUM_ORDER_DISPLAYED) {
            createItemMoreOrder(this, orderList)
        }
        if (orderList?.isNotEmpty() == true) {
            createItemActionText(this)
            if (orderType == NEW_ORDER_TYPE) {
                createItemButtonAcceptAllOrder(this, orderList)
            }
            createItemOpenOnPhone(this)
        }
    }
}

fun createItemMoreOrder(scalingLazyListScope: ScalingLazyListScope, orderList: List<OrderModel>?) {
    scalingLazyListScope.item {
        val totalOrderLeft = orderList?.size.orZero() - MAXIMUM_ORDER_DISPLAYED
        UnifyTypographyCompose(
            text = stringResource(
                id = com.tokopedia.tkpd.R.string.order_list_order_left_format,
                totalOrderLeft.toString()
            ),
            typographyConfig = { typography, context ->
                typography.setTextColor(TextGrayColor.toArgb())
                typography.setType(Typography.DISPLAY_1)
                typography.gravity = Gravity.CENTER
            }
        )
    }
}

fun createItemOpenOnPhone(scalingLazyListScope: ScalingLazyListScope) {
    scalingLazyListScope.item {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .height(52.dp)
                .clickable {

                }
                .background(color = ChipGrayColor),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                UnifyTypographyCompose(
                    text = stringResource(id = com.tokopedia.tkpd.R.string.order_list_text_open_on_phone),
                    typographyConfig = { typography, context ->
                        typography.setTextColor(context.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                        typography.setType(Typography.DISPLAY_2)
                        typography.setWeight(Typography.BOLD)
                        typography.gravity = Gravity.CENTER
                    }
                )
            }
        }
    }
}

fun createItemButtonAcceptAllOrder(
    scalingLazyListScope: ScalingLazyListScope,
    orderList: List<OrderModel>?
) {
    scalingLazyListScope.item {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .height(52.dp)
                .clickable {

                }
                .background(color = ChipGrayColor),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                UnifyTypographyCompose(
                    text = stringResource(id = com.tokopedia.tkpd.R.string.order_list_text_accept_all_order),
                    typographyConfig = { typography, context ->
                        typography.setTextColor(context.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                        typography.setType(Typography.DISPLAY_2)
                        typography.setWeight(Typography.BOLD)
                        typography.gravity = Gravity.CENTER
                    }
                )
            }
        }
    }
}

fun createItemActionText(scalingLazyListScope: ScalingLazyListScope) {
    scalingLazyListScope.item {
        UnifyTypographyCompose(
            text = stringResource(id = com.tokopedia.tkpd.R.string.order_list_footer_title),
            typographyConfig = { typography, _ ->
                typography.setTextColor(TextGrayColor.toArgb())
                typography.setType(Typography.DISPLAY_2)
                typography.setWeight(Typography.BOLD)
            }, modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

private fun createItemTotalNewOrder(
    scalingLazyListScope: ScalingLazyListScope,
    orderType: String,
    listOrder: List<OrderModel>?
) {
    val totalNewOrder = listOrder?.size.orZero()
    val orderListPageTitleStringRes = getOrderListPageTitleStringRes(orderType)
    scalingLazyListScope.item {
        UnifyTypographyCompose(
            text = stringResource(
                orderListPageTitleStringRes,
                totalNewOrder
            ),
            typographyConfig = { typography, _ ->
                typography.setTextColor(TextGrayColor.toArgb())
                typography.setType(Typography.DISPLAY_2)
                typography.setWeight(Typography.BOLD)
            }, modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

private fun getOrderListPageTitleStringRes(orderType: String): Int {
    return if (orderType == NEW_ORDER_TYPE) {
        com.tokopedia.tkpd.R.string.new_order_list_text_title
    } else {
        com.tokopedia.tkpd.R.string.ready_to_ship_order_list_text_title
    }
}

private fun getDueDateStringRes(orderType: String): Int {
    return if (orderType == NEW_ORDER_TYPE) {
        com.tokopedia.tkpd.R.string.new_order_list_text_due_response
    } else {
        com.tokopedia.tkpd.R.string.ready_to_shop_order_list_text_due_response
    }
}

fun createItemsNewOrderList(
    scalingLazyListScope: ScalingLazyListScope,
    orderType: String,
    listNewOrder: List<OrderModel>?,
    screenNavigation: ScreenNavigation
) {
    listNewOrder?.let {
        scalingLazyListScope.items(it.take(MAXIMUM_ORDER_DISPLAYED).size) { position ->
            val newOrderData = listNewOrder[position]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .clickable {
                        redirectToNewOrderDetailScreen(screenNavigation, newOrderData)
                    }
                    .background(color = ChipGrayColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    UnifyImageCompose(imageUnifyConfig = { imageUnify, _ ->
                        imageUnify.setImageUrl(newOrderData.products.firstOrNull()?.productImage.orEmpty())
                        imageUnify.type = ImageUnify.TYPE_CIRCLE
                    }, modifier = Modifier.size(32.dp))
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        UnifyTypographyCompose(
                            text = newOrderData.products.firstOrNull()?.productName.orEmpty(),
                            typographyConfig = { typography, context ->
                                typography.setType(Typography.DISPLAY_2)
                                typography.setWeight(Typography.BOLD)
                                typography.maxLines = 1
                                typography.ellipsize = TextUtils.TruncateAt.END
                                typography.setTextColor(context.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                            })
                        val dueDateStringRes = getDueDateStringRes(orderType)
                        UnifyTypographyCompose(
                            text = stringResource(id = dueDateStringRes),
                            typographyConfig = { typography, _ ->
                                typography.setType(Typography.DISPLAY_3)
                                typography.setTextColor(Text_DADCE0_Color.toArgb())
                            })
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val painter =
                                painterResource(id = com.tokopedia.tkpd.R.drawable.ic_order_list_due_date)
                            Icon(
                                modifier = Modifier.size(13.dp),
                                painter = painter,
                                contentDescription = "",
                            )
                            UnifyTypographyCompose(
                                text = newOrderData.deadLineText,
                                typographyConfig = { typography, _ ->
                                    typography.setType(Typography.DISPLAY_3)
                                    typography.setTextColor(TextYellowColor.toArgb())
                                },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun redirectToNewOrderDetailScreen(screenNavigation: ScreenNavigation, newOrderData: OrderModel) {
    screenNavigation.toNewOrderDetailScreen(newOrderData.orderId)
}

@Composable
fun UnifyTypographyCompose(
    text: String,
    typographyConfig: (Typography, Context) -> Unit,
    modifier: Modifier = Modifier
) {
    return AndroidView(
        factory = { context ->
            Typography(context).apply {
                this.text = text
                typographyConfig.invoke(this, context)
            }
        },
        update = {
            it.text = text
            typographyConfig.invoke(it, it.context)
        },
        modifier = modifier
    )
}

@Composable
fun UnifyImageCompose(
    imageUnifyConfig: (ImageUnify, Context) -> Unit,
    modifier: Modifier = Modifier
) {
    return AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageUnify(context).apply {
                imageUnifyConfig.invoke(this, context)
            }
        },
        update = {
            imageUnifyConfig.invoke(it, it.context)
        }
    )
}

