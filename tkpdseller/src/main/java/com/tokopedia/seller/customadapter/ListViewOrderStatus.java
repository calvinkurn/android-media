package com.tokopedia.seller.customadapter;

import java.util.ArrayList;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.seller.selling.model.shopconfirmationdetail.ShippingConfirmDetModel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewOrderStatus extends BaseAdapter {

	public static final String SYSTEM_TRACKER = "System-Tracker";
	public static final String TOKOPEDIA = "Tokopedia";
	public static final String BUYER = "Buyer";
	public static final String SELLER = "Seller";
	private ArrayList<ShippingConfirmDetModel.DataHistory> dataHistories;

//	private ArrayList<String> ActorList = new ArrayList<String>();
//	private ArrayList<String> DateList = new ArrayList<String>();
//	private ArrayList<String> StateList = new ArrayList<String>();
//	private ArrayList<String> CommentList = new ArrayList<String>();
	private Activity context;
	private LayoutInflater inflater;

	public ListViewOrderStatus(Activity context, ArrayList<ShippingConfirmDetModel.DataHistory> dataHistories){
		this.context = context;
		this.dataHistories = dataHistories;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataHistories.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataHistories.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public class Holder{
		TextView Actor;
		TextView Date;
		TextView State;
		TextView Comments;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder;
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.listview_order_status,null);
			holder.Actor = (TextView) convertView.findViewById(R.id.actor);
			holder.Date = (TextView) convertView.findViewById(R.id.date);
			holder.State = (TextView) convertView.findViewById(R.id.state);
			holder.Comments = (TextView) convertView.findViewById(R.id.comment);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		ShippingConfirmDetModel.DataHistory dataHistory = (ShippingConfirmDetModel.DataHistory)getItem(position);
		if(dataHistory.ActorList.equals(TOKOPEDIA) || dataHistory.ActorList.equals(SYSTEM_TRACKER))
			holder.Actor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_gray));
		else if(dataHistory.ActorList.equals(BUYER))
			holder.Actor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_orange));
		else if(dataHistory.ActorList.equals(SELLER))
			holder.Actor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));
		if(!dataHistory.CommentList.equals("null") && CommonUtils.checkNullForZeroJson(dataHistory.CommentList)){
			holder.Comments.setVisibility(View.VISIBLE);
			holder.Comments.setText(dataHistory.CommentList.replaceAll("<br/>\\p{Space}+", "\n"));
		}
		else{
			holder.Comments.setVisibility(View.GONE);
		}
		holder.Actor.setText(dataHistory.ActorList);
		holder.Date.setText(dataHistory.DateList);
		holder.State.setText(dataHistory.StateList);
		return convertView;
	}

}
