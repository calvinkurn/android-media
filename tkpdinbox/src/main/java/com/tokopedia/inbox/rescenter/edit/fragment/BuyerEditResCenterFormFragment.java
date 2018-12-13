package com.tokopedia.inbox.rescenter.edit.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.customview.EditCategorySectionView;
import com.tokopedia.inbox.rescenter.edit.customview.EditPackageStatusView;
import com.tokopedia.inbox.rescenter.edit.customview.EditProductTroubleView;
import com.tokopedia.inbox.rescenter.edit.customview.EditTroubleSectionView;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditResCenterImpl;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditResCenterPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 8/24/16.
 */
public class BuyerEditResCenterFormFragment extends BasePresenterFragment<BuyerEditResCenterPresenter>
        implements BuyerEditResCenterListener {

    private static final String ARGS_PARAM_PASS_DATA = "ARGS_PARAM_PASS_DATA";
    private static final String TAG_STEP_1 = "step_1";
    private static final String TAG_STEP_2 = "step_2";

    private ActionParameterPassData passData;

    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.include_loading)
    ProgressBar loading;
    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.view_edit_package_status)
    EditPackageStatusView editPackageStatusView;
    @BindView(R2.id.view_edit_category_section)
    EditCategorySectionView editCategorySectionView;
    @BindView(R2.id.view_edit_product_trouble_section)
    EditProductTroubleView editProductTroubleView;
    @BindView(R2.id.view_edit_trouble_section)
    EditTroubleSectionView editTroubleView;

    public static Fragment newInstance(ActionParameterPassData passData) {
        BuyerEditResCenterFormFragment fragment = new BuyerEditResCenterFormFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnLaunching(getActivity());
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(ARGS_PARAM_PASS_DATA, passData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        passData = savedState.getParcelable(ARGS_PARAM_PASS_DATA);
        setLoading(false);
        setMainView(true);
        if (passData.getFormData() == null) {
            presenter.setOnLaunching(getActivity());
        } else {
            renderPackageReceivedFormView();
            presenter.renderView(passData.getFormData());
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new BuyerEditResCenterImpl(this);
    }

    @Override
    public BuyerEditResCenterPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARGS_PARAM_PASS_DATA);
    }

    @Override
    public String getResolutionID() {
        return getPassData().getResolutionID();
    }

    @Override
    public Context getBaseContext() {
        return getActivity();
    }

    @Override
    public DetailResCenterData getDetailData() {
        return getPassData().getDetailData();
    }

    @Override
    public ActionParameterPassData getPassData() {
        return passData;
    }

    @Override
    public void setPassData(ActionParameterPassData passData) {
        this.passData = passData;
    }

    @Override
    public EditTroubleSectionView getEditTroubleView() {
        return editTroubleView;
    }

    @Override
    public EditPackageStatusView getEditPackageStatusView() {
        return editPackageStatusView;
    }

    @Override
    public EditCategorySectionView getEditCategorySectionView() {
        return editCategorySectionView;
    }

    @Override
    public EditProductTroubleView getEditProductTroubleView() {
        return editProductTroubleView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_buyer_edit_resolution;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        editPackageStatusView.setListener(this);
        editCategorySectionView.setListener(this);
        editProductTroubleView.setListener(this);
        editTroubleView.setListener(this);
    }

    @Override
    public void setLoading(boolean visible) {
        loading.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMainView(boolean visible) {
        mainView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTimeOutView(NetworkErrorHelper.RetryClickedListener rcListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), rcListener);
    }

    @Override
    public void setErrorView(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void renderShop(EditResCenterFormData form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getForm().getResolutionOrder().getOrderShopName()));
        String mShopName = form.getForm().getResolutionOrder().getOrderShopName();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(mShopName),
                spannableString.toString().indexOf(mShopName) + mShopName.length(),
                0
        );

        shopName.setMovementMethod(LinkMovementMethod.getInstance());
        shopName.setText(spannableString);
    }

    @Override
    public void renderInvoice(final EditResCenterFormData form) {
        SpannableString spannableString = new SpannableString(form.getForm().getResolutionOrder().getOrderInvoiceRefNum());
        String invoiceRefNum = form.getForm().getResolutionOrder().getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(getActivity(),
                                form.getForm().getResolutionOrder().getOrderPdfUrl(),
                                form.getForm().getResolutionOrder().getOrderInvoiceRefNum());
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(invoiceRefNum),
                spannableString.toString().indexOf(invoiceRefNum) + invoiceRefNum.length(),
                0
        );

        invoice.setMovementMethod(LinkMovementMethod.getInstance());
        invoice.setText(spannableString);
    }

    @Override
    public void renderPackageReceivedFormView() {
        editPackageStatusView.renderData(passData.getDetailData());
    }

    @Override
    public void renderCategoryTroubleView(EditResCenterFormData formData) {
        editCategorySectionView.renderData(formData);
    }

    @Override
    public void setCategoryTroubleViewVisibility(boolean visible) {
        editCategorySectionView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void renderProductTroubleListView(EditResCenterFormData formData) {
        editProductTroubleView.renderData(formData);
    }

    @Override
    public void setProductTroubleListViewVisibility(boolean visible) {
        editProductTroubleView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void renderTroubleView(EditResCenterFormData formData) {
        editTroubleView.renderData(formData);
    }

    @Override
    public void setTroubleViewVisibility(boolean visible) {
        editTroubleView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSolutionViewVisibility(boolean visible) {

    }

    @OnClick(R2.id.action_choose_solution)
    public void onButtonNextClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.setOnButtonNextClick(getActivity());
    }

    @OnClick(R2.id.action_abort)
    public void onButtonAbortClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_discard_changes)
                .setTitle(R.string.dialog_title_discard_changes)
                .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.action_keep, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setSnackBar(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    @Override
    public void openSolutionFragment() {
        if (getFragmentManager().findFragmentByTag(BuyerEditSolutionResCenterFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            BuyerEditSolutionResCenterFragment.newInstance(passData),
                            BuyerEditSolutionResCenterFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_2)
                    .commit();
        }
    }

    @Override
    public void openProductDetailTroubleFragment() {
        if (getFragmentManager().findFragmentByTag(BuyerEditProductResCenterFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            BuyerEditProductResCenterFragment.newInstane(getPassData()),
                            BuyerEditProductResCenterFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_1)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }
}
