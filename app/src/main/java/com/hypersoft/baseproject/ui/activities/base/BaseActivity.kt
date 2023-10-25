package com.hypersoft.baseproject.ui.activities.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hypersoft.baseproject.BuildConfig
import com.hypersoft.baseproject.commons.koin.DIComponent
import com.hypersoft.baseproject.commons.extensions.Extensions.goBackPressed
import com.hypersoft.baseproject.commons.firebase.FirebaseUtils.recordException
import com.hypersoft.baseproject.commons.utils.LocaleHelper

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes layoutId: Int,private val shouldActivityBackPress: Boolean = false) : AppCompatActivity() {

    private val baseTAG = "BaseTAG"

    protected val binding by lazy {
        DataBindingUtil.inflate<T>(
            layoutInflater,
            layoutId,
            null,
            false
        )
    }
    protected val diComponent by lazy { DIComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!shouldActivityBackPress) {
            registerBackPressDispatcher()
        }
    }

    protected fun withDelay(delay: Long = 300, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(block, delay)
    }

    protected fun showKeyboard() {
        try {
            val imm: InputMethodManager? =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } catch (ex: Exception) {
            ex.recordException("showKeyBoardTag")
        }
    }

    protected fun hideKeyboard() {
        try {
            val inputMethodManager: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view: IBinder? = findViewById<View?>(android.R.id.content)?.windowToken
            inputMethodManager.hideSoftInputFromWindow(view, 0)
        } catch (ex: Exception) {
            ex.recordException("hideKeyboard")
        }
    }

    /* ---------- Toast ---------- */

    protected fun showToast(message: String) {
        try {
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception) {
            ex.recordException("showToast : ${javaClass.simpleName}")
        }
    }

    protected fun debugToast(message: String) {
        if (BuildConfig.DEBUG) {
            showToast(message)
        }
    }

    protected fun showToast(@StringRes stringId: Int) {
        val message = getString(stringId)
        showToast(message)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            LocaleHelper.setLocale(
                newBase,
                diComponent.sharedPreferenceUtils.selectedLanguageCode
            )
        )
    }

    open fun onRecreate() {}

    private fun registerBackPressDispatcher() {
        goBackPressed {
            onBack()
        }
    }

    open fun onBack() {}
}