package org.acme;

import static java.util.Collections.*;
import static org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT.*;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.xml.namespace.QName;

import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.Detail;
import org.gcube.datapublishing.sdmx.api.registry.SDMXRegistryClient.References;
import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtual.sdmxregistry.GCubeProxy;
import org.virtual.sdmxregistry.GCubeRegistry;
import org.virtual.sdmxregistry.GenericProxy;
import org.virtual.sdmxregistry.GenericRegistry;
import org.virtual.sdmxregistry.RegistryBrowser;
import org.virtual.sdmxregistry.RegistryImporter;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Table;

public class APITests {

	private final static String address = "http://data.fao.org/sdmx/registry/";
	
	private GenericRegistry registry = new GenericRegistry(new QName("FAO's"), URI.create(address));
	private GCubeRegistry gregistry = new GCubeRegistry(new QName("Luigi's"), "/gcube/devsec");
	
	GenericProxy proxy = new GenericProxy(registry);
	GCubeProxy gservice = new GCubeProxy(gregistry);

	@Test
	public void discoveryQuery() throws Exception {

		SdmxBeans beans = proxy.factory().client().getCodelist("all", "all", "latest", Detail.allstubs, References.none);

		for (CodelistBean list : beans.getCodelists())
			System.out.println(list.getName());
	}
	

	@Test
	public void browseSources() throws Exception {

		RegistryBrowser browser = new RegistryBrowser(proxy.factory());
		
		Iterable<? extends Asset> assets = browser.discover(singletonList(SdmxCodelist.type));

		for (Asset asset : assets)
			System.out.println(asset);
	}
	
	
	@Test
	public void browseAndRetrieveCsvSources() {
		
		VirtualRepository repo  = new Repository();
		
		repo.discover(CsvCodelist.type);
		
		for (Asset asset : repo)
			System.out.println(asset);
		
		Table table = repo.retrieve(repo.iterator().next(),Table.class);
		
		System.out.println(table);
		
	}
	
	
	@Test
	public void browseGCUBESources() throws Exception {

		RegistryBrowser browser = new RegistryBrowser(gservice.factory());
		
		Iterable<? extends Asset> assets = browser.discover(singletonList(SdmxCodelist.type));

		for (Asset asset : assets)
			System.out.println(asset);
	}


	@Test
	public void retrieveQuery() throws Exception {

		SdmxBeans beans = proxy.factory().client().getCodelist("all", "CL_UNIT", "latest", Detail.full, References.none);
		toXML(beans);

	}
	
	@Test
	public void retrieveAsset() throws Exception {
		
		RegistryImporter importer = new RegistryImporter(proxy.factory());
		
		SdmxCodelist codelist = new SdmxCodelist("http://whatever.org","TEST_CODELIST","2.0", "whatever");
		
		CodelistBean bean = importer.retrieve(codelist);
		
		toXML(bean);

	}
	
	
	@Test
	public void retrieveFirstGcubeAsset() throws Exception {
		
		RegistryBrowser browser = new RegistryBrowser(gservice.factory());
		
		Iterable<? extends Asset> assets = browser.discover(singletonList(SdmxCodelist.type));
		
		RegistryImporter importer = new RegistryImporter(gservice.factory());
		
		CodelistBean bean = importer.retrieve((SdmxCodelist)assets.iterator().next());
		
		toXML(bean);

	}

	
	@Test(expected=IllegalStateException.class)
	public void cannotPublishInReadOnlyRegistry() throws Exception {

		VirtualRepository repository = new Repository();
		
		SdmxCodelist asset = new SdmxCodelist("test",repository.services().lookup(new QName("FAO Sdmx Registry")));
		
		repository.publish(asset,aCodelist("3.0"));
		
		
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

		SdmxServiceFactory.writer();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		StructureWriterManager manager = SdmxServiceFactory.writer();
		manager.writeStructures(beans,new SdmxStructureFormat(SDMX_V21_STRUCTURE_DOCUMENT), stream);
		
		System.out.println(stream.toString());
	}

}
