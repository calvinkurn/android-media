package com.tokopedia.seller.selling.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core.R;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.fragment.FragmentShopNewOrderDetailV2;
import com.tokopedia.seller.fragment.FragmentShopShippingDetailV2;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.orderstatus.fragment.FragmentShopTxStatusDetailV2;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.constant.SellingServiceConstant;
import com.tokopedia.seller.selling.presenter.listener.SellingView;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.presenter.ShippingImpl;
import com.tokopedia.core.service.DownloadService;

import org.parceler.Parcels;

/**
 * Created by Erry on 7/25/2016.
 */
public class SellingDetailActivity extends TkpdActivity implements  DownloadResultReceiver.Receiver {

    Toolbar toolbar;

    public enum Type {
        NEW_ORDER,
        SHIPING,
        STATUS,
        TRANSACTION
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_SELLING_DETAIL;
    }

    @Override
    public int getDrawerPosition() {
        return TkpdState.DrawerPosition.SHOP;
    }

    private FragmentManager fragmentManager;
    public static final String DATA_EXTRA2 = "DATA_EXTRA2";
    public static final String TYPE_EXTRA = "TYPE_EXTRA";
    public static final String DATA_EXTRA = "DATA_EXTRA";
    public static final String NEW_ORDER_TAG = "NEW_ORDER_TAG";
    public static final String SHIPING_TAG = "SHIPING_TAG";
    public static final String STATUS_TRANSACTION_TAG = "STATUS_TRANSACTION_TAG";
    private String TAG_FRAGMENT = "";
    DownloadResultReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_selling_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        setFragment((Type) getIntent().getExtras().getSerializable(TYPE_EXTRA));
    }

    private void setFragment(Type type){
        Fragment fragment = null;
        switch (type){
            case NEW_ORDER:
                OrderShippingList orderShippingList = Parcels.unwrap(getIntent().getParcelableExtra(DATA_EXTRA));
                String permission = getIntent().getStringExtra(DATA_EXTRA2);
                fragment = FragmentShopNewOrderDetailV2.createInstance(orderShippingList, permission, 0);
                TAG_FRAGMENT = NEW_ORDER_TAG;
                break;
            case SHIPING:
                ShippingImpl.Model model = Parcels.unwrap(getIntent().getParcelableExtra(DATA_EXTRA));
                fragment = FragmentShopShippingDetailV2.createInstance(generateParam(model), 0); //0 not use
                TAG_FRAGMENT = SHIPING_TAG;
                break;
            case STATUS:
            case TRANSACTION:
                SellingStatusTxModel data = Parcels.unwrap(getIntent().getParcelableExtra(DATA_EXTRA));
                fragment = FragmentShopTxStatusDetailV2.createInstance(data.DataList, data.Permission);
                TAG_FRAGMENT = STATUS_TRANSACTION_TAG;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment, TAG_FRAGMENT).commit();
    }

    private FragmentShopShippingDetailV2.Param generateParam(ShippingImpl.Model model) {
        FragmentShopShippingDetailV2.Param param = new FragmentShopShippingDetailV2.Param();
        param.orderShippingList = model.orderShippingList;
        param.orderId = model.OrderId;
        param.permission = model.Permission;
        param.userId = model.BuyerId;
        return param;
    }



    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(DownloadService.TYPE, DownloadService.INVALID_TYPE);
        Fragment fragment = null;
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                fragment = fragmentManager.findFragmentByTag(SHIPING_TAG);
                break;
            case SellingServiceConstant.REJECT_ORDER_WITH_REASON:
            case SellingService.CONFIRM_NEW_ORDER:
            case SellingService.REJECT_NEW_ORDER:
            case SellingService.PARTIAL_NEW_ORDER:
                fragment = fragmentManager.findFragmentByTag(NEW_ORDER_TAG);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if (fragment != null && type != DownloadService.INVALID_TYPE) {
            switch (resultCode) {
                case SellingService.STATUS_RUNNING:
                    switch (type) {
                        case SellingServiceConstant.REJECT_ORDER_WITH_REASON:
                        case SellingService.CONFIRM_SHIPPING:
                        case SellingService.CANCEL_SHIPPING:
                        case SellingService.CONFIRM_NEW_ORDER:
                        case SellingService.REJECT_NEW_ORDER:
                        case SellingService.PARTIAL_NEW_ORDER:
                            //[START] show progress bar
                            if (fragment instanceof SellingView) {
                                ((SellingView) fragment).showProgress();
                            }
                            break;
                    }
                    break;
                case SellingService.STATUS_FINISHED:
                    switch (type) {
                        case SellingServiceConstant.REJECT_ORDER_WITH_REASON:
                        case SellingService.CONFIRM_SHIPPING:
                        case SellingService.CANCEL_SHIPPING:
                        case SellingService.CONFIRM_NEW_ORDER:
                        case SellingService.REJECT_NEW_ORDER:
                        case SellingService.PARTIAL_NEW_ORDER:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
                    }
                    break;
                case SellingService.STATUS_ERROR:
                    switch (resultData.getInt(SellingService.NETWORK_ERROR_FLAG, SellingService.INVALID_NETWORK_ERROR_FLAG)) {
                        case SellingService.MESSAGE_ERROR_FLAG_RESPONSE:
                            String messageError = resultData.getString(SellingService.MESSAGE_ERROR_FLAG, SellingService.INVALID_MESSAGE_ERROR);
                            if (!messageError.equals(SellingService.INVALID_MESSAGE_ERROR)) {
                                ((BaseView) fragment).onMessageError(type, messageError);
                            }
                            break;
                        case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                        case NetworkConfig.INTERNAL_SERVER_ERROR:
                        case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                        case SellingService.INVALID_NETWORK_ERROR_FLAG:
                        default:
                            ((BaseView) fragment).onNetworkError(type, getString(R.string.error_connection_problem));
                    }
                    break;
            }// end of status download service
        }
    }

    public void SellingAction(int type, Bundle data) {
        switch (type) {
            case SellingService.REJECT_ORDER_WITH_REASON:
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
            case SellingService.CONFIRM_NEW_ORDER:
            case SellingService.REJECT_NEW_ORDER:
            case SellingService.PARTIAL_NEW_ORDER:
                SellingService.startSellingService(this, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }
}
