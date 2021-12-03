package ru.subnak.easybike.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.ActivityMainBinding
import ru.subnak.easybike.presentation.ui.fragments.UserFragment


class MainActivity : AppCompatActivity(), UserFragment.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //Cannot find NavController фикс
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)

        //Passing each menu ID as a set of Ids because each
        //menu should be considered as top level destinations.

        /*
        ActionBar включение/выключение
        val appBarConfiguration = AppBarConfiguration(
        setOf(
        R.id.navigation_map, R.id.navigation_statistic, R.id.navigationHistoryFragment, R.id.navigation_settings
        )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        */


        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_map,
                R.id.navigation_statistic,
                R.id.navigation_history,
                R.id.navigation_settings ->
                    navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }
    }

    override fun onEditingFinished() {
        finish()
    }


}