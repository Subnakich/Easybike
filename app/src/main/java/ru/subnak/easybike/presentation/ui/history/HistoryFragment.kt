package ru.subnak.easybike.presentation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentHistoryBinding
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.presentation.JourneyListAdapter

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }


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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupLongClickListener() {
        journeyListAdapter.onJourneyLongClickListener = {
            showDeleteJourneyDialog(it)
        }
    }

    private fun showDeleteJourneyDialog(journey: Journey) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel the Journey")
            .setMessage("Are you sure you want to cancel this journey")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _,_ -> viewModel.deleteJourney(journey) }
            .setNegativeButton("No") { d,_ -> d.cancel() }
            .create()

        dialog.show()
    }

}