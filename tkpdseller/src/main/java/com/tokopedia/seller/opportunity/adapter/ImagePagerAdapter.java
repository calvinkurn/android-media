package com.tokopedia.seller.opportunity.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.productdetail.ProductImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi-putra on 10/04/18.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ProductImage> productImages = new ArrayList<>();

    private OnActionListener actionListener;

    public ImagePagerAdapter(Context context, ArrayList<ProductImage> productImages) {
        this.context = context;
        this.productImages = productImages;
    }

    @Override
    public int getCount() {
        return productImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        String urlImage = productImages.get(position).getImageSrc();
        if (!TextUtils.isEmpty(urlImage)) {
            ImageHandler.loadImageFit2(context, imageView, urlImage);
        }
        imageView.setOnClickListener(new OnClickImage(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addAll(List<ProductImage> productImages) {
        this.productImages.clear();
        this.productImages.addAll(productImages);

        notifyDataSetChanged();
    }

    public void add(ProductImage productImage) {
        this.productImages.add(productImage);
        notifyDataSetChanged();
    }

    public void setActionListener(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ArrayList<String> getImageURIPaths() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            arrayList.add(productImage.getImageSrc());
        }
        return arrayList;
    }

    public ArrayList<String> getImageDescs() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            arrayList.add(productImage.getImageDescription());
        }
        return arrayList;
    }

    private class OnClickImage implements View.OnClickListener {
        private final int position;

        OnClickImage(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (actionListener != null)
                actionListener.onItemImageClicked(productImages.get(position), position);
        }
    }

    public interface OnActionListener {

        void onItemImageClicked(ProductImage productImage, int position);

    }
}
