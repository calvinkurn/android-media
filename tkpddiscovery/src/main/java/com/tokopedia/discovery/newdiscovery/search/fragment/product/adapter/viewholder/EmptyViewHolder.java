package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener;

/**
 * @author by errysuprayogi on 10/30/17.
 */

public class EmptyViewHolder extends AbstractViewHolder<EmptyModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_item_product_empty;

    private TextView txtEmptyContent;
    private Button emptyButtonItemButton;
    private final EmptyStateListener clickListener;

    public EmptyViewHolder(View itemView, EmptyStateListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        txtEmptyContent = (TextView) itemView.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) itemView.findViewById(R.id.button_add_promo);
        emptyButtonItemButton.setOnClickListener(this);
    }

    @Override
    public void bind(EmptyModel element) {
        txtEmptyContent.setText(element.getMessage());
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onEmptyButtonClicked();
        }
    }
}
