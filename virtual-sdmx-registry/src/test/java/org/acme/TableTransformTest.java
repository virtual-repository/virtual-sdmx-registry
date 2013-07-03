package org.acme;


import static org.junit.Assert.*;

import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Table;

import csv.Sdmx2Table;

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
				                                 .add(anno().title("title2").text("t1","en").text("t2","fr").text("t3","sp"))
				                                  .add(anno().text("t1","en").type("type"))
				                                )
				                  .end();
		
		Table table = transform.apply(asset, list);
		
		System.out.println(table.columns());
		
		assertEquals(6,table.columns().size()); //names in same language generate single column
		
	}
	
	

	static Builder list() {
		return new Builder();
	}
	
	static CodeBuilder code(String id) {
		return new CodeBuilder(id);
	}
	
	static AnnotationBuilder anno() {
		return new AnnotationBuilder();
	}
	
	static class Builder {
		
		private CodelistMutableBean bean = new CodelistMutableBeanImpl();
		
		public Builder() {
			bean.addName("en","testlist");
			bean.setAgencyId("SDMX");
			bean.setId("testlist");
		}
		
		public Builder add(CodeBuilder builder) {
			bean.addItem(builder.build());
			return this;
		}
		
		public CodelistBean end() {
			return bean.getImmutableInstance();
		}
		
	}
	
	static class CodeBuilder {
		
		private CodeMutableBean bean = new CodeMutableBeanImpl();
		
		public CodeBuilder(String id) {
			bean.setId(id);
		}
		
		CodeBuilder name(String val,String lang) {
			bean.addName(lang,val);
			return this;
		}
		
		CodeBuilder description(String val,String lang) {
			bean.addDescription(lang,val);
			return this;
		}
		
		CodeBuilder add(AnnotationBuilder builder) {
			bean.addAnnotation(builder.build());
			return this;
		}
		
		CodeMutableBean build() {
			return bean;
		}
	}
	
	static class AnnotationBuilder {
		
		private AnnotationMutableBean bean = new AnnotationMutableBeanImpl();
		
		AnnotationBuilder title(String title) {
			bean.setTitle(title);
			return this;
		}
		
		AnnotationBuilder type(String type) {
			bean.setType(type);
			return this;
		}
		
		AnnotationBuilder text(String val,String lang) {
			bean.addText(lang,val);
			return this;
		}
		
		AnnotationMutableBean build() {
			return bean;
		}
	}

}
