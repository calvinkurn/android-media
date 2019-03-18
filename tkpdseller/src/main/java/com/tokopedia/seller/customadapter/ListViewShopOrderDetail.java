package com.tokopedia.seller.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.shopconfirmationdetail.ShippingConfirmDetModel;

import java.util.ArrayList;

/**
 * modified by m.normansyah  11/05/2016
 */
public class ListViewShopOrderDetail extends BaseAdapter{
	public static final String PRODUCT_URI = "product_uri";
	public static final String PRODUCT_ID = "product_id";
	private ArrayList<ShippingConfirmDetModel.Data> datas;
	private Activity context;
	private LayoutInflater inflater;
	private ImageHandler image;
	 
	public ListViewShopOrderDetail(Activity context, ArrayList<ShippingConfirmDetModel.Data> datas){
		this.context = context;
		this.datas = datas;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	public class Holder{
		ImageView ProductImage;
		TextView ProductName;
		TextView ProductPrice;
		TextView TotalOrder;
		TextView TotalPrice;
		TextView Message;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		Holder holder;
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.listview_shop_order_detail_product,null);
			holder.ProductImage = (ImageView) convertView.findViewById(R.id.product_image);
			holder.ProductName = (TextView) convertView.findViewById(R.id.product_name);
			holder.ProductPrice = (TextView) convertView.findViewById(R.id.product_price);
			holder.TotalOrder = (TextView) convertView.findViewById(R.id.total_order);
			holder.TotalPrice = (TextView) convertView.findViewById(R.id.total_price);
			holder.Message = (TextView) convertView.findViewById(R.id.message);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}

		final ShippingConfirmDetModel.Data data = (ShippingConfirmDetModel.Data)getItem(position);
		//holder.NotesName.setText(NotesListString.get(position));
		holder.ProductName.setText(MethodChecker.fromHtml(data.NameList).toString());
		holder.ProductPrice.setText(data.PriceList);
		if(!data.MessageList.equals("null") && CommonUtils.checkNullForZeroJson(data.MessageList))
			holder.Message.setText(MethodChecker.fromHtml(data.MessageList));
		else
			holder.Message.setText("-");
		holder.TotalOrder.setText(" x " + data.TtlOrderList + " " + context.getString(R.string.title_item));
		holder.TotalPrice.setText(data.TtlPriceList);
		if(!data.ImageUrlList.equals("null"))
			ImageHandler.loadImageRounded2(context, holder.ProductImage, data.ImageUrlList);
		holder.ProductName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				context.startActivity(
//						ProductDetailRouter.createInstanceProductDetailInfoActivity(
//								context, getProductDataToPass(data)
//						)
//				);
			}
		});
		return convertView;
	}

	private ProductPass getProductDataToPass(ShippingConfirmDetModel.Data data) {
		return ProductPass.Builder.aProductPass()
				.setProductPrice(data.PriceList)
				.setProductId(data.ProductIdList)
				.setProductName(data.NameList)
				.setProductImage(data.ImageUrlList)
				.build();
	}

}

