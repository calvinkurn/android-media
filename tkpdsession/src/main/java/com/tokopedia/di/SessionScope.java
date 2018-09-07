package com.tokopedia.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author by nisie on 10/10/17.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface SessionScope {
}