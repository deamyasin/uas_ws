package com.foodorder.ui.home.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.foodorder.R
import kotlinx.android.synthetic.main.dialog_choose_language.view.*
import utils.LanguagePrefs

class ChooseLanguageDialog(context: Context) : android.app.AlertDialog(context) {

    val contexts: Context

    init {
        this.contexts = context
        setCanceledOnTouchOutside(false)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_choose_language, null)
        this.setView(dialogView)
        try {
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            val imm =
                (contexts as Activity).getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = contexts.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(contexts)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val languagePrefs = LanguagePrefs(contexts)

        dialogView.tv_dialog_choose_language_ar.isSelected = LanguagePrefs.getLang(contexts) == "ar"
        dialogView.tv_dialog_choose_language_en.isSelected = LanguagePrefs.getLang(contexts) == "en"

        dialogView.tv_dialog_choose_language_ar.setOnClickListener {
            if (LanguagePrefs.getLang(contexts) == "en") {
                dismiss()
                languagePrefs.saveLanguage("ar")
                languagePrefs.initLanguage("ar")
                (contexts as Activity).recreate()
            } else {
                dismiss()
            }
        }

        dialogView.tv_dialog_choose_language_en.setOnClickListener {
            if (LanguagePrefs.getLang(contexts) == "ar") {
                dismiss()
                languagePrefs.saveLanguage("en")
                languagePrefs.initLanguage("en")
                (contexts as Activity).recreate()
            } else {
                dismiss()
            }
        }

    }

}
