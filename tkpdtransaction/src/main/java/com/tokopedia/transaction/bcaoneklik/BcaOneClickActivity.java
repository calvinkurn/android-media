package com.tokopedia.transaction.bcaoneklik;

import android.os.Bundle;

import com.bca.xco.widget.BCARegistrasiXCOWidget;
import com.bca.xco.widget.BCAXCOListener;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickSuccessRegisterData;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickPresenter;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickPresenterImpl;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import rx.Subscriber;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_TOKEN_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.API_KEY;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.API_SEED;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.MERCHANT_ID;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public class BcaOneClickActivity extends TActivity implements BcaOneClickView{

    private BcaOneClickPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.bca_one_click_layout);
        presenter = new BcaOneClickPresenterImpl(this);
        BCARegistrasiXCOWidget bcaRegistrasiXCOWidget = (BCARegistrasiXCOWidget)
                findViewById(R.id.bca_registration_widget);
        bcaRegistrasiXCOWidget.setListener(new BCAXCOListener() {
            @Override
            public void onBCASuccess(String s, String s1, String s2, String s3) {
                BcaOneClickRegisterData bcaOneClickRegisterData = new BcaOneClickRegisterData();
                bcaOneClickRegisterData.setTokenId(s);
                bcaOneClickRegisterData.setCredentialType(s1);
                bcaOneClickRegisterData.setCredentialNumber(s2);
                bcaOneClickRegisterData.setMaxLimit(s3);
                presenter.addUserDataBca(BcaOneClickActivity.this, bcaOneClickRegisterData,
                        new Subscriber<BcaOneClickSuccessRegisterData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof ResponseRuntimeException) {
                            NetworkErrorHelper.showSnackbar(BcaOneClickActivity.this, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(BcaOneClickSuccessRegisterData bcaOneClickSuccessRegisterData) {
                        if(bcaOneClickSuccessRegisterData.isSuccess()) {
                            setResult(ListPaymentTypeActivity.REGISTER_BCA_ONE_CLICK_REQUEST_CODE);
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onBCATokenExpired(String s) {
                NetworkErrorHelper.showSnackbar(BcaOneClickActivity.this, s);
            }

            @Override
            public void onBCARegistered(String s) {

            }

            @Override
            public void onBCACloseWidget() {
                finish();
            }
        });
        bcaRegistrasiXCOWidget.openWidget(getIntent().getExtras().getString(ACCESS_TOKEN_EXTRAS),
                API_KEY,
                API_SEED,
                SessionHandler.getLoginID(this), MERCHANT_ID);

    }

    @Override
    public void onSuccessRegister() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
