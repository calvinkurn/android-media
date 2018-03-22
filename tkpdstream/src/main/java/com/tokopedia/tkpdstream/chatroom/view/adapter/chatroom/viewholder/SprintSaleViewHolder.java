package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.SprintSaleAdapter;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.common.design.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleViewHolder extends BaseChatViewHolder<SprintSaleViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.sprint_sale_holder;
    private static final int SPAN_COUNT = 2;
    private final ChatroomContract.View.SprintSaleViewHolderListener listener;

    ImageView sprintSaleIcon;
    TextView sprintSaleTitle;
    RecyclerView listProducts;
    SprintSaleAdapter productAdapter;
    SpaceItemDecoration itemDecoration;
    View mainLayout;

    public SprintSaleViewHolder(View itemView, ChatroomContract.View.SprintSaleViewHolderListener listener) {
        super(itemView);
        this.listener = listener;
        mainLayout = itemView.findViewById(R.id.main_layout);
        sprintSaleIcon = itemView.findViewById(R.id.sprintsale_icon);
        sprintSaleTitle = itemView.findViewById(R.id.sprintsale_title);
        productAdapter = SprintSaleAdapter.createInstance();
        itemDecoration = new SpaceItemDecoration((int) itemView.getContext().getResources()
                .getDimension(R.dimen.space_mini), 2);
        listProducts = itemView.findViewById(R.id.list_product);
        listProducts.addItemDecoration(itemDecoration);
        listProducts.setLayoutManager(new GridLayoutManager(itemView.getContext(), SPAN_COUNT,
                LinearLayoutManager.VERTICAL, false));
        listProducts.setAdapter(productAdapter);
    }

    @Override
    public void bind(final SprintSaleViewModel element) {
        super.bind(element);

        checkSprintSaleActive(element.isActive());
        setProducts(element.getListProducts());

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFlashSaleClicked(element.getRedirectUrl());
            }
        });
    }

    private void setProducts(ArrayList<SprintSaleProductViewModel> listProducts) {
        productAdapter.setList(listProducts);
    }

    private void checkSprintSaleActive(boolean isActive) {
        if (isActive) {
            setSprintSaleActive();
        } else {
            setSprintSaleFinished();
        }
    }

    private void setSprintSaleActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(sprintSaleIcon, R.drawable
                    .ic_sprint_sale_active);
        } else {
            sprintSaleIcon.setImageResource(R.drawable.ic_sprint_sale_active);
        }
        sprintSaleTitle.setText(R.string.title_sprintsale_started);
        sprintSaleTitle.setTextColor(MethodChecker.getColor(sprintSaleTitle.getContext(), R.color.medium_green));
    }

    private void setSprintSaleFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(sprintSaleIcon, R.drawable
                    .ic_sprint_sale_inactive);
        } else {
            sprintSaleIcon.setImageResource(R.drawable.ic_sprint_sale_inactive);
        }

        sprintSaleTitle.setText(R.string.title_sprintsale_finished);
        sprintSaleTitle.setTextColor(MethodChecker.getColor(sprintSaleTitle.getContext(), R.color.black_54));
    }

}