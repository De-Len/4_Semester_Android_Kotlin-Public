package com.example.lab7_googlemaps.ui

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lab7_googlemaps.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.widgets.ScaleBar
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(mapViewModel: MapViewModel) {

    val coroutineScope = rememberCoroutineScope()

    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val moscow = LatLng(55.751244, 37.618423)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(moscow, 12f)
    }

    val markerPositions = remember { mutableStateListOf<LatLng>() }

    val userLocation by mapViewModel.userLocation

    val markerState = rememberMarkerState(position = moscow)

    RequestPermissionForFineLocation(mapViewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text("Google Maps") },
//                actions = {
//                    IconButton(onClick = { /* Действие при нажатии */ }) {
//                        Icon(Icons.Default.MoreVert, contentDescription = "Поиск")
//                    }
//                },
            )

            Box(Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings,
                    onMapLongClick = { latLng ->
                        if (markerPositions.size < 2) {
                            markerPositions.add(latLng)
                        }
                    }
                ) {
//                    Marker(
//                        state = markerState,
//                        title = "Москва",
//                        snippet = "Столица России"
//                    )
                    // If the user's location is available, place a marker on the map
                    userLocation?.let {
                        Marker(
                            state = MarkerState(position = it), // Place the marker at the user's location
                            title = "Your Location", // Set the title for the marker
                            snippet = "This is where you are currently located.", // Set the snippet for the marker
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )
                        // Move the camera to the user's location with a zoom level of 10f
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
                    }

                    markerPositions.forEachIndexed { index, position ->
                        Marker(
                            state = MarkerState(position = position),
                            title = "Вы отметили здесь",
                            snippet = "${position.latitude}, ${position.longitude}"
                        )
                    }
                }
                ScaleBar(
                    modifier = Modifier
                        .padding(top = 5.dp, end = 15.dp)
                        .align(Alignment.TopEnd),
                    cameraPositionState = cameraPositionState
                )
                Column(modifier = Modifier
                    .padding(10.dp, 30.dp)
                    .align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // расстояние между элементами
                ) {
                    Button(onClick = {
                        markerPositions.clear()
                    },
                        modifier = Modifier
                            .size(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete markers",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }


                    Button(onClick = {
                        // Навести камеру на маркер
                        coroutineScope.launch {
                            userLocation?.let { it ->
                                cameraPositionState.animate(

                                    update = CameraUpdateFactory.newLatLngZoom(it, 15f),
                                    durationMs = 1000
                                )
                            }

                        }
                    },
                        modifier = Modifier
                            .size(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Blue
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Person,
                            contentDescription = "MyPosition",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    Button(onClick = {
                        // Навести камеру на маркер
                        coroutineScope.launch {
                            cameraPositionState.animate(

                                update = CameraUpdateFactory.newLatLngZoom(LatLng(55.354993, 86.085805), 15f),
                                durationMs = 1000
                            )


                        }
                    },
                        modifier = Modifier
                            .size(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Yellow
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Home,
                            contentDescription = "Kemerovo",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }



            }
        }
    }
}


@Composable
fun RequestPermissionForFineLocation(mapViewModel: MapViewModel) {
    // Handle permission requests for accessing fine location
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Fetch the user's location and update the camera if permission is granted
            mapViewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            // Handle the case when permission is denied
            Timber.e("Location permission was denied by the user.")
        }
    }

    // Request the location permission when the composable is launched
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            // Check if the location permission is already granted
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Fetch the user's location and update the camera
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            }
            else -> {
                // Request the location permission if it has not been granted
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}