package com.tokopedia.seller.shopsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.EditShippingActivity;

import java.util.ArrayList;

/**
 * Use in reflection by SellerRouter
 * If you want to rename the class or refactor its package, rename also the route at SellerRouter
 */
public class FragmentSettingShop extends TkpdFragment {
    private SimpleListTabViewAdapter lvAdapter;
    private ListView lvManage;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<Integer> ResID = new ArrayList<Integer>();
    private SplitInstallManager splitInstallManager;

    private SplitInstallStateUpdatedListener splitInstallStateUpdatedListener = splitInstallSessionState -> {
        String state = splitInstallSessionState.moduleNames() + " - " +
                splitInstallSessionState.bytesDownloaded() + "/" + splitInstallSessionState.totalBytesToDownload() + " - " +
                splitInstallSessionState.errorCode();
        switch (splitInstallSessionState.status()) {
            case SplitInstallSessionStatus.UNKNOWN:
                Toast.makeText(getActivity(), "UNKNOWN: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.PENDING:
                Toast.makeText(getActivity(), "PENDING: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.DOWNLOADING:
                Toast.makeText(getActivity(), "DOWNLOADING: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.DOWNLOADED:
                Toast.makeText(getActivity(), "DOWNLOADED: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.INSTALLING:
                Toast.makeText(getActivity(), "INSTALLING: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.INSTALLED:
                Toast.makeText(getActivity(), "INSTALLED: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.FAILED:
                Toast.makeText(getActivity(), "FAILED: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.CANCELED:
                Toast.makeText(getActivity(), "CANCELED: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                Toast.makeText(getActivity(), "REQUIRES_USER_CONFIRMATION: " + state, Toast.LENGTH_LONG).show();
            case SplitInstallSessionStatus.CANCELING:
                Toast.makeText(getActivity(), "CANCELING: " + state, Toast.LENGTH_LONG).show();
        }
    };

    public static FragmentSettingShop newInstance() {
        return new FragmentSettingShop();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SETTING_MANAGE_SHOP;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_manage_general, container, false);
        Name.clear();
        ResID.clear();
        Name.add(getString(R.string.title_shop_information_menu));
        Name.add(getString(R.string.title_shipping_menu));
        Name.add(getString(R.string.title_etalase_menu));
        Name.add(getString(R.string.title_notes_menu));
        Name.add(getString(R.string.title_location_menu));
        ResID.add(R.drawable.ic_set_shop_info);
        ResID.add(R.drawable.ic_set_shipping);
        ResID.add(R.drawable.ic_set_payment);
        ResID.add(R.drawable.ic_set_etalase);
        ResID.add(R.drawable.ic_set_notes);
        ResID.add(R.drawable.ic_set_location);
        lvManage = (ListView) mainView.findViewById(R.id.list_manage);
        lvAdapter = new SimpleListTabViewAdapter(getActivity(), Name, ResID);
        lvManage.setAdapter(lvAdapter);
        lvManage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Intent intent;
                switch (pos) {
                    case 0:
                        UnifyTracking.eventManageShopInfo(getActivity());
                        loadAndLaunchModule(ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), EditShippingActivity.class);
                        UnifyTracking.eventManageShopShipping(getActivity());
                        startActivity(intent);
                        break;
                    case 2:
                        UnifyTracking.eventManageShopEtalase(getActivity());
                        loadAndLaunchModule(ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE);
                        break;
                    case 3:
                        UnifyTracking.eventManageShopNotes(getActivity());
                        loadAndLaunchModule(ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES);
                        break;
                    case 4:
                        UnifyTracking.eventManageShopLocation(getActivity());
                        loadAndLaunchModule(ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS);
                        break;
                }
            }
        });
        splitInstallManager = SplitInstallManagerFactory.create(getActivity());
        return mainView;
    }

    private void loadAndLaunchModule(String deeplink) {
        String moduleName = getString(R.string.module_feature_shop_settings_sellerapp);
        if(splitInstallManager.getInstalledModules().contains(moduleName)) {
            Crashlytics.logException(new Exception("Installing module shop_settings_sellerapp"));
            goToPage(deeplink);
            return;
        }
        Tooltip tooltip = new Tooltip(getActivity());
        tooltip.setTitle(getString(R.string.dynamic_feature_title_install));
        tooltip.setDesc(getString(R.string.dynamic_feature_description_install));
        tooltip.setTextButton(getString(R.string.dynamic_feature_button_install));
        tooltip.getBtnAction().setOnClickListener(v -> {
            SplitInstallRequest splitInstallRequest = SplitInstallRequest.newBuilder().addModule(moduleName).build();
            splitInstallManager.startInstall(splitInstallRequest);
            Toast.makeText(getActivity(), "Installing", Toast.LENGTH_LONG).show();
            tooltip.dismiss();
        });
        tooltip.show();
    }

    private void goToPage(String deeplink) {
        if (ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO.equals(deeplink)) {
            startActivityForResult(RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO), 0);
        } else {
            RouteManager.route(getActivity(), deeplink);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
//        splitInstallManager.registerListener(splitInstallStateUpdatedListener);
        super.onResume();
    }

    @Override
    public void onPause() {
//        splitInstallManager.unregisterListener(splitInstallStateUpdatedListener);
        super.onPause();
    }
}