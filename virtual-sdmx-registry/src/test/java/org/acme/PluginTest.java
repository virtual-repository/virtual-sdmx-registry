package org.acme;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.virtual.sdmxregistry.GCubeRegistry;
import org.virtual.sdmxregistry.GenericRegistry;
import org.virtual.sdmxregistry.Registry;
import org.virtual.sdmxregistry.RegistryConfiguration;
import org.virtual.sdmxregistry.ServiceProperty;
import org.virtualrepository.impl.Services;

public class PluginTest {

	@Test
	public void configurationIsSerializable() throws Exception {
		
		GenericRegistry registry = new GenericRegistry(new QName("http://acme/org","test"),URI.create("http://acme.org/some/thing"));
		ServiceProperty p = new ServiceProperty();
		p.name="name";
		p.value="value";
		registry.addProperty(p);
		Registry gcubeRegistry = new GCubeRegistry(new QName("http://acme/org","test"),"/gcube/devsec");
		
		RegistryConfiguration config = new RegistryConfiguration(registry,gcubeRegistry);
		
		JAXBContext context = JAXBContext.newInstance(RegistryConfiguration.class);
		
		StringWriter writer = new StringWriter();
		context.createMarshaller().marshal(config,writer);
		
		StringReader reader = new StringReader(writer.toString());
		RegistryConfiguration parsed = (RegistryConfiguration) context.createUnmarshaller().unmarshal(reader);
		
		System.out.println(Arrays.asList(parsed.registries().iterator().next().properties()));
		assertEquals(config,parsed);
		
		
	}
	
	@Test
	public void pluginLoads() {
		
		Services repos = new Services();
		repos.load();
		
		assertTrue(repos.size()>0);
		
	}
}
