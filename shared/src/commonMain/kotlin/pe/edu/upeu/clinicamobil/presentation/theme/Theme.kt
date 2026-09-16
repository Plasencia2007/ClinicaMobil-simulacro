package pe.edu.upeu.clinicamobil.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors: ColorScheme = lightColorScheme(
    primary = ClinicaPrimaryLight,
    onPrimary = ClinicaOnPrimaryLight,
    primaryContainer = ClinicaPrimaryContainerLight,
    onPrimaryContainer = ClinicaOnPrimaryContainerLight,
    secondary = ClinicaSecondaryLight,
    onSecondary = ClinicaOnSecondaryLight,
    secondaryContainer = ClinicaSecondaryContainerLight,
    onSecondaryContainer = ClinicaOnSecondaryContainerLight,
    tertiary = ClinicaTertiaryLight,
    onTertiary = ClinicaOnTertiaryLight,
    tertiaryContainer = ClinicaTertiaryContainerLight,
    onTertiaryContainer = ClinicaOnTertiaryContainerLight,
    error = ClinicaErrorLight,
    onError = ClinicaOnErrorLight,
    errorContainer = ClinicaErrorContainerLight,
    onErrorContainer = ClinicaOnErrorContainerLight,
    background = ClinicaBackgroundLight,
    onBackground = ClinicaOnBackgroundLight,
    surface = ClinicaSurfaceLight,
    onSurface = ClinicaOnSurfaceLight,
    surfaceVariant = ClinicaSurfaceVariantLight,
    onSurfaceVariant = ClinicaOnSurfaceVariantLight,
    outline = ClinicaOutlineLight,
    outlineVariant = ClinicaOutlineVariantLight,
    scrim = ClinicaScrimLight,
    inverseSurface = ClinicaInverseSurfaceLight,
    inverseOnSurface = ClinicaInverseOnSurfaceLight,
    inversePrimary = ClinicaInversePrimaryLight,
    surfaceDim = ClinicaSurfaceDimLight,
    surfaceBright = ClinicaSurfaceBrightLight,
    surfaceContainerLowest = ClinicaSurfaceContainerLowestLight,
    surfaceContainerLow = ClinicaSurfaceContainerLowLight,
    surfaceContainer = ClinicaSurfaceContainerLight,
    surfaceContainerHigh = ClinicaSurfaceContainerHighLight,
    surfaceContainerHighest = ClinicaSurfaceContainerHighestLight
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = ClinicaPrimaryDark,
    onPrimary = ClinicaOnPrimaryDark,
    primaryContainer = ClinicaPrimaryContainerDark,
    onPrimaryContainer = ClinicaOnPrimaryContainerDark,
    secondary = ClinicaSecondaryDark,
    onSecondary = ClinicaOnSecondaryDark,
    secondaryContainer = ClinicaSecondaryContainerDark,
    onSecondaryContainer = ClinicaOnSecondaryContainerDark,
    tertiary = ClinicaTertiaryDark,
    onTertiary = ClinicaOnTertiaryDark,
    tertiaryContainer = ClinicaTertiaryContainerDark,
    onTertiaryContainer = ClinicaOnTertiaryContainerDark,
    error = ClinicaErrorDark,
    onError = ClinicaOnErrorDark,
    errorContainer = ClinicaErrorContainerDark,
    onErrorContainer = ClinicaOnErrorContainerDark,
    background = ClinicaBackgroundDark,
    onBackground = ClinicaOnBackgroundDark,
    surface = ClinicaSurfaceDark,
    onSurface = ClinicaOnSurfaceDark,
    surfaceVariant = ClinicaSurfaceVariantDark,
    onSurfaceVariant = ClinicaOnSurfaceVariantDark,
    outline = ClinicaOutlineDark,
    outlineVariant = ClinicaOutlineVariantDark,
    scrim = ClinicaScrimDark,
    inverseSurface = ClinicaInverseSurfaceDark,
    inverseOnSurface = ClinicaInverseOnSurfaceDark,
    inversePrimary = ClinicaInversePrimaryDark,
    surfaceDim = ClinicaSurfaceDimDark,
    surfaceBright = ClinicaSurfaceBrightDark,
    surfaceContainerLowest = ClinicaSurfaceContainerLowestDark,
    surfaceContainerLow = ClinicaSurfaceContainerLowDark,
    surfaceContainer = ClinicaSurfaceContainerDark,
    surfaceContainerHigh = ClinicaSurfaceContainerHighDark,
    surfaceContainerHighest = ClinicaSurfaceContainerHighestDark
)

@Composable
fun ClinicaMobilTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = ClinicaMobilShapes,
        content = content
    )
}
