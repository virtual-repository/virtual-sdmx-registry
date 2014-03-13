package org.virtual.sdmxregistry;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;

public interface ClientFactory {

	
	SDMXRegistryClient client();
	
}
