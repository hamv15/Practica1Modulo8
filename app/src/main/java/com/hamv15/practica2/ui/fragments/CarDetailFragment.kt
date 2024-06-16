package com.hamv15.practica2.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hamv15.practica2.R
import com.hamv15.practica2.application.Practica2App
import com.hamv15.practica2.data.CarRepository
import com.hamv15.practica2.data.remote.model.CarDetailDTO
import com.hamv15.practica2.databinding.FragmentCarDetailBinding
import com.hamv15.practica2.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private const val CAR_ID = "car_id"

class CarDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var fineLocationPermissionGranted = false
    private var _binding: FragmentCarDetailBinding? = null
    private val binding get() = _binding!!
    private var car_id: String? = null
    private lateinit var repository: CarRepository
    private lateinit var nombreConcesionario: String
    private lateinit var descripcion: String
    private var latitud by Delegates.notNull<Double>()
    private var longitud by Delegates.notNull<Double>()

    private var isMapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            car_id = args.getString(CAR_ID)
            Log.d(Constants.LOGTAG, getString(R.string.msj_id, car_id))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as Practica2App).repository
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        lifecycleScope.launch {
            car_id?.let { id ->
                val call: Call<CarDetailDTO> = repository.getCarDetail(id)
                val mc = MediaController(requireContext())
                call.enqueue(object : Callback<CarDetailDTO> {
                    override fun onResponse(p0: Call<CarDetailDTO>, response: Response<CarDetailDTO>) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE
                            lableFabricante.text = getString(R.string.lblFabricante)
                            lablelYear.text = getString(R.string.lblYear)
                            lablelVersion.text = getString(R.string.lblVersion)
                            lablelMotor.text = getString(R.string.lblMotor)
                            lablelPotencia.text = getString(R.string.lblPotencia)
                            lablelTransmision.text = getString(R.string.lblTransmision)
                            lablelVideo.text = getString(R.string.video)
                            tvModel.text = response.body()?.modelo
                            tvFabricante.text = response.body()?.marca
                            tvYear.text = response.body()?.anno.toString()
                            tvVersion.text = response.body()?.version
                            tvMotor.text = response.body()?.motor
                            tvPotencia.text = response.body()?.potencia
                            tvTransmision.text = response.body()?.transmision
                            Glide.with(requireActivity())
                                .load(response.body()?.imagen)
                                .into(ivImage)
                            mc.setAnchorView(vvVideo)
                            vvVideo.setMediaController(mc)
                            vvVideo.setVideoURI(Uri.parse(response.body()?.videoUrl))
                            vvVideo.requestFocus()
                            vvVideo.setOnPreparedListener {
                                vvVideo.start()
                            }
                            response.body()?.concesionario?.let { concesionario ->
                                nombreConcesionario = concesionario.nombreConcesionario.toString()
                                descripcion = concesionario.descripcion.toString()
                                latitud = concesionario.latitud!!
                                longitud = concesionario.longitud!!
                                if (isMapReady) {
                                    createMarker(latitud, longitud, nombreConcesionario, descripcion)
                                }
                            }
                            vvVideo.setOnErrorListener { mp, what, extra ->
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.error_al_reproducir_el_video),
                                    Toast.LENGTH_LONG
                                ).show()
                                true
                            }
                        }
                    }

                    override fun onFailure(p0: Call<CarDetailDTO>, p1: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msjErrorGetCarDetail), Toast.LENGTH_LONG
                        ).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, CarsListFragment())
                            .commitAllowingStateLoss()
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        isMapReady = true
        if (::nombreConcesionario.isInitialized && ::descripcion.isInitialized) {
            createMarker(latitud, longitud, nombreConcesionario, descripcion)
        }
        map.setOnMapLongClickListener { position ->
            val marker = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.concesionario))
            map.addMarker(marker)
        }
    }

    private fun createMarker(lat: Double, long: Double, title: String, snippet: String) {
        val coordinates = LatLng(lat, long)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(title)
            .snippet(snippet)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.concesionario))
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(gameId: String) =
            CarDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(CAR_ID, gameId)
                }
            }
    }
}
