package com.tokopedia.otp.cotp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.adapter.VerificationMethodAdapter;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.session.R;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseVerificationMethodFragment extends BaseDaggerFragment implements
        SelectVerification.View {

    private RecyclerView methodListRecyclerView;

    @Inject
    GlobalCacheManager cacheManager;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SELECT_VERIFICATION_METHOD;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerSessionComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseVerificationMethodFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_verification_method, parent, false);
        methodListRecyclerView = view.findViewById(R.id.method_list);
        prepareView();
        return view;
    }

    private void prepareView() {
        VerificationMethodAdapter adapter = VerificationMethodAdapter.createInstance(getList(),
                this);
        methodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        methodListRecyclerView.setAdapter(adapter);
    }

    private ArrayList<MethodItem> getList() {
        if (cacheManager != null
                && cacheManager.getConvertObjData(VerificationActivity.PASS_MODEL,
                VerificationPassModel.class) != null) {
            VerificationPassModel passModel = cacheManager.getConvertObjData(VerificationActivity
                    .PASS_MODEL, VerificationPassModel.class);
            return passModel.getListAvailableMethods();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onMethodSelected(int type) {
        if (getActivity() instanceof VerificationActivity) {
            switch (type) {
                case VerificationActivity.TYPE_SMS: {
                    ((VerificationActivity) getActivity()).goToSmsVerification();
                    break;
                }
                case VerificationActivity.TYPE_PHONE_CALL: {
                    ((VerificationActivity) getActivity()).goToCallVerification();
                    break;
                }
                default:
                    throw new RuntimeException("TYPE NOT DEFINED");
            }
        }
    }
}
