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
        val shopDomain = intent.getStringExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN)
        val weight = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT)
        val weightUnit = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT)
        return RatesEstimationDetailFragment.createInstance(shopDomain, weight, weightUnit)
    }

    override fun getComponent(): RatesEstimationComponent {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productWeight: String, productWeightUnit: String): Intent {
            return Intent(context, RatesEstimationDetailActivity::class.java)
                    .putExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeight)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, productWeightUnit)
        }
    }
}
