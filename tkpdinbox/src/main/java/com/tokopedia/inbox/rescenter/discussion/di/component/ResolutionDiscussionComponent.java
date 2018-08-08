package com.tokopedia.inbox.rescenter.discussion.di.component;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.discussion.di.module.ResolutionDiscussionModule;
import com.tokopedia.inbox.rescenter.discussion.di.scope.ResolutionDiscussionScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/18/17.
 */
@ResolutionDiscussionScope
@Component(modules = ResolutionDiscussionModule.class, dependencies = ResolutionDetailComponent.class)
public interface ResolutionDiscussionComponent {



}
