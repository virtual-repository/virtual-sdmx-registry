package org.virtual.sdmxregistry;

import static org.virtualrepository.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.virtual.sdmxregistry.csv.CsvRegistryImporter;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;


public abstract class RegistryProxy<T extends Registry> implements ServiceProxy {

	private final T registry;
	
	private final RegistryBrowser browser;
	
	private final List<Importer<?,?>> importers = new ArrayList<>();
	
	private final List<Publisher<?,?>> publishers = new ArrayList<>();
	
	public RegistryProxy(T registry) {
		
		notNull("registry proxy",registry);
		this.registry=registry;
		
		ClientFactory factory = factory();
			
		this.browser = new RegistryBrowser(factory);
		
		RegistryImporter importer = new RegistryImporter(factory); 
		this.importers.add(importer);
		this.importers.add(new CsvRegistryImporter(importer));
		
		if (!registry.isReadonly())
			publishers.add(new RegistryPublisher(factory));
		
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
		return importers;
	}

	@Override
	public List<Publisher<?,?>> publishers() {
		return publishers;
	}

	@Override
	public String toString() {
		return registry.toString();
	}
	
	
	//helper
	
	public abstract ClientFactory factory();

}
