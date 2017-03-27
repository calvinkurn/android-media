package com.tokopedia.discovery.intermediary.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.di.IntermediaryDependencyInjector;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;

import java.util.List;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryFragment extends BaseDaggerFragment implements IntermediaryContract.View {

    private IntermediaryContract.Presenter presenter;
    public static final String TAG = "INTERMEDIARY_FRAGMENT";

    public static IntermediaryFragment createInstance() {return new IntermediaryFragment();}

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        presenter = IntermediaryDependencyInjector.getPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_intermediary, container, false);

        presenter.attachView(this);
        //presenter.getIntermediaryCategory();
        return parentView;
    }

    @Override
    public void renderHeader(HeaderModel headerModel) {

    }

    @Override
    public void renderCategoryChildren(List<ChildCategoryModel> childCategoryModelList) {

    }

    @Override
    public void renderCuratedProducts(List<CuratedSectionModel> curatedSectionModelList) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void emptyState() {

    }
}
