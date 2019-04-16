package com.tokopedia.discovery.newdiscovery.search.fragment.product.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.price.DynamicBackgroundSeekBar;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.search.SearchNavigationListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.GuidedSearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GlobalNavViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobalNavWidget extends BaseCustomView {

    private static final int GLOBAL_NAV_SPAN_COUNT = 4;
    private int[] backgroundGradientColorList = {0xfffffae6, 0xffe5f5ff, 0xffebffef, 0xffffeaef};

    private LinearLayout globalNavContainerLayout;
    private TextView globalNavTitle;
    private TextView globalNavSeeAllButton;
    private RecyclerView globalNavRecyclerView;
    private GlobalNavWidgetAdapter adapter;
    private GlobalNavViewModel globalNavViewModel;
    private ClickListener clickListener;

    public GlobalNavWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public GlobalNavWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlobalNavWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.widget_global_nav, this);
        globalNavContainerLayout = (LinearLayout) view.findViewById(R.id.globalNavContainerLayout);
        globalNavTitle = (TextView) view.findViewById(R.id.globalNavTitle);
        globalNavSeeAllButton = (TextView) view.findViewById(R.id.globalNavSeeAllButton);
        globalNavRecyclerView = (RecyclerView) view.findViewById(R.id.globalNavRecyclerView);
        setupFont();
        setupBackground();
    }

    private void setupFont() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/NunitoSans-ExtraBold.ttf");
        globalNavTitle.setTypeface(typeface);
    }

    private void setupBackground() {
        int backgroundIndex = new Random().nextInt(backgroundGradientColorList.length);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.WHITE, backgroundGradientColorList[backgroundIndex]});
        gd.setCornerRadius(0f);

        globalNavContainerLayout.setBackground(gd);
    }

    private void setupRecyclerView() {
        adapter = new GlobalNavWidgetAdapter(clickListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), GLOBAL_NAV_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        globalNavRecyclerView.setLayoutManager(gridLayoutManager);
        globalNavRecyclerView.setAdapter(adapter);
    }

    private static class GlobalNavWidgetAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<GlobalNavViewModel.Item> itemList = new ArrayList<>();
        ClickListener itemClickListener;

        public GlobalNavWidgetAdapter(ClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemList(List<GlobalNavViewModel.Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_global_nav_item, parent, false);
            return new ViewHolder(view, itemClickListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout globalNavItemContainer;
        private ImageView globalNavItemImage;
        private TextView globalNavItemName;
        private TextView globalNavItemInfo;
        private ClickListener itemClickListener;
        private Context context;

        public ViewHolder(View itemView, ClickListener itemClickListener) {
            super(itemView);
            context = itemView.getContext();
            globalNavItemContainer = (LinearLayout)itemView.findViewById( R.id.globalNavItemContainer );
            globalNavItemImage = (ImageView)itemView.findViewById( R.id.globalNavItemImage );
            globalNavItemName = (TextView)itemView.findViewById( R.id.globalNavItemName );
            globalNavItemInfo = (TextView)itemView.findViewById( R.id.globalNavItemInfo );
            this.itemClickListener = itemClickListener;
        }

        public void bind(final GlobalNavViewModel.Item item) {
            globalNavItemName.setText(item.getName());
            globalNavItemInfo.setText(item.getInfo());
            ImageHandler.loadImageFitCenter(context, globalNavItemImage, item.getImageUrl());
            globalNavItemContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClickItem(item);
                    }
                }
            });
        }
    }

    private void renderData() {
        globalNavTitle.setText(globalNavViewModel.getTitle());
        adapter.setItemList(globalNavViewModel.getItemList());
        globalNavSeeAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onclickSeeAllButton(globalNavViewModel.getSeeAllApplink(),
                            globalNavViewModel.getSeeAllUrl());
                }
            }
        });
    }

    public void setData(GlobalNavViewModel globalNavViewModel, ClickListener clickListener) {
        this.globalNavViewModel = globalNavViewModel;
        this.clickListener = clickListener;
        setupRecyclerView();
        renderData();
    }

    public interface ClickListener {
        void onClickItem(GlobalNavViewModel.Item item);
        void onclickSeeAllButton(String applink, String url);
    }
}
