package ru.subnak.easybike.presentation.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentWelcomeBinding
import ru.subnak.easybike.presentation.utils.PermissionsUtility


class WelcomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("WelcomeFragmentBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        setViewVisibility()

        return binding.root
    }

    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.cant_work),
            PERMISSION_REQUEST_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog
                .Builder(requireActivity())
                .positiveButtonText(R.string.positive_button_ok)
                .negativeButtonText(R.string.negative_button_cancel)
                .build()
                .show()
        } else {
            requestLocationPermissions()
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                setViewVisibility()
                launchBackLocationPermission()
            }
        } else {
            setViewVisibility()
            launchUserCreateFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            launchUserCreateFragment()
        }
        binding.buttonRequestPermission.setOnClickListener {
            if (PermissionsUtility.hasLocationPermission(requireContext())) {
                launchUserCreateFragment()
            } else {
                requestLocationPermissions()
            }
        }
    }

    private fun launchBackLocationPermission() {
        findNavController()
            .navigate(R.id.action_navigation_welcome_to_navigation_background_permission)
    }

    private fun launchUserCreateFragment() {
        findNavController()
            .navigate(R.id.action_navigation_welcome_to_navigation_user_create)
    }

    private fun setViewVisibility() {
        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            binding.tvPermissionGranted.visibility = View.VISIBLE
            binding.buttonRequestPermission.visibility = View.GONE
        } else {
            binding.tvPermissionGranted.visibility = View.GONE
            binding.buttonRequestPermission.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            launchUserCreateFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val PERMISSION_REQUEST_LOCATION = 100
    }

}

