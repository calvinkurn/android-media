package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TextDrawable;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResChatActivityPresenter;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivity
        extends BasePresenterActivity<DetailResChatActivityListener.Presenter>
        implements DetailResChatActivityListener.View, HasComponent {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_SHOP_NAME = "shop_name";
    public static final String PARAM_USER_NAME = "user_name";
    public static final String PARAM_IS_SELLER = "is_seller";

    public static final int REQUEST_GO_DETAIL = 8888;
    private String resolutionId;
    private String shopName;
    private String userName;
    private boolean isSeller;

    public static Intent newBuyerInstance(Context context, String resolutionId, String shopName) {
        Intent intent = new Intent(context, DetailResChatActivity.class);
        intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
        intent.putExtra(PARAM_SHOP_NAME, shopName);
        intent.putExtra(PARAM_IS_SELLER, false);
        return intent;
    }

    public static Intent newSellerInstance(Context context, String resolutionId, String username) {
        Intent intent = new Intent(context, DetailResChatActivity.class);
        intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
        intent.putExtra(PARAM_USER_NAME, username);
        intent.putExtra(PARAM_IS_SELLER, true);
        return intent;
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) != null) {
            getFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resolutionId = extras.getString(PARAM_RESOLUTION_ID);
        isSeller = extras.getBoolean(PARAM_IS_SELLER);
        if (isSeller) {
            userName = extras.getString(PARAM_USER_NAME);
        } else {
            shopName = extras.getString(PARAM_SHOP_NAME);
        }
    }

    @Override
    protected void initialPresenter() {
        presenter = new DetailResChatActivityPresenter(this, resolutionId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_res_chat;
    }

    @Override
    protected void initView() {
        if (isSeller) {
            toolbar.setTitle(getString(R.string.complaint_from) +" "+ userName);
        } else {
            toolbar.setTitle(getString(R.string.complaint_to) +" "+ shopName);
        }
        presenter.initFragment(isSeller);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.action_detail, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_detail); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDetailMenuItem());
        return true;
    }

    private Drawable getDetailMenuItem() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.detail));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_detail) {
            Intent intent;
            if (isSeller) {
                intent = DetailResCenterActivity.newSellerInstance(DetailResChatActivity.this, resolutionId, userName);
            } else {
                intent = DetailResCenterActivity.newBuyerInstance(DetailResChatActivity.this, resolutionId, shopName);
            }
            startActivityForResult(intent, REQUEST_GO_DETAIL);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }


}
