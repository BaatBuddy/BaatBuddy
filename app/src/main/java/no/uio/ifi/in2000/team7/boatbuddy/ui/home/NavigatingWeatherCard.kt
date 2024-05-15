package no.uio.ifi.in2000.team7.boatbuddy.ui.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team7.boatbuddy.model.locationforecast.DayForecast
import no.uio.ifi.in2000.team7.boatbuddy.ui.Screen
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.InfoScreenViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.LocationForecastViewModel
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.ScoreToEmoji
import no.uio.ifi.in2000.team7.boatbuddy.ui.info.WeatherIcon
import no.uio.ifi.in2000.team7.boatbuddy.ui.profile.ProfileViewModel

@Composable
fun NavigatingWeatherCard(
    dayForecast: DayForecast,
    profileViewModel: ProfileViewModel,
    mapboxViewModel: MapboxViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    homeViewModel: HomeViewModel,
    locationForecastViewModel: LocationForecastViewModel,
    navController: NavController,
) {
    val emojiString = ScoreToEmoji(dayForecast.dayScore?.score) ?: ""
    val timeLocationData = dayForecast.middayWeatherData
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f ,
        animationSpec =  infiniteRepeatable(tween(1000, easing = LinearEasing)), label = ""
    )
    val borderColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.onPrimaryContainer,
        targetValue = MaterialTheme.colorScheme.secondaryContainer ,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val shape = RoundedCornerShape(20.dp)

    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            //.border(5.dp, borderColor)

        
        ,
        onClick = {
            // navigate to weather info screen with route selected
            profileViewModel.updatePickedRoute(mapboxUIState.generatedRoute)
            infoScreenViewModel.selectTab(1)
            homeViewModel.hideBottomSheet()
            locationForecastViewModel.updateSelectedDayRoute(dayForecast = dayForecast)

            navController.navigate(Screen.InfoScreen.route) {

                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        // TODO fix colors
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {

        Box(modifier = Modifier.width(IntrinsicSize.Max)){


        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                text = dayForecast.day,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${timeLocationData.airTemperature}℃",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            WeatherIcon(
                symbolCode = timeLocationData.symbolCode,
                modifier = Modifier
                    .fillMaxSize(0.15f)
            )

            Text(
                text = emojiString,
                fontSize = 30.sp

            )
        }
    }

}


@Composable
fun BestWeatherButton(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rainbowColors = listOf(
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color.Magenta
    )
    val colorIndex by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = rainbowColors.size - 1,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    val borderColor by animateColorAsState(rainbowColors[colorIndex])

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp)
            .border(
                width = 2.dp,
                brush = Brush.sweepGradient(rainbowColors),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = "Best weather",
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
