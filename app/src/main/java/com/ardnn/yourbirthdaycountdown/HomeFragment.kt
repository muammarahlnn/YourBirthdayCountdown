package com.ardnn.yourbirthdaycountdown

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.findNavController
import com.ardnn.yourbirthdaycountdown.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // get current date
        val calendar = Calendar.getInstance()
        var yearSelected: Int = calendar.get(Calendar.YEAR)
        var monthSelected: Int = calendar.get(Calendar.MONTH)
        var daySelected: Int = calendar.get(Calendar.DAY_OF_MONTH)

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
                view.findNavController().navigate(R.id.action_homeFragment_to_resultFragment)
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}