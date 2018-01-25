package com.tokopedia.movies.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naveengoyal on 1/9/18.
 */

public class CustomShowTimingLayout extends LinearLayout {


    public List<String> showTimings;
    public String movieClass;
    public int moviePrice;
    public List<PackageViewModel> pkPackageViewModel;
    public TextView theatreClass;
    public TextView theatreClassPrice;
    LinearLayout parentLayout;
    View movieGroup;
    View showTiming1;
    View showTiming2;
    View showTiming3;
    View showTiming4;
    View showTiming5;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;


    public CustomShowTimingLayout(Context context) {
        super(context);
//        initView();
    }

    public CustomShowTimingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        initView();
    }

    public CustomShowTimingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView();
    }


//    private void initView() {
//        inflate(getContext(), R.layout.include_show_timing, this);
//        ButterKnife.bind(this);
//        createLayout();
//    }
//
//    public void setPkgViewModel(List<PackageViewModel> pkgViewModel) {
//        this.pkPackageViewModel = pkgViewModel;
//    }
//
//    public void setInformation(String movieClass, int price, String showTimings) {
//        this.movieClass = movieClass;
//        this.moviePrice = price;
//
//        Log.d("Naveen", "Movie Class is" + movieClass + "Movie Price is" + moviePrice + "Showtiming is" + showTimings);
////        this.showTimings = showTimings;
//    }
//
//    public void clearParentLayout() {
//        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
//        if (parentLayout.getChildCount() > 0) {
//            parentLayout.removeAllViews();
//        }
//    }
//
//
//    public void createLayout() {
//        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
//
//        movieGroup = View.inflate(getContext(), R.layout.show_timing_layout, this);
//
//        /*if (movieGroup.getParent() != null) {
//            ((ViewGroup) movieGroup.getParent()).removeAllViews();
//            parentLayout.removeAllViews();
//        }*/
//
//        theatreClass = movieGroup.findViewById(R.id.theatre_class);
//        theatreClassPrice = movieGroup.findViewById(R.id.theatre_class_price);
//
//        showTiming1 = movieGroup.findViewById(R.id.showTiming1);
//        showTiming2 = movieGroup.findViewById(R.id.showTiming2);
//        showTiming3 = movieGroup.findViewById(R.id.showTiming3);
//        showTiming4 = movieGroup.findViewById(R.id.showTiming4);
//        showTiming5 = movieGroup.findViewById(R.id.showTiming5);
//
//
//        textView1 = (TextView) showTiming1.findViewById(R.id.show_timing);
//        textView2 = (TextView) showTiming2.findViewById(R.id.show_timing);
//        textView3 = (TextView) showTiming3.findViewById(R.id.show_timing);
//        textView4 = (TextView) showTiming4.findViewById(R.id.show_timing);
//        textView5 = (TextView) showTiming5.findViewById(R.id.show_timing);
//
//    }
//
//    public void bindData() {
//
//        for (int j = 0; j < pkPackageViewModel.size(); j++) {
//            theatreClass.setText(pkPackageViewModel.get(j).getDescription());
//            theatreClassPrice.setText("( Rp" + " " + pkPackageViewModel.get(j).getSalesPrice() + ")");
//
//
//            // +1 for Show More Item.
//            int totalLayout = pkPackageViewModel.size() / 5 + 1;
//            int showMorePosition = (pkPackageViewModel.size() % 5) + 1;
//
//            for (int i = 0; i < totalLayout; i++) {
//
////            showTiming1.setOnClickListener(this);
////            showTiming2.setOnClickListener(this);
////            showTiming3.setOnClickListener(this);
//
//                if (i == totalLayout - 1) {
//                    switch (showMorePosition) {
//                        case 1:
//                            textView1.setText(Utils.convertShowTiming(pkPackageViewModel.get(i).getShowDate()));
//                            showTiming2.setVisibility(View.INVISIBLE);
//                            showTiming3.setVisibility(View.INVISIBLE);
//                            showTiming4.setVisibility(View.INVISIBLE);
//                            showTiming5.setVisibility(View.INVISIBLE);
//                            break;
//                        case 2:
//                            textView2.setText(Utils.convertShowTiming(pkPackageViewModel.get(i).getShowDate()));
//                            showTiming3.setVisibility(View.INVISIBLE);
//                            showTiming4.setVisibility(View.INVISIBLE);
//                            showTiming5.setVisibility(View.INVISIBLE);
//                            break;
//                        case 3:
//                            textView3.setText(Utils.convertShowTiming(pkPackageViewModel.get(i).getShowDate()));
//                            showTiming4.setVisibility(View.INVISIBLE);
//                            showTiming5.setVisibility(View.INVISIBLE);
//                            break;
//                        case 4:
//                            textView4.setText(Utils.convertShowTiming(pkPackageViewModel.get(i).getShowDate()));
//                            showTiming5.setVisibility(View.INVISIBLE);
//                            break;
//                        case 5:
//                            textView5.setText(Utils.convertShowTiming(pkPackageViewModel.get(i).getShowDate()));
//                            break;
//                        default:
//                            break;
//
//                    }
//                } else {
//                    textView1.setText("");
//                    textView2.setText("");
//                    textView3.setText("");
//                    textView4.setText("");
//                    textView5.setText("");
//
//                }
//                parentLayout.addView(movieGroup);
//            }
//        }
//    }
}
