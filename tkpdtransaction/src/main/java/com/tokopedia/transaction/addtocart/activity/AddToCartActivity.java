package com.tokopedia.transaction.addtocart.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.addtocart.listener.AddToCartViewListener;
import com.tokopedia.transaction.addtocart.model.Insurance;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.addtocart.model.kero.Attribute;
import com.tokopedia.transaction.addtocart.model.kero.Product;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.ProductDetail;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shipment;
import com.tokopedia.transaction.addtocart.presenter.AddToCartPresenter;
import com.tokopedia.transaction.addtocart.presenter.AddToCartPresenterImpl;
import com.tokopedia.transaction.addtocart.receiver.ATCResultReceiver;
import com.tokopedia.transaction.addtocart.services.ATCIntentService;
import com.tokopedia.transaction.addtocart.utils.KeroppiConstants;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity.RESULT_NOT_SELECTED_DESTINATION;

/**
 * @author Angga.Prasetiyo on 11/03/2016.
 */
public class AddToCartActivity extends BasePresenterActivity<AddToCartPresenter>
        implements AddToCartViewListener, AdapterView.OnItemSelectedListener,
        TextWatcher, ATCResultReceiver.Receiver {
    public static final int REQUEST_CHOOSE_ADDRESS = 0;
    public static final int REQUEST_CHOOSE_LOCATION = 2;
    private static final String EXTRA_STATE_ORDER_DATA = "orderData";
    private static final String EXTRA_STATE_DESTINATION_DATA = "destinationData";
    private static final String EXTRA_STATE_LOCATION_PASS_DATA = "locationPassData";
    private static final String EXTRA_STATE_PRODUCT_DETAIL_DATA = "productDetailData";
    private static final String EXTRA_STATE_SHIPMENT_LIST_DATA = "shipmentsData";
    private static final String EXTRA_STATE_SHIPMENT_RATE_LIST_DATA = "shipmentRateAttrs";

    private ProductCartPass productCartPass;
    private TkpdProgressDialog progressDialog;
    private TkpdProgressDialog progressInitDialog;
    private OrderData orderData;
    private Destination mDestination;
    private LocationPass mLocationPass;
    private ProductDetail mProductDetail;
    private List<Shipment> mShipments;
    private List<Attribute> mShipmentRateAttrs;
    private Observable<Long> incrementObservable = Observable.interval(200, TimeUnit.MILLISECONDS);
    private SnackbarRetry snackbarRetry;
    private String insuranceInfo;
    private boolean mustReCalculateShippingRate;
    private boolean hasRecalculateShippingRate;

    @BindView(R2.id.tv_ticker_gtm)
    TextView tvTickerGTM;
    @BindView(R2.id.add_to_cart_coordinatlayout)
    CoordinatorLayout cartCoordinatLayout;
    @BindView(R2.id.iv_pic)
    ImageView ivProduct;
    @BindView(R2.id.tv_name)
    TextView tvProductName;
    @BindView(R2.id.preorder_layout)
    LinearLayout linearLayoutPreOrder;
    @BindView(R2.id.tv_preorder)
    TextView tvPreOrder;
    @BindView(R2.id.tv_preorder_info)
    TextView tvPreOrderInfo;
    @BindView(R2.id.et_form_qty)
    EditText etQuantity;
    @BindView(R2.id.sp_form_insurance)
    Spinner spInsurance;
    @BindView(R2.id.et_form_notes)
    EditText etRemark;
    @BindView(R2.id.tv_address_detail)
    TextView tvAddressDetail;
    @BindView(R2.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R2.id.btn_choose_address)
    TextView btnAddressChange;
    @BindView(R2.id.btn_add_address)
    TextView btnAddressNew;
    @BindView(R2.id.sp_shipment)
    Spinner spShippingAgency;
    @BindView(R2.id.sp_shipment_package)
    Spinner spShippingService;
    @BindView(R2.id.tv_price_product)
    TextView tvProductPrice;
    @BindView(R2.id.tv_price_shipping)
    TextView tvShippingPrice;
    @BindView(R2.id.tv_error_shipping)
    TextView tvErrorShipping;
    @BindView(R2.id.btn_buy)
    TextView btnBuy;
    @BindView(R2.id.layout_geo_location)
    View viewFieldLocation;
    @BindView(R2.id.et_geo_location)
    EditText etValueLocation;
    @BindView(R2.id.til_form_qty)
    TextInputLayout tilAmount;
    @BindView(R2.id.container)
    LinearLayout container;
    @BindView(R2.id.increase_button)
    View increaseButton;
    @BindView(R2.id.decrease_button)
    View decreaseButton;
    @BindView(R2.id.calculate_cart_progress_bar)
    View calculateCartProgressBar;
    @BindView(R2.id.layout_shipment_hour_atc)
    LinearLayout shipmentHourAtcLayout;
    @BindView(R2.id.arrow_max_hour)
    TextView arrowMaxHour;
    @BindView(R2.id.desc_max_hour)
    TextView descMaxHour;
    @BindView(R2.id.img_insurance_info)
    ImageView imgInsuranceInfo;

    private ATCResultReceiver atcReceiver;
    private Subscription subscription;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ADD_TO_CART;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.productCartPass = extras.getParcelable(TransactionAddToCartRouter.EXTRA_PRODUCT_CART);
    }

    @Override
    protected void initialPresenter() {
        presenter = new AddToCartPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_to_cart_tx_module;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressInitDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS);
        increaseButton.setOnTouchListener(onIncrementButtonTouchListener());
        decreaseButton.setOnTouchListener(onDecrementButtonTouchListener());
    }

    @Override
    protected void setViewListener() {
        ImageHandler.loadImageRounded2(this, ivProduct, productCartPass.getImageUri());
        this.tvProductName.setText(productCartPass.getProductName());
        etQuantity.addTextChangedListener(this);
    }

    @Override
    protected void initVar() {
        atcReceiver = new ATCResultReceiver(new Handler());
        atcReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
        calculateCartProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(this, message);
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void closeView() {
        this.finish();
    }


    @Override
    public void initialOrderData(AtcFormData data) {
        this.orderData = OrderData.createFromATCForm(data, productCartPass);
    }

    @Override
    public void renderFormProductInfo(ProductDetail data) {
        this.mProductDetail = data;
        presenter.processGetGTMTicker();
        List<Insurance> insurances = Insurance.createList(getResources()
                .getStringArray(R.array.insurance_option));
        ArrayAdapter<Insurance> insuranceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, insurances);
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInsurance.setAdapter(insuranceAdapter);
        spInsurance.setOnItemSelectedListener(this);
        switch (data.getProductMustInsurance()) {
            case 1:
                spInsurance.setSelection(0);
                spInsurance.setEnabled(false);
                orderData.setMustInsurance(1);
                break;
            default:
                spInsurance.setSelection(1);
                spInsurance.setEnabled(true);
                orderData.setMustInsurance(0);
                break;
        }
        if (data.getProductPreorder() != null
                && data.getProductPreorder().getPreorderStatus() == 1) {
            tvPreOrder.setVisibility(View.VISIBLE);
            linearLayoutPreOrder.setVisibility(View.VISIBLE);
            tvPreOrderInfo.setText(MessageFormat.format("{0} {1}",
                    data.getProductPreorder().getPreorderProcessTime(),
                    data.getProductPreorder().getPreorderProcessTimeTypeString()));
            btnBuy.setText(getString(R.string.title_pre_order));
        } else {
            tvPreOrder.setVisibility(View.GONE);
            linearLayoutPreOrder.setVisibility(View.GONE);
            btnBuy.setText(getString(R.string.title_buy));
        }
        tvProductName.setText(data.getProductName());
        etQuantity.setText(
                (orderData == null ? data.getProductMinOrder()
                        : String.valueOf(orderData.getQuantity()))
        );
        etQuantity.setEnabled(true);
        etRemark.setEnabled(true);
        if (!TextUtils.isEmpty(productCartPass.getNotes())) {
            etRemark.setText(productCartPass.getNotes());
        }
        tvProductPrice.setText(data.getProductPrice());
    }

    @Override
    public void renderFormAddress(Destination data) {
        if (data == null || !data.isCompleted()) {
            tvAddressName.setText(getString(R.string.error_no_address_title));
            tvAddressDetail.setText(getString(R.string.error_no_address));
            etValueLocation.setEnabled(false);
            etQuantity.setEnabled(false);
            btnAddressChange.setEnabled(false);
            btnAddressNew.setEnabled(true);
        } else {
            this.mDestination = data;
            tvAddressName.setText(MethodChecker.fromHtml(data.getAddressName()));
            tvAddressDetail.setText(MethodChecker.fromHtml(data.getAddressDetail()));
            etValueLocation.setEnabled(true);
            etQuantity.setEnabled(true);
            btnAddressChange.setEnabled(true);
            btnAddressNew.setEnabled(true);
            etValueLocation.setText(data.getGeoLocation(this));
        }
    }

    @Override
    public void renderFormShipment(List<Shipment> datas) {
        finishCalculateCartLoading();
        datas.add(0, Shipment.createSelectionInfo(getString(R.string.atc_selection_shipment_info)));
        ArrayAdapter<Shipment> agencyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, datas);
        agencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShippingAgency.setAdapter(agencyAdapter);
        spShippingAgency.setEnabled(true);
        spShippingAgency.setOnItemSelectedListener(this);
        spShippingService.setOnItemSelectedListener(this);
        spShippingService.setEnabled(true);
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getShipmentId().equals(orderData.getShipment())) {
                spShippingAgency.setSelection(i);
            }
        }
        this.mShipments = new ArrayList<>(datas);
        if (this.mShipments.size() > 0) {
            this.mShipments.remove(0);
        }
    }

    @Override
    public void renderFormShipmentRates(List<Attribute> datas) {
        finishCalculateCartLoading();
        datas.add(0, Attribute.createSelectionInfo(getString(R.string.atc_selection_shipment_info)));
        ArrayAdapter<Attribute> agencyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, datas);
        agencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShippingAgency.setAdapter(agencyAdapter);
        spShippingAgency.setEnabled(true);
        spShippingAgency.setOnItemSelectedListener(this);
        spShippingService.setOnItemSelectedListener(this);
        spShippingService.setEnabled(true);
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getShipperId().equals(orderData.getShipment())) {
                spShippingAgency.setSelection(i);
            }
        }
        btnBuy.setEnabled(true);

        this.mShipmentRateAttrs = new ArrayList<>(datas);
        if (this.mShipmentRateAttrs.size() > 0) {
            this.mShipmentRateAttrs.remove(0);
        }
    }

    @Override
    public void hideNetworkError() {
        container.setVisibility(View.VISIBLE);
        View mainView = findViewById(R.id.main_scroll);
        if (mainView != null) mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTickerGTM(String message) {
        tvTickerGTM.setText(MethodChecker.fromHtml(message));
        tvTickerGTM.setVisibility(View.VISIBLE);
        tvTickerGTM.setAutoLinkMask(0);
        Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
    }

    @Override
    public void hideTickerGTM() {
        tvTickerGTM.setVisibility(View.GONE);
    }

    @Override
    public void showAddressErrorMessage() {
        NetworkErrorHelper.showSnackbar(this,
                getString(R.string.address_not_supported_add_to_cart));
    }

    @Override
    public void enableQuantityTextWatcher() {
        etQuantity.addTextChangedListener(this);
    }

    @Override
    public void changeQuantity(String quantity) {
        etQuantity.setText(quantity);
    }

    @Override
    public void renderProductPrice(String price) {
        this.orderData.setPriceTotal(price);
        tvProductPrice.setText(price);
    }

    @Override
    public void disableAllForm() {
        etQuantity.setEnabled(false);
        etRemark.setEnabled(false);
        etValueLocation.setEnabled(false);
        spShippingAgency.setEnabled(false);
        spShippingService.setEnabled(false);
        btnAddressChange.setEnabled(false);
        btnAddressNew.setEnabled(false);
    }

    @Override
    public void disableBuyButton() {
        CommonUtils.dumper("buyrel disabled button called");
        btnBuy.setEnabled(false);
    }

    @Override
    public void enableBuyButton() {
        finishCalculateCartLoading();
        CommonUtils.dumper("buyrel enabled button called");
        btnBuy.setEnabled(true);
    }

    @Override
    public void showErrorMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCalculateProductErrorMessage(String errorMessage) {
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this,
                errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.calculateProduct(AddToCartActivity.this, orderData,
                                mustReCalculateShippingRate);
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void showCalculateShippingErrorMessage() {
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.calculateAllPrices(AddToCartActivity.this, orderData);
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void showCalculateAddressShippingError() {
        finishCalculateCartLoading();
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.calculateKeroAddressShipping(AddToCartActivity.this, orderData);
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void showUpdateAddressShippingError(String messageError) {
        finishCalculateCartLoading();

        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this, messageError,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processChooseGeoLocation(AddToCartActivity.this, orderData);
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void retryNoConnection(DialogNoConnection.ActionListener listener) {
        DialogNoConnection.createShow(this, listener);
    }

    @Override
    public void showInitLoading() {
        parentView.setVisibility(View.GONE);
        progressInitDialog.showDialog();
    }


    @Override
    public void hideInitLoading() {
        parentView.setVisibility(View.VISIBLE);
        progressInitDialog.dismiss();
    }

    @Override
    public void onCartFailedLoading() {
        hideInitLoading();
        View mainView = findViewById(R.id.main_scroll);
        if (mainView != null) mainView.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(this, parentView,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getCartFormData(AddToCartActivity.this, productCartPass);
                    }
                });
    }

    @Override
    public String getGoogleMapLocation() {
        if (etValueLocation.isShown()) {
            return etValueLocation.getText().toString();
        }
        return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter().getItem(position) instanceof Attribute) {
            List<Product> list = ((Attribute) parent.getAdapter()
                    .getItem(position)).getProducts();
            if (((Attribute) parent.getAdapter().getItem(position)).getProducts().size() != 1
                    && (list.size() < 1
                    || !((Attribute) parent.getAdapter()
                    .getItem(position)).getProducts().get(0).getShipperProductId().equals("0"))) {
                list.add(0, Product.createSelectionInfo(
                        getString(R.string.atc_selection_shipment_package_info)));
            }
            ArrayAdapter<Product> serviceAdapter = new ArrayAdapter<>(AddToCartActivity.this,
                    android.R.layout.simple_spinner_item, list);
            serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spShippingService.setAdapter(serviceAdapter);
            String shipperId = ((Attribute) parent.getAdapter()
                    .getItem(position)).getShipperId();
            orderData.setShipment(shipperId);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getShipperProductId().equals(orderData.getShipmentPackage())) {
                    spShippingService.setSelection(i);
                }
            }
            tvErrorShipping.setVisibility(View.GONE);
            mustReCalculateShippingRate = shipperId.equalsIgnoreCase(TkpdState.SHIPPING_ID.WAHANA);
            if (mustReCalculateShippingRate) {
                if (!hasRecalculateShippingRate) {
                    presenter.calculateProduct(this, orderData, mustReCalculateShippingRate);
                    hasRecalculateShippingRate = true;
                }
            } else {
                hasRecalculateShippingRate = false;
            }
        } else if (parent.getAdapter().getItem(position) instanceof Product) {
            orderData.setShipmentPackage(((Product) parent.getAdapter()
                    .getItem(position)).getShipperProductId());
            tvShippingPrice.setText(MessageFormat.format("{0}", ((Product)
                    parent.getAdapter().getItem(position)).getFormattedPrice()));
            tvErrorShipping.setVisibility(View.GONE);
            Product product = (Product) parent.getAdapter().getItem(position);
            if (product.getIsShowMap() == 1) {
                viewFieldLocation.setVisibility(View.VISIBLE);
                if (!(etValueLocation.getText().length() > 0)) {
                    Snackbar.make(
                            cartCoordinatLayout,
                            getString(R.string.message_gojek_shipping_package),
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            } else {
                renderFormAddress(orderData.getAddress());
                viewFieldLocation.setVisibility(View.GONE);
                clearRetryInstantCourierSnackbar();
            }

            if (product.getInsuranceMode() != null) {
                setInsuranceInfoButtonVisibility(product);
            }

            if (product.getMaxHoursId() != null && product.getDescHoursId() != null) {
                arrowMaxHour.setText(product.getMaxHoursId());
                descMaxHour.setText(product.getDescHoursId());
                shipmentHourAtcLayout.setVisibility(View.VISIBLE);
            } else {
                shipmentHourAtcLayout.setVisibility(View.GONE);
            }
        } else if (parent.getAdapter().getItem(position) instanceof Insurance) {
            orderData.setInsurance(((Insurance) parent.getAdapter().getItem(position)).isInsurance()
                    ? Insurance.INSURANCE : Insurance.NOT_INSURANCE);
        }
    }

    private void setInsuranceInfoButtonVisibility(Product product) {
        setInsuranceSpinnerVisibility(product);

        if (product.getShipperProductName().equals(getString(R.string.atc_selection_shipment_package_info))) {
            imgInsuranceInfo.setVisibility(View.GONE);
        } else {
            if (product.getInsuranceUsedInfo() != null && product.getInsuranceUsedInfo().length() > 0) {
                insuranceInfo = product.getInsuranceUsedInfo();
                imgInsuranceInfo.setVisibility(View.VISIBLE);
            } else {
                imgInsuranceInfo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setInsuranceSpinnerVisibility(Product product) {
        if (product.getInsuranceMode() != null) {
            if (product.getInsuranceMode() == KeroppiConstants.InsuranceType.MUST) {
                spInsurance.setEnabled(false);
                spInsurance.setSelection(0);
            } else if (product.getInsuranceMode() == KeroppiConstants.InsuranceType.NO) {
                spInsurance.setEnabled(false);
                spInsurance.setSelection(1);
            } else if (product.getInsuranceMode() == KeroppiConstants.InsuranceType.OPTIONAL) {
                spInsurance.setEnabled(true);
                if (product.getInsuranceUsedDefault() == KeroppiConstants.InsuranceUsedDefault.YES) {
                    spInsurance.setSelection(0);
                } else if (product.getInsuranceUsedDefault() == KeroppiConstants.InsuranceUsedDefault.NO) {
                    spInsurance.setSelection(1);
                }
            }
        } else {
            spInsurance.setEnabled(true);
            spInsurance.setSelection(1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            tvErrorShipping.setVisibility(View.GONE);
            switch (requestCode) {
                case REQUEST_CHOOSE_ADDRESS:
                    renderFormAddress(
                            Destination.convertFromBundle(
                                    data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                            )
                    );
                    this.orderData.setAddress(
                            Destination.convertFromBundle(
                                    data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                            )
                    );
                    startCalculateCartLoading();
                    presenter.calculateKeroAddressShipping(this, orderData);
                    CommonUtils.dumper("rates/v1 kerorates called request choose address");
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    Destination addressData = presenter.generateAddressData(data);
                    startCalculateCartLoading();
                    if (this.orderData.getAddress() == null || !this.orderData.getAddress().isCompleted()) {
                        this.orderData.setAddress(addressData);
                        presenter.getCartKeroToken(this, productCartPass, addressData);
                    } else {
                        renderFormAddress(addressData);
                        this.orderData.setAddress(addressData);
                        presenter.calculateKeroAddressShipping(this, orderData);
                    }
                    break;
                case REQUEST_CHOOSE_LOCATION:
                    etQuantity.removeTextChangedListener(this);
                    Bundle bundle = data.getExtras();
                    LocationPass locationPass = bundle.getParcelable(
                            GeolocationActivity.EXTRA_EXISTING_LOCATION
                    );
                    if (locationPass != null) {
                        startCalculateCartLoading();
                        presenter.updateAddressShipping(this, orderData, locationPass);
                        this.mLocationPass = locationPass;
                    }
                    break;
            }
        } else if (resultCode == RESULT_NOT_SELECTED_DESTINATION) {
            renderFormAddress(
                    Destination.convertFromBundle(
                            data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                    )
            );
            this.orderData.setAddress(
                    Destination.convertFromBundle(
                            data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                    )
            );
            startCalculateCartLoading();
            presenter.calculateKeroAddressShipping(this, orderData);
        }
    }

    @OnClick({R2.id.layout_value_geo_location, R2.id.et_geo_location})
    void actionGeoLocation() {
        clearRetryInstantCourierSnackbar();
        presenter.processChooseGeoLocation(this, orderData);
    }

    @OnClick(R2.id.btn_choose_address)
    void actionChangeAddress() {
        UnifyTracking.eventATCChangeAddress();
        Intent intent2 = new Intent(this, ChooseAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS,
                this.orderData.getAddress().getAddressId());
        intent2.putExtras(bundle);
        navigateToActivityRequest(intent2, REQUEST_CHOOSE_ADDRESS);
    }

    @OnClick(R2.id.btn_add_address)
    void actionAddNewAddress() {
        presenter.sendToGTM(this);
        Intent intent1 = new Intent(this, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ManageAddressConstant.IS_EDIT, false);
        intent1.putExtras(bundle);
        startActivityForResult(intent1, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @OnClick(R2.id.btn_buy)
    void actionBuy() {
        if (presenter.isValidOrder(this, orderData)) {
            presenter.addToCartService(this, atcReceiver, createFinalOrderData());
            presenter.sendAppsFlyerATC(this, orderData);

            processCartAnalytics(mProductDetail);
        }
    }

    private void processCartAnalytics(ProductDetail productDetail) {
        if (productDetail != null) {
            com.tokopedia.core.analytics.model.Product product = new com.tokopedia.core.analytics.model.Product();
            product.setCategoryName(productDetail.getProductCatName());
            product.setCategoryId(productDetail.getProductCatId());
            product.setName(productDetail.getProductName());
            product.setId(productDetail.getProductId());
            product.setPrice(productDetail.getProductPrice());

            TrackingUtils.sendMoEngageAddToCart(product);
        }
    }

    @OnClick(R2.id.increase_button)
    void actionIncreaseQuantity() {
        if (!etQuantity.getText().toString().isEmpty()
                && Integer.parseInt(etQuantity.getText().toString()) > 0) {
            etQuantity.setText(String
                    .valueOf(Integer.parseInt(etQuantity.getText().toString()) + 1));
        } else etQuantity.setText("1");
    }

    @OnClick(R2.id.decrease_button)
    void actionDecreaseQuantity() {
        if (!etQuantity.getText().toString().isEmpty()
                && Integer.parseInt(etQuantity.getText().toString()) > 1) {
            etQuantity.setText(String
                    .valueOf(Integer.parseInt(etQuantity.getText().toString()) - 1));
        } else etQuantity.setText("1");
    }

    @OnClick(R2.id.img_insurance_info)
    void showInsuranceInfo() {
        BottomSheetView bottomSheetView = new BottomSheetView(this);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getString(R.string.title_bottomsheet_insurance))
                .setBody(insuranceInfo)
                .setImg(R.drawable.ic_insurance)
                .build());

        bottomSheetView.show();
    }

    private OrderData createFinalOrderData() {
        OrderData finalOrder = this.orderData;
        finalOrder.setNotes(etRemark.getText().toString());
        return finalOrder;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable quantity) {
        if (subscription == null || subscription.isUnsubscribed()) quantityChangedEvent(quantity);
    }

    private void quantityChangedEvent(Editable quantity) {
        if (orderData == null) {
            presenter.getCartFormData(this, productCartPass);
            return;
        }
        orderData.setQuantity(quantity.length() == 0 ?
                0 : Integer.parseInt(etQuantity.getText().toString()));
        if (orderData.getQuantity() < orderData.getMinOrder()) {
            tilAmount.setError(getString(R.string.error_min_order)
                    + " " + orderData.getMinOrder());
            if (etQuantity.requestFocus())
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                );
        } else if (orderData.getAddress() == null) {
            showErrorMessage(getString(R.string.error_no_address));
        } else {
            CommonUtils.dumper("rates/v1 kerorates called aftertextchanged");
            orderData.setWeight(presenter.calculateWeight(orderData.getInitWeight(),
                    quantity.toString()));
            tilAmount.setError(null);
            tilAmount.setErrorEnabled(false);
            presenter.calculateAllPrices(AddToCartActivity.this, orderData);
            presenter.calculateProduct(AddToCartActivity.this, orderData,
                    mustReCalculateShippingRate);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ATCIntentService.RESULT_ADD_TO_CART_ERROR:
                showBuyError(resultData.getString(ATCIntentService.EXTRA_MESSAGE));
                break;
            case ATCIntentService.RESULT_ADD_TO_CART_SUCCESS:
                hideProgressLoading();
                presenter.sendAnalyticsATCSuccess(this, productCartPass, createFinalOrderData());
                presenter.sendAddToCartCheckoutAnalytic(this,
                        productCartPass,
                        etQuantity.getText().toString(),
                        getIntent().getExtras());
                presenter.processAddToCartSuccess(this,
                        resultData.getString(ATCIntentService.EXTRA_MESSAGE));
                presenter.setCacheCart(this);
                break;
            case ATCIntentService.RESULT_ADD_TO_CART_RUNNING:
                showProgressLoading();
                break;
            case ATCIntentService.RESULT_ADD_TO_CART_TIMEOUT:
                showBuyError(getString(R.string.msg_connection_timeout_toast));
                break;
            case ATCIntentService.RESULT_ADD_TO_CART_NO_CONNECTION:
                showBuyError(getString(R.string.msg_no_connection));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_STATE_ORDER_DATA, this.orderData);
        outState.putParcelable(EXTRA_STATE_DESTINATION_DATA, this.mDestination);
        outState.putParcelable(EXTRA_STATE_LOCATION_PASS_DATA, this.mLocationPass);
        outState.putParcelable(EXTRA_STATE_PRODUCT_DETAIL_DATA, this.mProductDetail);
        outState.putParcelableArrayList(
                EXTRA_STATE_SHIPMENT_LIST_DATA, (ArrayList<? extends Parcelable>) this.mShipments
        );
        outState.putParcelableArrayList(
                EXTRA_STATE_SHIPMENT_RATE_LIST_DATA,
                (ArrayList<? extends Parcelable>) this.mShipmentRateAttrs
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.orderData = savedInstanceState.getParcelable(EXTRA_STATE_ORDER_DATA);
            this.mDestination = savedInstanceState.getParcelable(EXTRA_STATE_DESTINATION_DATA);
            this.mLocationPass = savedInstanceState.getParcelable(EXTRA_STATE_LOCATION_PASS_DATA);
            this.mProductDetail = savedInstanceState.getParcelable(EXTRA_STATE_PRODUCT_DETAIL_DATA);
            this.mShipments = savedInstanceState.getParcelableArrayList(
                    EXTRA_STATE_SHIPMENT_LIST_DATA
            );
            this.mShipmentRateAttrs = savedInstanceState.getParcelableArrayList(
                    EXTRA_STATE_SHIPMENT_RATE_LIST_DATA
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.orderData == null) {
            presenter.getCartFormData(this, productCartPass);
        } else {
            hideNetworkError();
            renderFormProductInfo(this.mProductDetail);
            renderFormAddress(this.mDestination);
            if (this.mShipmentRateAttrs != null) {
                renderFormShipmentRates(this.mShipmentRateAttrs);
            }
            hideInitLoading();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showBuyError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
        progressDialog.dismiss();
    }

    private void startCalculateCartLoading() {
        calculateCartProgressBar.setVisibility(View.VISIBLE);
        tvShippingPrice.setVisibility(View.GONE);
    }

    private void finishCalculateCartLoading() {
        calculateCartProgressBar.setVisibility(View.GONE);
        tvShippingPrice.setVisibility(View.VISIBLE);
    }

    private Subscriber<Long> increaseQuantitySubscriber() {
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long timeCounter) {
                if (timeCounter > .8) actionIncreaseQuantity();
            }
        };
    }

    private Subscriber<Long> decreaseQuantitySubscriber() {
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long timerCounter) {
                if (timerCounter > .8) actionDecreaseQuantity();
            }
        };
    }

    private Observable<Long> incrementCounterSubscription() {
        return incrementObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private View.OnTouchListener onIncrementButtonTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (subscription != null) subscription.unsubscribe();
                    subscription = incrementCounterSubscription().subscribe(
                            increaseQuantitySubscriber()
                    );
                } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL ||
                        motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (subscription != null) {
                        subscription.unsubscribe();
                    }
                }
                return false;
            }
        };
    }

    private View.OnTouchListener onDecrementButtonTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (subscription != null) subscription.unsubscribe();
                    subscription = incrementCounterSubscription()
                            .subscribe(decreaseQuantitySubscriber());
                } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL ||
                        motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (subscription != null) {
                        subscription.unsubscribe();
                    }
                }
                return false;
            }
        };
    }

    private void clearRetryInstantCourierSnackbar() {
        if (snackbarRetry != null && snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
            btnBuy.setEnabled(true);
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
