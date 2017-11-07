package com.tokopedia.ride.base.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alvarisi on 3/13/17.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    protected abstract void initInjector();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public String getScreenName() {
        return ((BaseActivity) getActivity()).getScreenName();
    }
}