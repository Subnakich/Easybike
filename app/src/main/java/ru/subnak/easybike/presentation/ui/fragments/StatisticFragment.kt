package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentStatisticBinding
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.presentation.ui.viewmodels.StatisticViewModel
import ru.subnak.easybike.presentation.utils.Constants


class StatisticFragment : Fragment() {

    private val args by navArgs<StatisticFragmentArgs>()

    private lateinit var viewModel: StatisticViewModel


    private var _binding: FragmentStatisticBinding? = null
    private val binding: FragmentStatisticBinding
        get() =_binding ?: throw RuntimeException("FragmentStatisticBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[StatisticViewModel::class.java]
        viewModel.getUser(Constants.UNDEFINED_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val age = viewModel.user.value?.age
        val weight = viewModel.user.value?.weight
        val height = viewModel.user.value?.height
        val sex = viewModel.user.value?.sex
        var calories: Int? = null
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.sex) {
                "Male" -> calories = ((10 * it.weight + 6.25 * it.height - 5 * it.age + 5) * 1.2).toInt()
                "Female" -> calories = ((10 * it.weight + 6.25 * it.height - 5 * it.age - 161) * 1.2).toInt()
            }
            if (calories !== null) {
                binding.tvCaloriesStat.text = calories.toString()
            }
        })


        binding.tvSpeedStat.text = args.journey.speed.toString()
        binding.tvDistanceStat.text = args.journey.distance.toString()
        binding.tvDurationStat.text = args.journey.duration.toString()
        binding.ivMapStat.setImageBitmap(args.journey.img)
    }



}