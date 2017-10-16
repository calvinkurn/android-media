package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.listener.AdapterSelectionListener;
import com.tokopedia.topads.dashboard.view.listener.BindViewHolder;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddProductModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public class TopAdsAddProductListViewHolder extends RecyclerView.ViewHolder
        implements BindViewHolder<TopAdsAddProductModel> {

    private final View emptySpace;
    private final TextView topAdsAddproductListDescription;
    private final ImageView topAdsAddProductListImageView;
    private final TextView topAdsAddProductListSnippet;
    private final int topAdsSelectionColor;
    private final int transparantColor;
    int adapterPosition;
    private AdapterSelectionListener<TopAdsProductViewModel> adapterSelectionListener;
    private ImageHandler imageHandler;
    private TopAdsAddProductModel model;

    public TopAdsAddProductListViewHolder(View itemView, ImageHandler imageHandler
            , AdapterSelectionListener adapterSelectionListener) {
        super(itemView);

        topAdsAddProductListImageView = (ImageView)
                itemView.findViewById(R.id.top_ads_add_product_list_imageview);
        topAdsAddproductListDescription = (TextView)
                itemView.findViewById(R.id.top_ads_add_product_list_description);
        topAdsAddProductListSnippet = (TextView)
                itemView.findViewById(R.id.top_ads_add_product_list_snippet);
        emptySpace =
                itemView.findViewById(R.id.empty_space);

        topAdsSelectionColor = ContextCompat.getColor(itemView.getContext(), R.color.selection_color_top_ads);
        transparantColor = ContextCompat.getColor(itemView.getContext(), android.R.color.transparent);

        this.imageHandler = imageHandler;
        this.adapterSelectionListener = adapterSelectionListener;

        itemView.setOnClickListener(onClickListener());
    }

    @Override
    public void bind(TopAdsAddProductModel model) {
        this.model = model;
        adapterPosition = getAdapterPosition();

        boolean b = !adapterSelectionListener.isSelected(model.productDomain);// not selected
        if (b) {
            imageHandler.loadImage(topAdsAddProductListImageView, model.imageUrl, true);
            itemView.setBackgroundColor(transparantColor);
        } else {
            imageHandler.loadImage(topAdsAddProductListImageView, R.drawable.ic_top_ads_selected);
            itemView.setBackgroundColor(topAdsSelectionColor);
        }

        topAdsAddproductListDescription.setText(model.description);
        if (model.snippet != null && !model.snippet.isEmpty()) {
            topAdsAddProductListSnippet.setVisibility(View.VISIBLE);
            topAdsAddProductListSnippet.setText(model.snippet);
            emptySpace.setVisibility(View.GONE);
        } else {
            topAdsAddProductListSnippet.setVisibility(View.INVISIBLE);
            emptySpace.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reverse selection
                boolean reverseSelection = !adapterSelectionListener.isSelected(model.productDomain);

                if (adapterSelectionListener != null) {
                    if (reverseSelection) {
                        adapterSelectionListener.onChecked(adapterPosition, model.productDomain);
                    } else {
                        adapterSelectionListener.onUnChecked(adapterPosition, model.productDomain);
                    }
                }

                // reverse selection
                model.setSelected(reverseSelection);
            }
        };
    }


}
