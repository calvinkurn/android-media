import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.tokopedia.sellerapp.presentation.theme.TextBlueColor
import com.tokopedia.tkpd.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_1

@Composable
fun NewOrderSummaryScreen(
    navigateToNewOrderList: () -> Unit
) {
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
            //TODO implementation will be changed later
            val totalOrder = 20
            val orderPotential = "321.002.500"
            CreateOrderTitle()
            CreateOrderQuantity(totalOrder)
            CreateOrderPotential(orderPotential)
            CreateOpenOrder(
                navigateToNewOrderList = navigateToNewOrderList
            )
        }

    }

}

@Composable
fun CreateOpenOrder(navigateToNewOrderList: () -> Unit) {
    Button(
        modifier = Modifier
            .height(32.dp)
            .padding(PaddingValues(top = 6.dp)),
        onClick = {
            navigateToNewOrderList()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = TextBlueColor)
    ) {
        AndroidView(factory = {
            Typography(it).apply {
                text = it.getString(R.string.new_order_summary_text_open_order)
                setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
                fontType = DISPLAY_1
            }
        })
    }
}

@Composable
fun CreateOrderPotential(orderPotential: String) {
    AndroidView(factory = {
        Typography(it).apply {
            val textLabelPotency = it.getString(R.string.new_order_summary_text_potency)
            val textTotalPotency = String.format(
                it.getString(R.string.new_order_summary_text_potency_format),
                orderPotential
            )
            setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            val textPotencySpannable = SpannableString("$textLabelPotency $textTotalPotency")
            textPotencySpannable.setSpan(
                ForegroundColorSpan(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)),
                textPotencySpannable.indexOf(textTotalPotency),
                textPotencySpannable.indexOf(textTotalPotency) + textTotalPotency.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            text = textPotencySpannable
            fontType = DISPLAY_1
        }
    })
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
                setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
                text = totalOrder.toString()
                textSize = 30.0f
            }
        })
}

@Composable
private fun CreateOrderTitle() {
    AndroidView(
        factory = {
            Typography(it).apply {
                setTextColor(it.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
                text = it.getString(R.string.new_order_summary_text_title)
            }
        })
}
