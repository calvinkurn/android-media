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

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.seller.shopsettings.shipping.EditShippingActivity;
import com.tokopedia.shop.common.router.ShopSettingRouter;

import java.util.ArrayList;

/**
 * Use in reflection by SellerRouter
 * If you want to rename the class or refactor its package, rename also the route at SellerRouter
 */
public class FragmentSettingShop extends TkpdFragment{
	private SimpleListTabViewAdapter lvAdapter;
	private ListView lvManage;
	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<Integer> ResID = new ArrayList<Integer>();

	public static FragmentSettingShop newInstance() {
		return new FragmentSettingShop();
	}

	@Override
	protected String getScreenName() {
		return AppScreen.SCREEN_SETTING_MANAGE_SHOP;
	}

	@Override
	public void onAttach(Activity activity){
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
		lvManage = (ListView) mainView.findViewById (R.id.list_manage);
		lvAdapter = new SimpleListTabViewAdapter(getActivity(), Name, ResID);
		lvManage.setAdapter(lvAdapter);
		lvManage.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if (getActivity().getApplication() instanceof ShopSettingRouter){
					ShopSettingRouter router = (ShopSettingRouter) getActivity().getApplication();

					Intent intent;
					switch(pos) {
						case 0:
							UnifyTracking.eventManageShopInfo(getActivity());
							startActivityForResult(router.getManageShopBasicDataIntent(getActivity()), 0);
							break;
						case 1:
							intent = new Intent(getActivity(), EditShippingActivity.class);
							UnifyTracking.eventManageShopShipping(getActivity());
							startActivity(intent);
							break;
						case 2:
							UnifyTracking.eventManageShopEtalase(getActivity());
							startActivity(router.getManageShopEtalaseIntent(getActivity()));
							break;
						case 3:
							UnifyTracking.eventManageShopNotes(getActivity());
							startActivity(router.getManageShopNotesIntent(getActivity()));
							break;
						case 4:
							UnifyTracking.eventManageShopLocation(getActivity());
							startActivity(router.getManageShopLocationIntent(getActivity()));
							break;
					}
				}
			}
		});
		return mainView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isAdded() && getActivity() !=null) {
			ScreenTracking.screen(MainApplication.getAppContext(),getScreenName());
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
}