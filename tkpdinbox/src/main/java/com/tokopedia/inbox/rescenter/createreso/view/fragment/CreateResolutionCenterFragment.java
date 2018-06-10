package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.activity.AttachmentActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.CreateResolutionCenterPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.utils.CurrencyFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterFragment extends BaseDaggerFragment implements CreateResolutionCenter.View {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";
    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String PARAM_RESOLUTION_ID = "reso_id";
    public static final String PARAM_ORDER_ID = "order_id";

    private static final int REQUEST_STEP1 = 1001;
    private static final int REQUEST_STEP2 = 1002;
    private static final int REQUEST_STEP3 = 1003;

    FrameLayout ffChooseProductProblem, ffSolution, ffUploadProve;
    RelativeLayout rlProgress;
    TextView tvChooseProductProblem, tvChooseProductProblemTitle, tvSolution, tvSolutionTitle, tvUploadProve, tvUploadProveTitle;
    ImageView ivChooseProductProblem, ivSolution, ivUploadProve;
    Button btnCreateResolution;
    View problemView, footer;
    String orderId = "";
    ProgressBar progressBar;
    ResultViewModel resultViewModel;
    TypedValue typedValue100, typedValue70, typedValue38;
    Float float100, float70, float38;

    String resolutionId;

    @Inject
    CreateResolutionCenterPresenter presenter;

    public static CreateResolutionCenterFragment newInstance(ActionParameterPassData passData) {
        CreateResolutionCenterFragment fragment = new CreateResolutionCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CreateResolutionCenterFragment newRecomplaintInstance(String orderId, String resolutionId) {
        CreateResolutionCenterFragment fragment = new CreateResolutionCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ORDER_ID, orderId);
        bundle.putString(PARAM_RESOLUTION_ID , resolutionId);
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
        state.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        state.putString(PARAM_RESOLUTION_ID, resolutionId);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        resultViewModel = savedState.getParcelable(RESULT_VIEW_MODEL_DATA);
        resolutionId = savedState.getString(resolutionId);
        presenter.getRestoreData(resultViewModel);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments.get(PARAM_RESOLUTION_ID) != null) {
            resolutionId = arguments.getString(PARAM_RESOLUTION_ID);
            orderId = arguments.getString(PARAM_ORDER_ID);
        } else {
            ActionParameterPassData actionParameterPassData = (ActionParameterPassData) arguments.get(KEY_PARAM_PASS_DATA);
            orderId = actionParameterPassData.getOrderID();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_create_resolution_center_base;
    }

    @Override
    protected void initView(View view) {

        problemView = view.findViewById(R.id.problem_view);
        footer = view.findViewById(R.id.footer);
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

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        typedValue100 = new TypedValue();
        typedValue70 = new TypedValue();
        typedValue38 = new TypedValue();

        getResources().getValue(R.dimen.font_alpha_100, typedValue100, true);
        getResources().getValue(R.dimen.font_alpha_70, typedValue70, true);
        getResources().getValue(R.dimen.font_alpha_38, typedValue38, true);

        float100 = typedValue100.getFloat();
        float70 = typedValue70.getFloat();
        float38 = typedValue38.getFloat();

        rlProgress = (RelativeLayout) view.findViewById(R.id.rl_progress);
        updateView(new ResultViewModel());
        if (resolutionId != null) {
            presenter.loadProductProblem(orderId, resolutionId);
        } else {
            presenter.loadProductProblem(orderId);
        }
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
                UnifyTracking.eventCreateResoPre();
            }
        });
    }


    @Override
    public void updateView(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        ffSolution.setEnabled(false);
        ffUploadProve.setEnabled(false);
        btnCreateResolution.setEnabled(false);

        ffChooseProductProblem.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_layout_enable));


        if (resultViewModel.problem.size() != 0) {
            ffSolution.setEnabled(true);
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.ic_complete));
            ffChooseProductProblem.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_enable_with_green));
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_enable));
            updateProductProblemString(resultViewModel.problem, tvChooseProductProblem);
            tvSolution.setTextColor(context.getResources().getColor(R.color.black_70));
            tvSolutionTitle.setTextColor(context.getResources().getColor(R.color.black_70));
            ivChooseProductProblem.setAlpha(float100);
            ivSolution.setAlpha(float70);
            tvUploadProve.setText(context.getResources()
                    .getString(R.string.string_upload_prove_information));
            ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.chevron_thin_right));
            ivUploadProve.setAlpha(float38);
        } else {
            ffSolution.setEnabled(false);
            ivChooseProductProblem.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.chevron_thin_right));
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_disable));
            tvChooseProductProblem.setText(context.getResources().getString(R.string.string_choose_product_problem));
            tvSolution.setTextColor(context.getResources().getColor(R.color.black_38));
            tvSolutionTitle.setTextColor(context.getResources().getColor(R.color.black_38));
            ivChooseProductProblem.setAlpha(float100);
            ivSolution.setAlpha(float38);
            ivUploadProve.setAlpha(float38);
        }

        if (resultViewModel.solution != 0) {
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.ic_complete));
            ffSolution.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_enable_with_green));
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_enable));
            if (!resultViewModel.isAttachmentRequired) {
                ffUploadProve.setEnabled(false);
                tvUploadProve.setTextColor(context.getResources().getColor(R.color.black_38));
                tvUploadProveTitle.setTextColor(context.getResources().getColor(R.color.black_38));
                ivUploadProve.setAlpha(float38);
            } else {
                ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.chevron_thin_right));
                ffUploadProve.setEnabled(true);
                tvUploadProve.setTextColor(context.getResources().getColor(R.color.black_70));
                tvUploadProveTitle.setTextColor(context.getResources().getColor(R.color.black_70));
                ivUploadProve.setAlpha(float70);
            }
            updateSolutionString(resultViewModel, tvSolution);
            ivSolution.setAlpha(float100);
        } else {
            ffUploadProve.setEnabled(false);
            ivSolution.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.chevron_thin_right));
            ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_layout_disable));
            tvSolution.setText(context.getResources().getString(R.string.string_choose_solution));
            tvUploadProve.setTextColor(context.getResources().getColor(R.color.black_38));
            tvUploadProveTitle.setTextColor(context.getResources().getColor(R.color.black_38));
            if (resultViewModel.problem.size() != 0) {
                ivSolution.setAlpha(float70);
            } else {
                ivSolution.setAlpha(float38);
            }
        }

        btnCreateResolution.setEnabled(false);
        btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(),
                R.drawable.bg_button_disable));
        if (resultViewModel.problem.size() != 0 && resultViewModel.solution != 0) {
            if (resultViewModel.isAttachmentRequired) {
                if (resultViewModel.message.remark != null) {
                    btnCreateResolution.setEnabled(true);
                    btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(),
                            R.drawable.bg_button_enable));
                    ivUploadProve.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_complete));
                    ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(),
                            R.drawable.bg_layout_enable_with_green));
                    btnCreateResolution.setTextColor(context.getResources().getColor(R.color.white));
                    ivUploadProve.setAlpha(1f);
                    tvUploadProve.setText(context.getResources().getString(R.string.string_step3_complete));
                }
            } else {
                btnCreateResolution.setEnabled(true);
                btnCreateResolution.setBackground(ContextCompat.getDrawable(getActivity(),
                        R.drawable.bg_button_enable));
                ffUploadProve.setBackground(ContextCompat.getDrawable(getActivity(),
                        R.drawable.bg_layout_disable));
                btnCreateResolution.setTextColor(context.getResources().getColor(R.color.white));
                ffUploadProve.setEnabled(false);
                tvUploadProve.setText(context.getResources().getString(R.string.string_upload_prove_information));
            }
        }
    }

    public void updateProductProblemString(List<ProblemResult> problemResultList, TextView textView) {
        String problemResultString = "";
        boolean isType1Selected = false;
        for (ProblemResult problemResult : problemResultList) {
            if (problemResult.type == 1) {
                isType1Selected = true;
            }
        }
        if (isType1Selected) {
            problemResultString += context.getString(R.string.string_difference_ongkir);
            if (problemResultList.size() > 1) {
                problemResultString += " & ";
                problemResultString += (isType1Selected ?
                        problemResultList.size() - 1 :
                        problemResultList.size()) + " " + context.getString(R.string.string_problem_product);
            }
        } else {
            problemResultString += (isType1Selected ?
                    problemResultList.size() - 1 :
                    problemResultList.size()) + " " + context.getString(R.string.string_problem_product);
        }
        textView.setText(problemResultString);
    }

    public void updateSolutionString(ResultViewModel resultViewModel, TextView textView) {
        textView.setText(resultViewModel.refundAmount != 0 && resultViewModel.solutionName != null ?
                resultViewModel.solutionName.replace(
                        context.getResources().getString(R.string.string_return_value),
                        CurrencyFormatter.formatDotRupiah(String.valueOf(resultViewModel.refundAmount))) :
                resultViewModel.solutionName);
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

    public void showProgressBar(boolean isCreateReso) {
        if (!isCreateReso) {
            problemView.setVisibility(View.INVISIBLE);
            footer.setVisibility(View.INVISIBLE);
        }
        if (rlProgress.getVisibility() == View.GONE) {
            rlProgress.setVisibility(View.VISIBLE);
        }
        rlProgress.setEnabled(true);
        rlProgress.setClickable(true);
    }

    public void dismissProgressBar() {
        if (rlProgress.getVisibility() == View.VISIBLE) {
            rlProgress.setVisibility(View.GONE);
        }
        rlProgress.setEnabled(false);
        rlProgress.setClickable(false);
    }

    @Override
    public void showLoading(boolean isCreateReso) {
        showProgressBar(isCreateReso);
    }


    @Override
    public void successLoadProductProblemData(ProductProblemResponseDomain responseDomain) {
        problemView.setVisibility(View.VISIBLE);
        footer.setVisibility(View.VISIBLE);
        dismissProgressBar();
        presenter.updateProductProblemResponseDomain(responseDomain);
    }

    @Override
    public void errorLoadProductProblemData(String error) {
        dismissProgressBar();
        NetworkErrorHelper.showEmptyState(context, getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (resolutionId != null) {
                    presenter.loadProductProblem(orderId, resolutionId);
                } else {
                    presenter.loadProductProblem(orderId);
                }
            }
        });
    }

    @Override
    public void successCreateResoWithoutAttachment(String resolutionId, String cacheKey, String message, String shopName) {
        dismissProgressBar();
        finishResolution(resolutionId, message, shopName);
    }

    @Override
    public void errorCreateResoWithoutAttachment(String error) {
        dismissProgressBar();
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void successCreateResoWithAttachment(String resolutionId, String message, String shopName) {
        dismissProgressBar();
        finishResolution(resolutionId, message, shopName);
    }

    @Override
    public void errorCreateResoWithAttachment(String error) {
        dismissProgressBar();
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    private void finishResolution(String resolutionId, String message, String shopName) {
        dismissProgressBar();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        presenter.getInboxAndDetailResoStackBuilder(context, resolutionId, shopName).startActivities();
        getActivity().finish();
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
    public void transitionToSolutionPage(ResultViewModel resultViewModel) {
        Intent intent = SolutionListActivity.newCreateInstance(context, resultViewModel);
        startActivityForResult(intent, REQUEST_STEP2);
    }

    @Override
    public void transitionToUploadProvePage(ResultViewModel resultViewModel) {
        Intent intent = new Intent(getActivity(), AttachmentActivity.class);
        intent.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        startActivityForResult(intent, REQUEST_STEP3);
    }

    @Override
    public void showCreateComplainDialog(final ResultViewModel resultViewModel) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_create_complain);
        TextView tvProblem = (TextView) dialog.findViewById(R.id.tv_problem);
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnCreateComplain = (Button) dialog.findViewById(R.id.btn_create_complain);

        updateProductProblemString(resultViewModel.problem, tvProblem);
        updateSolutionString(resultViewModel, tvSolution);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                UnifyTracking.eventCreateResoUnconfirm();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                UnifyTracking.eventCreateResoUnconfirm();
            }
        });

        btnCreateComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultViewModel.isAttachmentRequired)
                    presenter.callCreateResolutionAPIWithAttachment();
                else
                    presenter.callCreateResolutionAPI();
                UnifyTracking.eventCreateResoConfirm();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STEP1) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.addResultFromStep1(data.<ProblemResult>getParcelableArrayListExtra(PROBLEM_RESULT_LIST_DATA));
            }
        } else if (requestCode == REQUEST_STEP2) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.addResultFromStep2((ResultViewModel) data.getParcelableExtra(RESULT_VIEW_MODEL_DATA));
            }
        } else if (requestCode == REQUEST_STEP3) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.addResultFromStep3((ResultViewModel) data.getParcelableExtra(RESULT_VIEW_MODEL_DATA));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
