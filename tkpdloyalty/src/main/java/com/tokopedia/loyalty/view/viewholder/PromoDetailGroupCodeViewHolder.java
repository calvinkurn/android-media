package com.tokopedia.loyalty.view.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailSingleCodeAdapter;
import com.tokopedia.loyalty.view.data.PromoCodeViewModel;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailGroupCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_GROUP_CODE = R.layout.item_view_promo_group_code_layout;

    private TextView tvGroupCodeTitle;
    private TextView tvGroupCodeDescription;
    private RecyclerView rvSingleCodeView;

    private Context context;

    public PromoDetailGroupCodeViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;

        this.tvGroupCodeTitle = itemView.findViewById(R.id.tv_group_code_title);
        this.tvGroupCodeDescription = itemView.findViewById(R.id.tv_group_code_description);
        this.rvSingleCodeView = itemView.findViewById(R.id.rv_single_code_view);
    }

    public void bind(PromoCodeViewModel viewModel) {

        this.tvGroupCodeTitle.setVisibility(TextUtils.isEmpty(viewModel.getGroupCodeTitle()) ?
                View.GONE : View.VISIBLE);
        this.tvGroupCodeTitle.setText(viewModel.getGroupCodeTitle());

        this.tvGroupCodeDescription.setVisibility(TextUtils.isEmpty(viewModel.getGroupCodeTitle()) ?
                View.GONE : View.VISIBLE);
        this.tvGroupCodeDescription.setText(viewModel.getGroupCodeDescription());

        this.rvSingleCodeView.setVisibility(View.VISIBLE);
        this.rvSingleCodeView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rvSingleCodeView.setLayoutManager(layoutManager);

        PromoDetailSingleCodeAdapter promoDetailSingleCodeAdapter =
                new PromoDetailSingleCodeAdapter(viewModel.getGroupCode());
        this.rvSingleCodeView.setAdapter(promoDetailSingleCodeAdapter);
    }
}
