package org.virtual.sdmxregistry;

import javax.xml.namespace.QName;

import org.virtualrepository.Property;

public interface Registry {
	

	public QName name();
	
	public boolean isReadonly();
	
	public Property[] properties();
	
}