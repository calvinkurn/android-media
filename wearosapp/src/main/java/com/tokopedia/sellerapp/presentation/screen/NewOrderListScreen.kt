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
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerapp.data.uimodel.OrderListItemUiModel
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextYellowColor
import com.tokopedia.sellerapp.presentation.theme.Text_DADCE0_Color
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

@Composable
fun NewOrderListScreen(
    sharedViewModel: SharedViewModel,
) {
    getNewOrderListData(sharedViewModel)
    CreateScreenScaffold(sharedViewModel)
}

fun getNewOrderListData(sharedViewModel: SharedViewModel) {
    sharedViewModel.getNewOrderListData()
}

@Composable
fun CreateScreenScaffold(sharedViewModel: SharedViewModel) {
    Scaffold {
        val orderList by sharedViewModel.newOrderListData.collectAsState()
        when (orderList) {
            is UiState.Success -> {
                CreateListNewOrder(orderList)
            }
            else -> {}
        }
    }
}

@Composable
fun CreateListNewOrder(orderList: UiState<List<OrderListItemUiModel>>) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        createItemTotalNewOrder(this, orderList.data?.size.orZero())
        createItemsNewOrderList(this, orderList.data)
        createItemActionText(this)
        createItemButtonAcceptAllOrder(this, orderList)
        createItemOpenOnPhone(this)
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
                    text = stringResource(id = com.tokopedia.tkpd.R.string.new_order_list_text_open_on_phone),
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
    orderList: UiState<List<OrderListItemUiModel>>
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
                    text = stringResource(id = com.tokopedia.tkpd.R.string.new_order_list_text_accept_all_order),
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
            text = "Actions",
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
    totalNewOrder: Int
) {
    scalingLazyListScope.item {
        UnifyTypographyCompose(
            text = stringResource(
                com.tokopedia.tkpd.R.string.new_order_list_text_title,
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

fun createItemsNewOrderList(
    scalingLazyListScope: ScalingLazyListScope,
    listNewOrder: List<OrderListItemUiModel>?
) {
    listNewOrder?.let {
        scalingLazyListScope.items(it.size) { position ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .clickable {

                    }
                    .background(color = ChipGrayColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    val newOrderData = listNewOrder[position]
                    UnifyImageCompose(imageUnifyConfig = { imageUnify, _ ->
                        imageUnify.setImageUrl(newOrderData.productImage)
                        imageUnify.type = ImageUnify.TYPE_CIRCLE
                    }, modifier = Modifier.size(32.dp))
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        UnifyTypographyCompose(
                            text = newOrderData.productName,
                            typographyConfig = { typography, context ->
                                typography.setType(Typography.DISPLAY_2)
                                typography.setWeight(Typography.BOLD)
                                typography.maxLines = 1
                                typography.ellipsize = TextUtils.TruncateAt.END
                                typography.setTextColor(context.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                            })
                        UnifyTypographyCompose(
                            text = stringResource(id = com.tokopedia.tkpd.R.string.new_order_list_text_due_response),
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
                                text = newOrderData.deadlineResponse,
                                typographyConfig = { typography, _ ->
                                    typography.setType(Typography.DISPLAY_3)
                                    typography.setTextColor(TextYellowColor.toArgb())
                                })
                        }
                    }
                }
            }
        }
    }
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
    )
}

