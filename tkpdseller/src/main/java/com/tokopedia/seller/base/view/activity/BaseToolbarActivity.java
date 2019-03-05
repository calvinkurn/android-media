package com.tokopedia.seller.base.view.activity;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.utils.MenuTintUtils;

/**
 * Created by nathan on 7/11/17.
 */

@Deprecated
abstract class BaseToolbarActivity extends BaseActivity {

    private final static int TOOLBAR_ELEVATION = 10;
    private final static int TEXT_COLOR_BACKGROUND_WHITE = R.color.black_70;
    protected Toolbar toolbar;

    protected abstract void setupFragment(Bundle savedInstanceState);

    @LayoutRes
    protected abstract int getLayoutRes();

    protected boolean isAllowElevation() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getThemeActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green_600));
        }
        setupLayout(savedInstanceState);
        setupFragment(savedInstanceState);
        /**
         * because toolbar background is black so set title and its child black
         * otherwise toolbar and its child is white color.
         */
        if (isToolbarWhite()) {
            setToolbarColorWhite();
            toolbar.setTitleTextAppearance(this, com.tokopedia.core2.R.style.ToolbarText_SansSerifMedium);
            toolbar.setSubtitleTextAppearance(this, com.tokopedia.core2.R.style.ToolbarSubtitleText_SansSerifMedium);
        } else {
            toolbar.setTitleTextAppearance(this, com.tokopedia.core2.R.style.ToolbarText);
            toolbar.setSubtitleTextAppearance(this, com.tokopedia.core2.R.style.ToolbarSubtitleText);
        }
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, closeButtonDrawable()));
        }
    }

    @DrawableRes
    protected int closeButtonDrawable() {
        return R.drawable.ic_filter_detail_close;
    }

    protected int getThemeActivity() {
        return R.style.Theme_Tokopedia3;
    }

    protected void setToolbarColorWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isAllowElevation()) {
            toolbar.setElevation(TOOLBAR_ELEVATION);
        }
        int textColor = ContextCompat.getColor(this, TEXT_COLOR_BACKGROUND_WHITE);
        toolbar.setTitleTextColor(textColor);
        toolbar.setSubtitleTextColor(textColor);
        int height = dpToPx(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_height_material));
        int width = dpToPx(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material));
        toolbar.setOverflowIcon(resize(ContextCompat.getDrawable(this, R.drawable.overflow_btn), width, height));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    protected boolean isShowCloseButton() {
        return false;
    }

    protected boolean isToolbarWhite() {
        return false;
    }

    @CallSuper
    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionMenuColor(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void updateOptionMenuColor(Menu menu) {
        if (isToolbarWhite()) {
            MenuTintUtils.tintAllIcons(menu, TEXT_COLOR_BACKGROUND_WHITE);
        } else {
            MenuTintUtils.tintAllIcons(menu, R.color.white);
        }
    }

    public void updateTitle(String title) {
        updateTitle(title, null);
    }

    public void updateTitle(String title, String subTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (TextUtils.isEmpty(subTitle)) {
            subTitle = "";
        }
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}