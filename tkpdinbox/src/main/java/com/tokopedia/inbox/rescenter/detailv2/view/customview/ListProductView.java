package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ComplaintProductAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductData;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ListProductView extends BaseView<ProductData, DetailResCenterFragmentView> {

    private RecyclerView listProduct;
    private View actionMore;
    private ComplaintProductAdapter adapter;

    public ListProductView(Context context) {
        super(context);
    }

    public ListProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_detail_product_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        listProduct = (RecyclerView) view.findViewById(R.id.list_product);
        actionMore = view.findViewById(R.id.action_see_more_product);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductData data) {
        initRecyclerView(data);
        actionMore.setOnClickListener(new ListProductViewOnClickListener());
        actionMore.setVisibility(
                data.getProductList() != null && data.getProductList().size() > 3 ?
                        VISIBLE : GONE
        );
        setVisibility(VISIBLE);

    }

    private void initRecyclerView(ProductData data) {
        listProduct.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listProduct.setLayoutManager(mLayoutManager);

        adapter = ComplaintProductAdapter.createLimitInstance(listener, data.getProductList());
        listProduct.setAdapter(adapter);
    }

    private class ListProductViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.action_see_more_product) {
                listener.setOnActionMoreProductClick();
            }
        }
    }
}