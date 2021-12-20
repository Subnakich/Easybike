package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentHistoryBinding
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.presentation.ui.adapters.JourneyListAdapter
import ru.subnak.easybike.presentation.ui.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var journeyListAdapter: JourneyListAdapter


    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        viewModel.journeyList.observe(viewLifecycleOwner) {
            journeyListAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvHistory) {
            journeyListAdapter = JourneyListAdapter()
            adapter = journeyListAdapter
        }
        setupLongClickListener()
        setupClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener() {
        journeyListAdapter.onJourneyClickListener = {
            runStatistic(it)
        }
    }

    private fun setupLongClickListener() {
        journeyListAdapter.onJourneyLongClickListener = {
            showMenuOfJourneyDialog(it)
        }
    }

    private fun runStatistic(journey: Journey) {
        findNavController().navigate(
            HistoryFragmentDirections.actionNavigationHistoryToNavigationStatistic(journey)
        )
    }

    private fun showMenuOfJourneyDialog(journey: Journey) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Journey menu")
            .setItems(R.array.dialog_menu) { d, k ->
                when (k) {
                    0 -> runStatistic(journey)
                    1 -> runStatistic(journey)
                    2 -> showDeleteJourneyDialog(journey)
                }
            }
            .create()
        dialog.show()
    }

    private fun showDeleteJourneyDialog(journey: Journey) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_the_journey))
            .setMessage(getString(R.string.sure_question_delete_hourney))
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(getString(R.string.positive_button_ok)) { _, _ ->
                viewModel.deleteJourney(
                    journey
                )
            }
            .setNegativeButton(getString(R.string.negative_button_cancel)) { d, _ -> d.cancel() }
            .create()

        dialog.show()
    }

}