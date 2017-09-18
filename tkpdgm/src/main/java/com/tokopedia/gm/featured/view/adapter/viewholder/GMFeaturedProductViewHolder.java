package com.tokopedia.gm.featured.view.adapter.viewholder;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.gm.R;
import com.tokopedia.gm.featured.constant.GMFeaturedProductTypeView;
import com.tokopedia.gm.featured.helper.ItemTouchHelperViewHolder;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class GMFeaturedProductViewHolder extends BaseMultipleCheckViewHolder<GMFeaturedProductModel> implements
        ItemTouchHelperViewHolder {
    private final AppCompatImageView icGMFeaturedProduct;
    private final TextView textProductName;
    private final TextView textPrice;
    private final AppCompatCheckBox checkBoxGM;
    private final FrameLayout dragAndDropView;
    AppCompatImageView dragAndDropGMFeaturedProduct;
    private OnStartDragListener onStartDragListener;
    private PostDataListener useCaseListener;

    public GMFeaturedProductViewHolder(View itemView, OnStartDragListener dragListener, PostDataListener useCaseListener) {
        super(itemView);
        dragAndDropView = (FrameLayout) itemView.findViewById(R.id.ic_drag_and_drop_gm_featured_product_container);
        dragAndDropGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_drag_and_drop_gm_featured_product);
        icGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_gm_featured_product);
        textProductName = (TextView) itemView.findViewById(R.id.text_product_name_gm_featured_product);
        textPrice = (TextView) itemView.findViewById(R.id.text_price_gm_featured_product);
        checkBoxGM = (AppCompatCheckBox) itemView.findViewById(R.id.check_box);
        this.onStartDragListener = dragListener;
        this.useCaseListener = useCaseListener;
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }

    @Override
    public void bindObject(GMFeaturedProductModel gmFeaturedProductModel) {
        bindData(gmFeaturedProductModel);
    }

    public void bindData(GMFeaturedProductModel GMFeaturedProductModel) {
        textProductName.setText(GMFeaturedProductModel.getProductName());
        textPrice.setText(GMFeaturedProductModel.getProductPrice());
        ImageHandler.loadImageRounded2(
                icGMFeaturedProduct.getContext(),
                icGMFeaturedProduct,
                GMFeaturedProductModel.getImageUrl()
        );

        dragAndDropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(GMFeaturedProductViewHolder.this);
                }
                return false;
            }
        });
    }

    @Override
    public void bindObject(final GMFeaturedProductModel GMFeaturedProductModel, boolean checked) {
        bindObject(GMFeaturedProductModel);
        setChecked(checked);
        switch (useCaseListener.getFeaturedProductType()){
            case GMFeaturedProductTypeView.DELETE_DISPLAY:
                checkBoxGM.setVisibility(View.VISIBLE);
                checkBoxGM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkedCallback != null){
                            checkedCallback.onItemChecked(GMFeaturedProductModel, checkBoxGM.isChecked());
                        }
                    }
                });
                dragAndDropGMFeaturedProduct.setVisibility(View.GONE);
                break;
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
                checkBoxGM.setVisibility(View.GONE);
                checkBoxGM.setOnClickListener(null);
                dragAndDropGMFeaturedProduct.setVisibility(View.VISIBLE);
                break;
            case GMFeaturedProductTypeView.DEFAULT_DISPLAY:
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

        int getFeaturedProductType();
    }
}
