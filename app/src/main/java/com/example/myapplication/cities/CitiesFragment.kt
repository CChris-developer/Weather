package com.example.myapplication.cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.dagger.App
import com.example.myapplication.databinding.FragmentCitiesBinding
import com.example.myapplication.utils.Consts
import com.example.myapplication.viewmodel.CitiesListViewModel

class CitiesFragment : Fragment() {
    private var _binding: FragmentCitiesBinding? = null
    private val binding: FragmentCitiesBinding
        get() = _binding!!

    private var citiesMap = mutableMapOf<String, String>()

    @Suppress("UNCHECKED_CAST")
    private val citiesListViewModel: CitiesListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CitiesListViewModel(App.component.getCitiesListRepository()) as T
            }
        }
    }
    private val adapter = CityAdapter { city -> onClickCity(city) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCitiesBinding.inflate(inflater)
        binding.progressBarCitiesList.visibility = View.VISIBLE
        val citiesList = citiesListViewModel.getAllCities()
        val citiesListForRecycleView = mutableListOf<String>()

        citiesList.forEach {
            citiesMap[it.name] = it.country
            citiesListForRecycleView.add("${it.name}, ${it.country}")
        }
        binding.citiesRecycler.adapter = adapter
        adapter.setData(citiesListForRecycleView)
        binding.progressBarCitiesList.visibility = View.GONE
        return binding.root
    }

    private fun onClickCity(cityName: String) {
        binding.progressBarCitiesList.visibility = View.VISIBLE
        binding.citiesRecycler.visibility = View.GONE
        val city = cityName.split(",")
        val bundle = Bundle().apply {
            putString(
                Consts.ARG_PARAM,
                "fromCitiesFrag&${city[0]}&${citiesMap[city[0]] ?: ""}"
            )
        }
        findNavController().navigate(
            R.id.action_citiesFragment2_to_selectedCityFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}