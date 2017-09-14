package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.EditSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostEditSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class PostEditSolutionUseCase extends UseCase<EditAppealResolutionSolutionDomain> {
    public static final String RESO_ID = "reso_id";
    public static final String OBJECT_RESULT = "object_result";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_REFUND_AMOUNT = "refundAmount";

    private PostEditSolutionRepository postEditSolutionRepository;

    public PostEditSolutionUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   PostEditSolutionRepository postEditSolutionRepository) {
        super(threadExecutor, postExecutionThread);
        this.postEditSolutionRepository = postEditSolutionRepository;
    }

    @Override
    public Observable<EditAppealResolutionSolutionDomain> createObservable(RequestParams requestParams) {
        return postEditSolutionRepository.postEditSolutionDataCloud(requestParams);
    }

    public static RequestParams postEditSolutionUseCaseParams(String resoId,
                                                              int solutionId,
                                                              long refundAmount) {
        try {
            JSONObject object = new JSONObject();
            object.put(PARAM_SOLUTION, solutionId);
            object.put(PARAM_REFUND_AMOUNT, refundAmount);
            RequestParams params = RequestParams.create();
            params.putString(RESO_ID, resoId);
            params.putString(OBJECT_RESULT, object.toString());
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RequestParams postEditSolutionUseCaseParamsWithoutRefund(String resoId,
                                                                           int solutionId) {
        try {
            JSONObject object = new JSONObject();
            object.put(PARAM_SOLUTION, solutionId);
            RequestParams params = RequestParams.create();
            params.putString(RESO_ID, resoId);
            params.putObject(OBJECT_RESULT, object);
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
