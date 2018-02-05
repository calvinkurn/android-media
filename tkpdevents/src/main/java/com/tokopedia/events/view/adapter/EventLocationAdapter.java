package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class EventLocationAdapter extends RecyclerView.Adapter<EventLocationAdapter.ViewHolder> {

    private List<EventLocationViewModel> eventLocationViewModels;
    private Context context;
    private ActionListener listener;

    public EventLocationAdapter(Context context, List<EventLocationViewModel> eventLocationViewModels, ActionListener listener) {
        this.context = context;
        this.eventLocationViewModels = eventLocationViewModels;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityName;
        public TextView district;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cityName = (TextView) itemView.findViewById(R.id.tv_city_name);
            district = (TextView) itemView.findViewById(R.id.tv_district);

        }


    }

    @Override
    public int getItemCount() {
        if (eventLocationViewModels != null) {
            return eventLocationViewModels.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cityName.setText(eventLocationViewModels.get(position).getName());
        holder.district.setText(eventLocationViewModels.get(position).getDistrict());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLocationItemSelected(eventLocationViewModels.get(position));
            }
        });
    }

    public void updateList(List<EventLocationViewModel> locationsList, String text) {
        List<EventLocationViewModel> tempList = new ArrayList<>();

        if (text.trim().length() == 0) {
            eventLocationViewModels = locationsList;
        } else {
            for (final EventLocationViewModel eventLocationViewModel : locationsList) {
                if (eventLocationViewModel.getSearchName().toLowerCase().contains(text.toLowerCase())) {
                    tempList.add(eventLocationViewModel);
                }
            }
            eventLocationViewModels = tempList;
        }
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onLocationItemSelected(EventLocationViewModel locationViewModel);
    }

}
