package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.CreateResolutionCenterPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterFragment extends BaseDaggerFragment implements CreateResolutionCenter.View {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";
    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    private static final int REQUEST_STEP1 = 1001;
    private static final int REQUEST_STEP2 = 1002;
    private static final int REQUEST_STEP3 = 1003;

    FrameLayout ffChooseProductProblem, ffSolution, ffUploadProve;
    TextView tvChooseProductProblem, tvChooseProductProblemTitle, tvSolution, tvSolutionTitle, tvUploadProve, tvUploadProveTitle;
    ImageView ivChooseProductProblem, ivSolution, ivUploadProve;
    Button btnCreateResolution;

    String orderId = "";

    @Inject
    CreateResolutionCenterPresenter presenter;

    public static CreateResolutionCenterFragment newInstance(ActionParameterPassData passData) {
        CreateResolutionCenterFragment fragment = new CreateResolutionCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CreateResolutionCenterFragment newInstance() {
        CreateResolutionCenterFragment fragment = new CreateResolutionCenterFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerCreateResoComponent daggerCreateResoComponent =
                (DaggerCreateResoComponent) DaggerCreateResoComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        ActionParameterPassData actionParameterPassData = (ActionParameterPassData)arguments.get(KEY_PARAM_PASS_DATA);
        orderId = actionParameterPassData.getOrderID();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_create_resolution_center_base;
    }

    @Override
    protected void initView(View view) {

        ffChooseProductProblem = (FrameLayout) view.findViewById(R.id.ff_product_problem);
        ffSolution = (FrameLayout) view.findViewById(R.id.ff_solution);
        ffUploadProve = (FrameLayout) view.findViewById(R.id.ff_prove);

        tvChooseProductProblem = (TextView) view.findViewById(R.id.tv_product_problem);
        tvChooseProductProblemTitle = (TextView) view.findViewById(R.id.tv_product_problem_title);
        tvSolution = (TextView) view.findViewById(R.id.tv_solution);
        tvSolutionTitle = (TextView) view.findViewById(R.id.tv_solution_title);
        tvUploadProve = (TextView) view.findViewById(R.id.tv_prove);
        tvUploadProveTitle = (TextView) view.findViewById(R.id.tv_prove_title);

        ivChooseProductProblem = (ImageView) view.findViewById(R.id.iv_product_problem);
        ivSolution = (ImageView) view.findViewById(R.id.iv_solution);
        ivUploadProve = (ImageView) view.findViewById(R.id.iv_prove);

        btnCreateResolution = (Button) view.findViewById(R.id.btn_create_resolution);

        updateView(new ResultViewModel());
        presenter.loadProductProblem(orderId);
    }

    @Override
    protected void setViewListener() {
        ffChooseProductProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.chooseProductProblemClicked();
            }
        });

        ffSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.solutionClicked();
            }
        });

        ffUploadProve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.uploadProveClicked();
            }
        });

        btnCreateResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createResoClicked();
            }
        });
    }


    @Override
    public void updateView(ResultViewModel resultViewModel) {

        ffChooseProductProblem.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));

        if (resultViewModel.problem.size() != 0) {
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));
        } else {
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_disable));
        }

        if (resultViewModel.solution != 0) {
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));
        } else {
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_disable));
        }

        if (resultViewModel.attachmentCount != 0) {
            ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
            btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
        } else {
            ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
            btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        }
    }

    @Override
    public void transitionToChooseProductAndProblemPage(ProductProblemListViewModel productProblemListViewModel,
                                                        ArrayList<ProblemResult> problemResults) {
        Intent intent = new Intent(getActivity(), ProductProblemListActivity.class);
        intent.putExtra(KEY_PARAM_PASS_DATA, productProblemListViewModel);
        intent.putParcelableArrayListExtra(PROBLEM_RESULT_LIST_DATA, problemResults);
        startActivityForResult(intent, REQUEST_STEP1);
    }

    @Override
    public void showCreateResoResponse(boolean isSuccess, String message) {
        if (isSuccess) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccessToast() {

    }

    @Override
    public void showErrorToast(String error) {
        Toast.makeText(getActivity(), "error : " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STEP1) {
            if (resultCode == getActivity().RESULT_OK) {
                presenter.addResultFromStep1(data.<ProblemResult>getParcelableArrayListExtra(PROBLEM_RESULT_LIST_DATA));
            }
        }
    }

    @Override
    public void transitionToSolutionPage(ResultViewModel resultViewModel) {
        Intent intent = new Intent(getActivity(), SolutionListActivity.class);
        intent.putExtra(KEY_PARAM_PASS_DATA, resultViewModel);
        intent.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        startActivityForResult(intent, REQUEST_STEP2);
    }
}
