package com.tokopedia.discovery.newdynamicfilter

import android.content.Intent
import android.support.v7.app.AppCompatActivity

import com.tokopedia.discovery.common.data.Option
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailOfferingAdapter

import java.util.ArrayList

class DynamicFilterOfferingActivity : DynamicFilterDetailGeneralActivity() {

    override fun getAdapter(): DynamicFilterDetailAdapter {
        return DynamicFilterDetailOfferingAdapter(this)
    }

    companion object {

        fun moveTo(activity: AppCompatActivity?,
                            pageTitle: String,
                            optionList: List<Option>,
                            isSearchable: Boolean,
                            searchHint: String,
                            isUsingTracking: Boolean) {

            if (activity != null) {
                val intent = Intent(activity, DynamicFilterOfferingActivity::class.java)
                intent.putExtra(AbstractDynamicFilterDetailActivity.EXTRA_PAGE_TITLE, pageTitle)
                intent.putParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_OPTION_LIST, ArrayList(optionList))
                intent.putExtra(AbstractDynamicFilterDetailActivity.EXTRA_IS_SEARCHABLE, isSearchable)
                intent.putExtra(AbstractDynamicFilterDetailActivity.EXTRA_SEARCH_HINT, searchHint)
                intent.putExtra(AbstractDynamicFilterDetailActivity.EXTRA_IS_USING_TRACKING, isUsingTracking)
                activity.startActivityForResult(intent, AbstractDynamicFilterDetailActivity.REQUEST_CODE)
            }
        }
    }
}
