package com.tokopedia.seller.seller.info.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.model.SellerInfoSectionModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoAdapter extends BaseListAdapter<SellerInfoModel> {

    private List<SellerInfoModel> rawModels = new ArrayList<>();

    private String[] monthNames;

    public SellerInfoAdapter(String[] monthNames) {
        this.monthNames = monthNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SellerInfoModel.TYPE:
                SellerInfoViewHolder sellerInfoViewHolder = new SellerInfoViewHolder(
                        getLayoutView(parent, R.layout.item_seller_info)
                );
                return sellerInfoViewHolder;
            case SellerInfoSectionModel.TYPE_:
                SellerInfoSectionViewHolder viewHolder = new SellerInfoSectionViewHolder(
                        getLayoutView(parent, R.layout.item_seller_info_section)
                );
                return viewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected void bindData(int position, RecyclerView.ViewHolder viewHolder) {
        final SellerInfoModel t = data.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClicked(t);
                }
            }
        });
        switch (t.getType()){
            case SellerInfoModel.TYPE:
                ((SellerInfoViewHolder)viewHolder).bindObject(t);
                break;
            default:
                ((SellerInfoSectionViewHolder)viewHolder).bindObject((SellerInfoSectionModel) t);
                break;
        }
    }

    public void clearRawAdapter(){
        this.rawModels.clear();
    }

    @Override
    public void addData(List<SellerInfoModel> data) {
        this.data.clear();
        rawModels.addAll(data);

        data = rawModels;

        Collections.sort(data, new Comparator<SellerInfoModel>() {
            public int compare(SellerInfoModel o1, SellerInfoModel o2) {
                return SellerInfoDateUtil.fromUnixTimeDate(o2.getCreateTimeUnix())
                        .compareTo(
                                SellerInfoDateUtil.fromUnixTimeDate(o1.getCreateTimeUnix())
                        );
            }
        });

        // for today
        if(data.size() > 1){
            if(data.get(0) != null){
                data.get(0).setToday(SellerInfoDateUtil.isToday(data.get(0).getCreateTimeUnix(), monthNames));
            }
        }

        // for yesterday
        if(data.size() > 2){
            if(data.get(1) != null){
                data.get(1).setYesterday(SellerInfoDateUtil.isYesterday(data.get(1).getCreateTimeUnix(), monthNames));
            }
        }

        // add sections in here
        List<SellerInfoModel> result = new ArrayList<>();
        SellerInfoSectionModel sectionTmp = null;
        int i = 0;
        for (; i < data.size(); ) {
            SellerInfoModel sellerInfoModel = data.get(i);

            if(sectionTmp==null){
                sectionTmp = new SellerInfoSectionModel();
                sectionTmp.setCreateTimeUnix(sellerInfoModel.getCreateTimeUnix());

                if (sellerInfoModel.isToday()) {
                    sectionTmp.setToday(sellerInfoModel.isToday());
                }

                if (sellerInfoModel.isYesterday()) {
                    sectionTmp.setYesterday(sellerInfoModel.isYesterday());
                }

                result.add(sectionTmp);
                continue;
            }

            boolean isSectionDiff = SellerInfoDateUtil.fromUnixTime(sectionTmp.getCreateTimeUnix(),monthNames)
                    .equals(SellerInfoDateUtil.fromUnixTime(sellerInfoModel.getCreateTimeUnix(), monthNames));
            if (!isSectionDiff) {
                sectionTmp = null;
                continue;
            }

            result.add(sellerInfoModel);

            i++;
        }


        super.addData(result);
    }


}
