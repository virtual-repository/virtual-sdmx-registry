package org.acme;


import static org.junit.Assert.*;
import static org.sdmx.CodelistBuilder.*;

import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtual.sdmxregistry.csv.Sdmx2Table;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Table;


public class TableTransformTest {

	Sdmx2Table transform = new Sdmx2Table();
	String name="list";
	SdmxCodelist asset = new SdmxCodelist("some", "things", "dontmatter", name);
	
	
	@Test
	public void namesBecomeStandardColumns() throws Exception {
		
		String[][] data = {{"test",null},{"test2","le testette"}};
		
		CodelistBean list = list().add(code("c1").name(data[0][0],"en"))
								  .add(code("c2").name(data[1][0],"en").name(data[1][1],"fr")).
							end();
		
		Table table = transform.apply(asset, list);
		
		assertEquals(3,table.columns().size()); //names in same language generate single column
		
	}
	
	@Test
	public void descriptionsBecomeStandardColumns() throws Exception {
		
		String[][] data = {{"test","desc1",null,null},{"test2", null,"le testette","desc2"}};
		
		CodelistBean list = list().add(code("c1").name(data[0][0],"en")
				                                 .description(data[0][1], "en"))
								  .add(code("c2").name(data[1][0],"en")
										  		 .name(data[1][2],"fr")
										  		 .description(data[1][3], "fr"))
								   .end();
		
		Table table = transform.apply(asset, list);
		
		System.out.println(table.columns());
		
		assertEquals(5,table.columns().size()); //names in same language generate single column
		
	}
	
	@Test
	public void annotationsBecomeStandardColumns() throws Exception {
		
		CodelistBean list = list().add(code("c1").name("test","en")
				                                 .add(anno().title("title"))
				                                 .add(anno().title("title2").text("t1","en").text("t2","fr").text("t3","es"))
				                                  .add(anno().text("t1","en").type("type"))
				                                )
				                  .end();
		
		Table table = transform.apply(asset, list);
		
		System.out.println(table.columns());
		
		assertEquals(6,table.columns().size()); //names in same language generate single column
		
	}
	

	
}
