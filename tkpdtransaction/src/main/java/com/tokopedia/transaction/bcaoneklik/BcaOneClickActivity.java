package com.tokopedia.transaction.bcaoneklik;

import android.os.Bundle;

import com.bca.xco.widget.BCARegistrasiXCOWidget;
import com.bca.xco.widget.BCAXCOListener;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickSuccessRegisterData;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickPresenter;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickPresenterImpl;

import rx.Subscriber;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public class BcaOneClickActivity extends TActivity implements BcaOneClickView{

    private BCARegistrasiXCOWidget bcaRegistrasiXCOWidget;
    private BcaOneClickPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.bca_one_click_layout);
        presenter = new BcaOneClickPresenterImpl(this);
        bcaRegistrasiXCOWidget = (BCARegistrasiXCOWidget)
                findViewById(R.id.bca_registration_widget);
        bcaRegistrasiXCOWidget.setListener(new BCAXCOListener() {
            @Override
            public void onBCASuccess(String s, String s1, String s2, String s3) {
                CommonUtils.dumper("PORING SUCCESS");
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

                    }

                    @Override
                    public void onNext(BcaOneClickSuccessRegisterData bcaOneClickSuccessRegisterData) {
                        if(bcaOneClickSuccessRegisterData.isSuccess()) {
                            //TODO set onSuccess case
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onBCATokenExpired(String s) {
                CommonUtils.dumper("PORING EXPIRED " + s);
            }

            @Override
            public void onBCARegistered(String s) {
                CommonUtils.dumper("PORING REGISTERED " + s);
            }

            @Override
            public void onBCACloseWidget() {
                CommonUtils.dumper("PORING CLOSED");
            }
        });
        bcaRegistrasiXCOWidget.openWidget(getIntent().getExtras().getString("access_token"),
                "540fdef7-18c3-41d0-8596-242f1c59ce29",
                "dc88dfc6-837b-44bb-b767-d168e17e80cd",
                SessionHandler.getLoginID(this), "61005");

    }

    @Override
    public void onSuccessRegister() {

    }
}
