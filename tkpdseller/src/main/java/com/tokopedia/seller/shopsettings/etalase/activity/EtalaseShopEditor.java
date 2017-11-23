package com.tokopedia.seller.shopsettings.etalase.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.NoResultHandler;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopEtalaseActService;
import com.tokopedia.core.network.apiservices.shop.MyShopEtalaseService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shop.model.etalasemodel.Data;
import com.tokopedia.core.shop.model.etalasemodel.List;
import com.tokopedia.core.shopinfo.models.etalase.AddEtalase;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.etalase.adapter.ListViewEtalaseEditor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Use in reflection by TkpdRouter
 * If you want to rename the class or refactor its package, rename also the route
 */
public class EtalaseShopEditor extends TActivity {

    public static final String STUART = "STUART";
    public static final String ETALASE_SHOP_EDITOR = "Etalase Shop Editor";
    private ArrayList<String> EtalaseName = new ArrayList<String>();
    private ArrayList<String> EtalaseID = new ArrayList<String>();
    private ArrayList<Integer> TotalProd = new ArrayList<Integer>();

    LazyListView lvEtalase;
    View mainView;


    private ListViewEtalaseEditor lvEtalaseAdapter;
    private TkpdProgressDialog progressdialog;
    private NoResultHandler noResult;
    private String IsAllowShop = "0";
    public String etalaseName = "";
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_ETALASE_LIST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inflateView(R.layout.activity_etalase_shop_editor);
        ButterKnife.bind(this);

        lvEtalase = (LazyListView) findViewById(R.id.listview_etalase);
        mainView = findViewById(R.id.mainView);

        noResult = new NoResultHandler(getWindow().getDecorView().getRootView());
        lvEtalaseAdapter = new ListViewEtalaseEditor(EtalaseShopEditor.this,
                EtalaseName, TotalProd, EtalaseID, IsAllowShop);
        lvEtalase.AddLoadingView();
        lvEtalase.setAdapter(lvEtalaseAdapter);
        lvEtalase.RemoveLoadingView();

        CheckCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    public void OnEmpty() {
        noResult.showMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.etalase_shop_editor, menu);
        if (IsAllowShop.equals("0")) {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if(item.getItemId()==R.id.action_add_new){
            ShowAddNewDialog();
            return true;
        }else if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

        // Handle your other action bar items...
    }

    private void CheckCache() {
        if (ShopSettingCache.getSetting(ShopSettingCache.CODE_ETALASE, this) != null) {
            Gson gson = new GsonBuilder().create();
            Data data = gson.fromJson(ShopSettingCache.getSetting(ShopSettingCache.CODE_ETALASE, this).toString(), Data.class);
            SetToUI(data);
        } else {
            GetEtalaseV4();
        }

        invalidateOptionsMenu();
    }

    private void SetToUI(Data data) {
        if (data.getList() != null && data.getList().size() > 0) {
            IsAllowShop = data.getIsAllow().toString();
            for (int i = 0; i < data.getList().size(); i++) {
                List list = data.getList().get(i);
                EtalaseName.add(MethodChecker.fromHtml(list.getEtalaseName()).toString());
                EtalaseID.add(list.getEtalaseId().toString());
                TotalProd.add(list.getEtalaseNumProduct());
            }

        } else {
            IsAllowShop = data.getIsAllow().toString();
            noResult.showMessage();
        }
        lvEtalaseAdapter.setAllow(IsAllowShop);
        lvEtalaseAdapter.notifyDataSetChanged();
    }

