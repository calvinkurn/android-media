package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

import butterknife.ButterKnife;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategorySectionViewHolder extends AbstractViewHolder<CategorySectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_section;
    private RecyclerView recyclerView;
    private SectionItemAdapter adapter;
    private Context context;
    private static final int spanCount = 5;
    private HomeCategoryListener listener;

    public CategorySectionViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        adapter = new SectionItemAdapter();
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                context.getResources().getDimensionPixelSize(R.dimen.home_card_category_item_margin), true));
    }

    @Override
    public void bind(CategorySectionViewModel element) {
        adapter.setSectionViewModel(element);
    }

    public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemViewHolder> {

        private CategorySectionViewModel sectionViewModel;

        public SectionItemAdapter() {
        }

        public void setSectionViewModel(CategorySectionViewModel sectionViewModel) {
            this.sectionViewModel = sectionViewModel;
            notifyDataSetChanged();
        }

        @Override
        public SectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SectionItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_section_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SectionItemViewHolder holder, final int position) {
            holder.title.setText(sectionViewModel.getSectionList().get(position).getTitle());
            ImageHandler.loadImageThumbs(context, holder.icon, sectionViewModel.getSectionList().get(position).getIcon());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickUseCase(sectionViewModel.getSectionList().get(position));
                    listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.getSectionList().get(position)));
                }
            });
        }

        private void eventClickUseCase(LayoutSections layoutSections) {
            if (layoutSections.getTypeCase() == LayoutSections.ICON_USE_CASE) {
                HomePageTracking.eventClickHomeUseCase(layoutSections.getTitle());
                HomeTrackingUtils.homeUsedCaseClick(layoutSections.getIcon(),getLayoutPosition(),layoutSections.getApplink());
            } else {
                HomePageTracking.eventClickDynamicIcons(layoutSections.getTitle());

            }
        }

        @Override
        public int getItemCount() {
            return sectionViewModel.getSectionList().size();
        }
    }

    public class SectionItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView icon;
        private TextView title;
        private LinearLayout container;

        public SectionItemViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            container = itemView.findViewById(R.id.container);
        }
    }
}
