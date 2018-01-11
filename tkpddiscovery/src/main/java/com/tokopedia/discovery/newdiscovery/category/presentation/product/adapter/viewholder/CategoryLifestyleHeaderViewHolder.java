package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.ChildCategoryLifestyleAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nakama on 1/4/18.
 */

public class CategoryLifestyleHeaderViewHolder extends AbstractViewHolder<CategoryHeaderModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_header_lifestyle;

    private final Context context;
    private final ImageView imageHeader;
    private final RelativeLayout imageHeaderContainer;
    private final View layoutChildCategory;
    private final RecyclerView listChildCategory;
    private final RevampCategoryAdapter.CategoryListener categoryListener;
    private final TextView titleHeader;
    private final TextView totalProduct;

    public CategoryLifestyleHeaderViewHolder(View itemView,
                                             RevampCategoryAdapter.CategoryListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.imageHeader = (ImageView) itemView.findViewById(R.id.image_header);
        this.titleHeader = (TextView) itemView.findViewById(R.id.title_header);
        this.totalProduct = (TextView) itemView.findViewById(R.id.total_product);
        this.imageHeaderContainer = (RelativeLayout) itemView.findViewById(R.id.image_header_container);
        this.layoutChildCategory = itemView.findViewById(R.id.view_child_category);
        this.listChildCategory = itemView.findViewById(R.id.recyclerview_child_category);
        this.categoryListener = listener;
    }

    @Override
    public void bind(CategoryHeaderModel model) {
        renderBannerCategory(model);
        renderChildCategory(model);
        renderTotalProduct(model);
    }

    private void trackImpression(CategoryHeaderModel model) {
        if (!model.isDoneTrackImpression()) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < model.getChildCategoryModelList().size(); i++) {
                ChildCategoryModel looper = model.getChildCategoryModelList().get(i);
                list.add(
                        DataLayer.mapOf(
                                "id", looper.getCategoryId(),
                                "name", String.format("category %s - subcategory banner", looper.getCategoryName()),
                                "position", i+1,
                                "creative", model.getHeaderModel().getCategoryName()
                                )
                );
            }
            model.setDoneTrackImpression(true);
            UnifyTracking.eventCategoryLifestyleImpression(list);
        }
    }

    private void renderBannerCategory(CategoryHeaderModel model) {
        renderSingleBanner(
                model.getHeaderImage(),
                model.getHeaderModel().getCategoryName()
        );
    }

    private boolean isHasBanner(CategoryHeaderModel model) {
        return model.getBannerModelList() != null && !model.getBannerModelList().isEmpty();
    }

    private void renderChildCategory(CategoryHeaderModel model) {
        if (isRootCategory(model) || !isHasChild(model)) {
            layoutChildCategory.setVisibility(View.GONE);
        } else {
            trackImpression(model);
            layoutChildCategory.setVisibility(View.VISIBLE);
            layoutChildCategory.setBackgroundColor(
                    TextUtils.isEmpty(model.getHeaderImageHexColor()) ?
                            ContextCompat.getColor(context, R.color.white) :
                            Color.parseColor(model.getHeaderImageHexColor())
            );

            ChildCategoryLifestyleAdapter adapter = new ChildCategoryLifestyleAdapter(categoryListener, model.getHeaderModel().getCategoryName());
            adapter.setListCategory(model.getChildCategoryModelList());
            adapter.notifyDataSetChanged();
            listChildCategory.setHasFixedSize(true);
            listChildCategory.setLayoutManager(generateLayoutManager(model.getChildCategoryModelList().size()));
            listChildCategory.setAdapter(adapter);
        }
    }

    private RecyclerView.LayoutManager generateLayoutManager(int size) {
        if (size == 3) {
            return new GridLayoutManager(context, 3);
        } else {
            return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        }
    }

    private boolean isRootCategory(CategoryHeaderModel model) {
        return TextUtils.equals(model.getDepartementId(), model.getRootCategoryId());
    }

    private boolean isHasChild(CategoryHeaderModel model) {
        return model.getChildCategoryModelList() != null &&
                !model.getChildCategoryModelList().isEmpty();
    }

    private boolean isOnRootCategory(CategoryHeaderModel model) {
        return model.getDepartementId().equals(model.getRootCategoryId());
    }

    protected void renderTotalProduct(CategoryHeaderModel model) {
        if (model.getTotalData() > 0) {
            totalProduct.setText(
                    String.format(
                            "%s Produk",
                            NumberFormat.getNumberInstance(Locale.US)
                                    .format(model.getTotalData())
                                    .replace(',', '.')
                    )
            );
            totalProduct.setVisibility(View.VISIBLE);
        } else {
            totalProduct.setVisibility(View.GONE);
        }
    }

    protected void renderSingleBanner(String headerImage, String categoryName) {
        ImageHandler.LoadImage(imageHeader, headerImage);
        titleHeader.setText(categoryName);
        titleHeader.setShadowLayer(24, 0, 0, R.color.checkbox_text);

        imageHeaderContainer.setVisibility(View.VISIBLE);
    }
}
