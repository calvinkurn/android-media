package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.session.R;

/**
 * @author by yfsx on 14/03/18.
 */

public class WelcomePageFragment extends BaseDaggerFragment {

    private final static String URL_BACKGROUND
            = "https://ecs7.tokopedia.net/img/android/welcome_baloon/xhdpi/welcome_baloon.png";

    private ImageView background;
    private TextView btnContinue;
    private TextView btnProfileCompletion;

    public static WelcomePageFragment newInstance() {
        return new WelcomePageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page, container, false);
        background = (ImageView) view.findViewById(R.id.background);
        btnContinue = (TextView) view.findViewById(R.id.btn_continue);
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
        loadImage(background, URL_BACKGROUND);
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
        return null;
    }

    @Override
    protected void initInjector() {

    }

    public void loadImage(ImageView imageview, String url) {
        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(imageview);
        }
    }
}
