import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.presentation.theme.NestLightNN0
import com.tokopedia.sellerapp.presentation.theme.NestLightNN1000
import com.tokopedia.sellerapp.presentation.theme.TextBlueColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_1

@Composable
fun OrderSummaryScreen(
    navigateToNewOrderList: (dataKey: String) -> Unit,
    sharedViewModel: SharedViewModel,
    dataKey: String
) {
    getOrderSummaryData(sharedViewModel, dataKey)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN1000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val summary by sharedViewModel.orderSummary.collectAsState()
            summary.data?.let {
                CreateOrderTitle(it.title)
                CreateOrderQuantity(it.counter.toIntOrZero())
                CreateOrderPotential(it.description)
                CreateOpenOrder(
                    navigateToNewOrderList = navigateToNewOrderList,
                    dataKey = dataKey
                )
            }
        }

    }

}

private fun getOrderSummaryData(
    sharedViewModel: SharedViewModel,
    dataKey: String
) {
    sharedViewModel.getOrderSummary(dataKey)
}

@Composable
fun CreateOpenOrder(
    navigateToNewOrderList: (dataKey: String) -> Unit,
    dataKey: String
) {
    Button(
        modifier = Modifier
            .height(32.dp)
            .padding(PaddingValues(top = 6.dp)),
        onClick = { navigateToNewOrderList(dataKey) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = TextBlueColor,
            contentColor = NestLightNN1000
        )
    ) {
        AndroidView(factory = {
            Typography(it).apply {
                text = it.getString(R.string.new_order_summary_text_open_order)
                fontType = DISPLAY_1
            }
        })
    }
}

@Composable
fun CreateOrderPotential(orderPotential: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            Typography(it).apply {
                text = HtmlCompat.fromHtml(orderPotential, HtmlCompat.FROM_HTML_MODE_COMPACT)
                fontType = DISPLAY_1
                setTextColor(NestLightNN0.toArgb())
            }
        },
        update = { it.text = HtmlCompat.fromHtml(orderPotential, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}

@Composable
fun CreateOrderQuantity(totalOrder: Int) {
    AndroidView(
        modifier = Modifier.padding(
            paddingValues = PaddingValues(
                vertical = 8.dp
            )
        ),
        factory = {
            Typography(it).apply {
                setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                text = totalOrder.toString()
                textSize = 30.0f
            }
        },
        update = {
            it.text = totalOrder.toString()
        }
    )
}

@Composable
private fun CreateOrderTitle(title: String) {
    AndroidView(
        factory = {
            Typography(it).apply {
                setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                text = title
            }
        }, update = {
            it.text = title
        })
}
