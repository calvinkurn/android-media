package com.tokopedia.seller.product.view.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.view.adapter.addurlvideo.AddUrlVideoAdapter;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

/**
 * @author normansyahputa on 4/13/17.
 */

public class AddUrlContainerViewHolder {

    public static final int MAX_ROWS = 3;
    private final View itemView;
    private final RecyclerView recyclerViewAddUrlVideo;
    private final AddUrlVideoAdapter addUrlVideoAdapter;
    private final TextView textAddUrlVideo;
    ProductAddActivity context;

    public AddUrlContainerViewHolder(View view) {
        this.itemView = view;

        if (itemView.getContext() instanceof ProductAddActivity) {
            context = (ProductAddActivity) itemView.getContext();
        }

        recyclerViewAddUrlVideo = (RecyclerView) view.findViewById(R.id.recycler_view_add_url_video);
        textAddUrlVideo = (TextView) view.findViewById(R.id.text_add_url_video);
        textAddUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.openAddYoutubeDialog();
            }
        });
        addUrlVideoAdapter = new AddUrlVideoAdapter(new ImageHandler(view.getContext()));
        addUrlVideoAdapter.setMaxRows(MAX_ROWS);
        addUrlVideoAdapter.setVideoSameWarn(itemView.getContext().getString(R.string.video_same_warn));
        recyclerViewAddUrlVideo.setAdapter(addUrlVideoAdapter);
        recyclerViewAddUrlVideo.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel) {
        try {
            addUrlVideoAdapter.add(addUrlVideoModel);
        } catch (IllegalArgumentException iae) {
            Toast.makeText(
                    itemView.getContext(),
                    iae.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


}
