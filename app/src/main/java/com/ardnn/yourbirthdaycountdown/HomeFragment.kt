package com.ardnn.yourbirthdaycountdown

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.findNavController
import com.ardnn.yourbirthdaycountdown.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentDate: IntArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // get current date as initial value for selected date
        currentDate = getCurrentDate() // [day, month, year]
        var daySelected = currentDate[0]
        var monthSelected = currentDate[1]
        var yearSelected = currentDate[2]

        with(binding) {
            etYourDob.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireActivity(),
                    { _, year, month, day ->
                        // update selected date
                        yearSelected = year
                        monthSelected = month
                        daySelected = day

                        // set edit text
                        val fixedMonth = month + 1
                        val date = "$day/$fixedMonth/$year"
                        etYourDob.setText(date)
                    },
                yearSelected, monthSelected, daySelected)
                datePickerDialog.show()
            }

            btnSubmit.setOnClickListener { view ->
                // check if user born in the future
                val bornInTheFuture = isUserBornInTheFuture(daySelected, monthSelected, yearSelected)
                
                if (bornInTheFuture) {
                    // tell the user that he/she inputted future date
                    Toast.makeText(activity, "You are not born yet", Toast.LENGTH_SHORT).show()
                } else {
                    // to result section
                    view.findNavController().navigate(R.id.action_homeFragment_to_resultFragment)
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentDate(): IntArray {
        val calendar = Calendar.getInstance()
        return intArrayOf(
            calendar.get(Calendar.DAY_OF_MONTH), // day
            calendar.get(Calendar.MONTH), // month
            calendar.get(Calendar.YEAR), // year
        )
    }

    private fun isUserBornInTheFuture(day: Int, month: Int, year: Int): Boolean {
        // first get current date
        val dayNow = currentDate[0]
        val monthNow = currentDate[1]
        val yearNow = currentDate[2]

        // check if born in the future
        if (year > yearNow) {
            return true
        } else if (year == yearNow) {
            if (month > monthNow) {
                return true
            } else if (month == monthNow) {
                if (day > dayNow) {
                    return true
                }
            }
        }
        return false
    }
}