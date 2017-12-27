package com.tokopedia.seller.shop.setting.view.fragment;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.view.CourierListViewGroup;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenterImpl;

import javax.inject.Inject;

public class ShopSettingLogisticFragment extends BasePresenterFragment{


}
