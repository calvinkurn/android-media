package com.tokopedia.tkpdpdp.estimasiongkir.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.fragment.RatesEstimationDetailFragment
import com.tokopedia.tkpdpdp.estimasiongkir.constant.RatesEstimationConstant
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.tkpdpdp.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationModule

class RatesEstimationDetailActivity : BaseSimpleActivity(), HasComponent<RatesEstimationComponent> {

    override fun getNewFragment(): Fragment {
        val productId = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_ID)
        val weightFmt = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT)
        return RatesEstimationDetailFragment.createInstance(productId, weightFmt)
    }

    override fun getComponent(): RatesEstimationComponent {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, productId: String, productWeigtFmt: String): Intent {
            return Intent(context, RatesEstimationDetailActivity::class.java)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_ID, productId)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeigtFmt)
        }
    }
}
