package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentUserBinding
import ru.subnak.easybike.presentation.ui.viewmodels.UserViewModel
import ru.subnak.easybike.presentation.utils.Constants

class UserFragment : Fragment() {

    private val args by navArgs<UserFragmentArgs>()

    private lateinit var viewModel: UserViewModel

    private var _binding: FragmentUserBinding? = null
    private val binding: FragmentUserBinding
    get() =_binding ?: throw RuntimeException("FragmentUserBinding == null")

    private var userId: Int = Constants.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode(args.mode)
        observeViewModel()
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputAge()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputWeight()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputHeight()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.rgSex.setOnCheckedChangeListener { _, _ -> viewModel.resetErrorInputSex() }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener()
        }
    }

    private fun onEditingFinishedListener() {
        launchMapFragment()
    }

    private fun launchRightMode(screenMode: String) {
        when (screenMode) {
            Constants.MODE_EDIT -> launchEditMode()
            Constants.MODE_ADD  -> launchAddMode()
        }
    }

    private fun getSex(id: Int): String {
        return when (id) {
            binding.rbMale.id -> "Male"
            binding.rbFemale.id -> "Female"
            else -> throw RuntimeException("Sex == null")
        }
    }

    private fun launchEditMode() {
        viewModel.getUser(userId)
        binding.saveButton.setOnClickListener {
            viewModel.editUser(
                binding.etName.text?.toString(),
                binding.etAge.text?.toString(),
                binding.etWeight.text?.toString(),
                binding.etHeight.text?.toString(),
                getSex(binding.rgSex.checkedRadioButtonId),
                Constants.UNDEFINED_ID
            )
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addUser(
                binding.etName.text?.toString(),
                binding.etAge.text?.toString(),
                binding.etWeight.text?.toString(),
                binding.etHeight.text?.toString(),
                getSex(binding.rgSex.checkedRadioButtonId),
                Constants.UNDEFINED_ID
            )
        }
    }

    private fun launchMapFragment() {
        findNavController().navigate(R.id.action_navigation_user_to_navigation_map)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}