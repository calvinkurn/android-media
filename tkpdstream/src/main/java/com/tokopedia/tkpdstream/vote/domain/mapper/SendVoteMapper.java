package com.tokopedia.tkpdstream.vote.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.domain.pojo.SendVotePojo;
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

public class SendVoteMapper implements Func1<Response<DataResponse<SendVotePojo>>, VoteInfoViewModel>{

    @Inject
    public SendVoteMapper() {
    }

    @Override
    public VoteInfoViewModel call(Response<DataResponse<SendVotePojo>> dataResponseResponse) {

        List<Visitable> list = new ArrayList<>();
        String title = "Title";
        String cr7 = "http://01a4b5.medialib.edu.glogster.com/media/55/5527aa424a7bc417e364f92537e4daa0f366ab6a2373dfa8616f8977f7b9c685/cristiano-ronaldo-portual-goal.jpg";
        String messi = "https://static.independent.co.uk/s3fs-public/styles/article_small/public/thumbnails/image/2014/07/09/23/10-messi.jpg";
        VoteViewModel channelViewModel = new VoteViewModel("Cristiano Ronaldo", cr7,40, VoteViewModel.DEFAULT, VoteViewModel.IMAGE_TYPE);
        list.add(channelViewModel);
        channelViewModel = new VoteViewModel("Lionel Messi", messi, 60, VoteViewModel.DEFAULT, VoteViewModel.IMAGE_TYPE);
        list.add(channelViewModel);

//        channelViewModel = new VoteViewModel("Cristiano Ronaldo",40, VoteViewModel.DEFAULT);
//        list.add(channelViewModel);
//        channelViewModel = new VoteViewModel("Lionel Messi", 60, VoteViewModel.DEFAULT);
//        list.add(channelViewModel);


        return null;
    }
}
