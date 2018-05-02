package com.tokopedia.seller.product.edit.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.activity.ProductAddWholesaleActivity;
import com.tokopedia.seller.product.edit.view.adapter.WholesaleAdapter;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleFragment extends BaseDaggerFragment implements WholesaleAdapter.Listener {

    private static final int MAX_WHOLESALE = 5;
    private WholesaleAdapter wholesaleAdapter;
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
        wholesaleAdapter = new WholesaleAdapter();
        wholesaleAdapter.setListener(this);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);

        textMainPrice = root.findViewById(R.id.text_main_price);

        textViewAddWholesale = root.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
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
    public void notifySizeChanged(int currentSize) {

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
}
