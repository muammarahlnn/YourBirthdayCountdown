package com.ardnn.yourbirthdaycountdown

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ardnn.yourbirthdaycountdown.databinding.FragmentResultBinding
import java.util.*
import kotlin.math.floor

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        // get args
        val name = ResultFragmentArgs.fromBundle(arguments as Bundle).name
        val selectedDate = // format: dd/mm/yyyy
            ResultFragmentArgs.fromBundle(arguments as Bundle).selectedDate

        // get user date of birth
        val userDob = selectedDate.split("/")
        val userDay = userDob[0].toInt()
        val userMonth = userDob[1].toInt() - 1 // -1 cause the format starts from 0
        var userYear = userDob[2].toInt()

        // check if user birthday already passed
        val isBirthdayPassed = isBirthdayAlreadyPassed(userDay, userMonth)

        // get user age
        var currentYear = HomeFragment.currentDate[2]
        if (isBirthdayPassed) currentYear++ // if already passed then user next year
        val userAge = currentYear  - userYear

        // set title
        val title = "$name's ${addOrdinal(userAge)} Birthday Countdown"
        binding.tvTitle.text = title

        // get user next birthday timestamp
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, userMonth, userDay, 0, 0, 0)
        val birthdayTimestamp = calendar.timeInMillis

        // timer that run every one second
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    birthdayCountdown(birthdayTimestamp)
                }
            }

        }, 0, 1000)


        // if button clicked
        binding.btnRefresh.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_resultFragment_to_homeFragment)
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isBirthdayAlreadyPassed(userDay: Int, userMonth: Int): Boolean {
        // get current date
        val currentDate: IntArray = HomeFragment.currentDate // [day, month, year]
        val currentDay = currentDate[0]
        val currentMonth = currentDate[1]

        // check if birthday is not passed yet
        if (userMonth > currentMonth) {
            return false
        } else if (userMonth == currentMonth) {
            if (userDay > currentDay) {
                return false
            }
        }
        return true
    }

    private fun addOrdinal(num: Int): String {
        return when (num % 10) {
            1 -> "${num}st"
            2 -> "${num}nd"
            3 -> "${num}rd"
            else -> "${num}th"
        }
    }

    private fun birthdayCountdown(birthdayTimestamp: Long) {
        // get current timestamp
        val nowTimestamp = Date().time

        // get user next birthday countdown timestamp
        val countdownTimestamp = birthdayTimestamp - nowTimestamp
        val daysCountdown = floor((countdownTimestamp / 1000 / 3600 / 24).toDouble())
        val hoursCountdown = floor((countdownTimestamp / 1000 / 3600 % 24).toDouble())
        val minutesCountdown = floor((countdownTimestamp / 1000 / 60 % 60).toDouble())
        val secondsCountdown = floor((countdownTimestamp / 1000 % 60).toDouble())

        // debug
//        Log.d("ResultFragment", "Now $nowTimestamp")
//        Log.d("ResultFragment", "Now ${Date(nowTimestamp)}")
//        Log.d("ResultFragment", "Birthday $birthdayTimestamp")
//        Log.d("ResultFragment", "Birthday ${Date(birthdayTimestamp)}")
//        Log.d("ResultFragment", "Birthday $countdownTimestamp")

        // set to widgets
        with (binding) {
            tvCountdownDays.text = daysCountdown.toInt().toString()
            tvCountdownHours.text = hoursCountdown.toInt().toString()
            tvCountdownMinutes.text = minutesCountdown.toInt().toString()
            tvCountdownSeconds.text = secondsCountdown.toInt().toString()
        }
    }

}