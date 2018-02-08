package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.listener.BindViewHolder;
import com.tokopedia.topads.dashboard.view.model.PromotedTopAdsAddProductModel;

/**
 * @author normansyahputa on 3/6/17.
 */

public class TopAdsPromotedViewHolder extends BaseTopAdsAddProductListViewHolder
        implements BindViewHolder<PromotedTopAdsAddProductModel> {
    private final String topAdsPromotedMessage;
    private final String promoted;
    private CheckBox disabledCheckBox;
    private TextView productDescription;
    private TextView snippetPromoted;
    private View emptySpacePromoted;

    public TopAdsPromotedViewHolder(final View itemView) {
        super(itemView);
        Resources resources = itemView.getContext().getResources();
        topAdsPromotedMessage = resources.getString(R.string.top_ads_promoted_message);
        promoted = resources.getString(R.string.promoted);
        disabledCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_top_ads_add_product_list_promoted);
        disabledCheckBox.setChecked(false);
        disabledCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        itemView.getContext(),
                        topAdsPromotedMessage,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        disabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                disabledCheckBox.setChecked(false);
            }
        });

    }

    @Override
    protected void initViews(View itemView) {
        description = (TextView) itemView.findViewById(R.id.top_ads_add_product_list_description_promoted);
        snippet = (TextView) itemView.findViewById(R.id.top_ads_add_product_list_snippet_promoted);
        emptySpace = itemView.findViewById(R.id.empty_space_promoted);
    }

    @Override
    public void bind(PromotedTopAdsAddProductModel model) {
        setDescriptionText(model.description);

        if (model.getSnippet() == null && model.getProductDomain().isPromoted()) {
            setSnippet(promoted);
        } else if (model.getSnippet() != null && !TextUtils.isEmpty(model.getSnippet())) {
            setSnippet(model.snippet);
        } else {
            setSnippet(promoted);
        }
    }
}
