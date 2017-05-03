package com.tokopedia.seller.topads.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupManagePromoFragment;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsGroupManagePromoActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsGroupManagePromoFragment.createInstance(
                        getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID),
                        getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, 0),
                        getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_GROUP_ID),
                        getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME)),
                        TopAdsGroupManagePromoFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public static Intent createIntent(Context context, String adId, int choosenOption,
                                      String groupName, String groupId){
        Intent intent = new Intent(context, TopAdsGroupManagePromoActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_CHOOSEN_OPTION_GROUP, choosenOption);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        return intent;
    }
}
