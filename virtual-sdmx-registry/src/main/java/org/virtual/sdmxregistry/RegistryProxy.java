package org.virtual.sdmxregistry;

import static org.virtualrepository.Utils.*;
import static org.virtualrepository.spi.ImportAdapter.*;

import java.util.Arrays;
import java.util.List;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient;
import org.virtual.sdmxregistry.transforms.Sdmx2Table;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.ServiceProxy;

public abstract class RegistryProxy<T extends Registry> implements ServiceProxy {

	private final T registry;
	
	private final RegistryBrowser browser;
	private final RegistryImporter importer;
	private final RegistryPublisher publisher;
	
	public RegistryProxy(T registry) {
		
		notNull("registry proxy",registry);
		this.registry=registry;
		
		SDMXRegistryClient endpoint = endpoint();
			
		this.browser = new RegistryBrowser(endpoint);
		this.importer = new RegistryImporter(endpoint);
		this.publisher = new RegistryPublisher(endpoint);
		
	}
	
	protected T registry() {
		return registry;
	}

	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<Importer<?,?>> importers() {
		return Arrays.<Importer<?,?>>asList(importer, adapt(importer,new Sdmx2Table()));
	}

	@Override
	public List<RegistryPublisher> publishers() {
		return Arrays.asList(publisher);
	}

	@Override
	public String toString() {
		return registry.toString();
	}
	
	
	//helper
	
	public abstract SDMXRegistryClient endpoint();

}
