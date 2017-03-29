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
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.di.IntermediaryDependencyInjector;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryFragment extends BaseDaggerFragment implements IntermediaryContract.View {

    private TkpdProgressDialog progressDialog;


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

        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);

        presenter.attachView(this);
        presenter.getIntermediaryCategory(((IntermediaryActivity) getActivity()).getDepartmentId());

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
        progressDialog.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
               //TODO
            }
        }).showRetrySnackbar();
    }
}
