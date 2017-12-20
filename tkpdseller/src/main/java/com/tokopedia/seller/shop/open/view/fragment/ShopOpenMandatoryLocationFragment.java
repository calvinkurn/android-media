package com.tokopedia.seller.shop.open.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.router.OnActivityResultListener;
import com.tokopedia.core.router.logistic.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.ShopOpenStepperModel;
 import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.holder.ShopOpenMandatoryLocationHeaderViewHolder;
import com.tokopedia.seller.shop.open.view.model.DestinationViewModel;
import com.tokopedia.seller.shopsettings.shipping.customview.ShippingHeaderLayout;


/**
 * @author normansyahputa
 */
public class ShopOpenMandatoryLocationFragment extends BaseDaggerFragment {

    public static final int REQUEST_CODE_ADDRESS = 1234;
    protected ShopOpenStepperModel stepperModel;
    protected StepperListener stepperListener;
    private LogisticRouter logisticRouter;
    private static final String TAG = "ShopOpenMandatoryLocati";
    private ShippingHeaderLayout fragmentShippingHeader;

    public static ShopOpenMandatoryLocationFragment getInstance(){
        return new ShopOpenMandatoryLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_open_location, container, false);
        initView(root);
        return root;
    }

    private void initView(View root){
        new ShopOpenMandatoryLocationHeaderViewHolder(root, new ShopOpenMandatoryLocationHeaderViewHolder.ViewHolderListener() {
            @Override
            public void navigateToChooseAddressActivityRequest() {
                if(logisticRouter == null)
                    return;

                logisticRouter.navigateToChooseAddressActivityRequest(
                        ShopOpenMandatoryLocationFragment.this,
                        null,
                        REQUEST_CODE_ADDRESS
                );
            }
        });

        fragmentShippingHeader = root.findViewById(R.id.fragment_shipping_header);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null){
            switch (requestCode){
                case REQUEST_CODE_ADDRESS:
                    logisticRouter.onActivityResultChooseAddress(requestCode, data, new OnActivityResultListener<DestinationViewModel>() {
                        @Override
                        public void onActivityResult(DestinationViewModel rawData) {
                            Log.d(TAG, rawData.toString());
                            fragmentShippingHeader.updateLocationData(
                                    rawData.getProvinceName(),
                                    rawData.getCityName(),
                                    rawData.getDistrictName());
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }

        if(context.getApplicationContext() instanceof LogisticRouter){
            logisticRouter = (LogisticRouter)context.getApplicationContext();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null) {
            setupArguments(getArguments());
        }
    }

    protected void setupArguments(Bundle arguments) {
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initInjector() {
        getComponent(ShopOpenDomainComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
