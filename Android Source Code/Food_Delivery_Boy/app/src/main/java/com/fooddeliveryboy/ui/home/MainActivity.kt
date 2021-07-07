package com.fooddeliveryboy.ui.home

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.SideMenuModel
import com.fooddeliveryboy.response.CommonResponse
import com.fooddeliveryboy.ui.completed_order.CompletedOrderFragment
import com.fooddeliveryboy.ui.home.adapter.SideMenuAdapter
import com.fooddeliveryboy.ui.home.fragment.HomeFragment
import com.fooddeliveryboy.ui.profile.ProfileFragment
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import utils.TrackerService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        LanguagePrefs(this)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        //After instantiating your ActionBarDrawerToggle
        toggle.isDrawerIndicatorEnabled = false
        val drawable = ContextCompat.getDrawable(
            this,
            R.drawable.ic_menu
        )
        toggle.setHomeAsUpIndicator(drawable)
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent))

        Glide.with(this)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    this,
                    BaseURL.KEY_IMAGE
                )
            )
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(iv_header_logo)

        tv_header_name.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_NAME)
        tv_header_number.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE)

        val sideMenuModelList = ArrayList<SideMenuModel>()
        sideMenuModelList.add(SideMenuModel(resources.getString(R.string.profile), false, false))
        sideMenuModelList.add(
            SideMenuModel(
                resources.getString(R.string.recent_orders),
                false,
                false
            )
        )
        sideMenuModelList.add(
            SideMenuModel(
                resources.getString(R.string.completed_orders),
                false,
                false
            )
        )
        sideMenuModelList.add(
            SideMenuModel(
                resources.getString(R.string.on_duty),
                true,
                false,
                SessionManagement.UserData.getSession(this, "is_available") == "1"
            )
        )
        sideMenuModelList.add(SideMenuModel(resources.getString(R.string.logout), false, false))

        val sideMenuAdapter =
            SideMenuAdapter(this, sideMenuModelList, object : SideMenuAdapter.OnItemClickListener {
                override fun onClick(position: Int, sideMenuModel: SideMenuModel) {
                    setItemSelected(sideMenuModel)
                }
            })

        rv_side_menu.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = sideMenuAdapter
        }

        if (savedInstanceState == null) {
            loadHome()
        }

        mainViewModel.makeFirebaseSubscribe(this, true)

        tv_home_en.setOnClickListener(this)
        tv_home_ar.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        val languagePrefs = LanguagePrefs(this)
        when (v.id) {
            R.id.tv_home_en -> {
                languagePrefs.saveLanguage("en")
                languagePrefs.initLanguage("en")
                recreate()
            }
            R.id.tv_home_ar -> {
                languagePrefs.saveLanguage("ar")
                languagePrefs.initLanguage("ar")
                recreate()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fr = supportFragmentManager.findFragmentById(R.id.contentPanel)
            val fmName = fr?.javaClass!!.simpleName

            if (!fmName.contentEquals("HomeFragment")) {
                loadHome()
            } else {
                super.onBackPressed()
            }
        }
    }

    val fm_home = HomeFragment()
    private fun loadHome() {
        setHeaderTitle(resources.getString(R.string.app_name))

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.contentPanel, fm_home, "Home_fragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun setItemSelected(sideMenuModel: SideMenuModel) {
        when (sideMenuModel.title) {
            resources.getString(R.string.profile) -> {
                setHeaderTitle(sideMenuModel.title)

                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, ProfileFragment(), "")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            resources.getString(R.string.recent_orders) -> {
                loadHome()
            }
            resources.getString(R.string.completed_orders) -> {
                setHeaderTitle(sideMenuModel.title)

                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, CompletedOrderFragment(), "")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            resources.getString(R.string.on_duty) -> {
                val params = HashMap<String, String>()
                params["delivery_boy_id"] =
                    SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
                params["status"] = if (sideMenuModel.isChecked) "1" else "0"

                mainViewModel.makeAddAvailable(params)
                    .observe(this, Observer { response: CommonResponse? ->
                        if (response != null && response.responce!!) {
                            SessionManagement.UserData.setSession(
                                this,
                                "is_available",
                                response.data!!.toString()
                            )
                        }
                    })
            }
            resources.getString(R.string.logout) -> {
                val sessionManagement = SessionManagement(this)
                sessionManagement.logoutSessionLogin()
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun setHeaderTitle(title: String) {
        toolbar.title = title
    }

    override fun onResume() {
        super.onResume()
        if (LanguagePrefs.getLang(this) == "en") {
            tv_home_en.isSelected = true
            tv_home_ar.isSelected = false
            tv_home_en.setBackgroundResource(R.drawable.xml_view_rounded_green)
            tv_home_ar.setBackgroundResource(R.color.colorWhite)
        } else {
            tv_home_en.isSelected = false
            tv_home_ar.isSelected = true
            tv_home_en.setBackgroundResource(R.color.colorWhite)
            tv_home_ar.setBackgroundResource(R.drawable.xml_view_rounded_green)
        }
        if (BaseURL.ALLOW_LANGUAGE) {
            cv_home_language.visibility = View.VISIBLE
        } else {
            cv_home_language.visibility = View.GONE
        }
    }

    private val REQUEST_CHECK_SETTINGS = 0x1

    fun displayLocationSettingsRequest(context: Context) {
        mainViewModel.locationSettingUpdateLiveData.observe(this, Observer { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.i(
                        TAG, "All location settings are satisfied."
                    )
                    SessionManagement.PermanentData.setSession(
                        this@MainActivity,
                        "isServiceStart",
                        true
                    )
                    if (!CommonActivity.isMyServiceRunning(
                            this@MainActivity,
                            TrackerService::class.java
                        )
                    ) {
                        fm_home.startTrackerService()
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
            mainViewModel.locationSettingUpdateLiveData.removeObservers(this)
        })
        mainViewModel.checkLocationSettingsRequest(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                if (!CommonActivity.isMyServiceRunning(
                        this@MainActivity,
                        TrackerService::class.java
                    )
                ) {
                    fm_home.startTrackerService()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                displayLocationSettingsRequest(this)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
