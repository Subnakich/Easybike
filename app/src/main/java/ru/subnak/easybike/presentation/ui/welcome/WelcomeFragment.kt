package ru.subnak.easybike.presentation.ui.welcome

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentWelcomeBinding


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

    private fun hasLocationPermission() =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        } else {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestBackgroundPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.Q_access_permission_dialog),
            PERMISSION_REQUEST_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

    }

    private fun requestLocationPermission() {
        if (hasLocationPermission()) {
            return
        }
        requestLocationPermissions()
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
            requestLocationPermission()
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
                requestBackgroundPermission()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                setViewVisibility()
                launchMapFragment()
            } else {
                binding.buttonRequestPermission.setText(R.string.full_time_access)
            }
        } else {
            setViewVisibility()
            launchMapFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasLocationPermission()) {
            launchMapFragment()
        }
        binding.buttonRequestPermission.setOnClickListener {
            if (hasLocationPermission()) {
                launchMapFragment()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun launchMapFragment() {
        findNavController().navigate(R.id.action_navigation_welcome_to_navigation_map)
    }

    private fun setViewVisibility() {
        if (hasLocationPermission()) {
            binding.tvPermissionGranted.visibility = View.VISIBLE
            binding.buttonRequestPermission.visibility = View.GONE
        } else {
            binding.tvPermissionGranted.visibility = View.GONE
            binding.buttonRequestPermission.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            launchMapFragment()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                binding.buttonRequestPermission.setText(R.string.full_time_access)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val PERMISSION_REQUEST_LOCATION = 100


        fun newInstance() = WelcomeFragment()
    }

}

