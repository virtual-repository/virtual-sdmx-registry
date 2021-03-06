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
	
	public ClientFactory factory() {
		
		return new ClientFactory() {
			
			@Override
			public SDMXRegistryClient client() {
				
				SDMXRegistryDescriptorImpl descriptor = new SDMXRegistryDescriptorImpl();
				
				descriptor.setUrl(SDMXRegistryInterfaceType.RESTV2_1,registry().address().toString());
				
				return new FusionRegistryClient(descriptor);
			}
		};
		
		
	}

}
