package com.tokopedia.tokocash.qrpayment.data.mapper;

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
    public BalanceTokoCash call(Wallet wallet) {
        if (wallet != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            //create an object if tokocash is not activated
            if (!wallet.getLinked()) {
                ActionBalance action = new ActionBalance();
                action.setLabelAction("Aktivasi TokoCash");
                action.setApplinks("tokopedia://wallet/activation");
                balanceTokoCash.setBalance("");
                balanceTokoCash.setTitleText("TokoCash");
                balanceTokoCash.setActionBalance(action);
                return balanceTokoCash;
            }

            if (wallet.getAction() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(wallet.getAction().getApplinks());
                actionBalance.setLabelAction(wallet.getAction().getText());
                actionBalance.setRedirectUrl(wallet.getAction().getRedirectUrl());
                actionBalance.setVisibility(wallet.getAction().getVisibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }

            balanceTokoCash.setAbTags(wallet.getAbTags());
            balanceTokoCash.setApplinks(wallet.getApplinks());
            balanceTokoCash.setBalance(wallet.getBalance());
            balanceTokoCash.setHoldBalance(wallet.getHoldBalance());
            balanceTokoCash.setLink(wallet.getLinked() ? 1 : 0);
            balanceTokoCash.setRawBalance(wallet.getRawBalance());
            balanceTokoCash.setRawHoldBalance(wallet.getRawHoldBalance());
            balanceTokoCash.setRawTotalBalance(wallet.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(wallet.getRedirectUrl());
            balanceTokoCash.setTitleText(wallet.getText());
            balanceTokoCash.setTotalBalance(wallet.getTotalBalance());


            return balanceTokoCash;
        }
        throw new RuntimeException("Error");
    }
}