package com.tokopedia.seller.myproduct;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.AbsListViewScrollDetector;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.SimpleListView;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.myproduct.model.ActResponseModelData;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.prototype.ProductCache;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.RefreshHandler.OnRefreshHandlerListener;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.RetryHandler;
import com.tokopedia.core.util.RetryHandler.OnConnectionTimeout;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.myproduct.ManageProductPermissionsDispatcher;
import com.tokopedia.seller.myproduct.adapter.ListViewManageProdAdapter;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.model.ManageProductModel;
import com.tokopedia.seller.myproduct.model.getProductList.ProductList;
import com.tokopedia.seller.myproduct.presenter.ManageProductPresenterImpl;
import com.tokopedia.seller.myproduct.presenter.ManageProductView;
import com.tokopedia.seller.myproduct.presenter.NetworkInteractor;
import com.tokopedia.seller.myproduct.presenter.NetworkInteractorImpl;
import com.tokopedia.seller.myproduct.service.ProductService;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;
import rx.subscriptions.CompositeSubscription;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;

@RuntimePermissions
public class ManageProduct extends TkpdActivity implements
        NetworkInteractorImpl.GetProductList,
        NetworkInteractorImpl.EditEtalase,
        NetworkInteractorImpl.ChangeCategories,
        NetworkInteractorImpl.ChangeInsurance,
        NetworkInteractorImpl.DeleteProduct,
        NetworkInteractorImpl.FetchEtalase,
        ManageProductView {
    public static final String IMAGE_GALLERY = "IMAGE_GALLERY";
    public static final String IMAGE_POSITION = "IMAGE_POSITION";
    public static final String TAG = "STUART";
    public static final String ETALASE_GUDANG = "ETALASE_GUDANG";
    public static final String ETALASE_ETALASE = "ETALASE_ETALASE";
    public static final String REFRESH_DATA = "REFRESH_DATA";
    public static final String SNACKBAR_CREATE = "SNACKBAR_CREATE";
    public static final String SORT_POSITION = "1";
    public static final String SORT_NEW_PRODUCT = "2";
    public static final String SORT_LAST_UPDATE = "3";
    public static final String SORT_PRODUCT_NAME = "4";
    public static final String SORT_MOST_VIEW = "5";
    public static final String SORT_MOST_TALK = "6";
    public static final String SORT_MOST_REVIEW = "7";
    public static final String SORT_MOST_BUY = "8";
    public static final String SORT_LOWEST_PRICE = "9";
    public static final String SORT_HIGHER_PRICE = "10";
    public static final String ACTION_ADD_PRODUCT = BuildConfig.APPLICATION_ID + ".ADD_PRODUCT";
    public CompositeSubscription compositeSubscription = new CompositeSubscription();
    SimpleListView lvListProd;
    TextView eAddTo;
    TextView eEtalase;
    Spinner SpinnerAddTo;
    Spinner SpinnerOption;
    Spinner spinnerInsurance;
    EditText EtalaseName;
    View footerLV;
    ImageView blurImage;

    ManageProductPresenterImpl manageProductPresenter;
    private ArrayList<String> menuName = new ArrayList<String>();
    private ArrayList<String> EtalaseFilters = new ArrayList<String>();
    private ArrayList<String> EtalaseIdFilters = new ArrayList<String>();
    private ArrayList<String> CategoriesFilter = new ArrayList<String>();
    private ArrayList<String> CategoriesIdFilter = new ArrayList<String>();
    private Spinner spinnerCat;
    private ArrayAdapter<String> adapterCat;
    private Spinner spinnerCat2;
    private ArrayAdapter<String> adapterCat2;
    private Spinner spinnerCat3;
    private ArrayAdapter<String> adapterCat3;
    private String[] SortValueList;
    private String mAddTo;
    private String mEtalase;
    private String IsAllowShop = "0";
    private ListViewManageProdAdapter lvadapter;
    private Boolean loading = false;
    private AlertDialog.Builder SortMenu;
    private AlertDialog SortDialog;
    private RefreshHandler Refresh;
    private ArrayAdapter<CharSequence> adapterCondition;
    private ArrayAdapter<CharSequence> adapterInsurance;
    private ArrayAdapter<String> CategoryAdapter;
    private ArrayAdapter<String> EtalaseAdapter;
    private String Sort = null;
    private String mDepartment;
    private String mInsurance;
    private ArrayList<String> data;
    private ArrayList<String> data_id;
    private ArrayList<String> data_id2;
    private ArrayList<String> data_id3;
    private ArrayList<String> spinnerItemList;
    private int LastSpinner;
    private int LastSpinnerPos;
    private String keyword;
    private boolean ActionTaken = false;
    private Dialog FilterDialog;
    private String FMenuId = "";
    private String Fctg = "";
    private String Fdep = "";
    private String Fpic = "";
    private String Fkond = "";
    private int isProdManager = 1;
    private int FSelEta = 0;
    private int FSelCat = 0;
    private int FSelCtg = 0;
    private int FSelImg = 0;
    private int FSelCond = 0;
    private boolean isMultiSelect = false;
    private TkpdProgressDialog progress;
    private PagingHandler mPaging = new PagingHandler();
    private RetryHandler retryHandler;
    private SimpleSpinnerAdapter simpleSpinnerAdapter;

    FloatingActionButton fabAddProduct;


    // NEW NETWORK
    private NetworkInteractor networkInteractorImpl;
    private Gson gson;
    private int multiActionCount;
    private Snackbar snackbar;
    public BroadcastReceiver onCompletedAddReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    };
    private boolean refreshData, isSnackBarShow;
    private BroadcastReceiver addProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetListViewMode();
                }
            });
        }
    };
    private String messageTAG = "ManageProduct";

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_MANAGE_PROD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_manage_product);
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        lvListProd = (SimpleListView) findViewById(R.id.prod_list);
        blurImage = (ImageView) findViewById(R.id.blur_image);
        fabAddProduct = (FloatingActionButton) findViewById(R.id.fab);

        checkLogin();
        if (this.isFinishing()) {
            return;
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onCompletedAddReceiver,
                new IntentFilter(ProductService.ACTION_COMPLETED_ADD_PRODUCT));
        drawer.setDrawerPosition(TkpdState.DrawerPosition.MANAGE_PRODUCT);

        getOverflowMenu();

        fabAddProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lvadapter.clearCheckdData();
                lvListProd.clearChoices();
                lvListProd.setItemChecked(-1, false);
                if (isProdManager == 1) {
                    ProductActivity.moveToAddProduct(ManageProduct.this);

                    // analytic below : https://phab.tokopedia.com/T18496
                    UnifyTracking.eventClickAPManageProductPlus();
                } else {
                    CommonUtils.UniversalToast(getBaseContext(),
                            getString(R.string.error_permission));
                }
            }
        });


