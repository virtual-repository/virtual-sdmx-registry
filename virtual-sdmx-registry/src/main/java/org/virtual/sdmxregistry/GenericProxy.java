package org.virtual.sdmxregistry;

import org.gcube.datapublishing.sdmx.api.model.SDMXRegistryInterfaceType;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.gcube.datapublishing.sdmx.impl.model.SDMXRegistryDescriptorImpl;
import org.gcube.datapublishing.sdmx.impl.registry.FusionRegistryClient;

public class GenericProxy extends RegistryProxy<GenericRegistry> {

	public GenericProxy(GenericRegistry registry) {
		
		super(registry);
	}

	//helper
	
	public SDMXRegistryClient endpoint() {
		
		SDMXRegistryDescriptorImpl descriptor = new SDMXRegistryDescriptorImpl();
		descriptor.setRest_url_V2_1(registry().address().toString());
		
		return new FusionRegistryClient(descriptor,SDMXRegistryInterfaceType.RESTV2_1);
		
	}

}
