package com.tokopedia.inbox.rescenter.edit.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.edit.customadapter.ProductAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditProductListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditProductImpl;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditProductPresenter;
import com.tokopedia.core.util.AppUtils;

/**
 * Created on 8/26/16.
 */
public class BuyerEditProductResCenterFragment
        extends BasePresenterFragment<BuyerEditProductPresenter>
        implements BuyerEditProductListener, View.OnClickListener {

    private static final String ARGS_PARAM_PASS_DATA = "pass_data";
    private static final String TAG_STEP_2 = "step_2";
    private ActionParameterPassData passData;
    private ProductAdapter adapter;
    private TextView invoice;
    private TextView shopName;
    private RecyclerView productRecyclerView;
    private View mainView;

    public static Fragment newInstane(ActionParameterPassData passData) {
        BuyerEditProductResCenterFragment fragment = new BuyerEditProductResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public BuyerEditProductResCenterFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
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
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new BuyerEditProductImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARGS_PARAM_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_product_trouble;
    }

    @Override
    protected void initView(View view) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        invoice = view.findViewById(R.id.invoice);
        shopName = view.findViewById(R.id.shop_name);
        productRecyclerView = view.findViewById(R.id.recycler_view);
        mainView = view.findViewById(R.id.main_view);
        view.findViewById(R.id.action_submit).setOnClickListener(this::onClick);
        view.findViewById(R.id.action_abort).setOnClickListener(this::onClick);
        adapter = new ProductAdapter(
                passData.getProductTroubleChoosenList(),
                passData.getTroubleCategoryChoosen(),
                passData.getFormData()
        );
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(adapter);
        renderInvoiceData(passData.getFormData().getForm());
        renderShopData(passData.getFormData().getForm());
    }

    private void renderShopData(EditResCenterFormData.Form form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getResolutionOrder().getOrderShopName()));
        String mShopName = form.getResolutionOrder().getOrderShopName();

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

    private void renderInvoiceData(final EditResCenterFormData.Form form) {
        SpannableString spannableString = new SpannableString(form.getResolutionOrder().getOrderInvoiceRefNum());
        String invoiceRefNum = form.getResolutionOrder().getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(getActivity(),
                                form.getResolutionOrder().getOrderPdfUrl(),
                                form.getResolutionOrder().getOrderInvoiceRefNum());
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
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public void onActionSubmit() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.onSubmitButtonClicked(getActivity());
    }

    public void onActionAbort() {
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
    public ActionParameterPassData getPassData() {
        return passData;
    }

    @Override
    public void showErrorMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public RecyclerView getProductRecyclerView() {
        return productRecyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
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
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.action_submit){
           onActionSubmit();
        }else if(id==R.id.action_abort){
            onActionAbort();
        }
    }
}
