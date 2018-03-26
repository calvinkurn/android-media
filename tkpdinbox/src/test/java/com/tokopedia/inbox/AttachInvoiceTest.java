package com.tokopedia.inbox;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.attachinvoice.data.mapper.TkpdResponseToInvoicesDataModelMapper;
import com.tokopedia.inbox.attachinvoice.data.repository.AttachInvoicesRepository;
import com.tokopedia.inbox.attachinvoice.data.repository.AttachInvoicesRepositoryImpl;
import com.tokopedia.inbox.attachinvoice.data.source.service.GetTxInvoicesService;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observer;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.inbox.inboxchat.domain.usecase.AttachImageUseCase.PARAM_DEVICE_ID;
import static com.tokopedia.inbox.rescenter.detailv2.domain.interactor.UploadImageUseCase.PARAM_USER_ID;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoiceTest {
    CountDownLatch countDownLatch;
    @Test
    public void testRepository() throws Exception{
        countDownLatch = new CountDownLatch(1);
        /*
        https://ws.tokopedia.com/v4/tx-order/get_tx_order_list.pl?per_page=10
        &device_id=foaHeLi-97o:APA91bGEXQlASdlj076hYhWDtk_7aGNOKmClIhMY2MrxMYmyQFKY01JJzVmL6_AtVroksd9HWjknNL8rRlPe-veBxP4BhgqZelmw0KrmulHwRjMudRqB4XMt_VLavCJecccIa2mP-1oP
        &user_id=7977933
        &os_type=1
        &start=20/2/201
        &end=22/3/2018
        &page=1&invoice=
        &hash=6d36ca00bde9031c02f6868576d08869
        &status=
        &device_time=1521688251
        */
        String deviceId = "foaHeLi-97o:APA91bGEXQlASdlj076hYhWDtk_7aGNOKmClIhMY2MrxMYmyQFKY01JJzVmL6_AtVroksd9HWjknNL8rRlPe-veBxP4BhgqZelmw0KrmulHwRjMudRqB4XMt_VLavCJecccIa2mP-1oP";
        String userId = "7977933";
        String hash = md5(userId + "~" + deviceId);
        HashMap<String,String> params = new HashMap<>();
        params.put("per_page","10");
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put("hash", hash);
        params.put("os_type", "1");
        params.put("device_time", String.valueOf((new Date().getTime()) / 1000));
        params.put("start","19/2/2018");
        params.put("end","21/3/2018");
        params.put("page","1");

        AttachInvoicesRepository repository = new AttachInvoicesRepositoryImpl(new GetTxInvoicesService(),new TkpdResponseToInvoicesDataModelMapper());
        repository.getUserInvoices(params).subscribe(new Observer<List<Invoice>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onNext(List<Invoice> invoices) {
                Assert.assertNotNull(invoices.get(0).getNumber());
                countDownLatch.countDown();
            }
        });
        countDownLatch.await(10, TimeUnit.SECONDS);
    }
}
