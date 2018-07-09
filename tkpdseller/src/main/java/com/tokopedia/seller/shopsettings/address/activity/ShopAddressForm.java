package com.tokopedia.seller.shopsettings.address.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.manage.people.address.model.DistrictRecommendationAddress;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopAddressActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.seller.shopsettings.address.model.saveaddress.SaveAddress;
import com.tokopedia.seller.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ShopAddressForm extends TActivity {
    private static final String TOKEN_KEY_PARAM = "token";
    private static final int DISTRICT_RECOMMENDATION_REQUEST_CODE = 199;
    private static final String ADDRESS = "district_recommendation_address";

    private final int HIDE_MENU = 1;
    private final int SHOW_MENU = 0;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    private View rootView;
    private View mainView;
    private EditText AddressName;
    private EditText Address;
    private EditText Phone;
    private EditText Fax;
    private EditText Email;
    private TextView BtnSave;

    private EditText etCompositeAddress;
    private AutoCompleteTextView tvZipCode;

    private List<String> zipCodes;
    private Token token;
    private boolean IsNewForm;
    private int mState = 0;
    private TkpdProgressDialog mProgressDialog;
    private Subscriber subscriber;

    private String provinceId;
    private String cityId;
    private String districtId;

    private TkpdProgressDialog progress;

    private void showNetworkError() {
        progress.dismiss();
        mState = HIDE_MENU;
        invalidateOptionsMenu();
        NetworkErrorHelper.showEmptyState(this, rootView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mState = SHOW_MENU;
                invalidateOptionsMenu();
                if (!IsNewForm) GetAddress();
            }
        });
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_ADDRESS_FORM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_address_form);

        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rootView = findViewById(R.id.rootView);
        mainView = findViewById(R.id.mainView);
        BtnSave = findViewById(R.id.btn_save);
        Email = findViewById(R.id.shop_email);
        Fax = findViewById(R.id.shop_fax);
        Phone = findViewById(R.id.shop_phone);
        Address = findViewById(R.id.address);
        AddressName = findViewById(R.id.address_name);

        etCompositeAddress = findViewById(R.id.district);
        tvZipCode = findViewById(R.id.postal_code);

        mainView.setVisibility(View.GONE);
        progress = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progress.showDialog();

        IsNewForm = getIntent().getExtras().getBoolean("is_new");
        token = getIntent().getParcelableExtra(TOKEN_KEY_PARAM);

        BtnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) SaveAddressV4();
            }
        });

        tvZipCode.setOnTouchListener(onZipCodeTouch());
        tvZipCode.setOnItemClickListener(onZipCodeItemClick());
        tvZipCode.addTextChangedListener(zipPostTextWatcher());
        etCompositeAddress.setOnClickListener(onCityDistrictClick());

        GetAddress();

        progress.dismiss();
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return this;
    }

    private View.OnClickListener onCityDistrictClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DistrictRecommendationActivity.createInstance(getActivity(), token);
                startActivityForResult(intent, DISTRICT_RECOMMENDATION_REQUEST_CODE);
            }
        };
    }

    private View.OnTouchListener onZipCodeTouch() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!tvZipCode.isPopupShowing()) tvZipCode.showDropDown();
                return false;
            }
        };
    }

    private AdapterView.OnItemClickListener onZipCodeItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && !Character.isDigit(tvZipCode.getText().toString().charAt(0))) {
                    tvZipCode.setText("");
                }
            }
        };
    }

    private TextWatcher zipPostTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void Loading() {
        if (mProgressDialog == null) {
            mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        }
        mProgressDialog.showDialog();
    }

    public boolean Validate() {
        boolean valid = true;
        AddressName.setError(null);
        Address.setError(null);

        if (Email.length() > 0) {
            if (!CommonUtils.EmailValidation(Email.getText().toString().trim())) {
                Email.setError(getString(R.string.error_invalid_email));
                valid = false;
            }
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
            String addressName, String address, String provinceId, String cityId,
            String districtId, String postCode, String email, String phone, String fax) {
        HashMap<String, String> addAddress = new HashMap<>();
        addAddress.put("location_address_name", addressName);
        addAddress.put("location_address_street", address);
        addAddress.put("location_address_province", provinceId);
        addAddress.put("location_address_city", cityId);
        addAddress.put("location_address_district", districtId);
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
                        SaveAddressV4();
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
//                        Snackbar snackbarError = SnackbarManager.make(ShopAddressForm.this,
//                                responses, Snackbar.LENGTH_LONG);
//                        snackbarError.show();
                        return;
                    }

                    jsonObject = new JSONObject(response.getStringData());
                    Gson gson = new GsonBuilder().create();
                    // NEEDS TO BE REMEMBER, THE RESPONSE FOR EDIT AND CREATE NEW ADDRESS IS DIFFERENCE
                    // CREATE NEW ADDRESS ALSO RETURN ADDRESS ID, BUT IT IS NOT USED
                    // TO REDUCE REDUNDANCY THE RESPONSE
                    // IS ONLY STORED ONCE AS SAVE ADDRESS OBJECT.
                    SaveAddress data = gson.fromJson(jsonObject.toString(), SaveAddress.class);
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

    private void SaveAddressV4() {
        Loading();

        HashMap<String, String> saveAddressParam = PrepareParamSaveAddress(
                AddressName.getText().toString(),
                Address.getText().toString(),
                provinceId,
                cityId,
                districtId,
                tvZipCode.getText().toString(),
                Email.getText().toString(),
                Phone.getText().toString(),
                Fax.getText().toString());

        if (IsNewForm) {
            initSubscriber();
            compositeSubscription.add(
                    new MyShopAddressActService().getApi().addLocation(
                            AuthUtil.generateParams(ShopAddressForm.this, saveAddressParam)
                    ).subscribeOn(Schedulers.newThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber));
        } else {
            initSubscriber();
            saveAddressParam.put("location_address_id", getIntent().getExtras().getString("a_id"));
            compositeSubscription.add(
                    new MyShopAddressActService().getApi().editLocation(
                            AuthUtil.generateParams(ShopAddressForm.this, saveAddressParam)
                    ).subscribeOn(Schedulers.newThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber));
        }
    }

    public void GetAddress() {
        Bundle data = getIntent().getExtras();

        if (data != null) {
            String location_name = data.getString("location_name");
            String location_address = data.getString("location_address");
            String phone = data.getString("phone");
            String fax = data.getString("fax");
            String email = data.getString("email");
            String provinceId = data.getString("province");
            String cityId = data.getString("city");
            String districtId = data.getString("district");
            String post_code = data.getString("post_code");
            String fullAddress = data.getString("address_detail");

            AddressName.setText(location_name);
            Address.setText(location_address);
            etCompositeAddress.setText(fullAddress);
            tvZipCode.setText(post_code);

            if (!TextUtils.isEmpty(phone)) {
                Phone.setText(phone);
            }
            if (!TextUtils.isEmpty(phone)) {
                Fax.setText(fax);
            }
            if (!TextUtils.isEmpty(email)) {
                Email.setText(email);
            }
        }
    }

    public void initializeZipCodes() {
        String header = getResources().getString(com.tokopedia.core.R.string.hint_type_postal_code);
        if (!zipCodes.contains(header)) zipCodes.add(0, header);

        ArrayAdapter<String> zipCodeAdapter = new ArrayAdapter<>(
                getContext(),
                com.tokopedia.core.R.layout.item_autocomplete_text_double_row,
                com.tokopedia.core.R.id.item,
                zipCodes);

        tvZipCode.setAdapter(zipCodeAdapter);
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
                SaveAddressV4();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DISTRICT_RECOMMENDATION_REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    DistrictRecommendationAddress address = bundle.getParcelable(ADDRESS);
                    if (address != null) {
                        List<String> compositeAddress = new ArrayList<>(Arrays.asList(
                                address.getProvinceName(),
                                address.getCityName(),
                                address.getDistrictName()
                        ));
                        String fullAddress = TextUtils.join(", ", compositeAddress);
                        etCompositeAddress.setText(fullAddress);

                        provinceId = String.valueOf(address.getProvinceId());
                        cityId = String.valueOf(address.getCityId());
                        districtId = String.valueOf(address.getDistrictId());

                        zipCodes = new ArrayList<>(address.getZipCodes());
                        initializeZipCodes();
                    }
                }
            }
        }
    }

}