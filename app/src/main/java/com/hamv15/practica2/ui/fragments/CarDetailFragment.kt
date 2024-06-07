package com.hamv15.practica2.ui.fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
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

private const val CAR_ID = "car_id"

class CarDetailFragment : Fragment() {

    private var _binding: FragmentCarDetailBinding? = null

    private val binding get() = _binding!!

    private var car_id: String? = null

    private lateinit var repository: CarRepository

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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Programar la conexión
        repository = (requireActivity().application as Practica2App).repository
        lifecycleScope.launch {
            car_id?.let {id ->
                //val call: Call<CarDetailDTO> = repository.getGameDetail(id)
                val call: Call<CarDetailDTO> = repository.getCarDetail(id)
                //Meterle media controller
                val mc = MediaController(requireContext())
                call.enqueue(object: Callback<CarDetailDTO> {
                    override fun onResponse(p0: Call<CarDetailDTO>, response: Response<CarDetailDTO>) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE
                            //Asignación de labels
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

                            //Fijar la imagen a traves de http
                            Glide.with(requireActivity())
                                .load(response.body()?.imagen)
                                .into(ivImage)
                            mc.setAnchorView(vvVideo)
                            vvVideo.setMediaController(mc)
                            vvVideo.setVideoURI(
                                Uri.parse(
                                    response.body()?.videoUrl
                                )
                            )
                            vvVideo.requestFocus()
                            vvVideo.setOnPreparedListener {
                                vvVideo.start()
                            }

                            // Agregar un listener para errores
                            vvVideo.setOnErrorListener { mp, what, extra ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error al reproducir el video",
                                    Toast.LENGTH_LONG
                                ).show()
                                true
                            }
                        }
                    }

                    override fun onFailure(p0: Call<CarDetailDTO>, p1: Throwable) {
                        //Manejar el error sin conexión
                        Toast.makeText(requireContext(),
                            getString(R.string.msjErrorGetCarDetail), Toast.LENGTH_LONG).show()
                    }

                })

            }
        }
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