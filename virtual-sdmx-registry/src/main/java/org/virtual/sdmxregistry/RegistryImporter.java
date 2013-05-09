package org.virtual.sdmxregistry;

import java.util.Iterator;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.Detail;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.References;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Importer;

public class RegistryImporter implements Importer<SdmxCodelist,SdmxBeans> {

	private final SDMXRegistryClient endpoint;

	public RegistryImporter(SDMXRegistryClient endpoint) {
		this.endpoint = endpoint;
	}
	
	@Override
	public Type<SdmxCodelist> type() {
		return SdmxCodelist.type;
	}

	@Override
	public Class<SdmxBeans> api() {
		return SdmxBeans.class;
	}

	@Override
	public SdmxBeans retrieve(SdmxCodelist asset) throws Exception {
		
		SdmxBeans beans = endpoint.getCodelist("all", asset.remoteId(), asset.version(), Detail.referencestubs, References.none);
	
		Iterator<CodelistBean> it = beans.getCodelists().iterator();

		if (it.hasNext())
			return beans;
		else
			throw new IllegalStateException("unknown codelist "+asset.remoteId()+"("+asset.version()+")");
	}

	
}
