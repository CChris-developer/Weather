package com.example.myapplication.search


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.myapplication.utils.Consts.DIALOG_CITY_LIST
import com.example.myapplication.utils.Consts.DIALOG_REQUEST_KEY
import com.example.myapplication.utils.Consts.DIALOG_RESULT
import com.example.myapplication.R


class SelectCityDialogFragment : DialogFragment() {

    private var foundCity = ""

    companion object {

        fun newInstance(requestKey: String, cityList: Array<String>): SelectCityDialogFragment {
            val frag = SelectCityDialogFragment()
            val args = Bundle()
            args.putStringArray(DIALOG_CITY_LIST, cityList)
            args.putString(DIALOG_REQUEST_KEY, requestKey)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cityList = arguments?.getStringArray(DIALOG_CITY_LIST)
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.many_cities)
            .setNegativeButton(
                R.string.cancel
            ) { dialog, _ ->
                parentFragmentManager.setFragmentResult(
                    arguments?.getString(DIALOG_REQUEST_KEY).toString(),
                    bundleOf(DIALOG_RESULT to foundCity)
                )
                dialog.cancel()
            }
            .setSingleChoiceItems(
                cityList, -1
            ) { dialog, which ->
                foundCity = cityList?.get(which) ?: ""
                parentFragmentManager.setFragmentResult(
                    arguments?.getString(DIALOG_REQUEST_KEY).toString(),
                    bundleOf(DIALOG_RESULT to foundCity)
                )
                dialog.dismiss()
            }
            .create()
    }
}