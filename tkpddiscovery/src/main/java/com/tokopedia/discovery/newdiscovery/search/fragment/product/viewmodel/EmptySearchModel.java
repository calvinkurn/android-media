package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;

/**
 * Created by henrypriyono on 10/31/17.
 */

public class EmptySearchModel implements Visitable<ProductListTypeFactory> {

    @DrawableRes
    private int imageRes;
    private String title;
    private String content;
    private String buttonText;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
