package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.create.customadapter.ProductAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.PassProductTrouble;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 8/3/16.
 */
public class ChooseProductSectionCreateResCenterView extends BaseView<CreateResCenterFormData, ChooseTroubleListener> {

    private RecyclerView productRecyclerView;

    private ProductAdapter adapter;

    public ChooseProductSectionCreateResCenterView(Context context) {
        super(context);
    }

    public ChooseProductSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ChooseTroubleListener chooseTroubleListener) {
        this.listener = chooseTroubleListener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_rescenter_choose_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull CreateResCenterFormData data) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProductAdapter(data);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(adapter);
    }

    public List<PassProductTrouble> getProductTrouble() {
        List<PassProductTrouble> passProductTroubleList = new ArrayList<>();

        if (getVisibility() == GONE) {
            return passProductTroubleList;
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            PassProductTrouble passProductTrouble = new PassProductTrouble();
            ProductAdapter.SimpleViewHolder holder = (ProductAdapter.SimpleViewHolder) productRecyclerView.getChildViewHolder(productRecyclerView.getChildAt(i));
            if (holder.checkView.isChecked()) {
                passProductTrouble.setProductData(adapter.getAllProduct().get(i));
                passProductTroubleList.add(passProductTrouble);
            }
        }
        return passProductTroubleList;
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        productRecyclerView = view.findViewById(R.id.product_recyclerview);
    }
}
