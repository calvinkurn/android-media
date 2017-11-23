package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.LabelsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/31/17.
 */

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.ViewHolder> {

    private static final String DEFAULT_WHITE = "#ffffff";
    private static final double MEDIAN_VALUE = 135;
    private List<LabelsViewModel> listLabel;

    public LabelsAdapter() {
        this.listLabel = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label,
                null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(listLabel.get(position).getTitle());
        LayerDrawable bgShape = (LayerDrawable) holder.label.getBackground();
        GradientDrawable background = (GradientDrawable) bgShape.findDrawableByLayerId(R.id
                .feed_container);
        if (!listLabel.get(position).getColor().toLowerCase().equals(DEFAULT_WHITE)) {
            background.setColor(Color.parseColor(listLabel.get(position)
                    .getColor()));
            background.setStroke(0, 0);
        } else {
            background.setColor(Color.WHITE);
            background.setStroke(1, Color.GRAY);

        }
        holder.label.setTextColor(getInverseColor(Color.parseColor(listLabel.get(position)
                .getColor())));
    }

    private int getInverseColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= MEDIAN_VALUE ? Color.BLACK : Color.WHITE;
    }

    @Override
    public int getItemCount() {
        return listLabel.size();
    }

    public void setList(List<LabelsViewModel> list) {
        this.listLabel = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
