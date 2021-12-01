package ru.subnak.easybike.presentation.ui.backloaction

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentBackLocationBinding
import ru.subnak.easybike.presentation.utils.PermissionsUtility

@RequiresApi(Build.VERSION_CODES.Q)
class BackLocationFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentBackLocationBinding? = null
    private val binding: FragmentBackLocationBinding
        get() = _binding ?: throw RuntimeException("FragmentBackLocationBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackLocationBinding.inflate(inflater, container, false)

        setViewVisibility()

        return binding.root
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
            requestBackgroundPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        setViewVisibility()
        launchMapFragment()
    }

    private fun requestBackgroundPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.Q_access_permission_dialog),
            PERMISSION_REQUEST_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            launchMapFragment()
        }
        binding.buttonRequestPermissionQ.setOnClickListener {
            if (PermissionsUtility.hasLocationPermission(requireContext())) {
                launchMapFragment()
            } else {
                requestBackgroundPermission()
            }
        }
    }

    private fun launchMapFragment() {
        findNavController().navigate(R.id.action_navigation_background_permission_to_navigation_map)
    }

    private fun setViewVisibility() {
        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            binding.tvPermissionGrantedQ.visibility = View.VISIBLE
            binding.buttonRequestPermissionQ.visibility = View.GONE
        } else {
            binding.tvPermissionGrantedQ.visibility = View.GONE
            binding.buttonRequestPermissionQ.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (PermissionsUtility.hasLocationPermission(requireContext())) {
            launchMapFragment()
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