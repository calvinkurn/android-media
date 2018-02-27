package com.tokopedia.tkpdstream.chatroom.view;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author by stevenfredian on 2/21/17.
 */

public class ShareItem {
    private Drawable icon;
    private String name;
    private View.OnClickListener onClickListener;

    public ShareItem(Drawable icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public ShareItem(Drawable icon, String name, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.name = name;
        this.onClickListener = onClickListener;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}