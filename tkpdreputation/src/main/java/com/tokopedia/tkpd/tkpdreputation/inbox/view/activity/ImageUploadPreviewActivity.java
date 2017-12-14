package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.ImageUploadPreviewFragment;

/**
 * @author by nisie on 9/4/17.
 */

public class ImageUploadPreviewActivity extends BasePresenterActivity {

    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String ARGS_POSITION = "ARGS_POSITION";


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
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {

        String fileLoc = "";
        if (getIntent().getExtras() != null
                && getIntent().getExtras().getString(ImageUploadHandler.FILELOC) != null)
            fileLoc = getIntent().getExtras().getString(ImageUploadHandler.FILELOC, "");

        boolean isUpdate = false;
        if (getIntent().getExtras() != null)
            isUpdate = getIntent().getExtras().getBoolean(IS_UPDATE, false);

        int position = 0;
        if (getIntent().getExtras() != null)
            position = getIntent().getExtras().getInt(ARGS_POSITION, 0);

        Fragment fragment = getFragmentManager().findFragmentByTag
                (ImageUploadPreviewFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = ImageUploadPreviewFragment.createInstance(fileLoc, isUpdate, position);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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

    public static Intent getCallingIntent(Context context, String fileLoc) {
        Intent intent = new Intent(context, ImageUploadPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ImageUploadHandler.FILELOC, fileLoc);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getUpdateCallingIntent(Context context, int position) {
        Intent intent = new Intent(context, ImageUploadPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_UPDATE, true);
        bundle.putInt(ARGS_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUploadHandler.REQUEST_CODE)
            getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                    resultCode, data);
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
