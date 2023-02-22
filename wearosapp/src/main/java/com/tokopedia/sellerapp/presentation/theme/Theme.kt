import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import com.tokopedia.sellerapp.presentation.theme.Purple200
import com.tokopedia.sellerapp.presentation.theme.Purple500
import com.tokopedia.sellerapp.presentation.theme.Purple700
import com.tokopedia.sellerapp.presentation.theme.Teal200
import com.tokopedia.sellerapp.presentation.theme.Typography

@Composable
fun WearAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorPalette else lightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}

internal val darkColorPalette: Colors = Colors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val lightColorPalette: Colors = Colors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)