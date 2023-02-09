@file:OptIn(ExperimentalTextApi::class)

package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import com.tokopedia.sellerapp.domain.model.OrderModel
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.theme.ActionButtonGrayColor
import com.tokopedia.sellerapp.presentation.theme.DP_10
import com.tokopedia.sellerapp.presentation.theme.DP_13
import com.tokopedia.sellerapp.presentation.theme.DP_18
import com.tokopedia.sellerapp.presentation.theme.DP_5
import com.tokopedia.sellerapp.presentation.theme.DP_6
import com.tokopedia.sellerapp.presentation.theme.DP_80
import com.tokopedia.sellerapp.presentation.theme.NEST_FONT_SIZE_LVL3
import com.tokopedia.sellerapp.presentation.theme.NEST_FONT_SIZE_LVL4
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL3
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL6
import com.tokopedia.sellerapp.presentation.theme.NEST_SPACING_LVL1
import com.tokopedia.sellerapp.presentation.theme.NEST_SPACING_LVL2
import com.tokopedia.sellerapp.presentation.theme.NEST_SPACING_LVL3
import com.tokopedia.sellerapp.presentation.theme.NestLightNN0
import com.tokopedia.sellerapp.presentation.theme.SP_18
import com.tokopedia.sellerapp.presentation.theme.SP_20
import com.tokopedia.sellerapp.presentation.theme.TextGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextYellowColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.MenuHelper
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.NumberConstant.ANIMATION_SHIMMERING_DURATION
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_400
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_500
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_700
import com.tokopedia.sellerapp.util.NumberConstant.MAX_LINES_1
import com.tokopedia.sellerapp.util.NumberConstant.SHIMMERING_DROP_OFF
import com.tokopedia.sellerapp.util.NumberConstant.SHIMMERING_TILT
import com.tokopedia.sellerapp.util.MenuHelper.getDataKeyByOrderStatus
import com.tokopedia.sellerapp.R

@Composable
fun NewOrderDetailScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
    orderId: String
) {
    getNewOrderDetailData(sharedViewModel, orderId)
    val orderDetail by sharedViewModel.orderDetail.collectAsState()
    orderDetail.data?.let { orderDetailData ->
        val orderType = listOf(orderDetailData).getDataKeyByOrderStatus()
        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            item {
                NewOrderDetailHeader(orderType)
            }
            item {
                NewOrderDetailMain(orderDetailData, orderType)
            }
            item {
                NewOrderDetailFooter(orderDetailData, orderType, screenNavigation, sharedViewModel)
            }
        }
    }
}

private fun getNewOrderDetailData(
    sharedViewModel: SharedViewModel,
    orderId: String
) {
    sharedViewModel.getOrderDetail(orderId)
}

@Composable
fun NewOrderDetailHeader(orderType: String) {
    NewOrderDetailSpacer(
        height = DP_18
    )
    Row(
        modifier = Modifier
            .padding(
                horizontal = DP_18
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(
                    size = DP_18
                )
                .padding(
                    end = DP_6
                ),
            painter = painterResource(
                id = R.drawable.ic_seller_toped
            ),
            contentDescription = stringResource(id = R.string.new_order_detail_content_description_seller_icon)
        )
        NewOrderDetailText(
            fontSize = NEST_FONT_SIZE_LVL3,
            text = MenuHelper.getTitleByDataKey(orderType),
            color = NestLightNN0,
            lineHeight = SP_18,
            weight = FONT_WEIGHT_500,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getDueDateStringRes(orderType: String): Int {
    return if (orderType == DATAKEY_NEW_ORDER) {
        R.string.new_order_list_text_due_response
    } else {
        R.string.ready_to_shop_order_list_text_due_response
    }
}

@Composable
fun NewOrderDetailMain(orderDetailData: OrderModel, orderType: String) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = DP_18
            )
    ) {
        NewOrderDetailSpacer(
            height = DP_5
        )
        NewOrderDetailText(
            fontSize = NEST_FONT_SIZE_LVL4,
            text = stringResource(id = getDueDateStringRes(orderType)),
            color = NestLightNN0,
            lineHeight = SP_20,
            weight = FONT_WEIGHT_500,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
        NewOrderDetailDate(orderDetailData)
        NewOrderDetailProductDescription(orderDetailData)
        val totalProductData = orderDetailData.products.size
        if (totalProductData > 1) {
            NewOrderDetailMoreProducts(totalProductData-1)
        }
        NewOrderDetailLocation(orderDetailData)
    }
}

@Composable
fun NewOrderDetailDate(orderDetailData: OrderModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(
                    size = DP_13
                ),
            painter = painterResource(
                id = com.tokopedia.iconunify.R.drawable.iconunify_clock_filled
            ),
            contentDescription = stringResource(id = R.string.new_order_detail_content_description_clocked_filled)
        )
        NewOrderDetailSpacer(
            height = NEST_SPACING_LVL1
        )
        NewOrderDetailText(
            modifier = Modifier
                .padding(
                    start = NEST_SPACING_LVL2
                ),
            fontSize = NEST_FONT_SIZE_LVL4,
            text = orderDetailData.deadLineText,
            color = TextYellowColor,
            lineHeight = SP_20,
            weight = FONT_WEIGHT_400,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NewOrderDetailProductDescription(orderDetailData: OrderModel) {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    GlideImage(
        modifier = Modifier
            .size(
                size = DP_80
            )
            .clip(
                shape = RoundedCornerShape(
                    size = DP_6
                )
            )
            .background(
                color = NestLightNN0
            ),
        imageModel = orderDetailData.products.firstOrNull()?.productImage.orEmpty(),
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = NestLightNN0,
            durationMillis = ANIMATION_SHIMMERING_DURATION,
            dropOff = SHIMMERING_DROP_OFF,
            tilt = SHIMMERING_TILT
        ),
        error = painterResource(
            id = R.drawable.imagestate_placeholder
        )
    )
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL4,
        text = orderDetailData.products.firstOrNull()?.productName.orEmpty(),
        color = NestLightNN0,
        lineHeight = SP_20,
        weight = FONT_WEIGHT_400
    )
}

