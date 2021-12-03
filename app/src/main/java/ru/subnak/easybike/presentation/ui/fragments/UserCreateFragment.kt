package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentUserCreateBinding
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.presentation.ui.viewmodels.UserCreateViewModel

class UserCreateFragment : Fragment() {


    private lateinit var viewModel: UserCreateViewModel

    private var _binding: FragmentUserCreateBinding? = null
    private val binding: FragmentUserCreateBinding
        get() = _binding ?: throw RuntimeException("FragmentUserCreateBinding == null")

    private var isCreated: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserCreateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UserCreateViewModel::class.java]
        viewModel.checkUser(User.UNDEFINED_ID)
        viewModel.userCheck.observe(viewLifecycleOwner, Observer {

            isCreated = viewModel.userCheck.value
            when (isCreated) {
                1 -> launchMapFragment()
                0 -> {
                    binding.buttonUserCreate.setOnClickListener {
                        launchUserCreate()
                    }
                }
                else -> throw RuntimeException("isCreated == null")
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun launchUserCreate() {
        findNavController().navigate(
            UserCreateFragmentDirections.actionNavigationUserCreateToNavigationUser(UserFragment.MODE_ADD)
        )
    }

    private fun launchMapFragment() {
        findNavController().navigate(
            R.id.action_navigation_user_create_to_navigation_map
        )
    }

}

