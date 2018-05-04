package com.tokopedia.seller.product.edit.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.activity.ProductAddWholesaleActivity;
import com.tokopedia.seller.product.edit.view.adapter.WholesaleAddAdapter;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleFragment extends BaseDaggerFragment implements WholesaleAddAdapter.Listener {

    private static final int MAX_WHOLESALE = 5;
    private WholesaleAddAdapter wholesaleAdapter;
    private TextView textViewAddWholesale, textMainPrice;
    private ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList;
    private double productPrice;

    public static ProductAddWholesaleFragment newInstance() {
        Bundle args = new Bundle();
        ProductAddWholesaleFragment fragment = new ProductAddWholesaleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Intent activityIntent = getActivity().getIntent();

        productWholesaleViewModelList = activityIntent.getParcelableArrayListExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_WHOLESALE_LIST);
        productPrice = activityIntent.getDoubleExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_MAIN_PRICE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_add_wholesale, container, false);

        RecyclerView recyclerViewWholesale = root.findViewById(R.id.recycler_view_wholesale);
        recyclerViewWholesale.setLayoutManager(new LinearLayoutManager(recyclerViewWholesale.getContext(), LinearLayoutManager.VERTICAL, false));
        wholesaleAdapter = new WholesaleAddAdapter();
        wholesaleAdapter.setListener(this);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);

        textMainPrice = root.findViewById(R.id.text_main_price);

        textViewAddWholesale = root.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WholesaleModel lastItem = wholesaleAdapter.getLastItem();
                WholesaleModel newWholesale = new WholesaleModel(lastItem.getQtyMin() + 1,
                        lastItem.getQtyPrice() - 1);
                wholesaleAdapter.addItem(newWholesale);
                updateWholesaleButton();
            }
        });

        Button buttonSave = root.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        renderData(productWholesaleViewModelList, productPrice);

        return root;
    }

    public void renderData(ArrayList<ProductWholesaleViewModel> productWholesaleViewModelArrayList, double productPrice){
        setWholesalePrice(productWholesaleViewModelArrayList);
        textMainPrice.setText("Rp " + productPrice);
    }

    @Override
    public int getCurrencyType() {
        return CurrencyTypeDef.TYPE_IDR;
    }

    public void setWholesalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        wholesaleAdapter.addAllWholeSalePrice(wholesalePrice);
        updateWholesaleButton();
    }

    private void updateWholesaleButton() {
        textViewAddWholesale.setVisibility(wholesaleAdapter.getItemCount() < MAX_WHOLESALE ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notifySizeChanged(int currentSize) {
        updateWholesaleButton();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_wholesale, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete_wholesale) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.dialog_delete_wholesale_title))
                    .setMessage(getString(R.string.dialog_delete_wholesale_message))
                    .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            wholesaleAdapter.removeAll();
                            wholesaleAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
