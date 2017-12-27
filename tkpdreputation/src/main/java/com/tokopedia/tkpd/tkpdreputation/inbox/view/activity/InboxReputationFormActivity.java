package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFormFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationFormActivity extends BasePresenterActivity
        implements HasComponent {


    public interface SkipListener {
        void skipReview();
    }

    public static final String ARGS_REVIEW_ID = "ARGS_REVIEW_ID";
    public static final String ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID";
    public static final String ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID";
    public static final String ARGS_SHOP_ID = "ARGS_SHOP_ID";
    private static final String ARGS_IS_SKIPPABLE = "ARGS_IS_SKIPPABLE";
    public static final String ARGS_PRODUCT_AVATAR = "ARGS_PRODUCT_AVATAR";
    public static final String ARGS_PRODUCT_NAME = "ARGS_PRODUCT_NAME";
    public static final String ARGS_PRODUCT_STATUS = "ARGS_PRODUCT_STATUS";
    public static final String ARGS_PRODUCT_URL = "ARGS_PRODUCT_URL";

    public static final String ARGS_IS_EDIT = "ARGS_IS_EDIT";
    public static final String ARGS_RATING = "ARGS_RATING";
    public static final String ARGS_REVIEW = "ARGS_REVIEW";
    public static final String ARGS_REVIEW_IMAGES = "ARGS_REVIEW_IMAGES";
    public static final String ARGS_ANONYMOUS = "ARGS_ANONYMOUS";
    public static final String ARGS_REVIEWEE_NAME = "ARGS_REVIEWEE_NAME";


    SkipListener listener;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getExtras() != null
                && getIntent().getBooleanExtra(ARGS_IS_SKIPPABLE, false)) {
            menu.add(Menu.NONE, R.id.action_skip, 0, "");
            MenuItem menuItem = menu.findItem(R.id.action_skip);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setIcon(getSkipMenu());
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    private Drawable getSkipMenu() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.action_skip));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_skip
                && listener != null) {
            listener.skipReview();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationFormFragment.class.getSimpleName());
        if (fragment == null && getIntent().getExtras().getBoolean(ARGS_IS_EDIT, false)) {
            fragment = InboxReputationFormFragment.createInstance(bundle);
        } else if (fragment == null) {
            fragment = InboxReputationFormFragment.createInstance(bundle);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        listener = (InboxReputationFormFragment) fragment;

    }

    @Override
    protected void setViewListener() {

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

    public static Intent getGiveReviewIntent(Context context, String reviewId,
                                             String reputationId, String productId,
                                             String shopId, boolean reviewIsSkippable,
                                             String productAvatar, String productName,
                                             String productUrl, String revieweeName,
                                             int productStatus) {
        Intent intent = new Intent(context, InboxReputationFormActivity.class);
        intent.putExtra(ARGS_PRODUCT_ID, productId);
        intent.putExtra(ARGS_REPUTATION_ID, reputationId);
        intent.putExtra(ARGS_REVIEW_ID, reviewId);
        intent.putExtra(ARGS_SHOP_ID, shopId);
        intent.putExtra(ARGS_IS_SKIPPABLE, reviewIsSkippable);
        intent.putExtra(ARGS_IS_EDIT, false);
        intent.putExtra(ARGS_PRODUCT_AVATAR, productAvatar);
        intent.putExtra(ARGS_PRODUCT_NAME, productName);
        intent.putExtra(ARGS_PRODUCT_URL, productUrl);
        intent.putExtra(ARGS_REVIEWEE_NAME, revieweeName);
        intent.putExtra(ARGS_PRODUCT_STATUS, productStatus);
        return intent;
    }

    public static Intent getEditReviewIntent(Context context, String reviewId,
                                             String reputationId, String productId,
                                             String shopId, int reviewStar, String review,
                                             ArrayList<ImageAttachmentViewModel> reviewAttachment,
                                             String productAvatar, String productName,
                                             String productUrl, boolean isAnonymous,
                                             String revieweeName, int productStatus) {
        Intent intent = new Intent(context, InboxReputationFormActivity.class);
        intent.putExtra(ARGS_PRODUCT_ID, productId);
        intent.putExtra(ARGS_REPUTATION_ID, reputationId);
        intent.putExtra(ARGS_REVIEW_ID, reviewId);
        intent.putExtra(ARGS_SHOP_ID, shopId);
        intent.putExtra(ARGS_IS_SKIPPABLE, false);
        intent.putExtra(ARGS_IS_EDIT, true);
        intent.putExtra(ARGS_RATING, reviewStar);
        intent.putExtra(ARGS_REVIEW, review);
        intent.putParcelableArrayListExtra(ARGS_REVIEW_IMAGES, reviewAttachment);
        intent.putExtra(ARGS_PRODUCT_AVATAR, productAvatar);
        intent.putExtra(ARGS_PRODUCT_NAME, productName);
        intent.putExtra(ARGS_PRODUCT_URL, productUrl);
        intent.putExtra(ARGS_ANONYMOUS, isAnonymous);
        intent.putExtra(ARGS_REVIEWEE_NAME, revieweeName);
        intent.putExtra(ARGS_PRODUCT_STATUS, productStatus);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUploadHandler.REQUEST_CODE
                || requestCode == ImageUploadHandler.CODE_UPLOAD_IMAGE)
            getSupportFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                    resultCode, data);
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setPadding(0,0,30,0);
    }
}
