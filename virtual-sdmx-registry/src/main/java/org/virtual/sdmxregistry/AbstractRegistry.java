package org.virtual.sdmxregistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.namespace.QName;

import org.virtualrepository.Property;


public class AbstractRegistry implements Registry {
	
	@XmlElement
	private QName name;
	
	@XmlElementRef
	private List<ServiceProperty> properties = new ArrayList<ServiceProperty>();
	
	protected AbstractRegistry() {}//for JAXB
	
	public AbstractRegistry(QName name) {
		this.name=name;
	}
	
	@Override
	public QName name() {
		return name;
	}
	
	@Override
	public Property[] properties() {
		List<Property> props = new ArrayList<Property>();
		for (ServiceProperty p : properties)
			props.add(p.description==null?new Property(p.name,p.value):new Property(p.name,p.value,p.description));
		return props.toArray(new Property[0]);
	}
	
	public void addProperty(ServiceProperty property) {
		this.properties.add(property);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractRegistry other = (AbstractRegistry) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}