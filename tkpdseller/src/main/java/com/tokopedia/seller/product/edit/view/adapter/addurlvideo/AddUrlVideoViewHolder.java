package com.tokopedia.seller.product.edit.view.adapter.addurlvideo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.model.AddUrlVideoModel;

/**
 * @author normansyahputa on 4/12/17.
 */

public class AddUrlVideoViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageAddVideoUrl;
    private TextView textAddVideoUrlTitle;
    private TextView textAddVideoUrlDescription;
    private ImageView imageAddVideoUrlRemove;

    private ImageHandler imageHandler;
    private AddUrlVideoModel addUrlVideoModel;
    private AddUrlVideoOnClickRemove addUrlVideoOnClickRemove;

    public AddUrlVideoViewHolder(View itemView, ImageHandler imageHandler) {
        super(itemView);
        this.imageHandler = imageHandler;
        initView(itemView);
    }

    private void initView(View itemView) {
        imageAddVideoUrl = (ImageView) itemView.findViewById(R.id.image_add_video_url);
        textAddVideoUrlTitle = (TextView) itemView.findViewById(R.id.text_add_video_url_title);
        textAddVideoUrlDescription = (TextView) itemView.findViewById(R.id.text_add_video_url_description);
        imageAddVideoUrlRemove = (ImageView) itemView.findViewById(R.id.image_add_video_url_remove);
    }

    public void bindData(
            AddUrlVideoModel addUrlVideoModel,
            AddUrlVideoOnClickRemove addUrlVideoOnClickRemove) {
        this.addUrlVideoModel = addUrlVideoModel;
        imageHandler.loadImage(
                imageAddVideoUrl,
                addUrlVideoModel.getThumbnailUrl()
        );

        textAddVideoUrlTitle.setText(addUrlVideoModel.getSnippetTitle());

        textAddVideoUrlDescription.setText(addUrlVideoModel.getSnippetDescription());

        addUrlVideoOnClickRemove.setIndex(getAdapterPosition());
        imageAddVideoUrlRemove.setOnClickListener(addUrlVideoOnClickRemove);
    }

    public static abstract class AddUrlVideoOnClickRemove implements View.OnClickListener {

        private int index;

        protected AddUrlVideoOnClickRemove() {
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            onClick(v, index);
        }

        public abstract void onClick(View v, int index);
    }
}
