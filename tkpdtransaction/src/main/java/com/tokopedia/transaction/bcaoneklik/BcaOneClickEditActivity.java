package com.tokopedia.transaction.bcaoneklik;

import android.os.Bundle;

import com.bca.xco.widget.BCAEditXCOWidget;
import com.bca.xco.widget.BCAXCOListener;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickEditPresenter;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickEditPresenterImpl;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import rx.Subscriber;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_TOKEN_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_XCOID_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.API_KEY;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.API_SEED;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_NUMBER_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_TYPE_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.MERCHANT_ID;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickEditActivity extends TActivity implements BcaOneClickEditView {

    private BcaOneClickEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.bca_one_click_edit_activity);
        presenter = new BcaOneClickEditPresenterImpl(this);
        BCAEditXCOWidget bcaEditXCOWidget = (BCAEditXCOWidget)
                findViewById(R.id.bca_edit_widget);
        bcaEditXCOWidget.setListener(new BCAXCOListener() {
            @Override
            public void onBCASuccess(String s, String s1, String s2, String s3) {
                BcaOneClickRegisterData bcaOneClickRegisterData = new BcaOneClickRegisterData();
                bcaOneClickRegisterData.setTokenId(s);
                bcaOneClickRegisterData.setCredentialType(
                        getIntent().getExtras().getString(CREDENTIAL_TYPE_EXTRAS)
                );
                bcaOneClickRegisterData.setCredentialNumber(
                        getIntent().getExtras().getString(CREDENTIAL_NUMBER_EXTRAS)
                );
                bcaOneClickRegisterData.setMaxLimit(s3);
                presenter.editUserDataBca(BcaOneClickEditActivity.this,
                        bcaOneClickRegisterData, new Subscriber<PaymentListModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if(e instanceof ResponseRuntimeException) {
                                    NetworkErrorHelper.showSnackbar(BcaOneClickEditActivity.this, e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(PaymentListModel paymentListModel) {
                                setResult(ListPaymentTypeActivity.EDIT_BCA_ONE_CLICK_REQUEST_CODE);
                            }
                        });
            }

            @Override
            public void onBCATokenExpired(String s) {
                NetworkErrorHelper.showSnackbar(BcaOneClickEditActivity.this, s);
            }

            @Override
            public void onBCARegistered(String s) {
                NetworkErrorHelper.showSnackbar(BcaOneClickEditActivity.this, s);
            }

            @Override
            public void onBCACloseWidget() {
                finish();
            }
        });
        bcaEditXCOWidget.openWidget(getIntent().getExtras().getString(ACCESS_TOKEN_EXTRAS),
                API_KEY,
                API_SEED,
                SessionHandler.getLoginID(this), MERCHANT_ID,
                getIntent().getExtras().getString(ACCESS_XCOID_EXTRAS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
