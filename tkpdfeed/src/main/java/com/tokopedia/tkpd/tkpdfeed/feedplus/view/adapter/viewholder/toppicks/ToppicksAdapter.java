package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.toppicks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksItemViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksAdapter extends RecyclerView.Adapter<ToppicksAdapter.ViewHolder> {

    private final FeedPlus.View.Toppicks toppicksListener;
    private ToppicksViewModel toppicksViewModel;

    public ToppicksAdapter(FeedPlus.View.Toppicks toppicksListener) {
        this.toppicksListener = toppicksListener;
    }

    public void setData(ToppicksViewModel toppicksViewModel) {
        this.toppicksViewModel = toppicksViewModel;
        notifyDataSetChanged();
    }

    public ArrayList<ToppicksItemViewModel> getList() {
        return toppicksViewModel.getList();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toppicksListener.onToppicksClicked(
                            toppicksViewModel.getPage(),
                            toppicksViewModel.getRowNumber(),
                            toppicksViewModel.getList().get(getAdapterPosition()).getName(),
                            toppicksViewModel.getList().get(getAdapterPosition()).getUrl(),
                            getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ToppicksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toppicks_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToppicksAdapter.ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.image, toppicksViewModel.getList().get(position).getImage());
    }

    @Override
    public int getItemCount() {
        if (toppicksViewModel.getList() != null
                && !toppicksViewModel.getList().isEmpty())
            return toppicksViewModel.getList().size();
        else
            return 0;
    }


}
