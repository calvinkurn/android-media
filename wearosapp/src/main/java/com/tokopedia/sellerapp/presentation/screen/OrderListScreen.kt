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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.theme.*
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.MenuHelper
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.UiState
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

private const val MAXIMUM_ORDER_DISPLAYED = 8

@Composable
fun OrderListScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
    orderType: String
) {
    getOrderListData(sharedViewModel, orderType)
    CreateScreenScaffold(sharedViewModel, screenNavigation, orderType)
}

private fun getOrderListData(
    sharedViewModel: SharedViewModel,
    dataKey: String
) {
    sharedViewModel.getOrderList(dataKey)
}

@Composable
fun CreateScreenScaffold(sharedViewModel: SharedViewModel, screenNavigation: ScreenNavigation, orderType: String) {
    Scaffold {
        val orderList by sharedViewModel.orderList.collectAsState()
        val orderCount by sharedViewModel.orderSummary.collectAsState()
        when (orderList) {
            is UiState.Success -> {
                CreateListNewOrder(
                    orderList.data?.take(MAXIMUM_ORDER_DISPLAYED).orEmpty(),
                    orderCount.data?.counter.toIntOrZero(),
                    orderType,
                    screenNavigation,
                    sharedViewModel

                )
            }
            else -> {}
        }
    }
}

@Composable
fun CreateListNewOrder(
    orderList: List<OrderModel>,
    orderCount: Int,
    orderType: String,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
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
        createItemTotalNewOrder(this, orderType, orderCount)
        if (orderList.isNotEmpty()) {
            createItemsNewOrderList(this, orderType, orderList, screenNavigation)
            val orderLeft = orderCount-orderList.size
            if(orderLeft > 0) {
                createItemMoreOrder(this, orderLeft)
            }
            createItemActionText(this)
            if (orderType == DATAKEY_NEW_ORDER) {
                createItemButtonAcceptAllOrder(this, orderList, screenNavigation, sharedViewModel)
            }
            createItemOpenOnPhone(this, orderType, sharedViewModel)
        }
    }
}

fun createItemMoreOrder(
    scalingLazyListScope: ScalingLazyListScope,
    orderLeft: Int
) {
    scalingLazyListScope.item {
        val totalOrderLeft = orderLeft
        UnifyTypographyCompose(
            text = stringResource(
                id = com.tokopedia.sellerapp.R.string.order_list_order_left_format,
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

fun createItemOpenOnPhone(scalingLazyListScope: ScalingLazyListScope, orderType: String, sharedViewModel: SharedViewModel) {
    scalingLazyListScope.item {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    sharedViewModel.openOrderPageBasedOnType(orderType)
                }
                .background(color = ChipGrayColor),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                UnifyTypographyCompose(
                    text = stringResource(id = com.tokopedia.sellerapp.R.string.order_list_text_open_on_phone),
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
    orderList: List<OrderModel>?,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    scalingLazyListScope.item {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    redirectToAcceptOrderScreen(orderList, screenNavigation, sharedViewModel)
                }
                .background(color = ChipGrayColor),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                UnifyTypographyCompose(
                    text = stringResource(id = com.tokopedia.sellerapp.R.string.order_list_text_accept_all_order),
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

private fun redirectToAcceptOrderScreen(
    orderList: List<OrderModel>?,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    orderList?.let {
        sharedViewModel.resetAcceptBulkOrderState()
        screenNavigation.toAcceptOrderScreen(it.map { it.orderId })
    }
}

fun createItemActionText(scalingLazyListScope: ScalingLazyListScope) {
    scalingLazyListScope.item {
        UnifyTypographyCompose(
            text = stringResource(id = com.tokopedia.sellerapp.R.string.order_list_footer_title),
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
    orderCount: Int
) {
    val title = MenuHelper.getTitleByDataKey(orderType)
    scalingLazyListScope.item {
        UnifyTypographyCompose(
            text = stringResource(
                com.tokopedia.sellerapp.R.string.order_list_text_title,
                title,
                orderCount
            ),
            typographyConfig = { typography, _ ->
                typography.setTextColor(TextGrayColor.toArgb())
                typography.setType(Typography.DISPLAY_2)
                typography.setWeight(Typography.BOLD)
            }, modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

private fun getDueDateStringRes(orderType: String): Int {
    return if (orderType == DATAKEY_NEW_ORDER) {
        com.tokopedia.sellerapp.R.string.new_order_list_text_due_response
    } else {
        com.tokopedia.sellerapp.R.string.ready_to_shop_order_list_text_due_response
    }
}

fun createItemsNewOrderList(
    scalingLazyListScope: ScalingLazyListScope,
    orderType: String,
    listNewOrder: List<OrderModel>,
    screenNavigation: ScreenNavigation
) {
    scalingLazyListScope.items(listNewOrder) { newOrderData ->
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
                            typography.setTextColor(NestLightNN0.toArgb())
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
                            painterResource(id = com.tokopedia.sellerapp.R.drawable.ic_order_list_due_date)
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

fun redirectToNewOrderDetailScreen(screenNavigation: ScreenNavigation, newOrderData: OrderModel) {
    screenNavigation.toOrderDetailScreen(newOrderData.orderId)
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

