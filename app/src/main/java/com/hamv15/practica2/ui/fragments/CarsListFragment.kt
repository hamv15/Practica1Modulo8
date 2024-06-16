package com.hamv15.practica2.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamv15.practica2.R
import com.hamv15.practica2.application.Practica2App
import com.hamv15.practica2.data.CarRepository
import com.hamv15.practica2.data.remote.model.CarDTO
import com.hamv15.practica2.databinding.FragmentCarsListBinding
import com.hamv15.practica2.ui.adapters.CarAdapter
import com.hamv15.practica2.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarsListFragment : Fragment() {

    private var _binding: FragmentCarsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: CarRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //El usuario ya ve el fragment en pantalla
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as Practica2App).repository

        lifecycleScope.launch {

            val call: Call<List<CarDTO>> = repository.getCars(Constants.URL_GET_CARS)

            call.enqueue(object : Callback<List<CarDTO>> {
                override fun onResponse(p0: Call<List<CarDTO>>, response: Response<List<CarDTO>>) {
                    //Respuesta del server
                    binding.pbLoading.visibility= View.GONE

                    Log.d(Constants.LOGTAG,
                        getString(R.string.log_respuesta_recibida, response.body()))

                    response.body()?.let {games ->
                        binding.rvCars.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = CarAdapter(games){car ->
                                //Aqui va la operación para el click de cada elemento
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, CarDetailFragment.newInstance(car.idAuto.toString()))
                                    .addToBackStack(null)
                                    .commit()
                            }
                            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.motor)
                            mediaPlayer.start()
                        }
                    }

                }

                override fun onFailure(p0: Call<List<CarDTO>>, error: Throwable) {
                    //Manejo del error
                    binding.pbLoading.visibility= View.GONE

                    //Mandar a llamar a un fragment para manejar el error de conexión
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_conn, error.message),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })


        }
    }


}