package com.tokopedia.seller.selling.orderReject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.seller.selling.orderReject.fragment.ConfirmRejectOrderFragment;
import com.tokopedia.seller.selling.orderReject.fragment.ShopClosedReasonFragment;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.selling.presenter.listener.SellingView;

import org.parceler.Parcels;

import butterknife.ButterKnife;

/**
 * Created by Erry on 6/3/2016.
 *
 */
public class ConfirmRejectOrderActivity extends TActivity implements DownloadResultReceiver.Receiver {

    public static final String TAG_REJECT_ORDER_FRAGMENT = "TAG_REJECT_ORDER_FRAGMENT";
    public static final String TAG_REJECT_ORDER_SHOP_CLOSE = "TAG_REJECT_ORDER_SHOP_CLOSE";
    public static final String REASON = "reason";
    public static final String INDEX = "index";
    public static final String TYPE = "type";
    public static final String ORDERS = "orders";
    public static final String ORDER_ID = "order_id";

    private FragmentManager fragmentManager;
    DownloadResultReceiver mReceiver;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ORDER_REJECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_confirm_reject_order);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();
        String reason = bundle.getString(REASON);
        OrderShippingList orderShippingList = Parcels.unwrap(bundle.getParcelable(ORDERS));
        String orderId = bundle.getString(ORDER_ID);
        int index = bundle.getInt(INDEX);

        Gson gson = new GsonBuilder().create();

        switch (index){
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, ConfirmRejectOrderFragment.newInstance(orderShippingList, reason, ProductListAdapter.Type.stock, orderId), TAG_REJECT_ORDER_FRAGMENT).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, ConfirmRejectOrderFragment.newInstance(orderShippingList, reason, ProductListAdapter.Type.varian, orderId), TAG_REJECT_ORDER_FRAGMENT).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, ConfirmRejectOrderFragment.newInstance(orderShippingList, reason, ProductListAdapter.Type.price, orderId), TAG_REJECT_ORDER_FRAGMENT).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, ShopClosedReasonFragment.newInstance(orderShippingList, reason, orderId), TAG_REJECT_ORDER_SHOP_CLOSE).commit();
                break;
        }
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(SellingService.TYPE, SellingService.INVALID_TYPE);
        Fragment fragment = null;
        switch (type) {
            case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
            case SellingService.REJECT_ORDER_CLOSE_SHOP:
                fragment = fragmentManager.findFragmentByTag(TAG_REJECT_ORDER_SHOP_CLOSE);
                break;
            case SellingService.GET_PRODUCT_FORM_EDIT:
            case SellingService.REJECT_ORDER:
            case SellingService.REJECT_ORDER_WITH_PRICE:
            case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
                fragment = fragmentManager.findFragmentByTag(TAG_REJECT_ORDER_FRAGMENT);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if (fragment != null && type != SellingService.INVALID_TYPE) {
            switch (resultCode) {
                case SellingService.STATUS_RUNNING:
                    switch (type) {
                        case SellingService.REJECT_ORDER:
                        case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
                        case SellingService.REJECT_ORDER_CLOSE_SHOP:
                        case SellingService.REJECT_ORDER_WITH_PRICE:
                            //[START] show progress bar
                            if (fragment instanceof SellingView) {
                                ((SellingView) fragment).showProgress();
                            }
                            break;
                        case SellingService.GET_PRODUCT_FORM_EDIT:
                            ((ConfirmRejectOrderFragment)fragment).showProgressBar();
                            break;
                        case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
                            ((ShopClosedReasonFragment) fragment).showProgressBar();
                            break;
                    }
                    break;
                case SellingService.STATUS_FINISHED:
                    switch (type) {
                        case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
                        case SellingService.GET_PRODUCT_FORM_EDIT:
                        case SellingService.REJECT_ORDER:
                        case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
                        case SellingService.REJECT_ORDER_CLOSE_SHOP:
                        case SellingService.REJECT_ORDER_WITH_PRICE:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
                    }
                    break;
                case SellingService.STATUS_ERROR:
                    switch (resultData.getInt(SellingService.NETWORK_ERROR_FLAG, SellingService.INVALID_NETWORK_ERROR_FLAG)) {
                        case ResponseStatus.SC_REQUEST_TIMEOUT:
                            ((BaseView) fragment).onNetworkError(type, getString(R.string.title_verification_timeout) + "\n" + getString(R.string.message_verification_timeout));
                            break;
                        case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                            ((BaseView) fragment).onNetworkError(type, getString(R.string.title_verification_timeout) + "\n" + getString(R.string.message_verification_timeout));
                            break;
                        case ResponseStatus.SC_BAD_REQUEST:
                            ((BaseView) fragment).onNetworkError(type, getString(R.string.title_verification_timeout) + "\n" + getString(R.string.message_verification_timeout));
                            break;
                        case ResponseStatus.SC_FORBIDDEN:
                            ((BaseView) fragment).onNetworkError(type, getString(R.string.title_verification_timeout) + "\n" + getString(R.string.message_verification_timeout));
                            break;
                        case SellingService.INVALID_NETWORK_ERROR_FLAG:
                        default:
                            String messageError = resultData.getString(SellingService.MESSAGE_ERROR_FLAG, SellingService.INVALID_MESSAGE_ERROR);
                            if (!messageError.equals(SellingService.INVALID_MESSAGE_ERROR)) {
                                ((BaseView) fragment).onMessageError(type, messageError);
                            }
                    }
                    break;
            }//end of switch
        }
    }

    public void ConfirmRejectOrder(int type, Bundle data) {
        switch (type) {
            case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
            case SellingService.GET_PRODUCT_FORM_EDIT:
            case SellingService.REJECT_ORDER_CLOSE_SHOP:
            case SellingService.REJECT_ORDER:
            case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
            case SellingService.REJECT_ORDER_WITH_PRICE:
                SellingService.startSellingService(this, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }
}
