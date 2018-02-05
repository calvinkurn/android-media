package com.tokopedia.movies.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.data.entity.response.Group;
import com.tokopedia.movies.view.customview.CustomShowTimingLayout;
import com.tokopedia.movies.view.customview.CustomTheatreClassLayout;
import com.tokopedia.movies.view.presenter.EventBookTicketPresenter;
import com.tokopedia.movies.view.utils.CurrencyUtil;
import com.tokopedia.movies.view.viewmodel.GroupViewModel;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 27/11/17.
 */

public class AddTicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GroupViewModel> groupViewModelList;
    Context mContext;
    EventBookTicketPresenter mPresenter;
    String title;


    public AddTicketAdapter(Context context, List<GroupViewModel> data, EventBookTicketPresenter presenter, String title) {
        groupViewModelList = data;
        mContext = context;
        mPresenter = presenter;
        this.title = title;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.add_tickets_layout, parent, false);
        TicketViewHolder holder = new TicketViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TicketViewHolder) holder).setViewHolder(groupViewModelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return groupViewModelList.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_theatre_name)
        TextView theatreName;
        @BindView(R2.id.theatreClassLayout)
        CustomTheatreClassLayout customTheatreClassLayout;

        GroupViewModel holderViewModel;
        int index;


        public TicketViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setViewHolder(GroupViewModel viewModel, int position) {
            this.holderViewModel = viewModel;
            this.index = position;
            theatreName.setText(viewModel.getName());
            customTheatreClassLayout.removeAllViews();
            customTheatreClassLayout.setPkgViewModelList(viewModel.getName(), holderViewModel.getPackages(), title);
        }

    }
}
