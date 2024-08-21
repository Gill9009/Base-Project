package com.hypersoft.baseproject.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hypersoft.baseproject.R
import com.hypersoft.baseproject.commons.listeners.RapidSafeListener.setOnRapidClickSafeListener
import com.hypersoft.baseproject.databinding.ActivitySampleResultBinding
import com.hypersoft.baseproject.ui.activities.base.BaseActivity

class SampleResult : BaseActivity<ActivitySampleResultBinding>(R.layout.activity_sample_result) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mbSubmitSampleResult.setOnRapidClickSafeListener { onSubmitClick() }
    }

    private fun onSubmitClick() {
        val text = binding.etTextSampleResult.text?.trim().toString()
        if (text.isEmpty()) {
            binding.ltTextSampleResult.error = "Field can't be Empty"
            binding.ltTextSampleResult.isErrorEnabled = true
            return
        }
        Intent().also {
            it.putExtra("text", text)
            setResult(Activity.RESULT_OK, it)
        }
        finish()
    }
}