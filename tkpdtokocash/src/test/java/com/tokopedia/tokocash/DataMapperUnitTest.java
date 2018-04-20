package com.tokopedia.tokocash;

import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepDetailMapper;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepLimitMapper;
import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.data.model.DetailText;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataMapperUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final int FAKE_INT = 9;
    private static final String FAKE_STRING = "fake_string";
    private static final double FAKE_DOUBLE = 100.56d;
    @InjectMocks
    AutoSweepDetailMapper dataAutoSweepDetailMapper;
    @InjectMocks
    com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepDetailMapper viewAutoSweepDetailMapper;
    @InjectMocks
    AutoSweepLimitMapper dataAutoSweepLimitMapper;
    @InjectMocks
    com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepLimitMapper viewAutoSweepLimitMapper;

    @Test
    public void autoSweepDetailsDataToDomain_isCorrect() throws Exception {
        ResponseAutoSweepDetail responseData = new ResponseAutoSweepDetail();
        AutoSweepDetail data = new AutoSweepDetail();
        data.setAccountStatus(FAKE_INT);
        data.setAmountLimit(FAKE_INT);
        data.setBalance(FAKE_DOUBLE);
        data.setAutoSweepStatus(FAKE_INT);
        DetailText detailText = new DetailText();
        detailText.setTitle(FAKE_STRING);
        detailText.setContent(FAKE_STRING);
        data.setText(detailText);
        responseData.setData(data);

        com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail domain
                = dataAutoSweepDetailMapper.transform(responseData);

        if (domain.getAccountStatus() == FAKE_INT
                && domain.getAmountLimit() == FAKE_INT
                && domain.getBalance() == FAKE_DOUBLE
                && domain.getTitle().equalsIgnoreCase(FAKE_STRING)
                && domain.getContent().equalsIgnoreCase(FAKE_STRING)) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void autoSweepDetailDomainToView_isCorrect() throws Exception {
        com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail data = new com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail();
        data.setAccountStatus(FAKE_INT);
        data.setAmountLimit(FAKE_INT);
        data.setBalance(FAKE_DOUBLE);
        data.setAutoSweepStatus(FAKE_INT);
        DetailText detailText = new DetailText();
        data.setTitle(FAKE_STRING);
        data.setContent(FAKE_STRING);

        com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail domain
                = viewAutoSweepDetailMapper.transform(data);

        if (domain.getAccountStatus() == FAKE_INT
                && domain.getAmountLimit() == FAKE_INT
                && domain.getBalance() == FAKE_DOUBLE
                && domain.getTitle().equalsIgnoreCase(FAKE_STRING)
                && domain.getContent().equalsIgnoreCase(FAKE_STRING)) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void autoSweepLimitDataToDomain_isCorrect() throws Exception {
        ResponseAutoSweepLimit responseData = new ResponseAutoSweepLimit();
        AutoSweepLimit data = new AutoSweepLimit();
        data.setStatus(true);
        data.setAmountLimit(FAKE_INT);
        data.setAutoSweep(FAKE_INT);
        responseData.setData(data);


        com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit domain
                = dataAutoSweepLimitMapper.transform(responseData);

        if (domain.getAutoSweep() == FAKE_INT
                && domain.getAmountLimit() == FAKE_INT
                && domain.isStatus()) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void autoSweepLimitDomainToView_isCorrect() throws Exception {
        com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit domain = new com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit();
        domain.setStatus(true);
        domain.setAmountLimit(FAKE_INT);
        domain.setAutoSweep(FAKE_INT);

        com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit  view
                = viewAutoSweepLimitMapper.transform(domain);

        if (view.getAutoSweep() == FAKE_INT
                && view.getAmountLimit() == FAKE_INT
                && view.isStatus()) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }
}
