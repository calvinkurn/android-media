@file:OptIn(ExperimentalTextApi::class)

package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import com.tokopedia.sellerapp.presentation.theme.ActionButtonBlackColor
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
import com.tokopedia.sellerapp.util.NumberConstant.ANIMATION_SHIMMERING_DURATION
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_400
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_500
import com.tokopedia.sellerapp.util.NumberConstant.FONT_WEIGHT_700
import com.tokopedia.sellerapp.util.NumberConstant.MAX_LINES_1
import com.tokopedia.sellerapp.util.NumberConstant.SHIMMERING_DROP_OFF
import com.tokopedia.sellerapp.util.NumberConstant.SHIMMERING_TILT
import com.tokopedia.tkpd.R

@Composable
fun NewOrderDetailScreen() {
    LazyColumn{
        item {
            NewOrderDetailHeader()
        }
        item {
            NewOrderDetailMain()
        }
        item {
            NewOrderDetailFooter()
        }
    }
}

@Composable
fun NewOrderDetailHeader() {
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
            contentDescription = "logo seller"
        )
        NewOrderDetailText(
            fontSize = NEST_FONT_SIZE_LVL3,
            text = "Pesanan Baru",
            color = NestLightNN0,
            lineHeight = SP_18,
            weight = FONT_WEIGHT_500,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NewOrderDetailMain() {
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
            text = "Batas Respons (P0):",
            color = NestLightNN0,
            lineHeight = SP_20,
            weight = FONT_WEIGHT_500,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
        NewOrderDetailDate()
        NewOrderDetailProductDescription()
        NewOrderDetailMoreProducts()
        NewOrderDetailLocation()
    }
}

@Composable
fun NewOrderDetailDate() {
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
            contentDescription = "logo seller"
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
            text = "13 Sep; 14:55",
            color = TextYellowColor,
            lineHeight = SP_20,
            weight = FONT_WEIGHT_400,
            maxLines = MAX_LINES_1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NewOrderDetailProductDescription() {
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
        imageModel = "https://asset.kompas.com/crops/0goP7FKwWF1qhOgFdSg5Q9QEOXg=/14x0:547x355/750x500/data/photo/2020/02/03/5e37dfdc0013d.png",
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
        text = "Air Jorda Gym Red Satin Original Produk Hoops Malaysia Tunai Dong - Red, Black",
        color = NestLightNN0,
        lineHeight = SP_20,
        weight = FONT_WEIGHT_400
    )
}

@Composable
fun NewOrderDetailMoreProducts() {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL4,
        text = "+2 produk lainnya",
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
}


@Composable
fun NewOrderDetailLocation() {
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL3
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL3,
        text = "Reguler - JNE",
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL1
    )
    NewOrderDetailText(
        fontSize = NEST_FONT_SIZE_LVL3,
        text = "D.I. Aceh",
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_400
    )
}

@Composable
fun NewOrderDetailFooter() {
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
        text = "Actions",
        color = TextGrayColor,
        lineHeight = SP_18,
        weight = FONT_WEIGHT_700,
    )
    NewOrderDetailActionButton(
        text = "Terima Pesanan"
    )
    NewOrderDetailSpacer(
        height = NEST_SPACING_LVL2
    )
    NewOrderDetailActionButton(
        text = "Buka di handphone"
    )
    NewOrderDetailSpacer(
        height = DP_18
    )
}

@Composable
fun NewOrderDetailActionButton(
    text: String
) {
    Button(
        onClick = {
          /* nothing to do for now */
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ActionButtonBlackColor
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

