package org.virtual.sdmxregistry;

import static org.virtual.sdmxregistry.Constants.*;

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
import org.virtualrepository.Property;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;


public class RegistryBrowser implements Browser {


	public static final Logger log = LoggerFactory.getLogger(RegistryBrowser.class);

	
	private final ClientFactory factory;

	public RegistryBrowser(ClientFactory factory) {
		this.factory = factory;
	}

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {

		//coding cautiously below: VR should not pass us an unsupported type
		
		if (types.contains(SdmxCodelist.type))
			return discoverSdmxCodelist();
		
		if (types.contains(CsvCodelist.type)) 
			return discoverCsvCodelist(discoverSdmxCodelist());
		
		
		throw new IllegalArgumentException("unsupported types "+types);
		
	}
	
	
	public List<SdmxCodelist> discoverSdmxCodelist() throws Exception {

		List<SdmxCodelist> assets = new ArrayList<SdmxCodelist>();

		SDMXRegistryClient client = factory.client();
		
		SdmxBeans beans = client.getCodelist("all", "all", "all", Detail.allstubs, References.none);

		for (CodelistBean list : beans.getCodelists())
			assets.add(toAsset(list));

		return assets;
	}
	
	public List<CsvCodelist> discoverCsvCodelist(List<SdmxCodelist> sdmxCodelists) throws Exception {

		List<CsvCodelist> assets = new ArrayList<CsvCodelist>();

		for (SdmxCodelist asset : sdmxCodelists)
			assets.add(toCsvAsset(asset));

		return assets;
	}
	
	//helper
	
	private SdmxCodelist toAsset(CodelistBean list) {
		
		SdmxCodelist asset = new SdmxCodelist(list.getUrn(),list.getId(), list.getVersion(), list.getName());
		
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
	
	private CsvCodelist toCsvAsset(SdmxCodelist asset) {
		
		CsvCodelist list = new CsvCodelist(asset.remoteId(),asset.name(),0);
		
		list.properties().add(new Property(URN_PROPERTY,asset.id()));
		list.properties().add(new Property(VERSION_PROPERTY,asset.version()));
		
		return list;
		
	}
}
