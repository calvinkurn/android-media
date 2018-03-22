package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

/**
 * Created by henrypriyono on 10/31/17.
 */

public class EmptySearchViewHolder extends AbstractViewHolder<EmptySearchModel> implements TopAdsItemClickListener {

    private final int MAX_TOPADS = 4;
    private TopAdsView topAdsView;
    private Config config;
    private TopAdsParams params = new TopAdsParams();
    private Context context;
    private ImageView noResultImage;
    private TextView emptyTitleTextView;
    private TextView emptyContentTextView;
    private Button emptyButtonItemButton;
    private final ItemClickListener clickListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_product;

    public EmptySearchViewHolder(View view, ItemClickListener clickListener, Config topAdsConfig) {
        super(view);
        noResultImage = (ImageView) view.findViewById(R.id.no_result_image);
        emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) view.findViewById(R.id.button_add_promo);
        this.clickListener = clickListener;
        context = itemView.getContext();
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .withMerlinCategory()
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        params.getParam().put(TopAdsParams.KEY_QUERY, topAdsConfig.getTopAdsParams()
                .getParam().get(TopAdsParams.KEY_QUERY));
        params.getParam().put(TopAdsParams.KEY_SRC, topAdsConfig.getTopAdsParams()
                .getParam().get(TopAdsParams.KEY_SRC));
        params.getParam().put(TopAdsParams.KEY_SEARCH_NF, "1");
        config.setTopAdsParams(params);
        topAdsView.setConfig(config);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(MAX_TOPADS);
        topAdsView.setAdsItemClickListener(this);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        //Not implemented just leave empty
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        //Not implemented just leave empty
    }

    @Override
    public void bind(EmptySearchModel model) {

        noResultImage.setImageResource(model.getImageRes());
        emptyTitleTextView.setText(model.getTitle());

        if (!TextUtils.isEmpty(model.getContent())) {
            emptyContentTextView.setText(model.getContent());
            emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            emptyContentTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(model.getButtonText())) {
            emptyButtonItemButton.setVisibility(View.GONE);
        } else {
            emptyButtonItemButton.setText(model.getButtonText());
            emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onEmptyButtonClicked();
                    }
                }
            });
            emptyButtonItemButton.setVisibility(View.VISIBLE);
        }
        topAdsView.loadTopAds();
    }
}
