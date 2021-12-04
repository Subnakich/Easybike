package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentUserCreateBinding
import ru.subnak.easybike.presentation.ui.viewmodels.MainViewModel
import ru.subnak.easybike.presentation.utils.Constants

class UserCreateFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()


    private var _binding: FragmentUserCreateBinding? = null
    private val binding: FragmentUserCreateBinding
        get() = _binding ?: throw RuntimeException("FragmentUserCreateBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserCreateBinding.inflate(inflater, container, false)
        setViewVisibility(false)
        viewModel.userCheck.observe(viewLifecycleOwner, Observer {
            when (viewModel.userCheck.value) {
                1 -> launchMapFragment()
                0 -> {
                    setViewVisibility(true)
                    binding.buttonUserCreate.setOnClickListener {
                        launchUserCreate()
                    }
                }
                else -> throw RuntimeException("isCreated == null")
            }
        })

        return binding.root
    }

    private fun setViewVisibility(visible: Boolean) {
        if (visible) {
            binding.tvTitleUserCreateFragment.visibility = View.VISIBLE
            binding.tvDescriptionUserCreateFragment.visibility = View.VISIBLE
            binding.buttonUserCreate.visibility = View.VISIBLE
        } else {
            binding.tvTitleUserCreateFragment.visibility = View.GONE
            binding.tvDescriptionUserCreateFragment.visibility = View.GONE
            binding.buttonUserCreate.visibility = View.GONE
        }
    }

    private fun launchUserCreate() {
        findNavController().navigate(
            UserCreateFragmentDirections.actionNavigationUserCreateToNavigationUser(Constants.MODE_ADD)
        )
    }

    private fun launchMapFragment() {
        findNavController().navigate(
            R.id.action_navigation_user_create_to_navigation_map
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

