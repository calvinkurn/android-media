package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.KolCommentFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentProductViewModel;

import static com.tokopedia.core.talkview.intentservice.TalkDetailIntentService.POSITION;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentActivity extends TActivity implements HasComponent {

    public static final String ARGS_HEADER = "ARGS_HEADER";
    public static final String ARGS_FOOTER = "ARGS_FOOTER";
    public static final String ARGS_ID = "ARGS_ID";
    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_KOL_ID = "id";
    public static final String ARGS_FROM_APPLINK = "isFromApplink";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment == null)
            fragment = KolCommentFragment.createInstance(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment).commit();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public static Intent getCallingIntent(Context context, KolCommentHeaderViewModel header,
                                          KolCommentProductViewModel productViewModel,
                                          int id, int rowNumber) {
        Intent intent = new Intent(context, KolCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_HEADER, header);
        bundle.putParcelable(ARGS_FOOTER, productViewModel);
        bundle.putInt(ARGS_ID, id);
        bundle.putInt(ARGS_POSITION, rowNumber);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(Constants.Applinks.KOLCOMMENT)
    public static Intent getCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, KolCommentActivity.class);
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, Integer.valueOf(bundle.getString(ARGS_KOL_ID)));
        args.putBoolean(ARGS_FROM_APPLINK, true);
        intent.putExtras(args);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onBackPressed() {
        UnifyTracking.eventKolCommentDetailBack();
        super.onBackPressed();
    }
}
