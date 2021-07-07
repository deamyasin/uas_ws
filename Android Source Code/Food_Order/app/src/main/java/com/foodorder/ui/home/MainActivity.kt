package com.foodorder.ui.home

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.foodorder.BuildConfig
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.models.SideMenuModel
import com.foodorder.ui.checkout.CheckoutActivity
import com.foodorder.ui.cart.CartFragment
import com.foodorder.ui.contact_us.ContactUsActivity
import com.foodorder.ui.home.adapter.SideMenuAdapter
import com.foodorder.ui.home.dialog.ChooseLanguageDialog
import com.foodorder.ui.home.fragment.HomeFragment
import com.foodorder.ui.login.LoginActivity
import com.foodorder.ui.menu.MenuFragment
import com.foodorder.ui.menu_item.MenuItemActivity
import com.foodorder.ui.my_order.MyOrderActivity
import com.foodorder.ui.order_detail.OrderDetailActivity
import com.foodorder.ui.profile.ProfileActivity
import com.foodorder.ui.track_order.TrackOrderFragment
import com.foodorder.ui.webview.CommonWebViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var menu_delete: MenuItem? = null
    }

    var sideMenuModelList = ArrayList<SideMenuModel>()
    lateinit var sideMenuAdapter: SideMenuAdapter

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonActivity.changeStatusBarColor(this, true)
        CommonActivity.setTransparentStatusBar(this)
        LanguagePrefs(this)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
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

        tv_drawer_version.text =
            "${resources.getString(R.string.version)} ${BuildConfig.VERSION_NAME}"

        sideMenuAdapter =
            SideMenuAdapter(this, sideMenuModelList, object : SideMenuAdapter.OnItemClickListener {
                override fun onClick(position: Int, sideMenuModel: SideMenuModel) {
                    sideMenuSelection(sideMenuModel)
                }
            })
        rv_drawer_menu.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = sideMenuAdapter
        }

        updateSideMenu()

        tv_header_name.setOnClickListener(this)
        tv_header_number.setOnClickListener(this)
        cl_bottom_home.setOnClickListener(this)
        cl_bottom_menu.setOnClickListener(this)
        cl_bottom_cart.setOnClickListener(this)
        cl_bottom_order.setOnClickListener(this)

        if (savedInstanceState == null) {
            loadHome()
        }

        mainViewModel.makeFirebaseSubscribe(this, SessionManagement.UserData.isLogin(this))

        if (intent.hasExtra("orderData")) {
            val orderModel = intent.getSerializableExtra("orderData") as OrderModel
            Intent(this, OrderDetailActivity::class.java).apply {
                putExtra("orderData", orderModel as Serializable)
                startActivity(this)
            }
        }

    }

    override fun onClick(v: View?) {
        val fr = supportFragmentManager.findFragmentById(R.id.contentPanel)
        val fmName = fr?.javaClass!!.simpleName
        var fm: Fragment? = null

        when (v?.id) {
            R.id.tv_header_name, R.id.tv_header_number -> {
                headerLoginClick()
            }
            R.id.cl_bottom_home -> {
                homeClick(fmName)
            }
            R.id.cl_bottom_menu -> {
                fm = menuClick(fmName)
            }
            R.id.cl_bottom_cart -> {
                fm = cartClick(fmName)
            }
            R.id.cl_bottom_order -> {
                fm = trackOrderClick(fmName)
            }
        }
        if (fm != null) {
            setFragment(fm, true)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fr = supportFragmentManager.findFragmentById(R.id.contentPanel)
            val fmName = fr?.javaClass!!.simpleName
            if (fmName == "HomeFragment") {
                finish()
            } else {
                loadHome()
            }
            //super.onBackPressed()
        }
    }

    private fun headerLoginClick() {
        if (!SessionManagement.UserData.isLogin(this)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            Intent(this, LoginActivity::class.java).apply {
                putExtra("isFinish", true)
                startActivityForResult(this, 6892)
            }
        }
    }

    private fun homeClick(fmName: String) {
        if (fmName != "HomeFragment")
            loadHome()
        setBottomSelection(1)
    }

    private fun menuClick(fmName: String): Fragment? {
        setBottomSelection(2)
        setHeaderTitle(resources.getString(R.string.menu))
        if (fmName != "MenuFragment")
            return MenuFragment()
        return null
    }

    private fun cartClick(fmName: String): Fragment? {
        if (SessionManagement.UserData.isLogin(this)) {
            setBottomSelection(3)
            setHeaderTitle(resources.getString(R.string.cart))
            if (fmName != "CartFragment")
                return CartFragment()
        } else {
            Intent(this, LoginActivity::class.java).apply {
                putExtra("isFinish", true)
                startActivityForResult(this, 6892)
            }
        }
        return null
    }

    private fun trackOrderClick(fmName: String): Fragment? {
        if (SessionManagement.UserData.isLogin(this)) {
            setBottomSelection(4)
            setHeaderTitle(resources.getString(R.string.track_orders))
            if (fmName != "TrackOrderFragment")
                return TrackOrderFragment()
        } else {
            Intent(this, LoginActivity::class.java).apply {
                putExtra("isFinish", true)
                startActivityForResult(this, 6892)
            }
        }
        return null
    }

    private fun updateSideMenu() {

        sideMenuModelList.clear()

        if (SessionManagement.UserData.isLogin(this)) {

            tv_header_name.text =
                SessionManagement.UserData.getSession(this, BaseURL.KEY_NAME).trim()
            tv_header_number.text =
                SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE).trim()

            val userImg = SessionManagement.UserData.getSession(this, BaseURL.KEY_IMAGE)
            Glide.with(this)
                .load(BaseURL.IMG_PROFILE_URL + userImg)
                .placeholder(R.color.colorGray)
                .error(R.color.colorGray)
                .into(iv_header_img)

            sideMenuModelList.add(
                SideMenuModel(
                    R.drawable.ic_user,
                    resources.getString(R.string.profile),
                    "",
                    isEnable = false,
                    isSelected = true
                )
            )
            sideMenuModelList.add(
                SideMenuModel(
                    R.drawable.ic_my_order,
                    resources.getString(R.string.my_orders),
                    "",
                    isEnable = false,
                    isSelected = false
                )
            )
        } else {
            tv_header_name.text = resources.getString(R.string.login)
            tv_header_number.text = resources.getString(R.string.now)
        }
        sideMenuModelList.add(
            SideMenuModel(
                R.drawable.ic_call,
                resources.getString(R.string.contact_us),
                "",
                isEnable = false,
                isSelected = false
            )
        )
        sideMenuModelList.add(
            SideMenuModel(
                R.drawable.ic_policy,
                resources.getString(R.string.policy),
                "",
                isEnable = false,
                isSelected = false
            )
        )
        sideMenuModelList.add(
            SideMenuModel(
                R.drawable.ic_about_us,
                resources.getString(R.string.about_us),
                "",
                isEnable = false,
                isSelected = false
            )
        )
        if (BaseURL.ALLOW_LANGUAGE) {
            sideMenuModelList.add(
                SideMenuModel(
                    R.drawable.ic_language,
                    resources.getString(R.string.change_language),
                    if (LanguagePrefs.getLang(this) == "ar")
                        resources.getString(R.string.arabic)
                    else
                        resources.getString(R.string.english),
                    isEnable = false,
                    isSelected = false
                )
            )
        }
        if (SessionManagement.UserData.isLogin(this)) {
            sideMenuModelList.add(
                SideMenuModel(
                    R.drawable.ic_logout,
                    resources.getString(R.string.logout),
                    "",
                    isEnable = false,
                    isSelected = false
                )
            )
        }

        sideMenuAdapter.notifyDataSetChanged()
    }

    private fun sideMenuSelection(sideMenuModel: SideMenuModel) {
        var commonIntent: Intent? = null
        when (sideMenuModel.title) {
            resources.getString(R.string.profile) -> {
                commonIntent = Intent(this, ProfileActivity::class.java)
            }
            resources.getString(R.string.my_orders) -> {
                Intent(this, MyOrderActivity::class.java).apply {
                    startActivityForResult(this, 6894)
                }
            }
            resources.getString(R.string.contact_us) -> {
                /*commonIntent = Intent(this, CommonWebViewActivity::class.java).apply {
                    putExtra("title", resources.getString(R.string.contact_us))
                    putExtra("url", BaseURL.CONTACT_URL)
                }*/
                commonIntent = Intent(this, ContactUsActivity::class.java)
            }
            resources.getString(R.string.policy) -> {
                commonIntent = Intent(this, CommonWebViewActivity::class.java).apply {
                    putExtra("title", resources.getString(R.string.policy))
                    putExtra("url", BaseURL.POLICY_URL)
                }
            }
            resources.getString(R.string.about_us) -> {
                commonIntent = Intent(this, CommonWebViewActivity::class.java).apply {
                    putExtra("title", resources.getString(R.string.about_us))
                    putExtra("url", BaseURL.ABOUT_URL)
                }
            }
            resources.getString(R.string.change_language) -> {
                ChooseLanguageDialog(this).show()
            }
            resources.getString(R.string.logout) -> {
                val sessionManagement = SessionManagement(this)
                sessionManagement.logoutSession()
                finish()
            }
        }
        if (commonIntent != null) {
            startActivity(commonIntent)
        }
        if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    val fm_home = HomeFragment()

    private fun loadHome() {
        setBottomSelection(1)
        setHeaderTitle(resources.getString(R.string.app_name))
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.contentPanel, fm_home, "Home_fragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun setBottomSelection(position: Int) {
        iv_bottom_home.isSelected = false
        iv_bottom_menu.isSelected = false
        iv_bottom_cart.isSelected = false
        iv_bottom_order.isSelected = false
        when (position) {
            1 -> iv_bottom_home.isSelected = true
            2 -> iv_bottom_menu.isSelected = true
            3 -> iv_bottom_cart.isSelected = true
            4 -> iv_bottom_order.isSelected = true
        }
    }

    fun setHeaderTitle(title: String) {
        tv_actionbar_title.text = title
        val totalDiscountPrice = CommonActivity.getCartNetAmount(this)

        if (title == resources.getString(R.string.cart)) {
            if (totalDiscountPrice > 0) {
                menu_delete?.icon = ContextCompat.getDrawable(this, R.drawable.ic_clear)
                menu_delete?.isVisible = true
            } else {
                menu_delete?.isVisible = false
            }
        } else if (title == resources.getString(R.string.menu)) {
            menu_delete?.icon = ContextCompat.getDrawable(this, R.drawable.ic_search_green)
            menu_delete?.isVisible = true
        } else {
            menu_delete?.icon = ContextCompat.getDrawable(this, R.drawable.ic_offer)
            menu_delete?.isVisible = true
        }
    }

    fun setFragment(fm: Fragment?, allowAnimation: Boolean) {
        val fragmentManager = supportFragmentManager
        if (fm != null) {
            if (allowAnimation) {
                if (LanguagePrefs.getLang(this@MainActivity).equals("ar")) {
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .add(R.id.contentPanel, fm)
                        .addToBackStack(null)
                        .commit()
                } else {
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out,
                            R.anim.fade_out,
                            R.anim.fade_in
                        )
                        .add(R.id.contentPanel, fm)
                        .addToBackStack(null)
                        .commit()
                }
            } else {
                fragmentManager.beginTransaction()
                    .add(R.id.contentPanel, fm)
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    fun updatePrice() {
        val totalDiscountPrice = CommonActivity.getCartNetAmount(this)
        if (totalDiscountPrice > 0) {
            tv_bottom_cart_counter.apply {
                visibility = View.VISIBLE
                text = CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(totalDiscountPrice.toString())
                )
            }
            CommonActivity.runBounceAnimation(this, tv_bottom_cart_counter)
        } else {
            tv_bottom_cart_counter.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu_delete = menu.findItem(R.id.action_delete)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_delete) {
            if (tv_actionbar_title.text.toString() == resources.getString(R.string.cart)) {
                CartFragment.showCartDeleteDialog(this)
            } else if (tv_actionbar_title.text.toString() == resources.getString(R.string.menu)) {
                Intent(this, MenuItemActivity::class.java).apply {
                    startActivityForResult(this, 6894)
                }
            } else {
                Intent(this, MenuItemActivity::class.java).apply {
                    putExtra("discount", true)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6894 && resultCode == Activity.RESULT_OK) {
            val fr = supportFragmentManager.findFragmentById(R.id.contentPanel)
            val fmName = fr?.javaClass!!.simpleName
            val cartFragment = CartFragment()
            if (fmName != "CartFragment") {
                setFragment(cartFragment, true)
            } else {
                cartFragment.updateCart()
            }
            setBottomSelection(3)
            setHeaderTitle(resources.getString(R.string.cart))
            updatePrice()
        }
    }

    override fun onResume() {
        super.onResume()
        updatePrice()
        updateSideMenu()
        BaseURL.HEADER_LANG = if (LanguagePrefs.getLang(this).equals("nl")) {
            "dutch"
        } else if (LanguagePrefs.getLang(this).equals("ar")) {
            "arabic"
        } else {
            "english"
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
