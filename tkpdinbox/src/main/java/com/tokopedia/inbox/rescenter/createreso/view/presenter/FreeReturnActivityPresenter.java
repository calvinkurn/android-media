package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.FreeReturnActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class FreeReturnActivityPresenter
        implements FreeReturnActivityListener.Presenter {
    Context context;
    FreeReturnActivityListener.View mainView;

    public FreeReturnActivityPresenter(Context context, FreeReturnActivityListener.View mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void attachView(FreeReturnActivityListener.View view) {
        this.mainView = view;
    }

    @Override
    public void initFragment(String url) {
        mainView.inflateFragment(FragmentGeneralWebView.createInstance(url),
                FragmentGeneralWebView.class.getSimpleName());
    }
}
