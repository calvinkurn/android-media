package com.tokopedia.seller.product.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.fragment.ProductDraftListFragment;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListActivity extends TkpdActivity {
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        drawer.setDrawerPosition(TkpdState.DrawerPosition.DRAFT_PRODUCT);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductDraftListFragment.newInstance(),
                            ProductDraftListFragment.TAG)
                    .commit();
        }
    }

}
