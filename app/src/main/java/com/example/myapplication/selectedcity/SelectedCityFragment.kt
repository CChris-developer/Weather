package com.example.myapplication.selectedcity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.dagger.App
import com.example.myapplication.utils.Consts.ARG_PARAM
import com.example.myapplication.R
import com.example.myapplication.utils.State
import com.example.myapplication.utils.Utils.getCurrentDateOfSpecificTimezone
import com.example.myapplication.utils.Utils.showAlert
import com.example.myapplication.databinding.FragmentSelectedCityBinding
import com.example.myapplication.models.Forecast
import com.example.myapplication.viewmodel.CityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SelectedCityFragment : Fragment() {
    private var _binding: FragmentSelectedCityBinding? = null
    private val binding: FragmentSelectedCityBinding
        get() = _binding!!
    private var passedValue: String? = null
    private lateinit var cityWeatherList: List<Forecast>
    private val forecastAdapter = ForecastAdapter()

    @Suppress("UNCHECKED_CAST")
    private val cityViewModel: CityViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CityViewModel(App.component.getCityRepository()) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedCityBinding.inflate(inflater)
        binding.progressBarSelCity.visibility = View.VISIBLE
        arguments?.let {
            passedValue = it.getString(ARG_PARAM)
        }
        if (passedValue != "") {
            val valuesList = passedValue?.split("&")
            binding.cityName.text = getString(R.string.geo, valuesList!![1], valuesList[2])
            if (valuesList[0] == "fromNet")
                cityWeatherList = listOf(
                    cityViewModel.getCityWeatherFromNet(
                        valuesList[1],
                        valuesList[2],
                        valuesList[4].toFloat(),
                        valuesList[5].toFloat(),
                        valuesList[3]
                    )!!
                )
            else {
                cityWeatherList = cityViewModel.getCityWeatherFromDb(valuesList[1], valuesList[2])
                val cityInfo = cityViewModel.getCityInfo(valuesList[1], valuesList[2])
                if (cityInfo != null) {
                    val currentDate = getCurrentDateOfSpecificTimezone(cityInfo.timeZone)
                    val currentDateList = cityWeatherList.filter { it.time == currentDate }
                    if (currentDateList.isEmpty()) {
                        cityWeatherList = cityViewModel.updateWeather(
                            valuesList[1],
                            valuesList[2],
                            cityInfo.latitude,
                            cityInfo.longitude,
                            cityInfo.timeZone
                        )
                    }
                }
            }
            binding.progressBarSelCity.visibility = View.GONE
            binding.forecastRecycler.adapter = forecastAdapter
            forecastAdapter.setData(cityWeatherList)
            viewLifecycleOwner.lifecycleScope.launch {
                cityViewModel.state.collectLatest { state ->
                    when (state) {
                        is State.Error -> {
                            showAlert(state.errorMessage.toString(), requireContext())
                        }

                        is State.NoData -> {
                            showAlert(state.noDataMessage.toString(), requireContext())
                        }

                        State.Success -> {
                        }
                    }
                }
            }
            binding.deleteCityButton.setOnClickListener {
                binding.progressBarSelCity.visibility = View.VISIBLE
                cityViewModel.deleteCity(valuesList[1], valuesList[2])
                if (cityViewModel.isDeleted) {
                    if (valuesList[0] == "fromSearchFrag" || valuesList[0] == "fromNet")
                        findNavController().navigate(
                            R.id.action_selectedCityFragment_to_searchFragment2
                        )
                    else
                        findNavController().navigate(
                            R.id.action_selectedCityFragment_to_citiesFragment2
                        )
                }
            }

            binding.backButton.setOnClickListener {
                if (valuesList[0] == "fromSearchFrag" || valuesList[0] == "fromNet")
                    findNavController().navigate(
                        R.id.action_selectedCityFragment_to_searchFragment2
                    )
                else
                    findNavController().navigate(
                        R.id.action_selectedCityFragment_to_citiesFragment2
                    )
            }
        } else {
            showAlert(getString(R.string.no_data), requireContext())
            binding.deleteCityButton.visibility = View.GONE
            binding.backButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_selectedCityFragment_to_searchFragment2
                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}