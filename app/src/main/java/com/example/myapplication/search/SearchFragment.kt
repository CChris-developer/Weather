package com.example.myapplication.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.dagger.App
import com.example.myapplication.cities.CityAdapter
import com.example.myapplication.utils.Consts.ARG_PARAM
import com.example.myapplication.utils.Consts.CITY_NAME
import com.example.myapplication.utils.Consts.DIALOG_REQUEST_KEY
import com.example.myapplication.utils.Consts.DIALOG_RESULT
import com.example.myapplication.utils.Consts.DIALOG_TAG
import com.example.myapplication.R
import com.example.myapplication.utils.State
import com.example.myapplication.utils.Utils.addCity
import com.example.myapplication.utils.Utils.citiesList
import com.example.myapplication.utils.Utils.cityCountryMap
import com.example.myapplication.utils.Utils.showAlert
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.models.CityInfo
import com.example.myapplication.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val adapter = CityAdapter { city -> onClickCity(city) }
    private val cityName = stringPreferencesKey(CITY_NAME)
    private var savedCityName = " "
    private lateinit var bundle: Bundle
    private var citiesCoordinatesMap = mutableMapOf<String, String>()
    private var cityFromDialog = ""
    private var dialog: SelectCityDialogFragment? = null

    private val dataStore = App.component.getPreferencesDataStore()

    @Suppress("UNCHECKED_CAST")
    private val searchViewModel: SearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(App.component.getSearchRepository()) as T
            }
        }
    }

    private fun readSavedCityName() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            dataStore.data.collectLatest {
                savedCityName = if (it[cityName] == null)
                    ""
                else
                    it[cityName].toString()
            }
        }
    }

    private suspend fun saveCityName(savingCityName: String) {
        dataStore.edit { settings ->
            settings[cityName] = savingCityName
        }
    }

    private fun onClickCity(city: String) {
        binding.searchString.setText(city)
        viewLifecycleOwner.lifecycleScope.launch {
            saveCityName(city)
            bundle = Bundle().apply {
                putString(
                    ARG_PARAM,
                    "fromSearchFrag&${city}&${cityCountryMap[city]?.trim()}"
                )
            }
            findNavController().navigate(
                R.id.action_searchFragment2_to_selectedCityFragment,
                bundle
            )
        }
    }

    private fun selectCity(cityList: List<CityInfo>, isFromNet: Boolean): Array<String> {
        var citiesArray = arrayOf("")
        citiesCoordinatesMap = mutableMapOf()
        citiesArray = citiesArray.drop(1).toTypedArray()
        cityList.forEach {
            citiesArray += "${it.name}, ${it.country}"
            if (isFromNet)
                citiesCoordinatesMap["${it.name}, ${it.country}"] =
                    "fromNet&${it.name}&${it.country ?: ""}&${it.timeZone ?: ""}&${it.latitude ?: 0.0f}&${it.longitude ?: 0.0f}"
            else
                citiesCoordinatesMap["${it.name}, ${it.country}"] =
                    "fromSearchFrag&${it.name}&${it.country ?: ""}"
        }
        return citiesArray
    }

    private fun transitToSelectedCityFrag(cityFromDialog: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val splittedCitiesArrString =
                cityFromDialog.split(", ")
            savedCityName =
                splittedCitiesArrString[0]
            val countryName =
                splittedCitiesArrString[1]
            saveCityName(savedCityName)
            addCity(
                savedCityName,
                countryName
            )
            bundle = Bundle().apply {
                putString(
                    ARG_PARAM,
                    citiesCoordinatesMap[cityFromDialog]
                )
            }
            findNavController().navigate(
                R.id.action_searchFragment2_to_selectedCityFragment,
                bundle
            )
        }
    }

    private fun checkDataFromDialog(cityFromDialog: String) {
        if (cityFromDialog != "") {
            binding.textInputLayout.visibility = View.INVISIBLE
            binding.lastCitiesRecycler.visibility = View.INVISIBLE
            transitToSelectedCityFrag(cityFromDialog)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.searchString.isEnabled = true
        }
    }

    private fun oneCityWeatherSearch(cityList: List<CityInfo>, isFromNet: Boolean) {
        Log.d("oneCityfun", "onecityfun")
        viewLifecycleOwner.lifecycleScope.launch {
            saveCityName(cityList.first().name)
            addCity(cityList.first().name, cityList.first().country)
            val str: String
            if (isFromNet)
                str =
                    "fromNet&${cityList.first().name}&${cityList.first().country ?: ""}&${cityList.first().timeZone ?: ""}&${cityList.first().latitude ?: 0.0f}&${cityList.first().longitude ?: 0.0f}"
            else
                str = "fromSearchFrag&${cityList.first().name}&${cityList.first().country ?: ""}"
            bundle = Bundle().apply {
                putString(
                    ARG_PARAM,
                    str
                )
            }

            findNavController().navigate(
                R.id.action_searchFragment2_to_selectedCityFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        readSavedCityName()
        while (savedCityName == " ") {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
            }
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.searchString.setText(savedCityName)
        val lastCitiesList = citiesList
        binding.lastCitiesRecycler.adapter = adapter
        adapter.setData(lastCitiesList)
        binding.progressBar.visibility = View.GONE
        binding.textInputLayout.setStartIconOnClickListener {
            binding.searchString.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewLifecycleOwner.lifecycleScope.launch {
                delay(300)
                if (binding.searchString.text.toString() != "") {
                    val cityFromDb =
                        searchViewModel.getCity(binding.searchString.text.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                    if (cityFromDb.isEmpty()) {
                        searchViewModel.state.collectLatest { state ->
                            when (state) {
                                is State.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.searchString.isEnabled = true
                                    showAlert(
                                        state.errorMessage.toString(),
                                        requireContext()
                                    )
                                }

                                is State.NoData -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.searchString.isEnabled = true
                                    showAlert(
                                        state.noDataMessage.toString(),
                                        requireContext()
                                    )
                                }

                                State.Success -> {
                                    if (searchViewModel.cityFromNet.size > 1) {
                                        dialog = SelectCityDialogFragment.newInstance(
                                            DIALOG_REQUEST_KEY,
                                            selectCity(searchViewModel.cityFromNet, true)
                                        )
                                        dialog!!.show(
                                            childFragmentManager,
                                            DIALOG_TAG
                                        )
                                        childFragmentManager.setFragmentResultListener(
                                            DIALOG_REQUEST_KEY, this@SearchFragment
                                        ) { _, result ->
                                            cityFromDialog =
                                                result.getString(DIALOG_RESULT, null)
                                            checkDataFromDialog(cityFromDialog)
                                        }
                                    } else {
                                        oneCityWeatherSearch(
                                            searchViewModel.cityFromNet,
                                            true
                                        )
                                    }
                                }
                            }
                        }
                    } else if (cityFromDb.size > 1) {
                        dialog = SelectCityDialogFragment.newInstance(
                            DIALOG_REQUEST_KEY,
                            selectCity(cityFromDb, false)
                        )
                        dialog!!.show(
                            childFragmentManager,
                            DIALOG_TAG
                        )
                        childFragmentManager.setFragmentResultListener(
                            DIALOG_REQUEST_KEY, this@SearchFragment
                        ) { _, result ->
                            cityFromDialog =
                                result.getString(DIALOG_RESULT, null)
                            checkDataFromDialog(cityFromDialog)
                        }
                    } else {
                        oneCityWeatherSearch(cityFromDb, false)
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.searchString.isEnabled = true
                    showAlert(getString(R.string.enter_city), requireContext())
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dialog = null
    }
}