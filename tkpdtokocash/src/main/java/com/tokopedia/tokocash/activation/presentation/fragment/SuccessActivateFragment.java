package com.tokopedia.tokocash.activation.presentation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.WalletUserSession;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/2/18.
 */

public class SuccessActivateFragment extends BaseDaggerFragment {

    private TextView descSuccess;
    private Button backToHomeBtn;
    private ActionListener listener;

    @Inject
    CacheManager globalCacheManager;
    @Inject
    WalletUserSession walletUserSession;

    public static SuccessActivateFragment newInstance() {
        SuccessActivateFragment fragment = new SuccessActivateFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_activate_tokocash, container, false);
        descSuccess = view.findViewById(R.id.desc_success);
        backToHomeBtn = view.findViewById(R.id.back_to_home_btn);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String phoneNumber = "<b>" + walletUserSession.getPhoneNumber() + "</b>";
        String desc = String.format(getActivity().getString(R.string.desc_success_tokocash), phoneNumber);
        descSuccess.setText(MethodChecker.fromHtml(desc));

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCacheBalanceTokoCash();
                listener.onBackPressToHome();
            }
        });
    }


    @NonNull
    private void deleteCacheBalanceTokoCash() {
        globalCacheManager.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    public interface ActionListener {
        void onBackPressToHome();
    }
}
