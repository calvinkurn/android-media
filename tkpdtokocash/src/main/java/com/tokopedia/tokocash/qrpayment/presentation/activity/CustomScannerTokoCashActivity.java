package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.InfoQrTokoCashContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.presenter.InfoQrTokoCashPresenter;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 12/29/17.
 */

public class CustomScannerTokoCashActivity extends BaseScannerQRActivity implements InfoQrTokoCashContract.View,
        HasComponent<TokoCashComponent> {

    public static final int RESULT_CODE_HOME = 1;
    public static final int RESULT_CODE__SCANNER = 2;
    private static final int REQUEST_CODE_NOMINAL = 211;

    private TokoCashComponent tokoCashComponent;
    private String resultScan = "";
    private TkpdProgressDialog progressDialog;
    private ImageView torch;
    private boolean isTorchOn;

    public static Intent newInstance(Context context) {
        return new Intent(context, CustomScannerTokoCashActivity.class);
    }

    @Inject
    public InfoQrTokoCashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getInflateViewId() {
        return R.layout.layout_scanner_qr;
    }

    @Override
    protected int getIdDecoratedBarcodeView() {
        return R.id.zxing_barcode_scanner;
    }

    @Override
    protected int getIdScannerLaser() {
        return R.id.scanner_laser;
    }

    @Override
    protected int getColorUpScannerLaser() {
        return R.drawable.digital_gradient_green_up;
    }

    @Override
    protected int getColorDownScannerLaser() {
        return R.drawable.digital_gradient_green_down;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void initView() {
        initInjector();
        presenter.attachView(this);
        toolbar.setTitle(getString(R.string.title_scan_qr));

        torch = (ImageView) findViewById(R.id.switch_flashlight);
        torch.setVisibility(!hasFlash() ? View.GONE : View.VISIBLE);
        decoratedBarcodeView.setTorchListener(getListener());
        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTorchOn) {
                    isTorchOn = false;
                    decoratedBarcodeView.setTorchOff();
                    torch.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_flash_turn_on));
                } else {
                    isTorchOn = true;
                    decoratedBarcodeView.setTorchOn();
                    torch.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_flash_turn_off));
                }
            }
        });
    }

    private DecoratedBarcodeView.TorchListener getListener() {
        return new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                isTorchOn = true;
            }

            @Override
            public void onTorchOff() {
                isTorchOn = false;
            }
        };
    }

    @Override
    protected void findResult(BarcodeResult barcodeResult) {
        if (barcodeResult.getText() != null) {
            decoratedBarcodeView.pause();
            hideAnimation();
            resultScan = barcodeResult.getText();
            presenter.getInfoQeTokoCash();
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.showDialog();
        } else {
            progressDialog = new TkpdProgressDialog(getApplicationContext(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        tokoCashComponent.inject(this);
    }

    @Override
    public RequestParams getInfoTokoCashParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, resultScan);
        return requestParams;
    }

    @Override
    public void showErrorGetInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_wrong_scan));
        builder.setMessage(getString(R.string.msg_dialog_wrong_scan));
        builder.setPositiveButton(getString(R.string.btn_dialog_wrong_scan),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        decoratedBarcodeView.resume();
                        animateScannerLaser();
                    }
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        decoratedBarcodeView.resume();
        animateScannerLaser();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyPresenter();
        super.onDestroy();
    }

    @Override
    public void showErrorNetwork(String message) {
        NetworkErrorHelper.createSnackbarWithAction(this, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        decoratedBarcodeView.resume();
                        animateScannerLaser();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void directPageToPayment(InfoQrTokoCash infoQrTokoCash) {
        Intent intent = NominalQrPaymentActivity.newInstance(getApplicationContext(), resultScan, infoQrTokoCash);
        startActivityForResult(intent, REQUEST_CODE_NOMINAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NOMINAL && resultCode == RESULT_CODE_HOME) {
            finish();
        }
    }
}