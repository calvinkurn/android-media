package com.tokopedia.inbox.rescenter.create.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.create.customadapter.ProductAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChoooseProductTroubleListener;
import com.tokopedia.inbox.rescenter.create.listener.ChooseProductTroubleListener;
import com.tokopedia.inbox.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.passdata.PassProductTrouble;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;
import com.tokopedia.inbox.rescenter.create.presenter.ChooseProductTroubleImpl;
import com.tokopedia.core.util.AppUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 8/2/16.
 */
public class ChooseProductTroubleFragment extends BasePresenterFragment<ChoooseProductTroubleListener>
        implements ChooseProductTroubleListener {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    private static final String TAG = ChooseProductTroubleFragment.class.getSimpleName();

    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.recycler_view)
    RecyclerView productRecyclerView;
    @BindView(R2.id.action_submit)
    View submitButton;
    @BindView(R2.id.action_abort)
    View actionAbort;

    private ActionParameterPassData passData;
    private CreateResCenterListener listener;
    private ProductAdapter adapter;

    public static ChooseProductTroubleFragment newInstance(ActionParameterPassData passData) {
        ChooseProductTroubleFragment fragment = new ChooseProductTroubleFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ChooseProductTroubleFragment() {
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
        presenter = new ChooseProductTroubleImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (CreateResCenterListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(KEY_PARAM_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_product_trouble;
    }

    @Override
    protected void initView(View view) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ProductAdapter(passData.getProductTroubleChoosenList(), passData.getTroubleCategoryChoosen());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(adapter);
        renderInvoiceData(passData.getFormData().getForm());
        renderShopData(passData.getFormData().getForm());
    }

    private void renderShopData(CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getOrderShopName()));
        String mShopName = form.getOrderShopName();

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

    private void renderInvoiceData(final CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(form.getOrderInvoiceRefNum());
        String invoiceRefNum = form.getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(
                                getActivity(),
                                form.getOrderPdfUrl(),
                                form.getOrderInvoiceRefNum()
                        );
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.onSubmitButtonClicked(getActivity());
            }
        });
        actionAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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
        });
    }

    @Override
    public ActionParameterPassData collectInputData() {
        List<PassProductTrouble> list = passData.getProductTroubleChoosenList();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ProductAdapter.FormViewHolder holder = (ProductAdapter.FormViewHolder) productRecyclerView.getChildViewHolder(productRecyclerView.getChildAt(i));
            Log.d(TAG, "collectInputData: " + String.valueOf(holder.boxDesc.getText()));
            list.get(i).setInputQuantity(Integer.parseInt(String.valueOf(holder.value.getText())));
            Log.d(TAG, "collectInputData: " + String.valueOf(holder.boxDesc.getText()));
            list.get(i).setInputDescription(String.valueOf(holder.boxDesc.getText()));
            list.get(i).setTroubleData((CreateResCenterFormData.TroubleData) holder.troubleSpinner.getItemAtPosition(holder.troubleSpinner.getSelectedItemPosition() - 1));
        }
        return passData;
    }

    @Override
    public void showErrorMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void openSolutionFragment() {
        listener.addSolutionFragmentStacked(passData);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

}
