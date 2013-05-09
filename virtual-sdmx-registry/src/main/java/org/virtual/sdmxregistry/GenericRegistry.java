package org.virtual.sdmxregistry;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name="generic")
public class GenericRegistry extends AbstractRegistry {
	
	@XmlElement
	private URI address;

	@SuppressWarnings("unused")
	private GenericRegistry(){} //for JAXB
	
	public GenericRegistry(QName name, URI address) {
		super(name);
		this.address = address;
	}
	
	public URI address() {
		return address;
	}

	@Override
	public String toString() {
		return "SDMXRegistry ["+name()+"," + address + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericRegistry other = (GenericRegistry) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

	
	
}