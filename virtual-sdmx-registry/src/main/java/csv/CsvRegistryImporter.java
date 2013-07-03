package csv;

import static org.virtual.sdmxregistry.Constants.*;
import static org.virtualrepository.spi.ImportAdapter.*;

import org.virtual.sdmxregistry.RegistryImporter;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.tabular.Table;

public class CsvRegistryImporter implements  Importer<CsvCodelist,Table> {

	
	private final Importer<SdmxCodelist,Table> inner;

	public CsvRegistryImporter(RegistryImporter inner) {
		this.inner=adapt(inner,new Sdmx2Table());
	}
	public Type<CsvCodelist> type() {
		return CsvCodelist.type;
	};
	
	@Override
	public Class<Table> api() {
		return inner.api();
	}
	
	@Override
	public Table retrieve(CsvCodelist asset) throws Exception {
		
		String urn = asset.properties().lookup(URN_PROPERTY).value(String.class);
		String version = asset.properties().lookup(VERSION_PROPERTY).value(String.class);
		
		SdmxCodelist sdmxAsset = new SdmxCodelist(urn,asset.id(),version, asset.name());
		return inner.retrieve(sdmxAsset);
	}
}
