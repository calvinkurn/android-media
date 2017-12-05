package com.tokopedia.seller.seller.info.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.model.SellerInfoSectionModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoAdapter extends BaseListAdapter<SellerInfoModel> {

    SellerInfoDateUtil sellerInfoDateUtil;

    public SellerInfoAdapter(SellerInfoDateUtil sellerInfoDateUtil) {
        this.sellerInfoDateUtil = sellerInfoDateUtil;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SellerInfoModel.TYPE:
                SellerInfoViewHolder sellerInfoViewHolder = new SellerInfoViewHolder(
                        getLayoutView(parent, R.layout.item_seller_info)
                );
                sellerInfoViewHolder.setSellerInfoDateUtil(sellerInfoDateUtil);
                return sellerInfoViewHolder;
            case SellerInfoSectionModel.TYPE_:
                SellerInfoSectionViewHolder viewHolder = new SellerInfoSectionViewHolder(
                        getLayoutView(parent, R.layout.item_seller_info_section)
                );
                viewHolder.setSellerInfoDateUtil(sellerInfoDateUtil);
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

    @Override
    public void addData(List<SellerInfoModel> data) {
        Collections.sort(data, new Comparator<SellerInfoModel>() {
            public int compare(SellerInfoModel o1, SellerInfoModel o2) {
                return sellerInfoDateUtil.fromUnixTime(o1.getCreateTimeUnix())
                        .compareTo(
                                sellerInfoDateUtil.fromUnixTime(o2.getCreateTimeUnix())
                        );
            }
        });

        // for today
        if(data.size() > 1){
            if(data.get(0) != null){
                data.get(0).setToday(sellerInfoDateUtil.isToday(data.get(0).getCreateTimeUnix()));
            }
        }

        // for yesterday
        if(data.size() > 2){
            if(data.get(1) != null){
                data.get(1).setYesterday(sellerInfoDateUtil.isYesterday(data.get(1).getCreateTimeUnix()));
            }
        }

        // add sections in here
        List<SellerInfoModel> result = new ArrayList<>();
        result.addAll(data);
        SellerInfoSectionModel sectionTmp = null;
        for(int i=0;i<result.size();i++){
            if(sectionTmp==null){
                sectionTmp = new SellerInfoSectionModel();
                sectionTmp.setCreateTimeUnix(result.get(i).getCreateTimeUnix());

                if(data.get(i).isToday()){
                    sectionTmp.setToday(data.get(i).isToday());
                }

                if(data.get(i).isYesterday()){
                    sectionTmp.setToday(data.get(i).isYesterday());
                }

                result.add(i, sectionTmp);
                continue;
            }

            boolean isSectionDiff = sectionTmp.getCreateTimeUnix()==result.get(i).getCreateTimeUnix();
            if(isSectionDiff)
                continue;

            sectionTmp = new SellerInfoSectionModel();
            sectionTmp.setCreateTimeUnix(result.get(i).getCreateTimeUnix());
            result.add(i, sectionTmp);
        }

        super.addData(result);
    }


}
