package ru.subnak.easybike.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentUserBinding
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.presentation.ui.viewmodels.UserViewModel

class UserFragment : Fragment() {

    private val args by navArgs<UserFragmentArgs>()

    private lateinit var viewModel: UserViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentUserBinding? = null
    private val binding: FragmentUserBinding
    get() =_binding ?: throw RuntimeException("FragmentUserBinding == null")

    private var userId: Int = User.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //parseArgs()
    }

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
            onEditingFinishedListener.onEditingFinished()
        }
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }

    private fun launchRightMode(screenMode: String) {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD  -> launchAddMode()
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
                1
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
                1
            )
        }
    }

    private fun launchMapFragment() {
        findNavController().navigate(R.id.action_navigation_user_to_navigation_map)
    }

//    private fun parseArgs() {
//        val args = requireArguments()
//        if (!args.containsKey(FRAGMENT_MODE)) {
//            throw RuntimeException("Param screen mode is absent")
//        }
//        val mode = args.getString(FRAGMENT_MODE)
//        if (mode != MODE_EDIT && mode != MODE_ADD) {
//            throw RuntimeException("Unknown screen mode $mode")
//        }
//        screenMode = mode
//        if (screenMode == MODE_EDIT) {
//            if (!args.containsKey(USER_ITEM_ID)) {
//                throw RuntimeException("Param shop item id is absent")
//            }
//            userId = args.getInt(USER_ITEM_ID, User.UNDEFINED_ID)
//        }
//    }

    companion object {

        private const val FRAGMENT_MODE = "extra_screen_mode"
        const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        private const val USER_ITEM_ID = "extra_user_id"

        fun newInstanceAddUser(): UserFragment {
            return UserFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAGMENT_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditUser(userId: Int): UserFragment {
            return UserFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAGMENT_MODE, MODE_EDIT)
                    putInt(USER_ITEM_ID, userId)
                }
            }
        }

    }

}