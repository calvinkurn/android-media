package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.session.R;

/**
 * @author by yfsx on 14/03/18.
 */

public class WelcomePageFragment extends BaseDaggerFragment {

    private final static String URL_BACKGROUND
            = "https://ecs7.tokopedia.net/img/android/icon_welcome/xxhdpi/welcome.png";

    private ImageView background;
    private Button btnContinue;
    private TextView btnProfileCompletion;

    public static WelcomePageFragment newInstance() {
        return new WelcomePageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page, container, false);
        background = (ImageView) view.findViewById(R.id.background);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        btnProfileCompletion = (TextView) view.findViewById(R.id.btn_profile_completion);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewListener();
    }

    public void initView() {
        ImageHandler.LoadImage(background, URL_BACKGROUND);
    }

    public void initViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        btnProfileCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    @Override
    protected String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_ACCOUNT_ACTIVATION;
    }

    @Override
    protected void initInjector() {

    }
}
