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
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.KolCommentFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentProductViewModel;

import org.json.JSONException;
import org.json.JSONObject;

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
        KolCommentHeaderViewModel kolCommentHeaderViewModel = new KolCommentHeaderViewModel();
        KolCommentProductViewModel kolCommentProductViewModel = new KolCommentProductViewModel();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_FROM_APPLINK, true);
        args.putInt(ARGS_ID, Integer.valueOf(bundle.getString(ARGS_KOL_ID)));

        String dataExtras = bundle.getString("extra");
        try {
            JSONObject jsonObject = new JSONObject(dataExtras);
            String avtar = jsonObject.getString("imageURL");
            String userName = jsonObject.getString("userName");
            String commentTime = jsonObject.getString("createTime");
            int id = jsonObject.getInt("cardID");
            String userId = String.valueOf(jsonObject.getInt("userID"));
            String userImage = jsonObject.getString("userPhoto");
            String review = jsonObject.getString("description");
            String userInfo = jsonObject.getString("userInfo");
            String price = jsonObject.getString("price");

            String time = TimeConverter.generateTime(commentTime);
            kolCommentHeaderViewModel =
                    new KolCommentHeaderViewModel(avtar, userName, review, time, userId);
            kolCommentProductViewModel =
                    new KolCommentProductViewModel(userImage, userInfo,
                            price, false);
            args.putInt(ARGS_ID, id);
            args.putBoolean(ARGS_FROM_APPLINK, false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.putParcelable(ARGS_HEADER, kolCommentHeaderViewModel);
        bundle.putParcelable(ARGS_FOOTER, kolCommentProductViewModel);

        bundle.putInt(ARGS_POSITION, 0);
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
