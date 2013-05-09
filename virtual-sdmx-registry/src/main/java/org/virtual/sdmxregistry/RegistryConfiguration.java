package org.virtual.sdmxregistry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="registries")
public class RegistryConfiguration {

	
	@XmlElementRefs(
		{@XmlElementRef(type=GenericRegistry.class),
		@XmlElementRef(type=GCubeRegistry.class)}
	)
	private Set<Registry> registries;
	
	RegistryConfiguration() {}
	
	public RegistryConfiguration(Registry ... registries) {
		this.registries = new HashSet<Registry>();
		for (Registry registry : registries)
			this.registries.add(registry); 
	}
	
	public Set<Registry> registries() {
		return registries;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registries == null) ? 0 : registries.hashCode());
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
		RegistryConfiguration other = (RegistryConfiguration) obj;
		if (registries == null) {
			if (other.registries != null)
				return false;
		} else if (!registries.equals(other.registries))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 100;
		return "RegistryConfiguration [registries=" + (registries != null ? toString(registries, maxLen) : null) + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
