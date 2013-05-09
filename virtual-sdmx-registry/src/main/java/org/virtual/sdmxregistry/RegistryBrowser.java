package org.virtual.sdmxregistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.Detail;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.References;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.AssetType;
import org.virtualrepository.sdmx.SdmxAsset;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Browser;


public class RegistryBrowser implements Browser {


	public static final Logger log = LoggerFactory.getLogger(RegistryBrowser.class);

	
	private final SDMXRegistryClient endpoint;

	public RegistryBrowser(SDMXRegistryClient endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public Iterable<SdmxAsset> discover(Collection<? extends AssetType> types) throws Exception {

		List<SdmxAsset> assets = new ArrayList<SdmxAsset>();

		SdmxBeans beans = endpoint.getCodelist("all", "all", "all", Detail.allstubs, References.none);

		for (CodelistBean list : beans.getCodelists())
			assets.add(toAsset(list));

		return assets;
	}
	
	//helper
	
	private SdmxAsset toAsset(CodelistBean list) {
		
		SdmxAsset asset = new SdmxCodelist(list.getUrn(),list.getId(), list.getVersion(), list.getName());
		
		try {
			asset.setURI(list.getStructureURL().toURI());
		}
		catch(Exception e) {
			log.trace("invalid structure url for asset "+asset.id());
		}
		if (list.isFinal().isSet())
			asset.setStatus(list.isFinal().isTrue()?"final":"partial");
		
		return asset;
	}
}
