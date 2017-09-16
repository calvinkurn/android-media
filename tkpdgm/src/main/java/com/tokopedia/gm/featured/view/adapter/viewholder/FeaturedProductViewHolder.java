package com.tokopedia.gm.featured.view.adapter.viewholder;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.gm.featured.constant.FeaturedProductType;
import com.tokopedia.gm.featured.helper.ItemTouchHelperViewHolder;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.view.adapter.model.FeaturedProductModel;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class FeaturedProductViewHolder extends BaseMultipleCheckViewHolder<FeaturedProductModel> implements
        ItemTouchHelperViewHolder {
    private final AppCompatImageView icGMFeaturedProduct;
    private final TextView textProductName;
    private final TextView textPrice;
    private final AppCompatCheckBox checkBoxGM;
    AppCompatImageView dragAndDropGMFeaturedProduct;
    private OnStartDragListener mDragStartListener;
    private PostDataListener useCaseListener;

    public FeaturedProductViewHolder(View itemView, OnStartDragListener mDragStartListener, PostDataListener useCaseListener) {
        super(itemView);

        dragAndDropGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_drag_and_drop_gm_featured_product);
        icGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_gm_featured_product);
        textProductName = (TextView) itemView.findViewById(R.id.text_product_name_gm_featured_product);
        textPrice = (TextView) itemView.findViewById(R.id.text_price_gm_featured_product);
        checkBoxGM = (AppCompatCheckBox) itemView.findViewById(R.id.check_box);
        this.mDragStartListener = mDragStartListener;

        this.useCaseListener = useCaseListener;
    }

    public void bindData(FeaturedProductModel featuredProductModel) {
        textProductName.setText(featuredProductModel.getProductName());

        textPrice.setText(featuredProductModel.getProductPrice());

        ImageHandler.loadImageRounded2(
                icGMFeaturedProduct.getContext(),
                icGMFeaturedProduct,
                featuredProductModel.getImageUrl()
        );

        dragAndDropGMFeaturedProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(FeaturedProductViewHolder.this);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {
        if (useCaseListener != null) {
            useCaseListener.postData();
        }
    }

    @Override
    public void bindObject(FeaturedProductModel featuredProductModel) {
        bindData(featuredProductModel);
    }

    @Override
    public void bindObject(final FeaturedProductModel featuredProductModel, boolean checked) {
        bindData(featuredProductModel);
        setChecked(checked);

        switch (useCaseListener.getFeaturedProductType()){
            case FeaturedProductType.DELETE_DISPLAY:
                checkBoxGM.setVisibility(View.VISIBLE);
                checkBoxGM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkedCallback != null){
                            checkedCallback.onItemChecked(featuredProductModel, checkBoxGM.isChecked());
                        }
                    }
                });
                dragAndDropGMFeaturedProduct.setVisibility(View.GONE);
                break;
            case FeaturedProductType.ARRANGE_DISPLAY:
                checkBoxGM.setVisibility(View.GONE);
                checkBoxGM.setOnClickListener(null);
                dragAndDropGMFeaturedProduct.setVisibility(View.VISIBLE);
                break;
            case FeaturedProductType.DEFAULT_DISPLAY:
            default:
                checkBoxGM.setVisibility(View.GONE);
                dragAndDropGMFeaturedProduct.setVisibility(View.GONE);
                checkBoxGM.setOnClickListener(null);
                break;
        }
    }

    @Override
    public boolean isChecked() {
        return checkBoxGM.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBoxGM.setChecked(checked);
    }

    public interface PostDataListener {
        void postData();

        int getFeaturedProductType();
    }
}