@Composable
fun NewOrderDetailMoreProducts(totalProductLeft: Int) {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL4,
        text = stringResource(
            id = com.tokopedia.sellerapp.R.string.order_detail_product_left_format,
            totalProductLeft.toString()
        ),
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
}


@Composable
fun NewOrderDetailLocation(orderDetailData: OrderModel) {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL3,
        text = orderDetailData.courierName,
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL1
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL3,
        text = orderDetailData.destinationProvince,
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
}

@Composable
fun NewOrderDetailFooter(
    orderDetailData: OrderModel,
    orderType: String,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL2
    )
    NewOrderDetailText(
        modifier = Modifier
            .height(
                height = NEST_LAYOUT_LVL6
            )
            .fillMaxWidth()
            .wrapContentSize(
                align = Alignment.Center
            )
            .padding(
                horizontal = NEST_LAYOUT_LVL3
            ),
        fontSize = NEST_FONT_SIZE_LVL3,
        text = stringResource(id = R.string.new_order_detail_footer_title),
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_700,
    )
    if(orderType == DATAKEY_NEW_ORDER) {
        NewOrderDetailActionButton(
            text = stringResource(id = R.string.new_order_detail_accept_order),
            onButtonClicked = {
                redirectToAcceptOrderScreen(
                    listOf(orderDetailData),
                    screenNavigation,
                    sharedViewModel
                )
            }
        )
    }
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL2
    )
    //Next feature
//    NewOrderDetailActionButton(
//        text = stringResource(id = R.string.new_order_detail_open_on_cellphone)
//    )
    NewOrderDetailSpacer(
        height = DP_18
    )
}

@Composable
fun NewOrderDetailActionButton(
    text: String,
    onButtonClicked: () -> Unit = {}
) {
    Button(
        onClick = {
            onButtonClicked()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ActionButtonGrayColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DP_10
            )
            .clip(
                RoundedCornerShape(
                    size = NEST_LAYOUT_LVL6
                )
            )
    ) {
        NewOrderDetailText(
            fontSize = NEST_FONT_SIZE_LVL3,
            text = text,
            color = NestLightNN0,
            lineHeight = SP_18,
            weight = FONT_WEIGHT_700
        )
    }
}

@Composable
fun NewOrderDetailText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    text: String,
    color: Color,
    lineHeight: TextUnit,
    weight: Int,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        fontSize = fontSize,
        color = color,
        text = text,
        lineHeight = lineHeight,
        fontWeight = FontWeight(
            weight = weight
        ),
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier
    )
}

@Composable
fun NewOrderDetailSpacer(height: Dp) {
    Spacer(
        modifier = Modifier
            .height(
                height = height
            )
    )
}

private fun redirectToAcceptOrderScreen(
    orderList: List<OrderModel>?,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    screenNavigation.popBackStack()
    orderList?.let {
        sharedViewModel.resetAcceptBulkOrderState()
        screenNavigation.toAcceptOrderScreen(it.map { it.orderId })
    }
}

