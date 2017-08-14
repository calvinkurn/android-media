package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.CreateResolutionCenterPresenter;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;

import javax.inject.Inject;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterFragment extends BaseDaggerFragment implements CreateResolutionCenterView {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";

    FrameLayout ffChooseProductProblem, ffSolution, ffUploadProve;
    TextView tvChooseProductProblem, tvChooseProductProblemTitle, tvSolution, tvSolutionTitle, tvUploadProve, tvUploadProveTitle;
    ImageView ivChooseProductProblem, ivSolution, ivUploadProve;
    Button btnCreateResolution;
    ButtonState buttonState;

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
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

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

        buttonState = new ButtonState();
        buttonState.isChooseProductProblemButtonEnabled = true;
        updateView(buttonState);
    }

    @Override
    protected void setViewListener() {
        ffChooseProductProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.chooseProductProblemClicked(buttonState);
            }
        });

        ffSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.solutionClicked(buttonState);
            }
        });

        ffUploadProve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.uploadProveClicked(buttonState);
            }
        });

        btnCreateResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createResoClicked(buttonState);
            }
        });
    }


    @Override
    public void updateView(ButtonState buttonState) {
        ffChooseProductProblem.setEnabled(buttonState.isChooseProductProblemButtonEnabled);
        ffSolution.setEnabled(buttonState.isSolutionButtonEnabled);
        ffUploadProve.setEnabled(buttonState.isUploadProveButtonEnabled);
        btnCreateResolution.setEnabled(buttonState.isCreateResolutionButtonEnabled);
        btnCreateResolution.setClickable(buttonState.isCreateResolutionButtonEnabled);

        if (buttonState.isChooseProductProblemButtonEnabled) {
            ffChooseProductProblem.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));
        } else {
            ffChooseProductProblem.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_disable));
        }

        if (buttonState.isChooseProductProblemHaveValue) {
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
        } else {
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
        }

        if (buttonState.isSolutionButtonEnabled) {
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));
        } else {
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_disable));
        }

        if (buttonState.isSolutionHaveValue) {
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
        } else {
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
        }

        if (buttonState.isUploadProveButtonEnabled) {
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));
        } else {
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_disable));
        }

        if (buttonState.isUploadProveHaveValue) {
            ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_complete));
        } else {
            ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.chevron_thin_right));
        }

        if (buttonState.isCreateResolutionButtonEnabled) {
            btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
        } else {
            btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        }
    }

    @Override
    public void showCreateResoResponse(boolean isSuccess, String message) {
        if (isSuccess) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
