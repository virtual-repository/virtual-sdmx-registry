package org.virtual.sdmxregistry;

import javax.xml.namespace.QName;

import org.virtualrepository.Property;

public interface Registry {
	

	public QName name();
	
	public Property[] properties();
	
}