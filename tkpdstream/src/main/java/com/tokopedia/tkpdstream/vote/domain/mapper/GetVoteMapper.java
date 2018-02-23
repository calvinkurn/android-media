package com.tokopedia.tkpdstream.vote.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.data.VoteInfoPojo;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class GetVoteMapper implements Func1<Response<DataResponse>, VoteInfoViewModel>{

    @Inject
    public GetVoteMapper() {
    }

//    @Override
//    public VoteInfoViewModel call(Response<DataResponse<List<VoteInfoPojo>>> dataResponseResponse) {
//
//        List<Visitable> list = new ArrayList<>();
//        String title = "Title";
//        for (int i = 0; i < 10; i++) {
//            VoteViewModel channelViewModel = new VoteViewModel("Option "+i, 10, VoteViewModel.DEFAULT);
//            list.add(channelViewModel);
//        }
//
//        return new VoteInfoViewModel(title, list, 100);
//    }

    @Override
    public VoteInfoViewModel call(Response<DataResponse> dataResponseResponse) {

        List<Visitable> list = new ArrayList<>();
        String title = "Title";
        for (int i = 0; i < 10; i++) {
            VoteViewModel channelViewModel = new VoteViewModel("Option "+i, 10, VoteViewModel.DEFAULT);
            list.add(channelViewModel);
        }

        return new VoteInfoViewModel(title, list, 100);
    }
}
