package com.mykola2312.outagefetch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.mykola2312.outagefetch.ui.theme.OutageFetchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OutageFetchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FetchScreen()
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExtractWebView() {
    val url = stringResource(id = R.string.fetch_url)

    AndroidView(factory = {
        WebView(it).apply {
            this.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            this.webViewClient = ExtractorClient()
        }
    }, update = {
        it.settings.javaScriptEnabled = true
        it.loadUrl(url);
    })
}

class ExtractorClient : WebViewClient() {
    companion object ExtractConstants {
        const val JS_EXTRACT = "document.getElementsByClassName(\"aos-init aos-animate\").item(0).children[4].lastChild.src";
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        view?.evaluateJavascript(JS_EXTRACT, ValueCallback<String> {
            if (it != null) {
                val scheduleImgUrl = it.removeSurrounding("\"")
                Log.i("extract", scheduleImgUrl)
            }
        })
    }
}

@Composable
fun FetchScreen() {
    ExtractWebView()
}

@Preview
@Composable
fun FetchScreenPreview() {
    FetchScreen()
}