import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.presentation.theme.NestDarkN0
import com.tokopedia.sellerapp.presentation.theme.NestLightBlue
import com.tokopedia.sellerapp.presentation.theme.NestLightG500
import com.tokopedia.sellerapp.presentation.theme.NestLightN0
import com.tokopedia.tkpd.R

@Composable
fun NewOrderSummaryScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NestDarkN0),
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
            CreateOpenOrder()
        }

    }

}

@Composable
fun CreateOpenOrder() {
    Button(
        modifier = Modifier
            .height(32.dp)
            .padding(PaddingValues(top = 4.dp)),
        onClick = {redirectToNewOrderListScreen()},
        colors = ButtonDefaults.buttonColors(backgroundColor = NestLightBlue)
    ) {
        Text(
            text = stringResource(id = R.string.new_order_summary_text_open_order),
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(PaddingValues(12.dp, 0.dp, 12.dp, 0.dp)),
        )
    }
}

fun redirectToNewOrderListScreen() {
    //TODO will be implemented later
}

@Composable
fun CreateOrderPotential(orderPotential: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append(stringResource(id = R.string.new_order_summary_text_potency))
            }
            withStyle(style = SpanStyle(color = NestLightG500)) {
                append(stringResource(id = R.string.new_order_summary_text_potency_format, orderPotential))
            }
        },
        color = NestLightN0,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Normal,
        maxLines = 1
    )
}

@Composable
fun CreateOrderQuantity(totalOrder: Int) {
    Text(
        text = totalOrder.toString(),
        color = NestLightN0,
        fontSize = 30.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(
            paddingValues = PaddingValues(
                vertical = 8.dp
            )
        )
    )
}

@Composable
private fun CreateOrderTitle() {
    Text(
        text = stringResource(id = R.string.new_order_summary_text_title),
        color = NestLightN0,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
    )
}
