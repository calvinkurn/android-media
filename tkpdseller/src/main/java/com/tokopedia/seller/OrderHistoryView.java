package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ListView;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.customadapter.ListViewOrderStatus;
import com.tokopedia.seller.selling.model.shopconfirmationdetail.ShippingConfirmDetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryView extends TActivity {

    //TODO: jangan lupa tambahin untuk buyer!
    public static Intent createInstance(Context context, String statusList) {
        Intent intent = new Intent(context, OrderHistoryView.class);
        Bundle bundle = new Bundle();
        bundle.putString("status_list", statusList);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstanceSeller(Context context, java.util.List<OrderHistory> statusList) {
        Intent intent = new Intent(context, OrderHistoryView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("state", OrderHistoryView.STATE_SELLER);
        bundle.putParcelable("status_list", Parcels.wrap(statusList));
        bundle.putBoolean("DATA_FROM_WS_V4", true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ORDER_HISTORY;
    }

    public static Intent createInstanceBuyer(Context context, String statusList,
                                             String preorderInfo, boolean wsv4) {
        Intent intent = new Intent(context, OrderHistoryView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("state", OrderHistoryView.STATE_BUYER);
        bundle.putString("status_list", statusList);
        bundle.putString("preorder_info", preorderInfo);
        bundle.putBoolean("DATA_FROM_WS_V4", wsv4);
        intent.putExtras(bundle);
        return intent;
    }


    private ArrayList<String> ActorList = new ArrayList<String>();
    private ArrayList<String> DateList = new ArrayList<String>();
    private ArrayList<String> StateList = new ArrayList<String>();
    private ArrayList<String> CommentList = new ArrayList<String>();
    private ListViewOrderStatus OrderAdapter;
    private ListView OrderStatus;

    public static int STATE_BUYER = 1;
    public static int STATE_SELLER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_order_history_view);
        OrderStatus = (ListView) findViewById(R.id.order_status);
        int State = getIntent().getExtras().getInt("state", STATE_BUYER);

        if(State == STATE_BUYER){
            if (getIntent().getBooleanExtra("DATA_FROM_WS_V4", false)) {
                renderViewFromWS4Buyer(); //ini pembelian
            } else {
                renderViewFromWSNewBuyer(); //ini pembelian
            }
        }else{
            if (getIntent().getBooleanExtra("DATA_FROM_WS_V4", false)) {
                renderViewFromWS4(); //ini penjualan
            } else {
                renderViewFromWSNew(); //ini penjualan
            }
        }

        ArrayList<ShippingConfirmDetModel.DataHistory> dataHistories = convertFromOldDatas();
        OrderAdapter = new ListViewOrderStatus(OrderHistoryView.this, dataHistories);
        OrderStatus.setAdapter(OrderAdapter);
        OrderAdapter.notifyDataSetChanged();
    }

    /**
     * JANGAN DI ACAK ACAK, INI UNTUK YANG PEMBELIAN
     */
    private void renderViewFromWSNewBuyer() {
        int State = getIntent().getExtras().getInt("state", STATE_BUYER);
        try {
            JSONArray StatusList = new JSONArray(getIntent().getExtras().getString("status_list"));
            JSONObject Status;
            for (int i = 0; i < StatusList.length(); i++) {
                if (StatusList.length() == 0) break;
                Status = new JSONObject(StatusList.getString(i));
                ActorList.add(Status.getString("action_by"));
                DateList.add(Status.getString("status_date"));
                String state;
                if (State == STATE_BUYER) {
                    state = Status.getString("state_buyer").replace("<br>", "\n").replace("<br/>", "\n");
                } else {
                    state = Status.getString("state_seller").replace("<br>", "\n").replace("<br/>", "\n");
                }
                state = MethodChecker.fromHtml(state).toString();
                StateList.add(state);
                CommentList.add(MethodChecker.fromHtml(Status.getString("comments")).toString());
                if (StatusList.length() == 1) break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * JANGAN DI ACAK ACAK, INI UNTUK YANG PEMBELIAN
     */
    private void renderViewFromWS4Buyer() {
        int State = getIntent().getExtras().getInt("state", STATE_BUYER);
        try {
            JSONArray StatusList = new JSONArray(getIntent().getExtras().getString("status_list"));
            JSONObject Status;
            JSONObject preOrder = null;
            for (int i = 0; i < StatusList.length(); i++) {
                if (StatusList.length() == 0) break;
                Status = new JSONObject(StatusList.getString(i));
                ActorList.add(Status.getString("history_action_by"));
                DateList.add(Status.getString("history_status_date_full"));
                String state;
                if (State == STATE_BUYER) {
                    state = Status.getString("history_buyer_status").replace("<br>", "\n").replace("<br/>", "\n");
                    String strPreorder = getIntent().getExtras().getString("preorder_info");
                    if (strPreorder != null) preOrder = new JSONObject(strPreorder);
                } else {
                    state = Status.getString("history_seller_status").replace("<br>", "\n").replace("<br/>", "\n");
                }
                state = MethodChecker.fromHtml(state).toString();
                StateList.add(state);
                if (preOrder != null) {
                    if (preOrder.getInt("preorder_status") == 1 && Status.getInt("history_order_status") == 400) {
                        CommentList.add("Lama waktu proses produk : "
                                + String.valueOf(preOrder.get("preorder_process_time"))
                                + " " + String.valueOf(preOrder.get("preorder_process_time_type_string")));
                    } else {
                        CommentList.add(Status.getString("history_comments").equals("0") ? ""
                                : Status.getString("history_comments").replaceAll("<br/>\\p{Space}+", "\n"));
                    }
                } else {
                    CommentList.add(Status.getString("history_comments").equals("0") ? ""
                            : MethodChecker.fromHtml(Status.getString("history_comments")).toString());
                }
                if (StatusList.length() == 1) break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static final ArrayList<ShippingConfirmDetModel.DataHistory> convertFromOldDatas(
            ArrayList<String> ActorList, ArrayList<String> DateList, ArrayList<String> StateList, ArrayList<String> CommentList
    ){
        ArrayList<ShippingConfirmDetModel.DataHistory> dataHistories = new ArrayList<>();
        for(int i=0; i<ActorList.size();i++){
            ShippingConfirmDetModel.DataHistory dataHistory = new ShippingConfirmDetModel.DataHistory();
            dataHistory.ActorList = ActorList.get(i);
            dataHistory.DateList = DateList.get(i);
            dataHistory.StateList = StateList.get(i);
            dataHistory.CommentList = CommentList.get(i);

            dataHistories.add(dataHistory);
        }
        return dataHistories;
    }

    @NonNull
    private ArrayList<ShippingConfirmDetModel.DataHistory> convertFromOldDatas() {
        return convertFromOldDatas(ActorList, DateList, StateList, CommentList);
    }

    private void renderViewFromWSNew() {
        int State = getIntent().getExtras().getInt("state", STATE_BUYER);
            List<OrderHistory> StatusList = Parcels.unwrap(getIntent().getExtras().getParcelable("status_list"));
            OrderHistory Status;
            for (int i = 0; i < StatusList.size(); i++) {
                if (StatusList.size() == 0) break;
                Status = StatusList.get(i);
                ActorList.add(Status.getHistoryActionBy());
                DateList.add(Status.getHistoryStatusDate());
                String state;
                if (State == STATE_BUYER) {
                    state = Status.getHistoryBuyerStatus();
                } else {
                    state = Status.getHistorySellerStatus();
                }
                state = MethodChecker.fromHtml(state).toString();
                StateList.add(state);
                CommentList.add(MethodChecker.fromHtml(Status.getHistoryComments()).toString());
                if (StatusList.size() == 1) break;
            }
    }

    private void renderViewFromWS4() {
        int State = getIntent().getExtras().getInt("state", STATE_BUYER);
        List<OrderHistory> StatusList = Parcels.unwrap(getIntent().getExtras().getParcelable("status_list"));
        OrderHistory Status;
        JSONObject preOrder = null;
            for (int i = 0; i < StatusList.size(); i++) {
                if (StatusList.size() == 0) break;
                Status = StatusList.get(i);
                ActorList.add(Status.getHistoryActionBy());
                DateList.add(Status.getHistoryStatusDateFull());
                String state;
                if (State == STATE_BUYER) {
                    state = Status.getHistoryBuyerStatus();
                    try {
                        String strPreorder = getIntent().getExtras().getString("preorder_info");
                        if (strPreorder != null) preOrder = new JSONObject(strPreorder);
                    }catch(JSONException jse){
                        jse.printStackTrace();
                    }
                } else {
                    state = Status.getHistorySellerStatus();
                }
                state = MethodChecker.fromHtml(state).toString();
                StateList.add(state);
//                CommentList.add(Status.getHistoryComments().equals("0") ? ""
//                        : MethodChecker.fromHtml(Status.getHistoryComments()).toString());
                try {
                    if (preOrder != null) {
                        if (preOrder.getInt("preorder_status") == 1 && Integer.parseInt(Status.getHistoryOrderStatus()) == 400) {
                            CommentList.add("Lama waktu proses produk : "
                                    + String.valueOf(preOrder.get("preorder_process_time"))
                                    + " " + String.valueOf(preOrder.get("preorder_process_time_type_string")));
                        } else {
                            CommentList.add(Status.getHistoryComments().equals("0") ? ""
                                    : Status.getHistoryComments().replaceAll("<br/>\\p{Space}+", "\n"));
                        }
                    } else {
                        CommentList.add(Status.getHistoryComments().equals("0") ? ""
                                : MethodChecker.fromHtml(Status.getHistoryComments()).toString());
                    }
                }catch(JSONException jse){
                    jse.printStackTrace();
                }
                if (StatusList.size() == 1) break;
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
