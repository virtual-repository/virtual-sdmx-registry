package org.virtual.sdmxregistry;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Plugin;

public class RegistryPlugin implements Plugin, Lifecycle {

	private static final String configurationFile = "sdmxregistries.xml";

	private final static Logger log = LoggerFactory.getLogger(RegistryPlugin.class);

	private final List<RepositoryService> services = new ArrayList<RepositoryService>();

	@Override
	public void init() throws Exception {
		
		log.info("configuring plugin");
		
		RegistryConfiguration config = loadConfiguration();	
		 
		for (Registry registry : config.registries()) {
			
			RegistryProxy<?> proxy = null;
			
			if (registry instanceof GenericRegistry)
				proxy = new GenericProxy((GenericRegistry) registry);
			
			else if (registry instanceof GCubeRegistry)
				proxy =new GCubeProxy((GCubeRegistry) registry);
			
			RepositoryService service = new RepositoryService(registry.name(),proxy,registry.properties());
			services.add(service);
		}
	}

	@Override
	public List<RepositoryService> services() {
		return services;
	}

	// helper
	private RegistryConfiguration loadConfiguration() throws Exception {

		JAXBContext context = JAXBContext.newInstance(RegistryConfiguration.class);

		URL configResource = getClass().getClassLoader().getResource(configurationFile);
		
		if (configResource==null)
			throw new IllegalStateException("cannot find configuration file "+configurationFile);

		return (RegistryConfiguration) context.createUnmarshaller().unmarshal(configResource);

	}
}
