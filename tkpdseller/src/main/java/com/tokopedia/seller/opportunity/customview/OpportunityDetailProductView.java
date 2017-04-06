package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

import butterknife.BindView;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailProductView extends BaseView<OpportunityItemViewModel, OpportunityView> {

    View actionSeeProduct;
    ImageView productImage;
    TextView productName;
    TextView productQuantity;
    TextView productDescription;


    public OpportunityDetailProductView(Context context) {
        super(context);
    }

    public OpportunityDetailProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OpportunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_opportunity_product_list_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionSeeProduct = view.findViewById(R.id.action_see_product);
        productImage = (ImageView) view.findViewById(R.id.product_image);
        productName = (TextView) view.findViewById(R.id.product_name);
        productQuantity = (TextView) view.findViewById(R.id.product_quantity);
        productDescription = (TextView) view.findViewById(R.id.product_description);

    }

    @Override
    public void renderData(@NonNull final OpportunityItemViewModel data) {
        ImageHandler.LoadImage(productImage, data.getOrderProducts().get(0).getProductPicture());
        productName.setText(data.getOrderProducts().get(0).getProductName());
        String quantity = data.getOrderProducts().get(0).getProductQuantity() + " " + getContext().getString(R.string.item);
        productQuantity.setText(quantity);
        productDescription.setText(data.getOrderProducts().get(0).getProductDescription());

        actionSeeProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActionSeeDetailProduct(String.valueOf(data.getOrderProducts().get(0).getProductId()));
            }
        });
    }
}
