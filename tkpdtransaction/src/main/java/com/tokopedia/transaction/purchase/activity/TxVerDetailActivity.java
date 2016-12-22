package com.tokopedia.transaction.purchase.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.adapter.TxVerInvoiceAdapter;
import com.tokopedia.transaction.purchase.listener.TxVerDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.Detail;
import com.tokopedia.transaction.purchase.presenter.TxVerDetailPresenter;
import com.tokopedia.transaction.purchase.presenter.TxVerDetailPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * TxVerDetailActivity
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
@RuntimePermissions
public class TxVerDetailActivity extends BasePresenterActivity<TxVerDetailPresenter>
        implements TxVerDetailViewListener, AdapterView.OnItemClickListener {
    private static final String EXTRA_TX_VER_DATA = "EXTRA_TX_VER_DATA";

    public static final int REQUEST_EDIT_PAYMENT = 42;

    private TxVerData txVerData;
    private TxVerInvoiceAdapter invoiceAdapter;

    @BindView(R2.id.listView1)
    ListView lvInvoice;
    @BindView(R2.id.date)
    TextView tvPaymentDate;
    @BindView(R2.id.total_invoice)
    TextView tvAmountPayment;
    @BindView(R2.id.account_owner)
    TextView tvOwnerAccountBank;
    @BindView(R2.id.account_number)
    TextView tvSysAccountBank;
    @BindView(R2.id.changePayment)
    View btnEditPayment;
    @BindView(R2.id.upload_button)
    View btnUploadProof;
    @BindView(R2.id.transfer_account_information)
    RelativeLayout holderAccountBankInfo;
    @BindView(R2.id.indomaret_code_detail_label)
    TextView tvPaymentCode;

    private TkpdProgressDialog mProgressDialog;
    private ImageUploadHandler imageUploadHandler;

    public static Intent createInstance(Context context, TxVerData txVerData) {
        Intent intent = new Intent(context, TxVerDetailActivity.class);
        intent.putExtra(EXTRA_TX_VER_DATA, txVerData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PAYMENT_VERIFICATION_DETAIL;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        txVerData = extras.getParcelable(EXTRA_TX_VER_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxVerDetailPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_verification_detail_tx_module;
    }

    @Override
    protected void initView() {
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        lvInvoice.setAdapter(invoiceAdapter);
        lvInvoice.setOnItemClickListener(this);
    }

    @Override
    protected void initVar() {
        invoiceAdapter = new TxVerInvoiceAdapter(this);
    }

    @Override
    protected void setActionVar() {
        renderViewData();
        presenter.getTxInvoiceData(this, txVerData);
    }

    private void renderViewData() {
        tvPaymentDate.setText(txVerData.getPaymentDate());
        tvAmountPayment.setText(txVerData.getPaymentAmount());
        tvOwnerAccountBank.setText(txVerData.getUserAccountNo());
        tvSysAccountBank.setText(txVerData.getSystemAccountNo());
        this.setTitle(txVerData.getPaymentRefNum());
        switch (presenter.getTypePaymentMethod(txVerData)) {
            case 1:
                holderAccountBankInfo.setVisibility(View.GONE);
                btnEditPayment.setVisibility(View.GONE);
                break;
            case 2:
                holderAccountBankInfo.setVisibility(View.GONE);
                btnEditPayment.setVisibility(View.GONE);
                tvPaymentCode.setVisibility(View.VISIBLE);
                tvPaymentCode.setText(String.format("Kode %s : %s",
                        txVerData.getBankName(), txVerData.getUserAccountName()));
                break;
        }

        btnUploadProof.setVisibility(txVerData.getButton().getButtonUploadProof() == 1
                ? View.VISIBLE : View.GONE);
        btnEditPayment.setVisibility(txVerData.getButton().getButtonEditPayment() == 1
                ? View.VISIBLE : View.GONE);

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        mProgressDialog.setCancelable(false);
        mProgressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    public void renderInvoiceList(List<Detail> detail) {
        invoiceAdapter.addAll(detail);
        invoiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Detail data = invoiceAdapter.getItem(position);
        AppUtils.InvoiceDialog(this, data.getUrl(), data.getInvoice());
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImageGallery.RESULT_CODE || resultCode == Activity.RESULT_OK
                || resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED) {
            switch (requestCode) {
                case REQUEST_EDIT_PAYMENT:
                    if (resultCode == RESULT_OK) {
                        setResult(RESULT_OK);
                        closeView();
                    } else if (resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED) {
                        if (data.hasExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)) {
                            NetworkErrorHelper.showSnackbar(this,
                                    data.getStringExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)
                            );
                        }
                    }
                    break;
                case ImageUploadHandler.REQUEST_CODE:
                    String imagePath = null;
                    if (data != null) {
                        imagePath = data.getExtras().getString(GalleryBrowser.IMAGE_URL, null);
                    } else if (imageUploadHandler.getCameraFileloc() != null) {
                        imagePath = imageUploadHandler.getCameraFileloc();
                    }
                    presenter.uploadProofImageWSV4(this, imagePath, txVerData);
                    break;
            }
        }
    }

    @OnClick(R2.id.changePayment)
    void actionEditPayment() {
        presenter.processEditPayment(this, txVerData);
    }

    @OnClick(R2.id.upload_button)
    void actionUploadProof() {
        imageUploadHandler = ImageUploadHandler.createInstance(this);
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TxVerDetailActivityPermissionsDispatcher.onActionImagePickerWithCheck(TxVerDetailActivity.this);
            }
        });
        myAlertDialog.setNegativeButton(getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TxVerDetailActivityPermissionsDispatcher.onActionCameraWithCheck(TxVerDetailActivity.this);

            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onActionCamera() {
        imageUploadHandler.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onActionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TxVerDetailActivityPermissionsDispatcher.onRequestPermissionsResult(
                TxVerDetailActivity.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this, listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this, listPermission);
    }
}
