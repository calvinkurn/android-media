package com.tokopedia.transaction.orders.orderdetails.view.activity

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderdetails.data.Status
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class SeeInvoiceActivity : BaseSimpleWebViewActivity(){

    var orderListAnalytics: OrderListAnalytics? = null
        @Inject set

    private lateinit var status: Status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_unduh_invoice, menu)
        return true
    }

    private fun doWebPrint(){
        val webView = WebView(this)
        webView.settings.javaScriptEnabled
        webView.settings.domStorageEnabled
        webView.settings.builtInZoomControls
        webView.settings.displayZoomControls
        val data = intent?.extras?.getString(KEY_URL, "defaultKey")
        webView.loadUrl(data)
        onPrintClicked(webView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_download -> {
                orderListAnalytics?.sendDownloadEventData(status?.status())
                doWebPrint()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun onPrintClicked(webView: WebView?){
        webView?.let{
            val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
            val jobName = getString(R.string.app_name) + " Document"
            val printAdapter: PrintDocumentAdapter
            printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.createPrintDocumentAdapter(jobName)
            } else {
                it.createPrintDocumentAdapter()
            }
            val builder = PrintAttributes.Builder()
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            printManager.print(jobName, printAdapter, builder.build())
        }
    }
}