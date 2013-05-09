package org.virtual.sdmxregistry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name="gcube")
public class GCubeRegistry extends AbstractRegistry {
	
	@XmlAttribute
	private String scope;
	
	@SuppressWarnings("unused")
	private GCubeRegistry(){} //for JAXB
	
	public GCubeRegistry(QName name, String scope) {
		super(name);
		this.scope = scope;
	}
	
	public String scope() {
		return scope;
	}

	@Override
	public String toString() {
		return "GCubeRegistry ["+name()+"," + scope + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		GCubeRegistry other = (GCubeRegistry) obj;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}

	
	
}