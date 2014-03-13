package org.virtual.sdmxregistry;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;

public class RegistryPublisher implements Publisher<SdmxCodelist,CodelistBean> {

	private final ClientFactory factory;

	public RegistryPublisher(ClientFactory factory) {
		this.factory = factory;
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
		
		SDMXRegistryClient client = factory.client();
		
		//we do not need to use the asset as the data is fully self-describing
		client.publish(codelist);
	}

	
}
