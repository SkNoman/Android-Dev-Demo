package com.example.crud.ui.dashboard.menus

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentFridgeBinding


class FragmentFridge : BaseFragmentWithBinding<FragmentFridgeBinding>(
    FragmentFridgeBinding::inflate)
{
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = binding.webViewFridge
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://waltonbd.com/direct-cool-refrigerator")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }


}