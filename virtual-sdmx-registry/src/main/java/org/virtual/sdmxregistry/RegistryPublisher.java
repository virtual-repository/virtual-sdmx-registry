package org.virtual.sdmxregistry;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;

public class RegistryPublisher implements Publisher<SdmxCodelist,CodelistBean> {

	private final SDMXRegistryClient endpoint;

	public RegistryPublisher(SDMXRegistryClient endpoint) {
		this.endpoint = endpoint;
	}
	
	@Override
	public Type<SdmxCodelist> type() {
		return SdmxCodelist.type;
	}

	@Override
	public Class<CodelistBean> api() {
		return CodelistBean.class;
	}

	@Override
	public void publish(SdmxCodelist asset, CodelistBean codelist) throws Exception {
		
		//we do not need to use the asset as the data is fully self-describing
		this.endpoint.publish(codelist);
	}

	
}
