package com.example.mullak.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.mullak.R
import com.example.mullak.databinding.FragmentMapsBinding
import com.example.mullak.ui.home.HomeFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.location.Geocoder
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentMapsBinding
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupMap()
        }
        else {
            // Do otherwise
        }
    }

    var currentLatLong: LatLng = LatLng(0.0, 0.0)

    companion object {
        private const val Location_REQUEST_CODE = 1
        lateinit var onLocationDetect: HomeFragment.LocationChange
    }

    private val callback = OnMapReadyCallback { googleMap ->


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }


    override fun onMapReady(gMap: GoogleMap?) {
        gMap?.let {
            googleMap = it
        }
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)

    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        googleMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                onLocationDetect.onChange(location = location,address = getAddressFromLocation(location)!!)
                val latlng = LatLng(location.latitude,location.longitude)
                googleMap.addMarker(MarkerOptions().position(latlng).title("Place"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,14F))

            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        googleMap.addMarker(markerOptions)
    }

    private fun getAddressFromLocation(location: Location): String? {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return addresses[0].getAddressLine(0)
    }

    override fun onMarkerClick(p0: Marker?) = false
}