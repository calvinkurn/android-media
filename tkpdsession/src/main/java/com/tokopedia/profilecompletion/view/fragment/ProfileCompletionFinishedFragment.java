package com.tokopedia.profilecompletion.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.session.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionFinishedFragment extends BaseDaggerFragment {

    public static final String TAG = "finished";
    private View finish;
    private UserSessionInterface userSession;


    public static ProfileCompletionFinishedFragment createInstance() {
        return new ProfileCompletionFinishedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_profile_completion_finish, container, false);
        initView(parentView);
        setViewListener();
        return parentView;
    }

    protected void initView(View view) {
        finish = view.findViewById(R.id.done);
    }

    protected void setViewListener() {
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    userSession = new UserSession(getContext());
                    Intent intent = RouteManager.getIntent(
                            getContext(),
                            ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID,
                                    userSession.getUserId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
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
}
