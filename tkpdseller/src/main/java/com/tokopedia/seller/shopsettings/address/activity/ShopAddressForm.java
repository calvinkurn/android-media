package com.tokopedia.seller.shopsettings.address.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopAddressActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shoplocation.model.saveaddress.SaveAddress;
import com.tokopedia.seller.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ShopAddressForm extends TActivity {
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
    private TextView BtnSave;
    private TextView ProvinceError;
    private TextView RegencyError;
    private TextView SubDistrictError;
    private Spinner SpinnerRegency;
    private Spinner SpinnerSubDistrict;
    private Spinner SpinnerProvince;
    private ArrayList<String> RegencyList = new ArrayList<String>();
    private ArrayList<String> RegencyID = new ArrayList<String>();
    private ArrayAdapter<String> RegencyAdapter;
    private ArrayList<String> SubDistrictList = new ArrayList<String>();
    private ArrayList<String> SubDistrictID = new ArrayList<String>();
    private ArrayAdapter<String> SubDistrictAdapter;
    private ArrayList<String> ProvinceList = new ArrayList<String>();
    private ArrayList<String> ProvinceID = new ArrayList<String>();
    private ArrayAdapter<String> ProvinceAdapter;
    private boolean IsNewForm;
    private int RegeLastIndex = 0;
    private int ProvLastIndex = 0;
    private int mState = 0;
    private TkpdProgressDialog mProgressDialog;
    private Subscriber subscriber;
    // this boolean is to check if fetching province, city and district done
    private boolean isFetchProvinceDone = false;
    private TkpdProgressDialog progress;

    private DataReceiver getDataReceiver(final String provinsi, final String city,
                                         final String district) {
        return new DataReceiver() {

            @Override
            public CompositeSubscription getSubscription() {
                return compositeSubscription;
            }

            @Override
            public void setDistricts(List<District> districts) {
                isFetchProvinceDone = true;
                if (IsNewForm) {
                    chooseDistrict(districts);
                } else {
                    chooseDistrict(districts, district);
                }
            }

            @Override
            public void setCities(List<City> cities) {
                if (IsNewForm) {
                    chooseCity(cities);
                } else {
                    chooseCity(cities, city);
                }
            }

            @Override
            public void setProvinces(List<Province> provinces) {
                if (IsNewForm) {
                    initProvince(provinces);
                } else {
                    initProvince(provinces, provinsi);
                }
            }

            @Override
            public void setBank(List<Bank> banks) {

            }

            @Override
            public void setShippingCity(List<District> districts) {

            }

            @Override
            public void onNetworkError(String message) {
                showNetworkError();
            }

            @Override
            public void onMessageError(String message) {
                showNetworkError();
            }

            @Override
            public void onUnknownError(String message) {
                showNetworkError();
            }

            @Override
            public void onTimeout() {
                showNetworkError();
            }

            @Override
            public void onFailAuth() {
                showNetworkError();
            }
        };
    }

    private void showNetworkError() {
        progress.dismiss();
        mState = HIDE_MENU;
        invalidateOptionsMenu();
        NetworkErrorHelper.showEmptyState(this, rootView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mState = SHOW_MENU;
                invalidateOptionsMenu();
                if (!IsNewForm) {
                    GetAddress();
                } else {
                    initProvince();
                }
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
        BtnSave = (TextView) findViewById(R.id.btn_save);
        PostCode = (EditText) findViewById(R.id.post_code);
        Email = (EditText) findViewById(R.id.shop_email);
        Fax = (EditText) findViewById(R.id.shop_fax);
        Phone = (EditText) findViewById(R.id.shop_phone);
        Address = (EditText) findViewById(R.id.address);
        AddressName = (EditText) findViewById(R.id.address_name);

        SpinnerProvince = (Spinner) findViewById(R.id.provinsi);
        SpinnerRegency = (Spinner) findViewById(R.id.regency);
        SpinnerSubDistrict = (Spinner) findViewById(R.id.sub_district);

        mainView.setVisibility(View.GONE);
        progress = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progress.showDialog();

        initProvince();

        initCity();

        initDistrict();

        initErrorView();

        IsNewForm = getIntent().getExtras().getBoolean("is_new");

        BtnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Validate()) {
                    SaveAddressV4();
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

    private void initErrorView() {
        ProvinceError = (TextView) findViewById(com.tokopedia.core.R.id.province_error);
        RegencyError = (TextView) findViewById(com.tokopedia.core.R.id.regency_error);
        SubDistrictError = (TextView) findViewById(com.tokopedia.core.R.id.sub_district_error);
    }

    private void initDistrict() {
        SubDistrictList.add(getString(com.tokopedia.core.R.string.msg_choose));
        SubDistrictAdapter = new ArrayAdapter<>(ShopAddressForm.this, android.R.layout.simple_spinner_item, SubDistrictList);
        SubDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerSubDistrict.setAdapter(SubDistrictAdapter);
        SpinnerSubDistrict.setVisibility(View.GONE);
        SpinnerSubDistrict.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    SubDistrictError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void chooseDistrict(List<District> districts, String district) {
        if (isFetchProvinceDone) {
            progress.dismiss();
            mainView.setVisibility(View.VISIBLE);
        }
        SubDistrictList.clear();
        SubDistrictID.clear();
        SubDistrictList.add(getString(com.tokopedia.core.R.string.msg_choose));
        for (District district1 : districts) {
            SubDistrictList.add(district1.getDistrictName());
            SubDistrictID.add(district1.getDistrictId());
        }
        SpinnerSubDistrict.setVisibility(View.VISIBLE);
        SubDistrictAdapter.notifyDataSetChanged();

        for (int k = 0; k < SubDistrictID.size(); k++) {
            if (SubDistrictID.get(k).equals(district)) {
                SpinnerSubDistrict.setSelection(k + 1);
                break;
            }
        }
    }

    private void chooseDistrict(List<District> districts) {
        if (isFetchProvinceDone) {
            progress.dismiss();
            mainView.setVisibility(View.VISIBLE);
        }
        SubDistrictList.clear();
        SubDistrictID.clear();
        SubDistrictList.add(getString(com.tokopedia.core.R.string.msg_choose));
        for (District district : districts) {
            SubDistrictList.add(district.getDistrictName());
            SubDistrictID.add(district.getDistrictId());
        }

        SpinnerSubDistrict.setVisibility(View.VISIBLE);
        SubDistrictAdapter.notifyDataSetChanged();
        SpinnerSubDistrict.setSelection(0);
    }

    private void initCity() {
        RegencyList.add(getString(com.tokopedia.core.R.string.msg_choose));
        RegencyAdapter = new ArrayAdapter<>(ShopAddressForm.this, android.R.layout.simple_spinner_item, RegencyList);
        RegencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerRegency.setAdapter(RegencyAdapter);
        SpinnerRegency.setVisibility(View.GONE);
        SpinnerRegency.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {
                if (pos == 0 && pos != RegeLastIndex) {
                    RegeLastIndex = pos;
                    SubDistrictList.clear();
                    SubDistrictID.clear();
                    SubDistrictList.add(getString(com.tokopedia.core.R.string.msg_choose));
                    SubDistrictAdapter.notifyDataSetChanged();
                    SpinnerSubDistrict.setSelection(0);
                    Log.i("Magic", "regen pos 0");
                }
                if (pos != 0 && pos != RegeLastIndex) {
                    RegeLastIndex = pos;
                    DataManagerImpl.getDataManager()
                            .getListDistrict(ShopAddressForm.this
                                    , getDataReceiver(null, null, null)
                                    , RegencyID.get(pos - 1));
                }
                if (pos != 0) {
                    RegencyError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void chooseCity(List<City> cities, String city) {
        RegencyList.clear();
        RegencyID.clear();

        RegencyList.add(getString(com.tokopedia.core.R.string.msg_choose));
        for (City city1 : cities) {
            RegencyList.add(city1.getCityName());
            RegencyID.add(city1.getCityId());
        }
        SpinnerRegency.setVisibility(View.VISIBLE);
        RegencyAdapter.notifyDataSetChanged();

        for (int j = 0; j < RegencyID.size(); j++) {
            if (RegencyID.get(j).equals(city)) {
                SpinnerRegency.setSelection(j + 1);
                RegeLastIndex = j + 1;

                City city1 = DbManagerImpl
                        .getInstance()
                        .getCity(city);

                DataManagerImpl.getDataManager()
                        .getListDistrict(this,
                                getDataReceiver(null, city, district),
                                city1.getCityId()
                        );
                break;
            }
        }
    }

    private void chooseCity(List<City> cities) {
        RegencyList.clear();
        RegencyID.clear();

        RegencyList.add(getString(R.string.msg_choose));
        for (City city : cities) {
            RegencyList.add(city.getCityName());
            RegencyID.add(city.getCityId());
        }
        SpinnerRegency.setVisibility(View.VISIBLE);
        RegencyAdapter.notifyDataSetChanged();
        SpinnerRegency.setSelection(0);

        SubDistrictList.clear();
        SubDistrictID.clear();
        SubDistrictList.add(getString(R.string.msg_choose));
        SubDistrictAdapter.notifyDataSetChanged();
        SpinnerSubDistrict.setSelection(0);
    }

    private void initProvince(List<Province> provinces, String province) {
        initProvince(provinces);

        for (int i = 0; i < ProvinceID.size(); i++) {
            if (ProvinceID.get(i).equals(province)) {
                SpinnerProvince.setSelection(i + 1);
                ProvLastIndex = i + 1;

                //[START] get city based on province
                Province provinsi = DbManagerImpl.getInstance()
                        .getProvinceFromProvinceId(province);

                DataManagerImpl.getDataManager()
                        .getListCity(
                                this,
                                getDataReceiver(provinsi.getProvinceName(), city, null),
                                provinsi.getProvinceId()
                        );

                break;
            }
        }
    }

    private void initProvince(List<Province> provinces) {
        if (IsNewForm) {
            mainView.setVisibility(View.VISIBLE);
            progress.dismiss();
        }

        ProvinceList = new ArrayList<>();
        ProvinceID = new ArrayList<>();

        ProvinceList.add(getString(R.string.msg_choose));
        for (Province province : provinces) {
            ProvinceList.add(province.getProvinceName());
            ProvinceID.add(province.getProvinceId());
        }
        ProvinceAdapter = new ArrayAdapter<>(ShopAddressForm.this, android.R.layout.simple_spinner_item, ProvinceList);
        ProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerProvince.setAdapter(ProvinceAdapter);
        SpinnerProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {
                if (pos == 0 && pos != ProvLastIndex) {
                    ProvLastIndex = pos;
                    RegencyList.clear();
                    RegencyID.clear();
                    SubDistrictList.clear();
                    SubDistrictID.clear();
                    RegencyList.add(getString(R.string.msg_choose));
                    SubDistrictList.add(getString(R.string.msg_choose));
                    RegencyAdapter.notifyDataSetChanged();
                    SubDistrictAdapter.notifyDataSetChanged();
                    SpinnerRegency.setSelection(0);
                    SpinnerSubDistrict.setSelection(0);
                    Log.i("Magic", "prov pos 0");
                }
                if (pos != 0 && pos != ProvLastIndex) {
                    ProvLastIndex = pos;
                    DataManagerImpl.getDataManager()
                            .getListCity(ShopAddressForm.this,
                                    getDataReceiver(
                                            null, null, null
                                    ),
                                    ProvinceID.get(pos - 1));
                }
                Log.i("Magic", "prov pos 1");
                if (pos != 0) {
                    ProvinceError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void initProvince() {
        DataManagerImpl.getDataManager().getListProvince(this, getDataReceiver(null, null, null));
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

        if (SpinnerProvince.getSelectedItemPosition() == 0) {
            ProvinceError.setVisibility(View.VISIBLE);
            valid = false;
        }
        if (SpinnerRegency.getSelectedItemPosition() == 0) {
            RegencyError.setVisibility(View.VISIBLE);
            valid = false;
        }
        if (SpinnerSubDistrict.getSelectedItemPosition() == 0) {
            SubDistrictError.setVisibility(View.VISIBLE);
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

    private void SaveAddressV4() {
        Loading();

        HashMap<String, String> saveAddressParam = PrepareParamSaveAddress(
                AddressName.getText().toString(),
                Address.getText().toString(),
                ProvinceID.get(SpinnerProvince.getSelectedItemPosition() - 1).toString(),
                RegencyID.get(SpinnerRegency.getSelectedItemPosition() - 1).toString(),
                SubDistrictID.get(SpinnerSubDistrict.getSelectedItemPosition() - 1).toString(),
                PostCode.getText().toString(),
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
        int magic = 0;
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

        SpinnerRegency.setVisibility(View.VISIBLE);
        SpinnerSubDistrict.setVisibility(View.VISIBLE);
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

        DataManagerImpl.getDataManager()
                .getListProvince(
                        this,
                        getDataReceiver(province, city, district)
                );
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


}