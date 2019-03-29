package com.tokopedia.tkpdpdp.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.report.ReportProductPass;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.session.model.network.ReportType;
import com.tokopedia.core.session.model.network.ReportTypeModel;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * @author stevenfredian on 7/4/16.
 */
public class ReportProductDialogFragment extends DialogFragment implements ReportProductDialogView {

    ProductDetailData productDetailData;

    TextView cancelButton;
    TextView reportButton;
    EditText reportDesc;
    TextInputLayout wrapper;
    Spinner reportTypeSpinner;
    TextView errorSpinner;
    TextView dummySpinner;
    TextView redirectText;
    View mainLayout;
    View actionLayout;
    ProgressBar progressBar;

    List<ReportType> reportTypeList;
    ProductDetailFragment listener;
    ArrayAdapter<String> reportTypeName;
    private Bundle recentBundle;
    private CacheInteractor cacheInteractor;
    private RetrofitInteractor retrofitInteractor;

    public static ReportProductDialogFragment createInstance(ProductDetailData productData, Bundle recentBundle) {
        ReportProductDialogFragment fragment = new ReportProductDialogFragment();
        fragment.productDetailData = productData;
        fragment.recentBundle = recentBundle;
        fragment.retrofitInteractor = new RetrofitInteractorImpl();
        fragment.cacheInteractor = new CacheInteractorImpl();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(com.tokopedia.core2.R.layout.dialog_report_product, container);
        initview(view);
        setContent();
        return view;
    }

    private void initview(View view) {
        cancelButton = (TextView) view.findViewById(R.id.cancel_but);
        reportButton = (TextView) view.findViewById(R.id.report);
        reportDesc = (EditText) view.findViewById(R.id.report_desc);
        wrapper = (TextInputLayout) view.findViewById(R.id.wrapper);
        reportTypeSpinner = (Spinner) view.findViewById(R.id.report_type);
        errorSpinner = (TextView) view.findViewById(R.id.error_spinner);
        dummySpinner = (TextView) view.findViewById(R.id.dummy_spinner);
        redirectText = (TextView) view.findViewById(R.id.redirect_text);
        mainLayout = view.findViewById(R.id.scrollView);
        actionLayout =  view.findViewById(R.id.action_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetError();
                if (isValidForm()) {
                    doReport();
                    reportButton.setEnabled(false);
                }
            }
        });

        dummySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummySpinner.setVisibility(View.GONE);
                reportTypeSpinner.setVisibility(View.VISIBLE);
                reportTypeSpinner.performClick();
            }
        });

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                errorSpinner.setVisibility(View.GONE);
                ReportType reportChose = reportTypeList.get(reportTypeSpinner.getSelectedItemPosition());
                int response = reportChose.getReportResponse();

                if (response == 0) {
                    wrapper.setVisibility(View.GONE);
                    reportButton.setVisibility(View.GONE);
                    redirectText.setVisibility(View.VISIBLE);
                    String link = reportChose.getReportUrl();
                    setRedirect(reportTypeName.getItem(reportTypeSpinner.getSelectedItemPosition()), false, link);
                } else {
                    wrapper.setVisibility(View.VISIBLE);
                    reportButton.setVisibility(View.VISIBLE);
                    redirectText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setContent() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        showLayout(false);
        cacheInteractor.loadReportTypeFromCache(this);
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        super.onResume();
    }

    private boolean isValidForm() {
        if (reportDesc.getText().toString().trim().length() > 0) {
            return true;
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(getString(com.tokopedia.core2.R.string.empty_desc));
            return false;
        }
    }


    private void setRedirect(String item, boolean status, String link) {
        String string = getResources().getString(com.tokopedia.core2.R.string.redirect_report_product);

        String caseReplace = "kasus";
        SpannableString stringNoResult = new SpannableString(string.replace(caseReplace, item));

        String linkStatus = "link ini";
        if (link.equals("https://www.tokopedia.com/contact-us.pl")) {
            status = true;
        }

        Intent intent = InboxRouter.getContactUsActivityIntent(getActivity());

        stringNoResult.setSpan(redirect(intent, status, link)
                , stringNoResult.toString().indexOf(linkStatus)
                , stringNoResult.toString().indexOf(linkStatus) + linkStatus.length(), 0);

        redirectText.setMovementMethod(LinkMovementMethod.getInstance());
        redirectText.setText(stringNoResult);
    }

    private ClickableSpan redirect(final Intent intent, final boolean status, final String link) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                intent.putExtra("PARAM_REDIRECT", status);
                intent.putExtra("PARAM_URL", link);
                getActivity().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getActivity(), com.tokopedia.core2.R.color.blue_link));
            }
        };
    }

    private void resetError() {
        errorSpinner.setVisibility(View.GONE);
        wrapper.setErrorEnabled(false);
        wrapper.setError(null);
    }

    private void doReport() {
        ReportProductPass pass = new ReportProductPass();
        pass.setProductID(String.valueOf(productDetailData.getInfo().getProductId()));
        int type = reportTypeList.get(reportTypeSpinner.getSelectedItemPosition()).getReportId();
        pass.setReportType(String.valueOf(type));
        pass.setDesc(reportDesc.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReportProductPass.TAG, pass);
        ((ProductInfoActivity) getActivity()).doReport(bundle);
        listener.setRecentBundle(bundle);
    }

    public void onErrorAction(String error) {
        if (error.toLowerCase().contains("kategori")) {
            errorSpinner.setVisibility(View.VISIBLE);
            errorSpinner.setText(error);
        } else if (error.toLowerCase().contains("deskripsi")) {
            wrapper.setErrorEnabled(true);
            wrapper.setError(error);
        } else {
            SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
            dismiss();
        }
    }


    public void saveToCache(String data) {
        cacheInteractor.saveReportTypeToCache(data);
    }


    @Override
    public void downloadReportType() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        retrofitInteractor.downloadReportType(getActivity(), productDetailData.getInfo().getProductId(), this);
    }

    public void setReportAdapterFromCache(String data) {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ReportTypeModel reportTypeModel = new GsonBuilder().create().fromJson(data, ReportTypeModel.class);
        reportTypeList = reportTypeModel.getReportType();
        setReportAdapterFromNetwork(reportTypeList);
    }

    @Override
    public void showError(String errorString) {
        SnackbarManager.make(getActivity(), errorString, Snackbar.LENGTH_LONG).show();
        dismiss();
    }

    public void setReportAdapterFromNetwork(List<ReportType> reportTypeList) {
        showLayout(true);
        List<String> reportNameList = new ArrayList<>();
        List<Integer> reportTypeValue = new ArrayList<>();
        for (ReportType reportType : reportTypeList) {
            reportNameList.add(reportType.getReportTitle());
            reportTypeValue.add(reportType.getReportId());
        }
        this.reportTypeList = reportTypeList;
        reportTypeName = new ArrayAdapter<>(getActivity(), com.tokopedia.core2.R.layout.spinner_item, reportNameList);
        reportTypeSpinner.setAdapter(reportTypeName);
        if (recentBundle != null) {
            ReportProductPass pass = (ReportProductPass) recentBundle.get(ReportProductPass.TAG);
            reportDesc.setText(pass.getDesc());
            int index = reportTypeValue.indexOf(Integer.parseInt(pass.getReportType()));
            reportTypeSpinner.setSelection(index);
        }
    }

    private void showLayout(boolean status) {
        if (status) {
            mainLayout.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            mainLayout.setVisibility(View.GONE);
            actionLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setListener(ProductDetailFragment productDetailFragment) {
        listener = productDetailFragment;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null)
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        retrofitInteractor.unSubscribeObservable();
    }

}
