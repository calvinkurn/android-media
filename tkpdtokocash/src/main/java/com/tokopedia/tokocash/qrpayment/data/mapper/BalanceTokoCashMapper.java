package com.tokopedia.tokocash.qrpayment.data.mapper;

import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.qrpayment.presentation.model.ActionBalance;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<BalanceTokoCashEntity, BalanceTokoCash> {

    @Override
    public BalanceTokoCash call(BalanceTokoCashEntity balanceTokoCashEntity) {
        if (balanceTokoCashEntity != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            if (balanceTokoCashEntity.getActionBalanceEntity() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(balanceTokoCashEntity.getActionBalanceEntity().getApplinks());
                actionBalance.setLabelAction(balanceTokoCashEntity.getActionBalanceEntity().getLabelAction());
                actionBalance.setRedirectUrl(balanceTokoCashEntity.getActionBalanceEntity().getRedirectUrl());
                actionBalance.setVisibility(balanceTokoCashEntity.getActionBalanceEntity().getVisibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }
            balanceTokoCash.setAbTags(balanceTokoCashEntity.getAbTags());
            balanceTokoCash.setApplinks(balanceTokoCashEntity.getApplinks());
            balanceTokoCash.setBalance(balanceTokoCashEntity.getBalance());
            balanceTokoCash.setHoldBalance(balanceTokoCashEntity.getHoldBalance());
            balanceTokoCash.setLink(balanceTokoCashEntity.getLink());
            balanceTokoCash.setRaw_balance(balanceTokoCashEntity.getRaw_balance());
            balanceTokoCash.setRawHoldBalance(balanceTokoCashEntity.getRawHoldBalance());
            balanceTokoCash.setRawThreshold(balanceTokoCashEntity.getRawThreshold());
            balanceTokoCash.setRawTotalBalance(balanceTokoCashEntity.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(balanceTokoCashEntity.getRedirectUrl());
            balanceTokoCash.setThreshold(balanceTokoCashEntity.getThreshold());
            balanceTokoCash.setTitleText(balanceTokoCashEntity.getTitleText());
            balanceTokoCash.setTotalBalance(balanceTokoCashEntity.getTotalBalance());

            return balanceTokoCash;
        }
        return null;
    }
}
