package com.tokopedia.tokocash.historytokocash.data.mapper;

import com.tokopedia.tokocash.historytokocash.data.entity.ActionHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.presentation.model.ActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.HeaderHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryMapper implements Func1<TokoCashHistoryEntity, TokoCashHistoryData> {

    @Inject
    public TokoCashHistoryMapper() {
    }

    @Override
    public TokoCashHistoryData call(TokoCashHistoryEntity tokoCashHistoryEntity) {
        return mappingData(tokoCashHistoryEntity);
    }

    private TokoCashHistoryData mappingData(TokoCashHistoryEntity tokoCashHistoryEntity) {
        TokoCashHistoryData tokoCashHistoryData = new TokoCashHistoryData();

        if (tokoCashHistoryEntity != null) {
            List<HeaderHistory> headerHistoryList = new ArrayList<>();

            for (int i = 0; i < tokoCashHistoryEntity.getHeader().size(); i++) {
                HeaderHistory headerHistory = new HeaderHistory();
                if (tokoCashHistoryEntity.getHeader() != null) {
                    headerHistory.setName(tokoCashHistoryEntity.getHeader().get(i).getName());
                    headerHistory.setType(tokoCashHistoryEntity.getHeader().get(i).getType());
                    headerHistory.setSelected(tokoCashHistoryEntity.getHeader().get(i).isSelected());
                }
                headerHistoryList.add(headerHistory);
            }
            tokoCashHistoryData.setHeaderHistory(headerHistoryList);

            if (tokoCashHistoryEntity.getItems() != null) {
                List<ItemHistory> itemHistoryList = new ArrayList<>();
                for (int i = 0; i < tokoCashHistoryEntity.getItems().size(); i++) {
                    ItemHistory itemHistory = new ItemHistory();
                    itemHistory.setTransactionId(tokoCashHistoryEntity.getItems()
                            .get(i).getTransaction_id());
                    itemHistory.setTransactionDetailId(tokoCashHistoryEntity.getItems()
                            .get(i).getTransaction_detail_id());
                    itemHistory.setTransactionType(tokoCashHistoryEntity.getItems()
                            .get(i).getTransaction_type());
                    itemHistory.setTitle(tokoCashHistoryEntity.getItems().get(i).getTitle());
                    itemHistory.setUrlImage(tokoCashHistoryEntity.getItems().get(i).getIcon_uri());
                    itemHistory.setDescription(tokoCashHistoryEntity.getItems()
                            .get(i).getDescription());
                    itemHistory.setTransactionInfoId(tokoCashHistoryEntity.getItems()
                            .get(i).getTransaction_info_id());
                    itemHistory.setTransactionInfoDate(tokoCashHistoryEntity.getItems()
                            .get(i).getTransaction_info_date());
                    itemHistory.setAmount(tokoCashHistoryEntity.getItems()
                            .get(i).getAmount());
                    itemHistory.setAmountChanges(tokoCashHistoryEntity.getItems()
                            .get(i).getAmount_changes());
                    itemHistory.setAmountChangesSymbol(tokoCashHistoryEntity.getItems()
                            .get(i).getAmount_changes_symbol());
                    itemHistory.setNotes(tokoCashHistoryEntity.getItems().get(i).getNotes());
                    itemHistory.setAmountPending(tokoCashHistoryEntity.getItems().get(i).getAmount_pending());

                    if (tokoCashHistoryEntity.getItems().get(i).getActions() != null) {
                        List<ActionHistory> actionHistoryList = new ArrayList<>();
                        for (int j = 0; j < tokoCashHistoryEntity.getItems().get(i).getActions().size(); j++) {
                            ActionHistoryEntity actionHistoryEntity = tokoCashHistoryEntity
                                    .getItems().get(i).getActions().get(j);
                            ActionHistory actionHistory = new ActionHistory();
                            actionHistory.setName(actionHistoryEntity.getName());
                            actionHistory.setMethod(actionHistoryEntity.getMethod());
                            actionHistory.setTitle(actionHistoryEntity.getTitle());
                            actionHistory.setUrl(actionHistoryEntity.getUrl());

                            ParamsActionHistory paramAction = new ParamsActionHistory();
                            paramAction.setRefundId(actionHistoryEntity.getParams().getRefundId());
                            paramAction.setRefundType(actionHistoryEntity.getParams().getRefundType());
                            actionHistory.setParams(paramAction);
                            actionHistory.setType(actionHistoryEntity.getType());
                            actionHistoryList.add(actionHistory);
                        }
                        itemHistory.setActionHistoryList(actionHistoryList);
                    }
                    itemHistoryList.add(itemHistory);
                }
                tokoCashHistoryData.setItemHistoryList(itemHistoryList);
            }
            tokoCashHistoryData.setNext_uri(tokoCashHistoryEntity.isNext_uri());
        }
        return tokoCashHistoryData;
    }
}