//        fabAddProduct = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
//        fabAddProduct.setListenerFabClick(new ListenerFabClick() {
//            @Override
//            public void onFabClick() {
//                if (!fabAddProduct.isShown()) {
//                    fabAddProduct.setVisibility(View.VISIBLE);
//                }
//
//                // analytic below : https://phab.tokopedia.com/T18496
//                UnifyTracking.eventClickAPManageProductPlus();
//            }
//        });
//
//        fabAddProduct.setMenuListener(new SimpleMenuListenerAdapter() {
//            @Override
//            public boolean onMenuItemSelected(MenuItem menuItem) {
//                int id = menuItem.getItemId();
//
//                lvadapter.clearChecdData();
//                lvListProd.clearChoices();
//                lvListProd.setItemChecked(-1, false);
//                if (id == R.id.action_instagram) {
//                    if(getApplication() instanceof TkpdCoreRouter)
//                        ((TkpdCoreRouter)getApplication()).startInstopedActivity(ManageProduct.this);
//
//                    // analytic below : https://phab.tokopedia.com/T18496
//                    UnifyTracking.eventClickInstoped();
//                } else if (id == R.id.action_gallery) {
//                    ManageProductPermissionsDispatcher.onAddFromGalleryWithCheck(ManageProduct.this);
//
//                } else if (id == R.id.action_camera) {
//                    ManageProductPermissionsDispatcher.onAddFromCameraWithCheck(ManageProduct.this);
//
//                }
//                return false;
//            }
//        });

        footerLV = View.inflate(this, R.layout.footer_list_view, null);
        footerLV.setOnClickListener(null);

        SortValueList = getResources().getStringArray(R.array.sort_value);
        SortMenu = new AlertDialog.Builder(this);
        progress = new TkpdProgressDialog(this,
                TkpdProgressDialog.NORMAL_PROGRESS);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sort_option, android.R.layout.select_dialog_item);
        SortMenu.setAdapter(adapter, onSortMenuClicked());
        SortDialog = SortMenu.create();
        blurImage = (ImageView) findViewById(R.id.blur_image);

        EtalaseFilters.add(getString(R.string.title_all_products));
        EtalaseFilters.add(getString(R.string.title_all_etalase));
        EtalaseFilters.add(getString(R.string.title_warehouse));
        EtalaseFilters.add(getString(R.string.title_under_review));
        EtalaseIdFilters.add("null");
        EtalaseIdFilters.add("etalase");
        EtalaseIdFilters.add("warehouse");
        EtalaseIdFilters.add("pending");
        CategoriesFilter.add(getString(R.string.title_all_categories));
        CategoriesIdFilter.add("null");

        List<CategoryDB> categoryDBs =
                DbManagerImpl.getInstance().getDepartmentParent();

        for (CategoryDB lvl1 : categoryDBs) {
            CategoriesFilter.add(lvl1.getNameCategory());
            CategoriesIdFilter.add(lvl1.getDepartmentId() + "");
        }
        CategoryAdapter = new ArrayAdapter<String>(ManageProduct.this,
                android.R.layout.simple_spinner_item, CategoriesFilter);
        CategoryAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvListProd = (SimpleListView) findViewById(R.id.prod_list);
        Refresh = new RefreshHandler(this, getWindow().getDecorView()
                .getRootView(), onRefresh());
        Refresh.setPullEnabled(false);

        lvadapter = new ListViewManageProdAdapter(ManageProduct.this, isProdManager);

        retryHandler = new RetryHandler(ManageProduct.this, footerLV, lvListProd);
        retryHandler.setRetryView();
        retryHandler.setOnTimeoutListener(onTimeout());

        lvListProd.setAdapter(lvadapter);
        lvListProd.addLoadingFooter();
        lvListProd.setOnScrollListener(onScroll());

        lvListProd.setOnItemClickListener(onListProdClicked());
        lvListProd.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvListProd.setMultiChoiceModeListener(onMultiChoice());

        manageProductPresenter = new ManageProductPresenterImpl(this);
        networkInteractorImpl = new NetworkInteractorImpl();
        gson = new GsonBuilder().create();

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onAddFromGallery() {
        GalleryActivity.moveToImageGalleryCamera(ManageProduct.this, 0, false, 5);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onAddFromCamera() {
        GalleryActivity.moveToImageGalleryCamera(this, 0, true, -1);
    }

    private void checkLogin() {
        Intent intent1;
        // check if not login, go to parent index home
        if (!SessionHandler.isV4Login(this)) {
            intent1 = SessionRouter.getLoginActivityIntent(this);
            intent1.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            startActivity(intent1);
            finish();
        } else {
            // check if id has a shop
            String shopID = SessionHandler.getShopID(this);
            if (shopID == null || shopID.equals("0")) {
                intent1 = new Intent(this, HomeRouter.getHomeActivityClass());
                intent1.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                        HomeRouter.INIT_STATE_FRAGMENT_HOME);
                SnackbarManager.make(this, "Anda belum memiliki toko", Snackbar.LENGTH_SHORT).show();
                startActivity(intent1);
                finish();
            }
        }
    }

    private void resetListViewMode() {
        lvListProd.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvListProd.clearChoices();
        lvListProd.post(new Runnable() {
            @Override
            public void run() {
                lvListProd.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            }
        });
    }

    private MultiChoiceModeListener onMultiChoice() {
        return new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                if (checked == true) {
                    lvadapter.addCheckedItem(position);
                } else {
                    lvadapter.removeCheckedItem(position);
                }
                mode.setTitle(Integer.toString(lvadapter.getCheckSize()));
                lvadapter.notifyDataSetChanged();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                if (item.getItemId() == R.id.action_update_etalase) {
                    ActionTaken = true;
                    ShowEtalaseChange();
                    mode.finish();

                    // analytic below : https://phab.tokopedia.com/T19758
                    UnifyTracking.eventChangeEtalaseProductTopMenu();
                    return true;
                } else if (item.getItemId() == R.id.action_update_categories) {
                    ActionTaken = true;
                    ShowCategoriesChange();
                    mode.finish();

                    // analytic below : https://phab.tokopedia.com/T19758
                    UnifyTracking.eventChangeCategoryProductTopMenu();
                    return true;
                } else if (item.getItemId() == R.id.action_update_insurance) {
                    ActionTaken = true;
                    ShowInsuranceChange();
                    mode.finish();

                    // analytic below : https://phab.tokopedia.com/T19758
                    UnifyTracking.eventChangeInsuranceProductTopMenu();
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    ActionTaken = true;
                    ShowDeleteChange();
                    mode.finish();

                    // analytic below : https://phab.tokopedia.com/T19758
                    UnifyTracking.eventDeleteProductTopMenu();
                    return true;
                }
                mode.finish();
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                System.out.println("FALSE");
                isMultiSelect = true;
                lvadapter.setMultiselect(isMultiSelect);
                getMenuInflater().inflate(R.menu.manage_product_contextual,
                        menu);
                lvadapter.clearCheckdData();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                System.out.println("TRUE");
                isMultiSelect = false;
                lvadapter.setMultiselect(isMultiSelect);
                if (!ActionTaken)
                    ClearCheckedData();
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are
                // deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        };
    }

    private DialogInterface.OnClickListener onSortMenuClicked() {
        return new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
//				Sort = SortValueList[which];
                Sort = which + 2 + "";
                switch (which) {
                    case 0:
                        Sort = SORT_POSITION;
                        break;
                    case 1:
                        Sort = SORT_NEW_PRODUCT;
                        break;
                    case 3:
                        Sort = SORT_PRODUCT_NAME;
                        break;
                    case 4:
                        Sort = SORT_MOST_VIEW;
                        break;
                    case 5:
                        Sort = SORT_MOST_TALK;
                        break;
                    case 6:
                        Sort = SORT_MOST_REVIEW;
                        break;
                    case 7:
                        Sort = SORT_MOST_BUY;
                        break;
                    case 8:
                        Sort = SORT_LOWEST_PRICE;
                        break;
                    case 9:
                        Sort = SORT_HIGHER_PRICE;
                        break;
                    default:
                    case 2:
                        Sort = SORT_LAST_UPDATE;
                        break;
                }
                TriggerLoadNewData();
            }
        };
    }

    private OnItemClickListener onListProdClicked() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (lvadapter.getCount() > 0) {
                    startActivityForResult(
                            ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                    ManageProduct.this, lvadapter.getProductId(position)
                            ), 2
                    );
                }
            }
        };
    }

    private OnScrollListener onScroll() {
        //[BUGFIX] AN-1430 Suggestion: Product Feed: Plus (+) icon should be
        // located on top right of the Product Settings page.
        return new AbsListViewScrollDetector() {
            @Override
            public void onScrollUp() {
                if (fabAddProduct.isShown()) {
                    Animation animationFadeOut = AnimationUtils.loadAnimation(ManageProduct.this, R.anim.fade_out_fab);
                    fabAddProduct.startAnimation(animationFadeOut);
                }
                fabAddProduct.setVisibility(View.GONE);
                //fabAddProduct.setVisibility(View.GONE);
            }

            @Override
            public void onScrollDown() {
                if (!fabAddProduct.isShown()) {
                    Animation animationFadeIn = AnimationUtils.loadAnimation(ManageProduct.this, R.anim.fade_in_fab);
                    fabAddProduct.startAnimation(animationFadeIn);
                }
                fabAddProduct.setVisibility(View.VISIBLE);
                //fabAddProduct.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                super.onScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                Log.d(TAG, "totalItemCount " + totalItemCount + " firstVisibleItem " + firstVisibleItem + " visibleItemCount " + visibleItemCount
                        + " = " + (totalItemCount - (firstVisibleItem + visibleItemCount)));
                if (totalItemCount - (firstVisibleItem + visibleItemCount) < 2 && totalItemCount != 0) {
                    Log.d(TAG, "isLoading " + loading + " and " + mPaging.CheckNextPage() + " and " + mPaging.getPage());
                    if (!loading && mPaging.CheckNextPage()) {
//						CurrPage += 1;
                        mPaging.nextPage();
                        // :(
                        GetProductList(mPaging.getPage(), Sort);
                        // activitycom.TriggerLoadTalkData(k+1);
                        lvListProd.addLoadingFooter();
                        loading = true;
                    }

//                    Animation animationFadeOut = AnimationUtils.loadAnimation(ManageProduct.this, R.anim.fade_out_fab);
//                    fabAddProduct.startAnimation(animationFadeOut);
//                    fabAddProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void setScrollThreshold(int scrollThreshold) {
                super.setScrollThreshold(scrollThreshold);
            }

            @Override
            public void setListView(@NonNull AbsListView listView) {
                super.setListView(listView);
            }

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCompletedAddReceiver);
        if (addProductReceiver.isOrderedBroadcast()) unregisterReceiver(addProductReceiver);
    }

    private OnConnectionTimeout onTimeout() {
        return
                new OnConnectionTimeout() {

                    @Override
                    public void onRetry() {
                        loading = true;
                        if (lvListProd.getFooterViewsCount() == 0) lvListProd.addLoadingFooter();
                    }


                    @Override
                    public void onConnectionTimeOut() {
                        loading = false;
                        lvListProd.removeLoading();
                        Refresh.setRefreshing(false);

                        if (lvadapter.getCount() == 0 && !lvListProd.hasNoRes) {
                            Refresh.setPullEnabled(false);
                        } else {
                            Refresh.setPullEnabled(true);
                        }

                        if (Refresh.isRefreshing()) {
                            CommonUtils.UniversalToast(ManageProduct.this, getString(R.string.msg_connection_timeout_toast));
                            retryHandler.disableRetryView();
                        }
                    }
                };
    }

    private OnRefreshHandlerListener onRefresh() {
        return new OnRefreshHandlerListener() {

            @Override
            public void onRefresh(View view) {
                if (!loading)
                    retryHandler.disableRetryView();
                if (!isMultiSelect)
                    TriggerLoadNewData();
                else if (Refresh.isRefreshing()) {
                    Refresh.finishRefresh();
                }
            }
        };
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClearCheckedData() {
        Log.i(TAG, "Clearing checked data");
        lvadapter.clearCheckdData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_product) {
            if (isProdManager == 1) {
                ProductActivity.moveToAddProduct(this);

                // analytic below : https://phab.tokopedia.com/T18496
                UnifyTracking.eventClickAPManageProductTop();
            } else {
                CommonUtils.UniversalToast(getBaseContext(),
                        getString(R.string.error_permission));
            }
            return true;
        } else if (item.getItemId() == R.id.filter) {
            ShowFilterDialog();
            return true;
        } else if (item.getItemId() == R.id.sort) {
            if (!loading)
                SortDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_product, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.manage_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        // ActionBar ab = getActionBar();
        // ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
        final MenuItem add = menu.findItem(R.id.add_product);
        final MenuItem filter = menu.findItem(R.id.filter);
        final MenuItem sort = menu.findItem(R.id.sort);


        //Custom Search Icon
        int mSearchIconId = getResources().getIdentifier("android:id/search_button", null, null);
        int closeButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);

        ImageView mSearchIcon = (ImageView) searchView.findViewById(mSearchIconId);
        ImageView closeButtonImage = (ImageView) searchView.findViewById(closeButtonId);

        mSearchIcon.setImageResource(R.drawable.ic_new_action_search);
        closeButtonImage.setImageResource(R.drawable.ic_new_action_clear_search);


        searchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisible(false);
                sort.setVisible(false);
                filter.setVisible(false);

            }
        });
        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (hasFocus) {
                        Log.d(TAG, "Images FOCUS");
                    } else {
                        Log.d(TAG, "Images FOCUS LOST");
                    }
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                add.setVisible(true);
                sort.setVisible(true);
                filter.setVisible(true);
                if (!loading && searchView.getQuery().length() == 0) {
                    lvListProd.removeNoResult();
                    lvListProd.addLoadingFooter();
                    keyword = "";
                    ClearData();
//					CurrPage = 1;
                    mPaging.resetPage();
                    GetProductList(mPaging.getPage(), Sort);
                }
                return false;
            }
        });

        searchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(keyword, false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!loading) {
                    lvListProd.removeNoResult();
                    lvListProd.addLoadingFooter();
                    keyword = query;
                    ClearData();
                    mPaging.resetPage();
                    GetProductList(mPaging.getPage(), Sort);
                    KeyboardHandler.DropKeyboard(ManageProduct.this, searchView);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onFailureEditEtalase(Throwable e, final String productId, final String etalaseId, final String etalaseName, final int addTo) {
        progress.dismiss();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                progress.showDialog();
                networkInteractorImpl.editEtalase(getApplicationContext(), productId, etalaseId, etalaseName, addTo);
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, listener);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onSuccessEditEtalase(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
        } catch (JSONException je) {
            Log.e(TAG, messageTAG + je.getLocalizedMessage());
        }

        ActResponseModelData data =
                gson.fromJson(jsonObject.toString(), ActResponseModelData.class);

        if ((data.getIsSuccess() == 1)) {
            multiActionCount--;
            if (multiActionCount == 0) {
                progress.dismiss();
                ClearCheckedData();
                ClearData();
                ManageProductPresenterImpl.resetCache(null);
                CheckCache();
            }
        }
    }

    public void ShowEtalaseChange() {
        LayoutInflater li = LayoutInflater.from(ManageProduct.this);
        View promptsView = li.inflate(R.layout.prompt_dialog_etalase, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ManageProduct.this);

        alertDialogBuilder.setView(promptsView);

        SpinnerAddTo = (Spinner) promptsView.findViewById(R.id.spinner_add_to);
        SpinnerOption = (Spinner) promptsView.findViewById(R.id.spinner);
        EtalaseName = (EditText) promptsView.findViewById(R.id.etalase_name);
        eAddTo = (TextView) promptsView.findViewById(R.id.errorAddto);
        eEtalase = (TextView) promptsView.findViewById(R.id.errorEtalase);
        eAddTo.setVisibility(View.GONE);
        eEtalase.setVisibility(View.GONE);
        mAddTo = null;
        mEtalase = null;

        ArrayAdapter<CharSequence> adapterAddTo = ArrayAdapter
                .createFromResource(this, R.array.add_to,
                        android.R.layout.simple_spinner_item);
        adapterAddTo
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAddTo.setAdapter(adapterAddTo);

        final EtalaseChanging etalaseChanging = new EtalaseChanging() {
            @Override
            public void createEtalaseSpinner(final String addTo) {
                SpinnerOption.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapterEtalase = new ArrayAdapter<String>(
                        ManageProduct.this, android.R.layout.simple_spinner_item,
                        lvadapter.getMenuName(addTo));
                adapterEtalase
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerOption.setAdapter(adapterEtalase);
                SpinnerOption.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView,
                                               View selectedItemView, int position, long id) {
                        eEtalase.setVisibility(View.GONE);
                        EtalaseName.setVisibility(View.GONE);
                        if (position == SpinnerOption.getAdapter().getCount() - 1) {
                            mEtalase = "new";
                            EtalaseName.setVisibility(View.VISIBLE);
                        } else if (position == 0) {
                            mEtalase = null;
                        } else {
                            mEtalase = lvadapter.getMenuId(SpinnerOption.getSelectedItemPosition() - 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            }
        };

        SpinnerAddTo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                eAddTo.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        mAddTo = "2";
                        break;
                    case 1:
                        mAddTo = "1";
                        break;
                }
                etalaseChanging.createEtalaseSpinner(mAddTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(getString(R.string.action_edit),
                null);
        alertDialogBuilder.setNegativeButton(getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(ManageProduct.this, "please implement change etalase", Toast.LENGTH_SHORT).show();
                        if (!ValidateEtalaseChange()) {
                            String ETName = EtalaseName.getText().toString();
                            changeEtalase(mEtalase, lvadapter.CheckedProductId(), Integer.parseInt(mAddTo), ETName);
                            ClearCheckedData();
                            ActionTaken = false;
                            alertDialog.dismiss();
                        }
                    }
                });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ActionTaken = false;
                        ClearCheckedData();
                        alertDialog.dismiss();

                    }
                });

    }

    public void ShowDeleteChange() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.message_confirm_delete));

        myAlertDialog.setPositiveButton(getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteProduct(lvadapter.CheckedProductId());
                        ClearCheckedData();
                        ActionTaken = false;
                    }

                });

        myAlertDialog.setNegativeButton(getString(R.string.title_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ActionTaken = false;
                        ClearCheckedData();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void ShowInsuranceChange() {
        LayoutInflater li = LayoutInflater.from(ManageProduct.this);
        View promptsView = li.inflate(R.layout.prompt_dialog_spinner, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ManageProduct.this);

        alertDialogBuilder.setView(promptsView);

        spinnerInsurance = (Spinner) promptsView
                .findViewById(R.id.spinner_option);
        adapterInsurance = ArrayAdapter.createFromResource(this,
                R.array.insurance, android.R.layout.simple_spinner_item);
        adapterInsurance
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInsurance.setAdapter(adapterInsurance);

        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(getString(R.string.action_edit),
                null);
        alertDialogBuilder.setNegativeButton(getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(ManageProduct.this, "please implement change insurance", Toast.LENGTH_SHORT).show();
                        if (!ValidateInsuranceChange()) {
                            changeInsurance(mInsurance, lvadapter.CheckedProductId());
                            ClearCheckedData();
                            ActionTaken = false;
                            alertDialog.dismiss();
                        }
                    }
                });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ActionTaken = false;
                        ClearCheckedData();
                        alertDialog.dismiss();

                    }
                });

    }

    public void ShowFilterDialog() {
        FilterDialog = new Dialog(this);
        FilterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FilterDialog.setContentView(R.layout.dialog_product_filter);
        Spinner Etalase = (Spinner) FilterDialog.findViewById(R.id.etalase);
        Spinner Category = (Spinner) FilterDialog.findViewById(R.id.categories);
        Spinner Catalog = (Spinner) FilterDialog.findViewById(R.id.catalog);
        Spinner Image = (Spinner) FilterDialog.findViewById(R.id.image);
        Spinner Condition = (Spinner) FilterDialog.findViewById(R.id.condition);
        TextView SubmitButton = (TextView) FilterDialog
                .findViewById(R.id.submit_button);
        Etalase.setAdapter(EtalaseAdapter);
        Category.setAdapter(CategoryAdapter);
        Etalase.setSelection(FSelEta);
        Category.setSelection(FSelCat);
        Catalog.setSelection(FSelCtg);
        Image.setSelection(FSelImg);
        Condition.setSelection(FSelCond);

        Etalase.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (arg2 > 0)
                    FMenuId = EtalaseIdFilters.get(arg2);
                else
                    FMenuId = "";
                FSelEta = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Category.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (arg2 > 0)
                    Fdep = CategoriesIdFilter.get(arg2);
                else
                    Fdep = "";
                FSelCat = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Catalog.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (arg2 == 0)
                    Fctg = "";
                else if (arg2 == 1)
                    Fctg = "1";
                else if (arg2 == 2)
                    Fctg = "2";
                FSelCtg = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Image.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (arg2 == 0)
                    Fpic = "";
                else if (arg2 == 1)
                    Fpic = "1";
                else if (arg2 == 2)
                    Fpic = "2";
                FSelImg = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Condition.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (arg2 == 0)
                    Fkond = "";
                else if (arg2 == 1)
                    Fkond = "1";
                else if (arg2 == 2)
                    Fkond = "2";
                FSelCond = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SubmitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                TriggerLoadNewData();
                FilterDialog.dismiss();
            }
        });
        FilterDialog.show();
    }

    public void ShowCategoriesChange() {
        LayoutInflater li = LayoutInflater.from(ManageProduct.this);
        View promptsView = li.inflate(R.layout.prompt_dialog_categories, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ManageProduct.this);

        alertDialogBuilder.setView(promptsView);

        spinnerCat2 = (Spinner) promptsView.findViewById(R.id.categories2);
        spinnerCat2.setVisibility(View.GONE);
        spinnerCat3 = (Spinner) promptsView.findViewById(R.id.categories3);
        spinnerCat3.setVisibility(View.GONE);

        List<CategoryDB> level1 =
                DbManagerImpl.getInstance().getDepartmentParent();
        data = new ArrayList<>();
        data_id = new ArrayList<>();
        for (CategoryDB lvl1 : level1) {
            data.add(lvl1.getNameCategory());
            data_id.add(lvl1.getDepartmentId() + "");
        }
        spinnerItemList = new ArrayList<String>();
        spinnerItemList.add("Pilih Kategori");
        spinnerItemList.addAll(data);
        spinnerCat = (Spinner) promptsView.findViewById(R.id.categories);
        adapterCat = new ArrayAdapter<>(ManageProduct.this,
                android.R.layout.simple_spinner_item, spinnerItemList);
        adapterCat
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapterCat);
        spinnerCat.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                spinnerCat2.setVisibility(View.GONE);
                spinnerCat3.setVisibility(View.GONE);
                LastSpinner = 1;
                if (position > 0) {
                    List<CategoryDB> level2 = DbManagerImpl.getInstance().getDepartmentChild(2, Integer.parseInt(data_id.get(position - 1)));
                    data = new ArrayList<>();
                    data_id2 = new ArrayList<>();
                    for (CategoryDB lvl2 : level2) {
                        data.add(lvl2.getNameCategory());
                        data_id2.add(lvl2.getDepartmentId() + "");
                    }
                    if (!data.isEmpty()) {
                        spinnerItemList = new ArrayList<>();
                        spinnerItemList.add("Pilih Kategori");
                        spinnerItemList.addAll(data);
                        adapterCat2 = new ArrayAdapter<>(
                                ManageProduct.this,
                                android.R.layout.simple_spinner_item,
                                spinnerItemList);
                        adapterCat2
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCat2.setAdapter(adapterCat2);
                        spinnerCat2.setVisibility(View.VISIBLE);
                    } else {
                        LastSpinnerPos = position;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerCat2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                spinnerCat3.setVisibility(View.GONE);
                LastSpinner = 2;
                if (position > 0) {
                    List<CategoryDB> level3 = DbManagerImpl.getInstance().getDepartmentChild(3, Integer.parseInt(data_id2.get(position - 1)));
                    data = new ArrayList<>();
                    data_id3 = new ArrayList<>();
                    for (CategoryDB lvl3 : level3) {
                        data.add(lvl3.getNameCategory());
                        data_id3.add(lvl3.getDepartmentId() + "");
                    }
                    if (!data.isEmpty()) {
                        spinnerItemList = new ArrayList<String>();
                        spinnerItemList.add("Pilih Kategori");
                        spinnerItemList.addAll(data);
                        adapterCat3 = new ArrayAdapter<String>(
                                ManageProduct.this,
                                android.R.layout.simple_spinner_item,
                                spinnerItemList);
                        adapterCat3
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCat3.setAdapter(adapterCat3);
                        spinnerCat3.setVisibility(View.VISIBLE);
                    } else {
                        LastSpinnerPos = position;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerCat3.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                LastSpinnerPos = position;
                LastSpinner = 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        alertDialogBuilder.setPositiveButton(getString(R.string.action_edit),
                null);
        alertDialogBuilder.setNegativeButton(getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(ManageProduct.this, "please implement change category", Toast.LENGTH_SHORT).show();
                        try {
                            if (!ValidateCategoriesChange()) {
                                if (lvadapter != null && !lvadapter.CheckedProductId().isEmpty()) {
                                    changeCategories(mDepartment, lvadapter.CheckedProductId());
                                }
                                ClearCheckedData();
                                ActionTaken = false;
                                alertDialog.dismiss();
                                LastSpinnerPos = 0;
                            } else {
                                Toast.makeText(ManageProduct.this, getResources().getString(R.string.error_select_category),
                                        Toast.LENGTH_SHORT).show();
                                LastSpinnerPos = 0;
                            }
                        } catch (Exception e) {
                            Toast.makeText(ManageProduct.this, getResources().getString(R.string.error_select_category),
                                    Toast.LENGTH_SHORT).show();
                            LastSpinnerPos = 0;
                        }
                    }
                });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ActionTaken = false;
                        ClearCheckedData();
                        alertDialog.dismiss();

                    }
                });

    }

    // https://phab.tokopedia.com/T7269
    // [BUG] Manage Product - When user want to set multiple product become stok kosong,
    // apps not validate user to input/select the showcase
    public Boolean ValidateEtalaseChange() {
        Boolean ContainError = false;
        EtalaseName.setError(null);
        if (mAddTo.equals("0")) {
            ContainError = true;
            eAddTo.setVisibility(View.VISIBLE);
        } else if ((mAddTo.equals("1") || mAddTo.equals("2")) && mEtalase == null) {
            ContainError = true;
            eEtalase.setVisibility(View.VISIBLE);
        } else if (mAddTo.equals("1") && mEtalase.equals("new")) {
            if (TextUtils.isEmpty(EtalaseName.getText())) {
                ContainError = true;
                EtalaseName.setError(getString(R.string.error_field_required));
            }
        }
        return ContainError;
    }
    // https://phab.tokopedia.com/T7269
    // [BUG] Manage Product - When user want to set multiple product become stok kosong,
    // apps not validate user to input/select the showcase

    public Boolean ValidateCategoriesChange() throws Exception {
        Boolean ContainError = false;
        if (LastSpinnerPos == 0) ContainError = true;
        else {
            switch (LastSpinner) {
                case 1:
                    mDepartment = data_id.get(LastSpinnerPos - 1);
                    break;
                case 2:
                    mDepartment = data_id2.get(LastSpinnerPos - 1);
                    break;
                case 3:
                    mDepartment = data_id3.get(LastSpinnerPos - 1);
                    break;
            }
        }
        return ContainError;
    }

    public Boolean ValidateInsuranceChange() {
        Boolean ContainError = false;
        switch (spinnerInsurance.getSelectedItemPosition()) {
            case 0:
                mInsurance = "0";
                //=====================================DUMPER KRIS
                CommonUtils.dumper("Case0");
                //=====================================

                break;
            case 1:
                mInsurance = "1";
                //=====================================DUMPER KRIS
                CommonUtils.dumper("Case1");
                //=====================================
                break;
        }
        return ContainError;
    }

    public void CheckCache() {
        Refresh.setPullEnabled(false);
        clearData();
        lvListProd.removeNoResult();
        lvListProd.addLoadingFooter();
        GetProductList(mPaging.getPage(), null);

    }

    private void GetProductList(final int page, String sort) {
        if (!loading) {
            loading = true;
            getProductList(
                    sort, keyword, Integer.toString(page),
                    FMenuId, Fctg, Fdep,
                    Fpic, Fkond);
        }

    }

    private void onSuccessGetProductList() {
        invalidateOptionsMenu();
        lvadapter.setProdManager(isProdManager);

        if (lvListProd.getFooterViewsCount() > 0) {
            lvListProd.removeFooterView(footerLV);
        }

        finishLoading();

    }

    private void deleteProd(String ID) {
        List<String> items = Arrays.asList(ID.split("~"));
        for (int i = 0; i < items.size(); i++) {
            ProductCache.DeleteCache(items.get(i), this);
        }
    }

    private void TriggerLoadNewData() {
        if (!Refresh.isRefreshing()) {
            clearData();
            lvListProd.addLoadingFooter();
        }
        manageProductPresenter.resetCache();
        mPaging.resetPage();
        GetProductList(mPaging.getPage(), Sort);
    }

    private void clearData() {
        lvadapter.clearEditMode();
        lvadapter.clearDatas2();
        lvadapter.notifyDataSetChanged();
    }

    public void ClearData() {
        manageProductPresenter.resetCache();
        lvadapter.clearEditMode();
        lvadapter.clearDatas();
        mPaging.resetPage();
    }

    private void clearCache() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ClearData();
        refreshIntent();
        // from add Product
        // create snackbar
        if (refreshData) {
            manageProductPresenter.resetCache();
        }
        if (isSnackBarShow) {
            snackbar = Snackbar.make(parentView, getString(R.string.upload_product_waiting), Snackbar.LENGTH_LONG);
            snackbar.show();
            isSnackBarShow = false;
            getIntent().putExtra(SNACKBAR_CREATE, isSnackBarShow);
        }

        // never hit list product is etalase not available
        List<EtalaseDB> etalaseDBs = DbManagerImpl.getInstance().getEtalases();
        if (etalaseDBs.isEmpty()) {
            fetchEtalase(this);
        } else {
            initEtalaseFilter(etalaseDBs);
            CheckCache();
        }
        registerReceiver(addProductReceiver, new IntentFilter(ACTION_ADD_PRODUCT));
    }

    private void initEtalaseFilter(List<EtalaseDB> etalaseDBs) {
        for (EtalaseDB etalaseDB :
                etalaseDBs) {
            EtalaseFilters.add(String.valueOf(MethodChecker.fromHtml(etalaseDB.getEtalaseName())));
            EtalaseIdFilters.add(String.valueOf(etalaseDB.getEtalaseId()));
        }
        EtalaseAdapter = new ArrayAdapter<String>(
                ManageProduct.this,
                android.R.layout.simple_spinner_item,
                EtalaseFilters);
        EtalaseAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void refreshIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            refreshData = bundle.getBoolean(REFRESH_DATA, false);
            isSnackBarShow = bundle.getBoolean(SNACKBAR_CREATE, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
                Intent intent = new Intent(ManageProduct.this, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
                intent.putExtra(GalleryBrowser.IMAGE_URLS, Parcels.wrap(imageUrls));
                intent.putExtra(ProductActivity.ADD_PRODUCT_IMAGE_LOCATION, -1);
                intent.putExtras(bundle);

                ManageProduct.this.startActivity(intent);
            }

            @Override
            public void onSuccess(String path, int position) {
                Intent intent = new Intent(ManageProduct.this, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
                intent.putExtra(IMAGE_GALLERY, path);
                intent.putExtra(ProductActivity.ADD_PRODUCT_IMAGE_LOCATION, position);
                intent.putExtras(bundle);

                ManageProduct.this.startActivity(intent);
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ManageProduct.this;
            }
        }, requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            IsAllowShop = "1";
            ClearData();
        }
    }

    @Override
    public void onBackPressed() {
        if (lvadapter.CloseQuickEdit())
            super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add_product);

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

    private void setToUI(ManageProductPresenterImpl.CacheManageProduct cacheManageProduct) {

        if (mPaging.getPage() == 1) {
            clearData();
        }

        PagingHandler.PagingHandlerModel pagingHandlerModel =
                cacheManageProduct.pagingHandlerModel;
        mPaging.setNewParameter(pagingHandlerModel);
        if (mPaging.CheckNextPage()) {
            lvListProd.addLoadingFooter();
            loading = false;
        } else {
            lvListProd.removeLoading();
            loading = true;
        }
        IsAllowShop = cacheManageProduct.IsAllowShop;

        lvListProd.removeNoResult();// remove no result
        if (checkCollectionNotNull(cacheManageProduct.productModels)) {
            lvadapter.setData(new ArrayList<ManageProductModel>(cacheManageProduct.productModels));
        } else {
            lvListProd.addNoResult();
        }

        isProdManager = cacheManageProduct.isProdManager;
        lvadapter.setProdManager(isProdManager);
        invalidateOptionsMenu();
    }


    private void SetToUI(ProductList.Data Result) {
        ArrayList<ManageProductModel> manageProductModels = new ArrayList<>();
        ArrayList<ManageProductModel> oldManageProductModels = lvadapter.getManageProductModels();
        lvListProd.removeLoading();
        if (mPaging.getPage() == 1) {
            clearData();
        }
        PagingHandler.PagingHandlerModel pagingHandlerModel =
                PagingHandler.createPagingHandlerModel(0, Result.getPaging().getUriNext(), (Result.getPaging().getUriPrevious()).toString());
        mPaging.setNewParameter(pagingHandlerModel);
        IsAllowShop = Result.getIsProductManager();

        lvListProd.removeNoResult();// remove no result
        if (checkCollectionNotNull(Result.getList())) {
            List<ProductList.Product> itemCase = Result.getList();
            for (int i = 0; i < itemCase.size(); i++) {
                manageProductModels.add(manageProductPresenter.convertTo(itemCase.get(i)));
            }
        } else {
            lvListProd.addNoResult();
        }

        oldManageProductModels.addAll(manageProductModels);
        isProdManager = Integer.parseInt(Result.getIsProductManager());
        lvadapter.setData(oldManageProductModels);
        lvadapter.setProdManager(isProdManager);
        invalidateOptionsMenu();

        // save cache
        ManageProductPresenterImpl.CacheManageProduct manageProduct = new ManageProductPresenterImpl.CacheManageProduct();
        manageProduct.IsAllowShop = IsAllowShop;
        manageProduct.isProdManager = isProdManager;
        if (checkCollectionNotNull(manageProductModels)) {
            manageProduct.productModels = new ArrayList<>(manageProductModels);
        }
        manageProduct.pagingHandlerModel = pagingHandlerModel;
    }


    public void createSnackBar(String message, View.OnClickListener listener) {
        int lenght = (listener != null) ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_SHORT;
        snackbar = Snackbar
                .make(parentView, message, lenght);
        if (listener != null) {
            snackbar.setAction(getString(R.string.title_retry), listener);
        }
        snackbar.show();
    }


    // SET NETWORK INTERACTOR
    private void getProductList(
            String sort, String keyword, String page,
            String etalase_id, String catalog_id, String department_id,
            String picture_status, String condition
    ) {
        ((NetworkInteractorImpl) networkInteractorImpl).setGetProductList(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        networkInteractorImpl.getProductList(this,
                sort, keyword, page,
                etalase_id, catalog_id, department_id,
                picture_status, condition);
    }


    private void changeEtalase(
            String etalaseID, List<String> ID, int addTo,
            String EtalaseName
    ) {
        progress.showDialog();
        multiActionCount = ID.size();
        ((NetworkInteractorImpl) networkInteractorImpl).setEditEtalase(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        for (int i = 0; i < ID.size(); i++) {
            networkInteractorImpl.editEtalase(this, ID.get(i), etalaseID, EtalaseName, addTo);
        }
    }

    private void changeCategories(
            String CtgID, List<String> ID
    ) {
        progress.showDialog();
        multiActionCount = ID.size();
        ((NetworkInteractorImpl) networkInteractorImpl).setChangeCategories(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        for (int i = 0; i < ID.size(); i++) {
            networkInteractorImpl.changeCategories(this,
                    CtgID, ID.get(i), SessionHandler.getShopID(this));
        }
    }

    private void changeInsurance(
            String insuranceID, List<String> ID
    ) {
        progress.showDialog();
        multiActionCount = ID.size();
        ((NetworkInteractorImpl) networkInteractorImpl).setChangeInsurance(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        for (int i = 0; i < ID.size(); i++)
            networkInteractorImpl.changeInsurance(this,
                    insuranceID, ID.get(i));

    }

    private void deleteProduct(
            List<String> ID
    ) {
        progress.showDialog();
        multiActionCount = ID.size();
        ((NetworkInteractorImpl) networkInteractorImpl).setDeleteProduct(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        for (int i = 0; i < ID.size(); i++) {
            networkInteractorImpl.deleteProduct(this,
                    ID.get(i));
        }

    }

    public void fetchEtalase(Context context) {
        ((NetworkInteractorImpl) networkInteractorImpl).setFetchEtalase(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
        networkInteractorImpl.fetchEtalase(context);
    }

    @Override
    public void onSuccessGetProductList(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
            ProductList.Data data =
                    gson.fromJson(jsonObject.toString(), ProductList.Data.class);
            SetToUI(data);
            onSuccessGetProductList();
            lvadapter.notifyDataSetChanged();
        } catch (JSONException je) {
            Log.e(TAG, messageTAG + je.getLocalizedMessage());
        }
    }

    @Override
    public void onSuccessChangeCategories(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
        } catch (JSONException je) {
            Log.e(TAG, messageTAG + je.getLocalizedMessage());
        }

        ActResponseModelData data =
                gson.fromJson(jsonObject.toString(), ActResponseModelData.class);

        if (data.getIsSuccess() == 1) {
            multiActionCount--;
            if (multiActionCount == 0) {
                progress.dismiss();
                ClearCheckedData();
                ClearData();
                ManageProductPresenterImpl.resetCache(null);
                CheckCache();
            }
        }
    }

    @Override
    public void onSuccessChangeInsurance(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
        } catch (JSONException je) {
            Log.e(TAG, messageTAG + je.getLocalizedMessage());
        }

        ActResponseModelData data =
                gson.fromJson(jsonObject.toString(), ActResponseModelData.class);
        if (data.getIsSuccess() == 1) {
            multiActionCount--;
            if (multiActionCount == 0) {
                progress.dismiss();
                ClearCheckedData();
                ClearData();
                ManageProductPresenterImpl.resetCache(null);
                CheckCache();
            }
        }
    }

    @Override
    public void onSuccessDeleteProduct(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
        } catch (JSONException je) {
            Log.e(TAG, messageTAG + je.getLocalizedMessage());
        }

        ActResponseModelData data =
                gson.fromJson(jsonObject.toString(), ActResponseModelData.class);

        if (data.getIsSuccess() == 1) {
            multiActionCount--;
            if (multiActionCount == 0) {
                progress.dismiss();
                ClearCheckedData();
                ClearData();
                ManageProductPresenterImpl.resetCache(null);
                CheckCache();
            }
        }
    }

    @Override
    public void onSuccessFetchEtalase(Response<TkpdResponse> responseData) {
        if (responseData.isSuccessful()) {
            TkpdResponse response = responseData.body();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response.getStringData());
            } catch (JSONException je) {
                Log.e(TAG, messageTAG + je.getLocalizedMessage());
            }
            if (!response.isError()) {

                //[START] add gudang jika belum ada.
                DbManagerImpl.getInstance().saveGudangIfNotInDb();
                //[OLD] add gudang jika belum ada.

                GetEtalaseModel.Data data = gson.fromJson(jsonObject.toString(), GetEtalaseModel.Data.class);
                for (GetEtalaseModel.EtalaseModel etalaseModel : data.getEtalaseModels()) {
                    etalaseModel.setDbId(DbManagerImpl.getInstance().saveEtalase(etalaseModel));
                }
                // get data from db and save it to the filter
                List<EtalaseDB> etalaseDBs = DbManagerImpl.getInstance().getEtalases();
                initEtalaseFilter(etalaseDBs);

                CheckCache();

            } else {
                Log.e(TAG, "Fetch Etalase Error");
            }
        } else {
            Log.e(TAG, "Fetch Etalase Error");
        }
    }

    void finishLoading() {
        progress.dismiss();

        Refresh.finishRefresh();
        loading = false;
        lvListProd.removeLoading();
        Refresh.setPullEnabled(true);
    }

    @Override
    public void onFailureGetProductList(Throwable e) {
        finishLoading();
        View.OnClickListener retryGetProductList = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                progress.showDialog();
                GetProductList(mPaging.getPage(), Sort);
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, retryGetProductList);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onFailureFetchEtalase(Throwable e) {
        finishLoading();

        View.OnClickListener retryFetchEtalase = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                fetchEtalase(getApplicationContext());
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, retryFetchEtalase);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onFailureChangeCategories(Throwable e, final String CtgID, final String ID, final String shopID) {
        finishLoading();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                progress.showDialog();
                networkInteractorImpl.changeCategories(getApplicationContext(), CtgID, ID, shopID);
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, listener);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onFailureChangeInsurance(Throwable e, final String insuranceID, final String ID) {
        finishLoading();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                progress.showDialog();
                networkInteractorImpl.changeInsurance(getApplicationContext(), insuranceID, ID);
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, listener);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onFailureDeleteProduct(Throwable e, final String ID) {
        finishLoading();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                progress.showDialog();
                networkInteractorImpl.deleteProduct(getApplicationContext(), ID);
            }
        };
        String message = CommonUtils.generateMessageError(this, e.getMessage());
        if (message.contains("koneksi")) {
            createSnackBar(message, listener);
        } else {
            createSnackBar(message, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ManageProductPermissionsDispatcher.onRequestPermissionsResult(ManageProduct.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this, listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this, listPermission);
    }

    interface EtalaseChanging {
        void createEtalaseSpinner(String addTo);
    }

}