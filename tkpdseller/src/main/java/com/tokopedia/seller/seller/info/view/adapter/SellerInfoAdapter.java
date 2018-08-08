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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoAdapter extends BaseListAdapter<SellerInfoModel> {

    public static final int YESTERDAY_MINIMUM_SIZE = 2;
    public static final int TODAY_MINIMUM_SIZE = 1;
    public static final int TODAY_INDEX = 0;
    public static final int YESTERDAY_INDEX = 1;
    private List<SellerInfoModel> rawModels = new ArrayList<>();

    private String[] monthNames;

    public SellerInfoAdapter(String[] monthNames) {
        this.monthNames = monthNames;
    }

    private Map<SellerInfoModel, Integer> positions = new HashMap<>();

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
    protected void bindData(final int position, RecyclerView.ViewHolder viewHolder) {
        final SellerInfoModel t = data.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rawModels.isEmpty())
                    return;

                // change rawModels read status
                Integer pos = positions.get(t);
                if(pos != null){
                    rawModels.get(pos).setRead(true);
                }
                // change data status
                SellerInfoAdapter.this.data.get(position).setRead(true);
                notifyItemChanged(position);

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
        if(data.size() > TODAY_MINIMUM_SIZE){
            SellerInfoModel sellerInfoModel = data.get(TODAY_INDEX);
            if( sellerInfoModel != null){
                sellerInfoModel.setToday(SellerInfoDateUtil.isToday(sellerInfoModel.getCreateTimeUnix(), monthNames));
            }
        }

        // for yesterday
        if(data.size() > YESTERDAY_MINIMUM_SIZE){
            SellerInfoModel sellerInfoModel = data.get(YESTERDAY_INDEX);
            if( sellerInfoModel != null){
                sellerInfoModel.setYesterday(SellerInfoDateUtil.isYesterday(sellerInfoModel.getCreateTimeUnix(), monthNames));
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

            positions.put(sellerInfoModel, i);
            result.add(sellerInfoModel);

            i++;
        }


        super.addData(result);
    }
}
