package com.tokopedia.shop.note.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;
import com.tokopedia.shop.note.view.fragment.ShopNoteDetailFragment;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailActivity extends BaseSimpleActivity {

    private String shopNoteId;

    public static Intent createIntent(Context context, String shopNoteId) {
        Intent intent = new Intent(context, ShopNoteDetailActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_NOTE_ID, shopNoteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopNoteId = getIntent().getStringExtra(ShopParamConstant.SHOP_NOTE_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopNoteDetailFragment.newInstance(shopNoteId);
    }
}
