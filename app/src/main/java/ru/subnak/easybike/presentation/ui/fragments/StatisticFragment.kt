package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentStatisticBinding
import ru.subnak.easybike.presentation.ui.viewmodels.StatisticViewModel


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StatisticViewModel::class.java]
        binding.tvSpeedStat.text = args.journey.speed.toString()
        binding.tvDistanceStat.text = args.journey.distance.toString()
        binding.tvDurationStat.text = args.journey.duration.toString()
        binding.ivMapStat.setImageBitmap(args.journey.img)
    }

}