package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemDetailFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 21/08/17.
 */

public class ProductProblemDetailFragment extends BaseDaggerFragment implements com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragment.View {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";
    public static final String PROBLEM_RESULT_DATA = "problem_result_data";
    public static final String RESULT_DATA = "result_data";
    public static final String RESULT_STEP_CODE = "result_step_code";
    public static final int FREE_RETURN = 3;

    ProductProblemViewModel productProblemViewModel;
    ProblemResult problemResult;

    ImageView ivProductImage, btnInfo;
    TextView tvProductName, tvProductPrice, tvQty;
    Button btnArrived, btnNotArrived, btnSaveAndChooseOther, btnSave, btnCancel;
    LinearLayout llFreeReturn;
    SpinnerTextView stvProblem;
    TkpdTextInputLayout tilComplainReason;
    EditText etComplainReason;
    ImageButton btnPlus, btnMinus;

    ProductProblemDetailFragmentPresenter presenter;

    public static ProductProblemDetailFragment newInstance(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult) {
        ProductProblemDetailFragment fragment = new ProductProblemDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_PROBLEM_DATA, productProblemViewModel);
        bundle.putParcelable(PROBLEM_RESULT_DATA, problemResult);
        fragment.setArguments(bundle);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new ProductProblemDetailFragmentPresenter(getActivity());
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productProblemViewModel = arguments.getParcelable(PRODUCT_PROBLEM_DATA);
        problemResult = arguments.getParcelable(PROBLEM_RESULT_DATA);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_problem_detail;
    }

    @Override
    protected void initView(View view) {
        ivProductImage = (ImageView) view.findViewById(R.id.iv_product_image);
        tvProductName = (TextView) view.findViewById(R.id.tv_product_name);
        tvProductPrice = (TextView) view.findViewById(R.id.tv_product_price);
        tvQty = (TextView) view.findViewById(R.id.tv_qty);
        btnArrived = (Button) view.findViewById(R.id.btn_arrived);
        btnNotArrived = (Button) view.findViewById(R.id.btn_not_arrived);
        btnSaveAndChooseOther = (Button) view.findViewById(R.id.btn_save_and_choose_other);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        stvProblem = (SpinnerTextView) view.findViewById(R.id.stv_problem);
        tilComplainReason = (TkpdTextInputLayout) view.findViewById(R.id.til_complain);
        etComplainReason = (EditText) view.findViewById(R.id.et_complain);
        btnPlus = (ImageButton) view.findViewById(R.id.btn_plus);
        btnMinus = (ImageButton) view.findViewById(R.id.btn_minus);
        btnInfo = (ImageView) view.findViewById(R.id.btn_info);
        llFreeReturn = (LinearLayout) view.findViewById(R.id.ll_free_return);

        stvProblem.setHint(getActivity().getResources().getString(R.string.string_choose_problem));
        tilComplainReason.setHint(getActivity().getResources().getString(R.string.string_complain_reason));

        presenter.populateData(productProblemViewModel, problemResult);

    }

    @Override
    protected void setViewListener() {
        btnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btnArrivedClicked();
            }
        });

        btnNotArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btnNotArrivedClicked();
            }
        });

        stvProblem.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                presenter.updateTroubleValue(value);
            }
        });

        etComplainReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.updateComplainReason(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.increaseQty();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.decreaseQty();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btnSaveClicked(false);
            }
        });

        btnSaveAndChooseOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btnSaveClicked(true);
            }
        });
    }

    @Override
    protected String getScreenName() {
        return productProblemViewModel.getOrder().getProduct().getName();
    }

    @Override
    public void populateDataToScreen(ProductProblemViewModel productProblemViewModel) {
        Glide.with(getActivity()).load(productProblemViewModel.getOrder().getProduct().getThumb()).into(ivProductImage);
        tvProductName.setText(productProblemViewModel.getOrder().getProduct().getName());
        tvProductPrice.setText(productProblemViewModel.getOrder().getProduct().getAmount().getIdr());
        if (productProblemViewModel.getOrder().getDetail().getReturnable() == FREE_RETURN) {
            llFreeReturn.setVisibility(View.VISIBLE);
        } else {
            llFreeReturn.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateArriveStatusButton(boolean isArrived, boolean canShowInfo) {
        btnInfo.setVisibility(View.GONE);
        if(canShowInfo) {
            presenter.updateSpinner(true);
            btnInfo.setVisibility(View.VISIBLE);
            buttonDisabled(btnNotArrived);
            buttonSelected(btnArrived);
        } else {
            if (isArrived) {
                buttonCanSelected(btnNotArrived);
                buttonSelected(btnArrived);
            } else {
                buttonCanSelected(btnArrived);
                buttonSelected(btnNotArrived);
            }
        }
//        if (isArrived) {
//            buttonCanSelected(btnNotArrived);
//            buttonSelected(btnArrived);
//        } else {
//            buttonCanSelected(btnArrived);
//            buttonSelected(btnNotArrived);
//        }


    }
    public void buttonCanSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable1));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.green_btn));
    }

    public void buttonSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_70));
    }

    @Override
    public void populateReasonSpinner(String[] reasonStringArray) {
        if (reasonStringArray != null) {
            stvProblem.setValues(reasonStringArray);
            stvProblem.setEntries(reasonStringArray);
        }
    }

    @Override
    public void updateComplainReasonView(boolean isSuccess, String message) {
        if (!isSuccess) {
            tilComplainReason.setError(message);
        } else {
            tilComplainReason.hideErrorSuccess();
        }
    }

    @Override
    public void updateComplainReasonValue(String complainString) {
        etComplainReason.setText(complainString);
    }

    @Override
    public void updatePlusMinusButton(int currentValue, int maxValue) {
        tvQty.setText(String.valueOf(currentValue));
        btnPlus.setEnabled(true);
        btnMinus.setEnabled(true);
        if (currentValue == 1) {
            btnMinus.setEnabled(false);
        }
        if (currentValue == maxValue) {
            btnPlus.setEnabled(false);
        }
    }

    @Override
    public void updateBottomMainButton(boolean isEnabled) {
        buttonDisabled(btnSaveAndChooseOther);
        buttonDisabled(btnSave);
        if (isEnabled) {
            buttonSelected(btnSaveAndChooseOther);
            buttonCanSelected(btnSave);
        }
    }

    @Override
    public void saveData(ProblemResult problemResult, int resultStepCode) {
        Intent output = new Intent();
        output.putExtra(RESULT_DATA, problemResult);
        output.putExtra(RESULT_STEP_CODE, resultStepCode);
        getActivity().setResult(getActivity().RESULT_OK, output);
        getActivity().finish();
    }
}
