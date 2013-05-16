package org.acme;

import static java.util.Collections.*;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.xml.namespace.QName;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.Detail;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.References;
import org.junit.Test;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtual.sdmxregistry.GCubeProxy;
import org.virtual.sdmxregistry.GCubeRegistry;
import org.virtual.sdmxregistry.GenericProxy;
import org.virtual.sdmxregistry.GenericRegistry;
import org.virtual.sdmxregistry.RegistryBrowser;
import org.virtual.sdmxregistry.RegistryImporter;
import org.virtual.sdmxregistry.RegistryPublisher;
import org.virtualrepository.Asset;
import org.virtualrepository.sdmx.SdmxCodelist;

public class APITests {

	private final static String address = "http://pc-fortunati.isti.cnr.it:8080/FusionRegistry/ws/rest/";
	
	private GenericRegistry registry = new GenericRegistry(new QName("Luigi's"), URI.create(address));
	private GCubeRegistry gregistry = new GCubeRegistry(new QName("Luigi's"), "/gcube/devsec");
	
	GenericProxy proxy = new GenericProxy(registry);
	GCubeProxy gservice = new GCubeProxy(gregistry);

	@Test
	public void discoveryQuery() throws Exception {

		SdmxBeans beans = proxy.endpoint().getCodelist("all", "all", "latest", Detail.allstubs, References.none);

		for (CodelistBean list : beans.getCodelists())
			System.out.println(list.getName());
	}
	

	@Test
	public void browseSources() throws Exception {

		RegistryBrowser browser = new RegistryBrowser(proxy.endpoint());
		
		Iterable<? extends Asset> assets = browser.discover(singletonList(SdmxCodelist.type));

		for (Asset asset : assets)
			System.out.println(asset);
	}
	
	
	@Test
	public void browseGCUBESources() throws Exception {

		RegistryBrowser browser = new RegistryBrowser(gservice.endpoint());
		
		Iterable<? extends Asset> assets = browser.discover(singletonList(SdmxCodelist.type));

		for (Asset asset : assets)
			System.out.println(asset);
	}


	@Test
	public void retrieveQuery() throws Exception {

		SdmxBeans beans = proxy.endpoint().getCodelist("all", "CL_UNIT", "latest", Detail.full, References.none);
		toXML(beans);

	}
	
	@Test
	public void retrieveAsset() throws Exception {
		
		RegistryImporter importer = new RegistryImporter(proxy.endpoint());
		
		SdmxCodelist codelist = new SdmxCodelist("http://whatever.org","TEST_CODELIST","2.0", "whatever");
		
		CodelistBean bean = importer.retrieve(codelist);
		
		toXML(bean);

	}
	
	@Test
	public void publishAsset() throws Exception {

		RegistryPublisher importer = new RegistryPublisher(proxy.endpoint());
		
		importer.publish(null,aCodelist("3.0"));
		
	}
	
	//helpers
	
	
	CodelistBean aCodelist(String version) {
		
		/** Create codelist with two codes **/
		CodelistMutableBean codelist = new CodelistMutableBeanImpl();
		codelist.setAgencyId("SDMX");
		codelist.setId("TEST_CODELIST");
		codelist.setVersion(version);
		codelist.addName("en", "Test codelist");
		 
		//First code
		CodeMutableBean code1 = new CodeMutableBeanImpl();
		code1.addName("en", "Test code");
		code1.setId("TEST_CODE_1");
		code1.addDescription("en", "Test description");
		AnnotationMutableBean annotation = new AnnotationMutableBeanImpl();
		annotation.setTitle("Annotation title");
		annotation.addText("en", "Annotation text");
		code1.addAnnotation(annotation);
		
		codelist.addItem(code1);
		
		return codelist.getImmutableInstance();
	}
	
	
	void toXML(CodelistBean bean) {

		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		toXML(beans);
		
		
	}
	
	void toXML(SdmxBeans beans) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = new StructureWritingManagerImpl();
		manager.writeStructures(beans,format, stream);
		
		System.out.println(stream.toString());
	}

}
