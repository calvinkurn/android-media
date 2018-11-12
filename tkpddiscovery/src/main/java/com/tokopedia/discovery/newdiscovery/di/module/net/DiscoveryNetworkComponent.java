package com.tokopedia.discovery.newdiscovery.di.module.net;

import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;

import dagger.Component;

@DiscoveryScope
@Component(modules = DiscoveryNetModule.class)
public interface DiscoveryNetworkComponent {

}