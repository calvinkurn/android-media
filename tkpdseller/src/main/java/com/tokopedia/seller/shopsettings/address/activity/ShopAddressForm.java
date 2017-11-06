package com.tokopedia.seller.shopsettings.address.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.manage.people.address.activity.DistrictRecommendationActivity;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shoplocation.model.saveaddress.SaveAddress;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.address.presenter.ShopAddressFormPresenter;
import com.tokopedia.seller.shopsettings.address.presenter.ShopAddressFormPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class ShopAddressForm extends TActivity implements ShopAddressFormView {
    private static final int GET_DISTRICT_RECCOMENDATION_REQUEST_CODE = 100;

    private final int HIDE_MENU = 1;
    private final int SHOW_MENU = 0;
    CompositeSubscription compositeSubscription = new CompositeSubscription();
    String city;
    String district;
    private View rootView;
    private View mainView;
    private EditText AddressName;
    private EditText PostCode;
    private EditText Address;
    private EditText Phone;
    private EditText Fax;
    private EditText Email;
    private EditText cityOrDistrict;
    private TextView BtnSave;
    private TextView ProvinceError;
    private TextView RegencyError;
    private TextView SubDistrictError;
    private boolean IsNewForm;
    private int mState = 0;
    private TkpdProgressDialog mProgressDialog;
    private Subscriber subscriber;
    private ShopAddressFormPresenter presenter;
    // this boolean is to check if fetching province, city and district done
    private boolean isFetchProvinceDone = false;
    private TkpdProgressDialog progress;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_ADDRESS_FORM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_address_form);

        presenter = new ShopAddressFormPresenterImpl();
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rootView = findViewById(R.id.rootView);
        mainView = findViewById(R.id.mainView);
        BtnSave = (TextView) findViewById(R.id.btn_save);
        PostCode = (EditText) findViewById(R.id.post_code);
        Email = (EditText) findViewById(R.id.shop_email);
        Fax = (EditText) findViewById(R.id.shop_fax);
        Phone = (EditText) findViewById(R.id.shop_phone);
        Address = (EditText) findViewById(R.id.address);
        AddressName = (EditText) findViewById(R.id.address_name);
        cityOrDistrict = (EditText) findViewById(R.id.city_or_district);
        mainView.setVisibility(View.GONE);
        progress = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progress.showDialog();

        initErrorView();

        IsNewForm = getIntent().getExtras().getBoolean("is_new");

        cityOrDistrict.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(DistrictRecommendationActivity.createInstance(ShopAddressForm.this),
                        GET_DISTRICT_RECCOMENDATION_REQUEST_CODE);
            }
        });

        BtnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Validate()) {
//                    SaveAddressV4();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!IsNewForm) {
                    GetAddress();
                }
            }
        }, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GET_DISTRICT_RECCOMENDATION_REQUEST_CODE:
                    presenter.setSelectedAddress((Address) data.getParcelableExtra(
                            DistrictRecomendationFragmentView.Constant.INTENT_DATA_ADDRESS
                    ));
                    cityOrDistrict.setText(getFormattedAddress(presenter.getSelectedAddress()));

                    break;
            }
        }
    }

    private String getFormattedAddress(Address address) {
        return address.getProvinceName() + ", " + address.getCityName() + ", " + address.getDistrictName();
    }

    private void initErrorView() {
        ProvinceError = (TextView) findViewById(com.tokopedia.core.R.id.province_error);
        RegencyError = (TextView) findViewById(com.tokopedia.core.R.id.regency_error);
        SubDistrictError = (TextView) findViewById(com.tokopedia.core.R.id.sub_district_error);
    }

    public boolean Validate() {
        boolean valid = true;
        AddressName.setError(null);
        Address.setError(null);
        PostCode.setError(null);
        ProvinceError.setError(null);
        RegencyError.setError(null);
        SubDistrictError.setError(null);
        ProvinceError.setVisibility(View.INVISIBLE);
        RegencyError.setVisibility(View.INVISIBLE);
        SubDistrictError.setVisibility(View.INVISIBLE);
        RegencyError.setError(null);
        SubDistrictError.setError(null);
        if (Email.length() > 0) {
            if (!CommonUtils.EmailValidation(Email.getText().toString().trim())) {
                Email.setError(getString(R.string.error_invalid_email));
                valid = false;
            }
        }

        if (PostCode.length() == 0) {
            PostCode.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if (PostCode.length() > 10) {
            PostCode.setError(getString(R.string.error_max_post_code));
            valid = false;
        }
        if (PostCode.length() < 5) {
            PostCode.setError(getString(R.string.error_min_post_code));
            valid = false;
        }
        if (AddressName.length() == 0) {
            AddressName.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if (AddressName.length() > 128) {
            AddressName.setError(getString(R.string.error_max_128_character));
            valid = false;
        }
        if (Address.length() == 0) {
            Address.setError(getString(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }

    private HashMap<String, String> PrepareParamSaveAddress(
            String addressName, String address, String provinceID, String regencyID,
            String subDistricID, String postCode, String email, String phone, String fax) {
        HashMap<String, String> addAddress = new HashMap<>();
        addAddress.put("location_address_name", addressName);
        addAddress.put("location_address_street", address);
        addAddress.put("location_address_province", provinceID);
        addAddress.put("location_address_city", regencyID);
        addAddress.put("location_address_district", subDistricID);
        addAddress.put("location_address_postal", postCode);
        addAddress.put("location_address_email", email);
        addAddress.put("location_address_phone", phone);
        addAddress.put("location_address_fax", fax);
        return addAddress;
    }

    private void initSubscriber() {
        subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                NetworkErrorHelper.showEmptyState(ShopAddressForm.this, rootView, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
//                        SaveAddressV4();
                    }
                });
            }

            @Override
            public void onNext(Response<TkpdResponse> responseData) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                TkpdResponse response = responseData.body();

                JSONObject jsonObject = null;
                try {
                    List<String> errorMessages = response.getErrorMessages();
                    if (errorMessages != null && errorMessages.size() > 0 && !TextUtils.isEmpty(errorMessages.get(0))) {
                        String responses = "";
                        for (int i = 0; i < response.getErrorMessages().size(); i++) {
                            responses += response.getErrorMessages().get(i) + " ";
                        }
                        Snackbar snackbarError = SnackbarManager.make(ShopAddressForm.this,
                                responses,
                                Snackbar.LENGTH_LONG);
                        snackbarError.show();
                        return;
                    }

                    jsonObject = new JSONObject(response.getStringData());
                    Gson gson = new GsonBuilder().create();
                    // NEEDS TO BE REMEMBER, THE RESPONSE FOR EDIT AND CREATE NEW ADDRESS IS DIFFERENCE
                    // CREATE NEW ADDRESS ALSO RETURN ADDRESS ID, BUT IT IS NOT USED
                    // TO REDUCE REDUNDANCY THE RESPONSE
                    // IS ONLY STORED ONCE AS SAVE ADDRESS OBJECT.
                    SaveAddress data =
                            gson.fromJson(jsonObject.toString(), SaveAddress.class);
                    if (data.getIsSuccess() == 1) {
                        NetworkErrorHelper.removeEmptyState(rootView);
                        Intent intent = new Intent(ShopAddressForm.this, ManageShopAddress.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("is_new", IsNewForm);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } catch (JSONException je) {

                }
            }
        };
    }

//    private void SaveAddressV4() {
//        showLoading();
//
//        HashMap<String, String> saveAddressParam = PrepareParamSaveAddress(
//                AddressName.getText().toString(),
//                Address.getText().toString(),
//                ProvinceID.get(SpinnerProvince.getSelectedItemPosition() - 1).toString(),
//                RegencyID.get(SpinnerRegency.getSelectedItemPosition() - 1).toString(),
//                SubDistrictID.get(SpinnerSubDistrict.getSelectedItemPosition() - 1).toString(),
//                PostCode.getText().toString(),
//                Email.getText().toString(),
//                Phone.getText().toString(),
//                Fax.getText().toString());
//
//        if (IsNewForm) {
//            initSubscriber();
//            compositeSubscription.add(
//                    new MyShopAddressActService().getApi().addLocation(
//                            AuthUtil.generateParams(ShopAddressForm.this, saveAddressParam)
//                    ).subscribeOn(Schedulers.newThread())
//                            .unsubscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(subscriber));
//        } else {
//            initSubscriber();
//            saveAddressParam.put("location_address_id", getIntent().getExtras().getString("a_id"));
//            compositeSubscription.add(
//                    new MyShopAddressActService().getApi().editLocation(
//                            AuthUtil.generateParams(ShopAddressForm.this, saveAddressParam)
//                    ).subscribeOn(Schedulers.newThread())
//                            .unsubscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(subscriber));
//        }
//    }

    public void GetAddress() {
        Bundle data = getIntent().getExtras();
        String location_name = data.getString("location_name");
        String location_address = data.getString("location_address");
        String post_code = data.getString("post_code");
        String phone = data.getString("phone");
        String fax = data.getString("fax");
        String email = data.getString("email");
        String province = data.getString("province");
        city = data.getString("city");
        district = data.getString("district");
        AddressName.setText(location_name);
        Address.setText(location_address);
        PostCode.setText(post_code);
        if (!phone.equals("null")) {
            Phone.setText(phone);
        }
        if (!fax.equals("null")) {
            Fax.setText(fax);
        }
        if (!email.equals("null")) {
            Email.setText(email);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_btn, menu);
        MenuItem item = menu.findItem(R.id.action_send);
        item.setTitle(getString(com.tokopedia.core.R.string.title_action_save_address));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_send);
        if (mState == SHOW_MENU) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_send) {
            if (Validate()) {
//                SaveAddressV4();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        }
        mProgressDialog.showDialog();
    }
}