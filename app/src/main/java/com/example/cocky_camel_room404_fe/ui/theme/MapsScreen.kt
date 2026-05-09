package com.example.cocky_camel_room404_fe

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    onBack: () -> Unit,
    viewModel: MapsViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }

    val userLocationLabel = stringResource(R.string.maps_user_location)
    val routeErrorMsg = stringResource(R.string.maps_route_error)

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        Configuration.getInstance().userAgentValue = context.packageName
        viewModel.updatePermissionStatus(context)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.setPermissionGranted(granted)
        if (granted) {
            viewModel.requestCurrentLocation(context, fusedLocationClient)
        }
    }

    LaunchedEffect(viewModel.hasLocationPermission) {
        if (viewModel.hasLocationPermission) {
            viewModel.requestCurrentLocation(context, fusedLocationClient)
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val statusBarBg = if (viewModel.errorModeActive) Color(0xFF250000) else Color.Black
    val searchBarBg = if (viewModel.errorModeActive) Color(0xCC330000) else Color(0xD92A2A2A)
    val panelBg = if (viewModel.errorModeActive) Color(0xFF120000) else Color(0xEE1A1A1A)
    val directionsButtonBg = if (viewModel.errorModeActive) Color(0xFF8B0000) else Color(0xFF03A9F4)

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapContext ->
                MapView(mapContext).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(41.5625, 2.0089))
                    mapViewRef = this
                }
            },
            update = { mapView ->
                val location = viewModel.currentLocation
                if (location != null) {
                    val userPoint = GeoPoint(location.latitude, location.longitude)

                    mapView.overlays.removeAll { overlay ->
                        overlay is Marker && overlay.id == "user_location_marker"
                    }

                    val marker = Marker(mapView).apply {
                        id = "user_location_marker"
                        position = userPoint
                        title = userLocationLabel
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)

                    if (!viewModel.hasCenteredCamera) {
                        mapView.controller.setZoom(17.0)
                        mapView.controller.animateTo(userPoint)
                        viewModel.hasCenteredCamera = true
                    }
                    mapView.invalidate()
                }
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusBarBg)
            ) {
                FakeStatusBar(modifier = Modifier.padding(top = 12.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(searchBarBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White,
                    modifier = Modifier.clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.maps_search_placeholder),
                    color = Color(0xFFD0D0D0),
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
            }
        }

        if (!viewModel.hasLocationPermission) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xCC2A2A2A)),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.maps_permission_required),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    fontSize = 12.sp
                )
            }
        }

        FloatingActionButton(
            onClick = {
                if (viewModel.hasLocationPermission) {
                    viewModel.hasCenteredCamera = false
                    viewModel.requestCurrentLocation(context, fusedLocationClient)
                } else {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 170.dp),
            containerColor = Color(0xFF2A2A2A),
            contentColor = Color(0xFF03A9F4)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = stringResource(R.string.maps_my_location))
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = panelBg)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(stringResource(R.string.maps_last_location_label), color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = viewModel.currentLocation?.let { "Lat ${"%.5f".format(it.latitude)}, Lon ${"%.5f".format(it.longitude)}" }
                        ?: stringResource(R.string.maps_default_address),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.triggerErrorMode()
                        Toast.makeText(context, routeErrorMsg, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = directionsButtonBg)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.maps_get_directions))
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapViewRef?.onResume()
                Lifecycle.Event.ON_PAUSE -> mapViewRef?.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapViewRef?.onDetach()
            mapViewRef = null
        }
    }
}
