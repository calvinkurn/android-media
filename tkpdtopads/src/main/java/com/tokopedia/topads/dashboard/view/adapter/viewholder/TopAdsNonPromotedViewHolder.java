package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.listener.AdapterSelectionListener;
import com.tokopedia.topads.dashboard.view.listener.BindViewHolder;
import com.tokopedia.topads.dashboard.view.model.NonPromotedTopAdsAddProductModel;

/**
 * @author normansyahputa on 3/6/17.
 */

public class TopAdsNonPromotedViewHolder extends BaseTopAdsAddProductListViewHolder
        implements BindViewHolder<NonPromotedTopAdsAddProductModel> {

    private final CheckBox checkBoxNonPromoted;
    private final int topAdsSelectionColor;
    private final int transparantColor;
    private final AdapterSelectionListener adapterSelectionListener;
    private final String promoted;
    private NonPromotedTopAdsAddProductModel model;
    private int adapterPosition;

    public TopAdsNonPromotedViewHolder(final View itemView
            , AdapterSelectionListener adapterSelectionListener) {
        super(itemView);

        checkBoxNonPromoted = (CheckBox) itemView.findViewById(R.id.checkbox_top_ads_add_product_list_non_promoted);
        checkBoxNonPromoted.setClickable(false);
        Resources resources = itemView.getContext().getResources();
        promoted = resources.getString(R.string.promoted);

        topAdsSelectionColor = ContextCompat.getColor(itemView.getContext(), R.color.selection_color_top_ads);
        transparantColor = ContextCompat.getColor(itemView.getContext(), android.R.color.white);

        this.adapterSelectionListener = adapterSelectionListener;

        itemView.setOnClickListener(onClickListener());
    }

    @Override
    public void bind(NonPromotedTopAdsAddProductModel model) {
        this.model = model;
        adapterPosition = getAdapterPosition();

        boolean b = !adapterSelectionListener.isSelected(model.productDomain);// not selected
        if (b) {
            checkBoxNonPromoted.setChecked(false);
            itemView.setBackgroundColor(transparantColor);
        } else {
            checkBoxNonPromoted.setChecked(true);
            itemView.setBackgroundColor(topAdsSelectionColor);
        }

        setDescriptionText(model.description);

        if (model.getSnippet() == null && model.getProductDomain().isPromoted()) {
            setSnippet(promoted);
        } else if (model.getSnippet() != null) {
            setSnippet(model.snippet);
        } else {
            setSnippet(null);
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
            }
        };
    }

    @Override
    protected void initViews(View itemView) {
        description = (TextView) itemView.findViewById(R.id.top_ads_add_product_list_description_non_promoted);
        snippet = (TextView) itemView.findViewById(R.id.top_ads_add_product_list_snippet_non_promoted);
        emptySpace = itemView.findViewById(R.id.empty_space_non_promoted);
    }
}
