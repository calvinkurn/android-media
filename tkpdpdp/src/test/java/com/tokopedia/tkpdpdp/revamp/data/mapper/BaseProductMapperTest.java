package com.tokopedia.tkpdpdp.revamp.data.mapper;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseProductMapperTest {

    private BaseProductMapper mapper;

    @Mock
    public Gson gson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mapper = new BaseProductMapper(gson);
    }

    @Test
    public void whenResponse_NULL_throw_Exception() {
//        //given
//        BaseProductMapper spyMapper = spy(mapper);
//        Response<String> response = mock(any());
//        when(response.isSuccessful()).thenReturn(false);
//
//        //when
//        spyMapper.call(any());
//
//        verify(response)
    }
}