    public void GetEtalaseV4() {
        HashMap<String, String> params = new HashMap<>();
        params.put("shop_id", SessionHandler.getShopID(this));
        compositeSubscription.add(new MyShopEtalaseService().getApi().getShopEtalase(AuthUtil.generateParams(this, params))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                invalidateOptionsMenu();
                                lvEtalaseAdapter.setAllow(IsAllowShop);
                                lvEtalase.RemoveLoadingView();
                            }

                            @Override
                            public void onError(Throwable e) {
                                NetworkErrorHelper.showEmptyState(EtalaseShopEditor.this, mainView, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetEtalaseV4();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    Data data = gson.fromJson(jsonObject.toString(), Data.class);
                                    SetToUI(data);
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        }
                ));
    }

    private void ShowAddNewDialog() {
        LayoutInflater li = LayoutInflater.from(EtalaseShopEditor.this);
        View promptsView = li.inflate(R.layout.prompt_new_talk, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EtalaseShopEditor.this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.message_talk);
        userInput.setHint(R.string.title_etalase_name);
        userInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (count >= 3)
                    userInput.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.submit_but), null)
                .setNegativeButton(getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                final Button b = alertDialog
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (userInput.getText().toString().trim().length() == 0) {
                            userInput.setError(getString(R.string.error_field_required));
                        } else if (userInput.getText().toString().trim().length() > 2) {
                            if (!EtalaseName.contains(userInput.getText()
                                    .toString().trim())) {
                                progressdialog = new TkpdProgressDialog(
                                        EtalaseShopEditor.this,
                                        TkpdProgressDialog.NORMAL_PROGRESS);
                                progressdialog.showDialog();
                                etalaseName = userInput.getText().toString().trim();
                                AddEtalase(etalaseName);
                                alertDialog.dismiss();
                                UnifyTracking.eventEtalaseAdd();
                            } else
                                userInput
                                        .setError(getString(R.string.error_etalase_exist));
                        } else
                            userInput
                                    .setError(getString(R.string.error_min_3_character));
                    }
                });
            }
        });
        // show it
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    private HashMap<String, String> generateParam(String eName) {
        HashMap<String, String> param = new HashMap<>();
        param.put("etalase_name", eName);
        return param;
    }

    private void processSuccessAddEtalase(AddEtalase.Data data, final String eName) {
        ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ETALASE, EtalaseShopEditor.this);
        if (data.getIsSuccess() == 1) {
            if (EtalaseName.size() == 0)
                noResult.removeMessage();
            EtalaseName.add(eName);
            EtalaseID.add(data.getEtalaseId());
            TotalProd.add(0);
            Toast.makeText(EtalaseShopEditor.this,
                    R.string.message_success_add_etalase,
                    Toast.LENGTH_LONG).show();
            lvEtalaseAdapter.notifyDataSetChanged();
        }
    }

    private void AddEtalase(final String eName) {
        compositeSubscription.add(new MyShopEtalaseActService().getApi().addEtalase(
                AuthUtil.generateParams(this, generateParam(eName))
        ).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                progressdialog.dismiss();
                                LocalCacheHandler.clearCache(getBaseContext(), TkpdCache.ETALASE_ADD_PROD);
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressdialog.dismiss();
                                Snackbar.make(mainView, getString(R.string.error_connection_problem), Snackbar.LENGTH_INDEFINITE)
                                        .setAction(R.string.title_try_again, new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AddEtalase(etalaseName);
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();
                                    String messageError = "";

                                    if (response.isError()) {
                                        progressdialog.dismiss();
                                        Snackbar.make(mainView, response.getErrorMessages().toString().replace("[", "").replace("]", ""), Snackbar.LENGTH_INDEFINITE)
                                                .show();
                                    } else {
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response.getStringData());

                                            Gson gson = new GsonBuilder().create();
                                            AddEtalase.Data data = gson.fromJson(jsonObject.toString(), AddEtalase.Data.class);
                                            if (data.getIsSuccess() != 1 && response.getErrorMessages().size() > 0) {
                                                progressdialog.dismiss();
                                                for (int i = 0; i < response.getErrorMessages().size(); i++) {
                                                    messageError += response.getErrorMessages().get(i) + " ";
                                                }
                                                Snackbar snackbarError = SnackbarManager.make(EtalaseShopEditor.this,
                                                        messageError,
                                                        Snackbar.LENGTH_LONG);
                                                snackbarError.show();
                                                return;
                                            } else if (data.getIsSuccess() == 1) {
                                                processSuccessAddEtalase(data, eName);
                                            }
                                        } catch (JSONException je) {
                                            Log.e(STUART, EtalaseShopEditor.class.getSimpleName() + je.getLocalizedMessage());
                                        }
                                    }
                                } else {
                                    progressdialog.dismiss();
                                    Snackbar.make(mainView, getString(R.string.error_connection_problem), Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.title_try_again, new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AddEtalase(etalaseName);
                                                }
                                            })
                                            .show();
                                }
                            }
                        }
                ));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_add_new);

        if (IsAllowShop.equals("1")) {
            item.setVisible(true);
            item.setEnabled(true);
            //item.getIcon().setAlpha(255);
        } else {
            item.setVisible(false);
            item.setEnabled(false);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
