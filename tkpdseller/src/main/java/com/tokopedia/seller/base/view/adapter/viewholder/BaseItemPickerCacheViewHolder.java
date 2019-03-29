package com.tokopedia.seller.base.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.common.util.ItemPickerType;
import com.tokopedia.seller.R;

/**
 * Created by nathan on 6/23/17.
 */
@Deprecated
public abstract class BaseItemPickerCacheViewHolder<T extends ItemPickerType> extends BaseViewHolder<T> {

    protected ImageView imageView;
    protected TextView titleTextView;
    protected ImageButton closeImageButton;

    public interface RemoveCallback<T> {
        void onRemove(T t);
    }

    public BaseItemPickerCacheViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        closeImageButton = (ImageButton) itemView.findViewById(R.id.image_button_close);
    }

    protected RemoveCallback<T> removeCallback;

    public void setRemoveCallback(RemoveCallback<T> removeCallback) {
        this.removeCallback = removeCallback;
    }

    @Override
    public void bindObject(final T t) {
        titleTextView.setText(MethodChecker.fromHtml(t.getTitle()));
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeCallback != null) {
                    removeCallback.onRemove(t);
                }
            }
        });
    }
}