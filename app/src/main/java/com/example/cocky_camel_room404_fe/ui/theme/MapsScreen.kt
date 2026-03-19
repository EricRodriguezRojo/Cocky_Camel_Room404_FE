package com.example.cocky_camel_room404_fe

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var hasCenteredCamera by remember { mutableStateOf(false) }
    var errorModeActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    fun requestCurrentLocation() {
        if (!hasLocationPermission) return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                } else {
                    val tokenSource = CancellationTokenSource()
                    fusedLocationClient
                        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
                        .addOnSuccessListener { freshLocation ->
                            if (freshLocation != null) {
                                currentLocation = freshLocation
                            }
                        }
                }
            }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (granted) {
            requestCurrentLocation()
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            requestCurrentLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(errorModeActive) {
        if (errorModeActive) {
            delay(2200)
            errorModeActive = false
        }
    }

    val statusBarBg = if (errorModeActive) Color(0xFF250000) else Color.Black
    val searchBarBg = if (errorModeActive) Color(0xCC330000) else Color(0xD92A2A2A)
    val panelBg = if (errorModeActive) Color(0xFF120000) else Color(0xEE1A1A1A)
    val directionsButtonBg = if (errorModeActive) Color(0xFF8B0000) else Color(0xFF03A9F4)

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
                val location = currentLocation
                if (location != null) {
                    val userPoint = GeoPoint(location.latitude, location.longitude)

                    mapView.overlays.removeAll { overlay ->
                        overlay is Marker && overlay.id == "user_location_marker"
                    }

                    val marker = Marker(mapView).apply {
                        id = "user_location_marker"
                        position = userPoint
                        title = "Tu ubicación"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)

                    if (!hasCenteredCamera) {
                        mapView.controller.setZoom(17.0)
                        mapView.controller.animateTo(userPoint)
                        hasCenteredCamera = true
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
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Buscar en Google Maps", color = Color(0xFFD0D0D0), modifier = Modifier.weight(1f))
                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
            }
        }

        if (!hasLocationPermission) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xCC2A2A2A)),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(
                    "Activa el permiso de ubicación para centrar el mapa en tu posición.",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    fontSize = 12.sp
                )
            }
        }

        FloatingActionButton(
            onClick = {
                if (hasLocationPermission) {
                    hasCenteredCamera = false
                    requestCurrentLocation()
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
            Icon(Icons.Default.MyLocation, contentDescription = null)
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = panelBg)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Última ubicación registrada", color = Color.Gray, fontSize = 12.sp)
                Text(
                    currentLocation?.let { "Lat ${"%.5f".format(it.latitude)}, Lon ${"%.5f".format(it.longitude)}" }
                        ?: "Carrer de l'Arquitecte Muncunill",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        errorModeActive = true
                        Toast.makeText(context, "Error de ruta: servicio no disponible.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = directionsButtonBg)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cómo llegar")
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