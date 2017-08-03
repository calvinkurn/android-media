package com.tokopedia.transaction.bcaoneklik;

import android.content.Intent;
import android.os.Bundle;

import com.bca.xco.widget.BCAEditXCOWidget;
import com.bca.xco.widget.BCAXCOListener;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickEditPresenter;
import com.tokopedia.transaction.bcaoneklik.presenter.BcaOneClickEditPresenterImpl;

import rx.Subscriber;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickEditActivity extends TActivity implements BcaOneClickEditView {

    private BCAEditXCOWidget bcaEditXCOWidget;
    private BcaOneClickEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.bca_one_click_edit_activity);
        presenter = new BcaOneClickEditPresenterImpl(this);
        bcaEditXCOWidget = (BCAEditXCOWidget)
                findViewById(R.id.bca_edit_widget);
        bcaEditXCOWidget.setListener(new BCAXCOListener() {
            @Override
            public void onBCASuccess(String s, String s1, String s2, String s3) {
                BcaOneClickRegisterData bcaOneClickRegisterData = new BcaOneClickRegisterData();
                bcaOneClickRegisterData.setTokenId(s);
                bcaOneClickRegisterData.setCredentialType(s1);
                bcaOneClickRegisterData.setCredentialNumber(s2);
                bcaOneClickRegisterData.setMaxLimit(s3);
                presenter.editUserDataBca(BcaOneClickEditActivity.this,
                        bcaOneClickRegisterData, new Subscriber<PaymentListModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(PaymentListModel paymentListModel) {
                                /*Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("edit_result", paymentListModel);
                                intent.putExtras(intent);*/
                                setResult(ListPaymentTypeActivity.EDIT_BCA_ONE_CLICK_REQUEST_CODE);
                                finish();
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
        bcaEditXCOWidget.openWidget(getIntent().getExtras().getString("access_token"),
                "540fdef7-18c3-41d0-8596-242f1c59ce29",
                "dc88dfc6-837b-44bb-b767-d168e17e80cd",
                SessionHandler.getLoginID(this), "61005",
                getIntent().getExtras().getString("XCOID"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
