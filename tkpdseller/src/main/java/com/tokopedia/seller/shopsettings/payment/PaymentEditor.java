package com.tokopedia.seller.shopsettings.payment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.ListViewPaymentEditor;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopPaymentService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PaymentEditor extends TActivity {
    private ListView PaymentMethods;
    private ListViewPaymentEditor PaymentAdapter;
    private TkpdProgressDialog mProgressDialog;
    private View mainView;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_S_PAYMENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateView(R.layout.activity_payment_editor);

        mainView = findViewById(R.id.mainView);
        PaymentMethods = (ListView) findViewById(R.id.listview_payment);
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
        mProgressDialog.showDialog();
        getPaymentMethods();
    }



    private void setToUI(JSONObject Result) {
        mProgressDialog.dismiss();
        ArrayList<String> PaymentIconUri = new ArrayList<String>();
        ArrayList<String> PaymentInfo = new ArrayList<String>();
        try {
            JSONArray PaymentOptions = new JSONArray(Result.getString("payment_options"));
            JSONObject PaymentLoc = new JSONObject(Result.getString("loc"));
            JSONObject PaymentOption;
            for (int i = 0; i < PaymentOptions.length(); i++) {
                PaymentOption = new JSONObject(PaymentOptions.getString(i));
                PaymentIconUri.add(PaymentOption.getString("payment_image"));
                PaymentInfo.add(MethodChecker.fromHtml(PaymentLoc.getString(PaymentOption.getString("payment_id"))).toString());
            }
            PaymentAdapter = new ListViewPaymentEditor(PaymentEditor.this, PaymentIconUri, PaymentInfo);
            PaymentMethods.setAdapter(PaymentAdapter);
            PaymentAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getPaymentMethods() {
        new MyShopPaymentService().getApi().getPaymentInfo(AuthUtil.generateParams(this, new HashMap<String, String>()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mProgressDialog.dismiss();
                                NetworkErrorHelper.showEmptyState(PaymentEditor.this, mainView, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        mProgressDialog.showDialog();
                                        getPaymentMethods();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                mProgressDialog.dismiss();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

//									Gson gson = new GsonBuilder().create();
//									MyShopPayment.Data data =
//											gson.fromJson(jsonObject.toString(), MyShopPayment.Data.class);

                                    setToUI(jsonObject);

                                } catch (JSONException je) {
                                    Log.e("STUART", PaymentEditor.class.getSimpleName() + je.getLocalizedMessage());
                                }
                            }
                        }
                );
    }

}
