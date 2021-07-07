package com.fooddeliveryboy.ui.profile

import Config.BaseURL
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fooddeliveryboy.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_profile.view.*
import utils.SessionManagement

/**
 * Created on 06-04-2020.
 */
class ProfileFragment : Fragment() {

    lateinit var contexts: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        Glide.with(contexts)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    contexts,
                    BaseURL.KEY_IMAGE
                )
            )
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(rootView.iv_profile_img)

        rootView.tv_profile_name.text =
            SessionManagement.UserData.getSession(contexts, BaseURL.KEY_NAME)
        rootView.tv_profile_email.text =
            SessionManagement.UserData.getSession(contexts, BaseURL.KEY_EMAIL)
        rootView.tv_profile_phone.text =
            SessionManagement.UserData.getSession(contexts, BaseURL.KEY_MOBILE)
        rootView.tv_profile_vehicle_no.text =
            SessionManagement.UserData.getSession(contexts, "id_proof")
        rootView.tv_profile_licence_no.text =
            SessionManagement.UserData.getSession(contexts, "licence_no")

        PushDownAnim.setPushDownAnimTo(rootView.btn_profile_change_password)

        rootView.btn_profile_change_password.setOnClickListener {
            Intent(contexts, ChangePasswordActivity::class.java).apply {
                startActivity(this)
            }
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}