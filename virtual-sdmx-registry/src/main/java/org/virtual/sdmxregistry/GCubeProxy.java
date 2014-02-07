package org.virtual.sdmxregistry;

import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.gcube.datapublishing.sdmx.impl.model.GCubeSDMXRegistryDescriptor;
import org.gcube.datapublishing.sdmx.impl.registry.FusionRegistryClient;

public class GCubeProxy extends RegistryProxy<GCubeRegistry> {

	public GCubeProxy(GCubeRegistry registry) {
		
		super(registry);
	}

	//helper
	
	public SDMXRegistryClient endpoint() {
		
		ScopeProvider.instance.set(registry().scope());
		
		return new FusionRegistryClient(new GCubeSDMXRegistryDescriptor());
		
	}

}
