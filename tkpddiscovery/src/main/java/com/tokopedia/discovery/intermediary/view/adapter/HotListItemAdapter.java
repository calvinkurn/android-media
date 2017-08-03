package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.home.presenter.HotList.CATALOG_KEY;
import static com.tokopedia.core.home.presenter.HotList.HOT_KEY;
import static com.tokopedia.core.home.presenter.HotList.TOPPICKS_KEY;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BANNER_HOTLIST = -1;
    public static final int SHORT_HEIGHT_HOTLIST = -2;

    private List<HotListModel> hotListModelList = new ArrayList<>();
    private final int homeMenuWidth;
    private final Context context;
    private final String categoryId;

    public HotListItemAdapter(List<HotListModel> hotListModelList, int homeMenuWidth,
                              Context context, String categoryId) {
        this.hotListModelList = hotListModelList;
        this.homeMenuWidth = homeMenuWidth;
        this.context = context;
        this.categoryId = categoryId;
    }

    @Override
    public int getItemViewType(int position) {
        if (hotListModelList.size()>=3 && position==0) {
            return BANNER_HOTLIST;
        } else if (hotListModelList.size()>=3 && position<3) {
            return SHORT_HEIGHT_HOTLIST;
        } else {
            return position;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SHORT_HEIGHT_HOTLIST:
                @SuppressLint("InflateParams") View v = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_hotlist_square, null
                );
                v.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(v);
            case BANNER_HOTLIST:
                @SuppressLint("InflateParams") View vBanner = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_hotlist_banner, null
                );
                vBanner.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(vBanner);
            default:
                @SuppressLint("InflateParams") View vDefault = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.item_hotlist, null
                );
                vDefault.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(vDefault);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        HotListItemRowHolder hotListItemRowHolder = (HotListItemRowHolder) holder;

        final HotListModel hotListModel = hotListModelList.get(i);
        switch (getItemViewType(i)) {
            case SHORT_HEIGHT_HOTLIST:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage,hotListModel.getImageUrlSquare());
                break;
            case BANNER_HOTLIST:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage,hotListModel.getImageUrlBanner());
                break;
            default:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage,hotListModel.getImageUrl());
        }
        hotListItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventHotlistIntermediary(categoryId,hotListModel.getTitle());
                String url = hotListModel.getUrl();
                URLParser urlParser = new URLParser(url);
                switch (urlParser.getType()) {
                    case HOT_KEY:
                        Bundle bundle = new Bundle();
                        bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS, urlParser.getHotAlias());
                        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
                        moveToHotlistActivity(bundle,context);
                        break;
                    case CATALOG_KEY:
                        context.startActivity(
                                DetailProductRouter.getCatalogDetailActivity(context, urlParser.getHotAlias()));
                        break;
                    case TOPPICKS_KEY:
                        if (!TextUtils.isEmpty(url)) {
                            context.startActivity(TopPicksWebView.newInstance(context, url));
                        }
                        break;
                    default:
                        bundle = new Bundle();
                        bundle.putString(BrowseProductRouter.DEPARTMENT_ID,
                                urlParser.getDepIDfromURI(context));

                        bundle.putInt(BrowseProductRouter.FRAGMENT_ID,
                                BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);

                        bundle.putSerializable(BrowseProductRouter.EXTRA_FILTER, urlParser.getParamKeyValueMap());
                        bundle.putString(BrowseProductRouter.EXTRA_TITLE, hotListModel.getTitle());
                        bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
                        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
                        moveToHotlistActivity(bundle,context);
                        break;
                }
            }
        });
    }

    private void moveToHotlistActivity(Bundle bundle, Context context) {
        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return hotListModelList.size();
    }


    class HotListItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_image_hoth)
        ImageView itemImage;

        HotListItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }


}