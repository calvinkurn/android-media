package com.tokopedia.tokocash.qrpayment.data.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.qrpayment.presentation.model.ActionBalance;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<Wallet, BalanceTokoCash> {

    @Inject
    public BalanceTokoCashMapper() {
    }

    @Override
    public BalanceTokoCash call(Wallet balanceTokoCashEntity) {
        if (balanceTokoCashEntity != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();
            CommonUtils.dumper("Response Wallet Vishal :: " + balanceTokoCash.toString());

            if (balanceTokoCashEntity.getAction() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(balanceTokoCashEntity.getAction().getApplinks());
                actionBalance.setLabelAction(balanceTokoCashEntity.getAction().getText());
                actionBalance.setRedirectUrl(balanceTokoCashEntity.getAction().getRedirectUrl());
                actionBalance.setVisibility(balanceTokoCashEntity.getAction().getVisibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }
            //balanceTokoCash.setAbTags(balanceTokoCashEntity.getAbTags());
            balanceTokoCash.setApplinks(balanceTokoCashEntity.getApplinks());
            balanceTokoCash.setBalance(balanceTokoCashEntity.getBalance());
            balanceTokoCash.setHoldBalance(balanceTokoCashEntity.getHoldBalance());
            balanceTokoCash.setLink(balanceTokoCashEntity.getLinked() ? 1 : 0);
            balanceTokoCash.setRawBalance(balanceTokoCashEntity.getRawBalance());
            balanceTokoCash.setRawHoldBalance(balanceTokoCashEntity.getRawHoldBalance());
            //balanceTokoCash.setRawThreshold(balanceTokoCashEntity.getRawThreshold());
            balanceTokoCash.setRawTotalBalance(balanceTokoCashEntity.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(balanceTokoCashEntity.getRedirectUrl());
            //balanceTokoCash.setThreshold(balanceTokoCashEntity.gett());
            balanceTokoCash.setTitleText(balanceTokoCashEntity.getText());
            balanceTokoCash.setTotalBalance(balanceTokoCashEntity.getTotalBalance());

            return balanceTokoCash;
        }
        throw new RuntimeException("Error");
    }
}