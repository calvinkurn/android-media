package com.tokopedia.movies.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naveengoyal on 1/10/18.
 */

public class CustomTheatreClassLayout extends LinearLayout {

    @BindView(R2.id.theatre_class)
    TextView theatreClass;
    @BindView(R2.id.theatre_class_price)
    TextView theatreClassPrice;
    @BindView(R2.id.showTimeLayout)
    LinearLayout customShowTimeLayout;
    @BindView(R2.id.theatre_class_layout)
    LinearLayout theatreClassLayout;

    public String title;
    public String theatreName;

    List<PackageViewModel> pkgViewModelList;

    public CustomTheatreClassLayout(Context context) {
        super(context);
        initView();
    }

    public CustomTheatreClassLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomTheatreClassLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.show_timing_layout, this);
        ButterKnife.bind(this);
    }

    public void setPkgViewModelList(String theatreName, List<PackageViewModel> pkgViewModelList, String title) {
        this.pkgViewModelList = pkgViewModelList;
        this.title = title;
        this.theatreName = theatreName;
        setTheatreClassInfo();
    }

    public void setTheatreClassInfo() {
        HashMap<String, LinearLayout> set = new HashMap<>();
        LinearLayout showtimell = null;

        for (int i = 0; i < pkgViewModelList.size(); i++) {
            if (!set.containsKey(pkgViewModelList.get(i).getDescription())) {
                View view = inflate(getContext(), R.layout.show_timing_layout, this);
                ButterKnife.bind(view, this);
                theatreClass.setText(pkgViewModelList.get(i).getDescription() + "-" + Utils.movieDimension(title));
                theatreClassPrice.setText("(Rp" + " " + pkgViewModelList.get(i).getSalesPrice() + ")");
                showtimell = new LinearLayout(getContext());
                showtimell.setOrientation(LinearLayout.HORIZONTAL);
                customShowTimeLayout.addView(showtimell);
                set.put(pkgViewModelList.get(i).getDescription(), showtimell);
            } else {
                showtimell = set.get(pkgViewModelList.get(i).getDescription());
            }
            int fix_count = showtimell.getChildCount();
            if (fix_count == 5) {
                LinearLayout customShowTimeLayout = (LinearLayout) showtimell.getParent();
                showtimell = new LinearLayout(getContext());
                showtimell.setOrientation(LinearLayout.HORIZONTAL);
                customShowTimeLayout.addView(showtimell);
                set.put(pkgViewModelList.get(i).getDescription(), showtimell);

            }

            SeatLayoutInfo seatLayoutInfo = new SeatLayoutInfo();
            seatLayoutInfo.setMovieName(title);
            seatLayoutInfo.setTheatreName(theatreName);
            seatLayoutInfo.setTheatreClass(pkgViewModelList.get(i).getDescription() + "-" + Utils.movieDimension(title));
            seatLayoutInfo.setShowTiming(Utils.convertShowTiming(pkgViewModelList.get(i).getShowDate()));
            seatLayoutInfo.setUrl(pkgViewModelList.get(i).getFetchSectionUrl());
            seatLayoutInfo.setTicketPrice(pkgViewModelList.get(i).getSalesPrice());
            CustomShowTimeLayout customShowTimeLayout = new CustomShowTimeLayout(getContext(), seatLayoutInfo);
            customShowTimeLayout.setText(Utils.convertShowTiming(pkgViewModelList.get(i).getShowDate()));
            showtimell.addView(customShowTimeLayout);
        }
    }

}
