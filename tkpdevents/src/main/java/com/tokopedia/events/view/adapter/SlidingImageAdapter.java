package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class SlidingImageAdapter extends PagerAdapter {


    private List<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private EventHomePresenter mPresenter;


    public SlidingImageAdapter(Context context, List<String> IMAGES, EventHomePresenter presenter) {
        this.context = context;
        this.IMAGES = IMAGES;
        this.mPresenter = presenter;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.evnt_banner_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.banner_item);

        ImageHandler.loadImageCover2(imageView, IMAGES.get(position));
        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onClickBanner();
            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
