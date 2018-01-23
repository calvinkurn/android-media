package com.tokopedia.seller.shopsettings.address.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.NoResultHandler;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopAddressActService;
import com.tokopedia.core.network.apiservices.shop.MyShopAddressService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.prototype.ShopCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shoplocation.model.deletelocation.DeleteLocationResponse;
import com.tokopedia.core.shoplocation.model.getshopaddress.ShopAddress;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.seller.shop.common.di.component.DaggerDeleteCacheComponent;
import com.tokopedia.seller.shop.common.di.component.DeleteCacheComponent;
import com.tokopedia.seller.shopsettings.address.adapter.ListViewManageShopLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ManageShopAddress extends TActivity {
    private final int HIDE_MENU = 1;
    private final int SHOW_MENU = 0;

    private ArrayList<String> LocationNameList = new ArrayList<String>();
    private ArrayList<String> LocationAddressList = new ArrayList<String>();
    private ArrayList<String> LocationPhoneList = new ArrayList<String>();
    private ArrayList<String> LocationFaxList = new ArrayList<String>();
    private ArrayList<String> LocationEmailList = new ArrayList<String>();
    private ArrayList<String> LocationPostList = new ArrayList<String>();
    private ArrayList<String> LocationId = new ArrayList<String>();
    private ArrayList<String> LocationProvinceId = new ArrayList<String>();
    private ArrayList<String> LocationCityId = new ArrayList<String>();
    private ArrayList<String> LocationDistrictId = new ArrayList<String>();
    private ArrayList<String> LocationAddress = new ArrayList<String>();

    private LazyListView LocationListView;
    private ListViewManageShopLocation LocationAdapter;
    private NoResultHandler noResult;
    private TkpdProgressDialog MainProgress;
    private View mainView;
    private String IsAllowShop = "1";
    private int mState = SHOW_MENU;
    private SessionHandler session = new SessionHandler(this);
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    DeleteShopInfoUseCase deleteShopInfoUseCase;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_ADDRESS_EDITOR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inflateView(R.layout.activity_manage_shop_address);
        MainProgress = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        MainProgress.setLoadingViewId(R.id.include_loading);
        mainView = findViewById(R.id.main_view);
        noResult = new NoResultHandler(mainView);
        LocationListView = (LazyListView) findViewById(R.id.listview_shop_location);
        LocationListView.AddLoadingView();
        LocationAdapter = new ListViewManageShopLocation(ManageShopAddress.this, LocationNameList, LocationAddressList, LocationPhoneList, LocationFaxList, LocationEmailList, IsAllowShop);
        LocationListView.setAdapter(LocationAdapter);
        LocationListView.RemoveLoadingView();
        MainProgress.showDialog();
        mainView.setVisibility(View.GONE);
        CheckCache();

        DeleteCacheComponent deleteCacheComponent =
                DaggerDeleteCacheComponent.builder().appComponent(getApplicationComponent()).build();
        deleteCacheComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private HashMap<String, String> PrepareParamDeleteLocation(String position) {
        HashMap<String, String> deleteLocation = new HashMap<>();
        deleteLocation.put("location_address_id", position);
        return deleteLocation;
    }

    public void DeleteLocationV4(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManageShopAddress.this);


        builder
                .setMessage(getString(R.string.dialog_delete_address))
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UnifyTracking.eventShopDeleteAddress();
                        compositeSubscription.add(new MyShopAddressActService().getApi().deleteLocation(
                                AuthUtil.generateParams(ManageShopAddress.this, PrepareParamDeleteLocation(LocationId.get(position)))
                        ).subscribeOn(Schedulers.newThread())
                                .unsubscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new Subscriber<Response<TkpdResponse>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                NetworkErrorHelper.showDialog(ManageShopAddress.this,
                                                        new NetworkErrorHelper.RetryClickedListener() {
                                                            @Override
                                                            public void onRetryClicked() {
                                                                DeleteLocationV4(position);
                                                            }
                                                        }
                                                );
                                            }

                                            @Override
                                            public void onNext(Response<TkpdResponse> responseData) {
                                                TkpdResponse response = responseData.body();

                                                JSONObject jsonObject = null;
                                                try {
                                                    ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ADDRESS, ManageShopAddress.this);
                                                    ShopCache.DeleteCache(session.getShopID(), ManageShopAddress.this);
                                                    if (deleteShopInfoUseCase!= null) {
                                                        deleteShopInfoUseCase.executeSync(RequestParams.EMPTY);
                                                    }

                                                    jsonObject = new JSONObject(response.getStringData());
                                                    Gson gson = new GsonBuilder().create();
                                                    DeleteLocationResponse data =
                                                            gson.fromJson(jsonObject.toString(), DeleteLocationResponse.class);

                                                    if (data.getIsSuccess().toString().equals("1")) {
                                                        LocationId.remove(position);
                                                        LocationNameList.remove(position);
                                                        LocationPhoneList.remove(position);
                                                        LocationPostList.remove(position);
                                                        LocationFaxList.remove(position);
                                                        LocationAddressList.remove(position);
                                                        LocationEmailList.remove(position);
                                                        LocationProvinceId.remove(position);
                                                        LocationAddress.remove(position);
                                                        LocationCityId.remove(position);
                                                        LocationDistrictId.remove(position);
                                                        LocationAdapter.notifyDataSetChanged();
                                                    }
                                                    if (LocationId.size() == 0) {
                                                        LocationListView.setVisibility(View.GONE);
                                                        noResult.showMessage();
                                                        //										ErrorMessage.setVisibility(View.VISIBLE);
                                                        //										ErrorMessage.setText(R.string.title_no_result);
                                                    }

                                                } catch (JSONException je) {
//													Log.e(STUART, MESSAGE_TAG + je.getLocalizedMessage());
                                                }
                                            }
                                        }
                                ));

                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    public void EditLocation(int position) {
        Intent intent = new Intent(ManageShopAddress.this, ShopAddressForm.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_new", false);
        bundle.putString("a_id", LocationId.get(position));
        bundle.putString("location_name", LocationNameList.get(position));
        bundle.putString("location_address", LocationAddress.get(position));
        bundle.putString("phone", LocationPhoneList.get(position));
        bundle.putString("fax", LocationFaxList.get(position));
        bundle.putString("email", LocationEmailList.get(position));
        bundle.putString("post_code", LocationPostList.get(position));
        bundle.putString("province", LocationProvinceId.get(position));
        bundle.putString("city", LocationCityId.get(position));
        bundle.putString("district", LocationDistrictId.get(position));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 0 create new address
        // 1 edit address
        if (resultCode == RESULT_OK && data != null)
            if (data.getExtras().getBoolean("is_new")) {
                Toast.makeText(ManageShopAddress.this, getString(R.string.title_success_add_addr), Toast.LENGTH_SHORT).show();
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ADDRESS, ManageShopAddress.this);
                GetShopLocationsV4();
            } else {
                Toast.makeText(ManageShopAddress.this, getString(R.string.title_success_edit_addr), Toast.LENGTH_SHORT).show();
            }

        if (resultCode == RESULT_OK) {
            ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ADDRESS, ManageShopAddress.this);
            ShopCache.DeleteCache(session.getShopID(), ManageShopAddress.this);
            if (deleteShopInfoUseCase!= null) {
                deleteShopInfoUseCase.executeSync(RequestParams.EMPTY);
            }
            GetShopLocationsV4();
        }
    }

    private void SetToUI(JSONObject Result) {
        try {
            if (!Result.getString("locations").equals("null")) {
                LocationListView.setVisibility(View.VISIBLE);
                JSONArray data = new JSONArray(Result.getString("locations"));
                IsAllowShop = Result.getString("is_allow");
                JSONObject Location;
                LocationId.clear();
                LocationNameList.clear();
                LocationPhoneList.clear();
                LocationPostList.clear();
                LocationFaxList.clear();
                LocationAddressList.clear();
                LocationEmailList.clear();
                LocationProvinceId.clear();
                LocationAddress.clear();
                LocationCityId.clear();
                LocationDistrictId.clear();
                for (int i = 0; i < data.length(); i++) {
                    Location = new JSONObject(data.getString(i));
                    LocationId.add(Location.getString("addr_id"));
                    LocationNameList.add(MethodChecker.fromHtml(Location.getString("addr_name")).toString());
                    LocationPhoneList.add(Location.getString("phone"));
                    LocationFaxList.add(Location.getString("fax"));
                    LocationEmailList.add(Location.getString("email"));
                    LocationAddressList.add(
                            MethodChecker.fromHtml(Location.getString("address")).toString()
                                    + "\n" + Location.getString("district_name")
                                    + ", " + Location.getString("city_name")
                                    + ", " + Location.getString("postal_code")
                    );
                    LocationAddress.add(MethodChecker.fromHtml(Location.getString("address")).toString());
                    LocationProvinceId.add(Location.getString("province_id"));
                    LocationCityId.add(Location.getString("city_id"));
                    LocationDistrictId.add(Location.getString("district_id"));
                    LocationPostList.add(Location.getString("postal_code"));
                }
            } else if (LocationId.size() == 0) {
                LocationListView.setVisibility(View.GONE);
                noResult.showMessage();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainProgress.dismiss();
        mainView.setVisibility(View.VISIBLE);
        LocationAdapter.notifyDataSetChanged();
    }

    private void SetToUIV4(ShopAddress data) {
        noResult.removeMessage();
        if (data.getList() != null && data.getList().size() > 0) {
            LocationListView.setVisibility(View.VISIBLE);
            IsAllowShop = data.getIsAllow() + "";
            LocationId.clear();
            LocationNameList.clear();
            LocationPhoneList.clear();
            LocationPostList.clear();
            LocationFaxList.clear();
            LocationAddressList.clear();
            LocationEmailList.clear();
            LocationProvinceId.clear();
            LocationAddress.clear();
            LocationCityId.clear();
            LocationDistrictId.clear();
            for (int i = 0; i < data.getList().size(); i++) {
                com.tokopedia.core.shoplocation.model.getshopaddress.List Location = data.getList().get(i);
                LocationId.add(Location.getLocationAddressId());
                LocationNameList.add(MethodChecker.fromHtml(Location.getLocationAddressName()).toString());
                if (!CommonUtils.checkNullForZeroJson(Location.getLocationPhone())) {
                    Location.setLocationPhone("");
                }
                if (!CommonUtils.checkNullForZeroJson(Location.getLocationFax())) {
                    Location.setLocationFax("");
                }
                if (!CommonUtils.checkNullForZeroJson(Location.getLocationEmail())) {
                    Location.setLocationEmail("");
                }
                LocationPhoneList.add(Location.getLocationPhone());
                LocationFaxList.add(Location.getLocationFax());
                LocationEmailList.add(Location.getLocationEmail());
                LocationAddressList.add(
                        MethodChecker.fromHtml(Location.getLocationAddress()).toString()
                                + "\n" + Location.getLocationDistrictName()
                                + ", " + Location.getLocationCityName()
                                + ", " + Location.getLocationPostalCode()
                );
                LocationAddress.add(MethodChecker.fromHtml(Location.getLocationAddress()).toString());
                LocationProvinceId.add(Location.getLocationProvinceId());
                LocationCityId.add(Location.getLocationCityId());
                LocationDistrictId.add(Location.getLocationDistrictId());
                LocationPostList.add(Location.getLocationPostalCode());
            }
        } else {
            LocationListView.setVisibility(View.GONE);
            noResult.showMessage();
        }
        MainProgress.dismiss();
        mainView.setVisibility(View.VISIBLE);
        LocationAdapter.notifyDataSetChanged();
    }

    private void CheckCache() {
        if (ShopSettingCache.getSetting(ShopSettingCache.CODE_ADDRESS, ManageShopAddress.this) != null) {
            SetToUI(ShopSettingCache.getSetting(ShopSettingCache.CODE_ADDRESS, ManageShopAddress.this));
        } else
            GetShopLocationsV4();
    }


    private void GetShopLocationsV4() {
        compositeSubscription.add(new MyShopAddressService().getApi().getLocation(
                AuthUtil.generateParams(ManageShopAddress.this, new HashMap<String, String>())
        ).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                MainProgress.dismiss();
                                mState = HIDE_MENU;
                                invalidateOptionsMenu();
                                NetworkErrorHelper.showEmptyState(ManageShopAddress.this, mainView, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetShopLocationsV4();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();
                                mState = SHOW_MENU;
                                invalidateOptionsMenu();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    ShopAddress data =
                                            gson.fromJson(jsonObject.toString(), ShopAddress.class);

                                    SetToUIV4(data);

                                } catch (JSONException je) {

                                }
                            }
                        }
                ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_address) {
            if (LocationNameList.size() < 3) {
                Intent intent = new Intent(ManageShopAddress.this, ShopAddressForm.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_new", true);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(ManageShopAddress.this, getString(R.string.error_max_address), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_shop_address, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.add_address);

        if (IsAllowShop.equals("1") && mState != HIDE_MENU) {
            item.setVisible(true);
            item.setEnabled(true);
            //item.getIcon().setAlpha(255);
        } else {
            item.setVisible(false);
            item.setEnabled(false);
        }

        return true;
    }
}
