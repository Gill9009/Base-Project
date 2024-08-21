package com.hypersoft.baseproject.ui.activities

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hypersoft.baseproject.BuildConfig
import com.hypersoft.baseproject.MainNavGraphDirections
import com.hypersoft.baseproject.R
import com.hypersoft.baseproject.commons.listeners.RapidSafeListener.setOnRapidClickSafeListener
import com.hypersoft.baseproject.databinding.ActivityMainBinding
import com.hypersoft.baseproject.commons.utils.CleanMemory
import com.hypersoft.baseproject.commons.utils.CleanMemory.isActivityRecreated
import com.hypersoft.baseproject.commons.utils.SettingUtils.feedback
import com.hypersoft.baseproject.commons.utils.SettingUtils.privacyPolicy
import com.hypersoft.baseproject.commons.utils.SettingUtils.rateUs
import com.hypersoft.baseproject.commons.utils.SettingUtils.shareApp
import com.hypersoft.baseproject.ui.activities.base.BaseActivity
import java.util.Locale

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     *  No need to setContentView()
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarMain)
        setUI()
        initNavController()
        initNavListener()
        initNavDrawerListeners()
    }

    private fun setUI() {
        binding.includeDrawer.tvNavVersionName.text =
            String.format(Locale.getDefault(), "Version ${BuildConfig.VERSION_NAME}")
    }

    private fun initNavController() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragmentContainer.id) as NavHostFragment).navController
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.fragmentHome), binding.drawerLayoutMain)
        //binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initNavListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentHome -> {
                    lockDrawer(false)
                    setToolbarIcon(R.drawable.ic_nav_option)
                }

                else -> {
                    lockDrawer(true)
                    setToolbarIcon(R.drawable.ic_nav_back)
                }
            }
        }
    }

    private fun lockDrawer(isLock: Boolean) {
        if (isLock) binding.drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else binding.drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun openDrawer() {
        binding.drawerLayoutMain.openDrawer(GravityCompat.START)
    }

    private fun setToolbarIcon(icon: Int) {
        binding.toolbarMain.setNavigationIcon(icon)
    }

    private fun initNavDrawerListeners() {
        binding.includeDrawer.navChangeLanguge.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            val action = MainNavGraphDirections.actionFragmentLanguage()
            navController.navigate(action)
        }

        binding.includeDrawer.navPrivacyPolicy.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            privacyPolicy()
        }

        binding.includeDrawer.navShareApp.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            shareApp()
        }

        binding.includeDrawer.navRateUs.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            rateUs()
        }

        binding.includeDrawer.navFeedback.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            feedback()
        }

        binding.includeDrawer.navUpdateApp.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            rateUs()
        }

        binding.includeDrawer.navRemoveAds.setOnRapidClickSafeListener {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            showToast("Action Remove Ads")
        }
    }

    fun homeBackPressed() {
        onBack()
    }

    override fun onBack() {
        super.onBack()
        if (binding.drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        if (navController.currentDestination?.id == R.id.fragmentHome) {
            navController.navigate(R.id.dialogExit)
        }
    }

    /**
     *  Call 'CleanMemory.clean()' to avoid memory leaks.
     *  This destroys all the resources
     */


    override fun onRecreate() {
        isActivityRecreated = true
        recreate()
    }

    override fun onDestroy() {
        if (!isActivityRecreated) {
            CleanMemory.clean()
        } else {
            isActivityRecreated = false
        }
        super.onDestroy()
    }
}