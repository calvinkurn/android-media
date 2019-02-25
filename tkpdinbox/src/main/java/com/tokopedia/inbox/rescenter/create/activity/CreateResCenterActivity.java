package com.tokopedia.inbox.rescenter.create.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.rescenter.create.fragment.ChooseProductTroubleFragment;
import com.tokopedia.inbox.rescenter.create.fragment.ChooseSolutionFragment;
import com.tokopedia.inbox.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.presenter.CreateResCenterImpl;
import com.tokopedia.inbox.rescenter.create.presenter.CreateResCenterPresenter;
import com.tokopedia.inbox.rescenter.create.service.CreateResCenterReceiver;
import com.tokopedia.inbox.rescenter.create.service.CreateResCenterService;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_WEBVIEW_RESO_ENABLED_TOGGLE;

public class CreateResCenterActivity extends BasePresenterActivity<CreateResCenterPresenter>
        implements CreateResCenterListener, CreateResCenterReceiver.Receiver, HasComponent {

    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String KEY_PARAM_ORDER_ID = "ORDER_ID";
    public static final String KEY_PARAM_FLAG_RECEIVED = "FLAG_RECEIVED";
    public static final String KEY_PARAM_TROUBLE_ID = "TROUBLE_ID";
    public static final String KEY_PARAM_SOLUTION_ID = "SOLUTION_ID";
    public static final String KEY_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String TAG_STEP_1 = "step_1";
    private static final String TAG_STEP_2 = "step_2";

    private Bundle bundleData;
    private Uri uriData;
    private CreateResCenterReceiver receiver;
    private String resolutionId;
    private String orderId;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_ADD;
    }

    public static Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getApplinkIntent(context, orderId);
        }
        if (intent == null) {
            intent = new Intent(context, CreateResCenterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ORDER_ID, orderId);
            bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 1);
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Intent getCreateResCenterActivityIntent(Context context, String orderId, int troubleId, int solutionId) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getApplinkIntent(context, orderId);
        }
        if (intent == null) {
            intent = new Intent(context, CreateResCenterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(InboxRouter.EXTRA_ORDER_ID, orderId);
            bundle.putInt(InboxRouter.EXTRA_STATE_FLAG_RECEIVED, 0);
            bundle.putInt(InboxRouter.EXTRA_TROUBLE_ID, troubleId);
            bundle.putInt(InboxRouter.EXTRA_SOLUTION_ID, solutionId);
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Intent newInstance(Context context, String orderID) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getApplinkIntent(context, orderID);
        }
        if (intent == null) {
            intent = new Intent(context, CreateResCenterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(KEY_PARAM_ORDER_ID, orderID);
            bundle.putInt(KEY_PARAM_FLAG_RECEIVED, 1);
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Intent newRecomplaintInstance(Context context, String orderID, String resolutionId) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getApplinkIntent(context, orderID);
        }
        if (intent == null) {
            intent = new Intent(context, CreateResCenterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(KEY_PARAM_ORDER_ID, orderID);
            bundle.putString(KEY_PARAM_RESOLUTION_ID, resolutionId);
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Intent newInstancePackageNotReceived(Context context,
                                                       String orderID,
                                                       int troubleID,
                                                       int solutionID) {
        Intent intent = new Intent(context, CreateResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PARAM_ORDER_ID, orderID);
        bundle.putInt(KEY_PARAM_FLAG_RECEIVED, 0);
        bundle.putInt(KEY_PARAM_TROUBLE_ID, troubleID);
        bundle.putInt(KEY_PARAM_SOLUTION_ID, solutionID);
        intent.putExtras(bundle);
        return intent;
    }

    private static boolean isToggleResoEnabled(Context context) {
//        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
//        return remoteConfig.getBoolean(APP_WEBVIEW_RESO_ENABLED_TOGGLE);
        return true;
    }

    private static Intent getApplinkIntent(Context context, String orderId) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            return ((ResolutionRouter)context.getApplicationContext()).getApplinkIntent(context,
                    String.format(ResolutionUrl.RESO_APPLINK + ResolutionUrl.RESO_CREATE, orderId));
        }
        return null;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
        if (extras.get(KEY_PARAM_RESOLUTION_ID) != null) {
            resolutionId = extras.getString(KEY_PARAM_RESOLUTION_ID);
            orderId = extras.getString(KEY_PARAM_ORDER_ID);
            toolbar.setTitle(R.string.string_title_create_recomplaint);
            setTitle(getResources().getString(R.string.string_title_create_recomplaint));
        }
    }

    @Override
    protected void initialPresenter() {
        presenter = new CreateResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_resolution_center;
    }

    @Override
    protected void initView() {
        if (resolutionId == null) {
            presenter.initFragment(this, uriData, bundleData);
        } else {
            presenter.initRecomplaintFragment(this, orderId, resolutionId);
        }
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
            .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        receiver = new CreateResCenterReceiver(new Handler());
        receiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void addSolutionFragmentStacked(ActionParameterPassData passData) {
        if (getFragmentManager().findFragmentByTag(ChooseSolutionFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, ChooseSolutionFragment.newInstance(passData), ChooseSolutionFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_2)
                    .commit();
        }
    }

    @Override
    public void addProductDetailTroubleFragmentStacked(ActionParameterPassData passData) {
        if (getFragmentManager().findFragmentByTag(ChooseProductTroubleFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, ChooseProductTroubleFragment.newInstance(passData), ChooseProductTroubleFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_1)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        UnifyTracking.eventCreateResoAbandon(this);
    }

    @Override
    public void startCreateResCenterService(ActionParameterPassData passData) {
        CreateResCenterService.startActionCreateResolution(this, passData, receiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String action = resultData.getString(CreateResCenterService.EXTRA_PARAM_ACTION_TYPE, "");
        if (action.equals(CreateResCenterService.ACTION_CREATE_RESOLUTION)) {
            if (getFragmentManager().findFragmentByTag(TAG_STEP_2) == null) {
                ((ChooseSolutionFragment) getFragmentManager()
                        .findFragmentByTag(ChooseSolutionFragment.class.getSimpleName()))
                        .onGetResultCreateResCenter(resultCode, resultData);
            }
        }
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }
}
