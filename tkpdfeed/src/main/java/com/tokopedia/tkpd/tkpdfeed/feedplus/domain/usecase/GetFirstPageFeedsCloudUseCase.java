package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    public GetFirstPageFeedsCloudUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread, feedRepository);
    }


}
