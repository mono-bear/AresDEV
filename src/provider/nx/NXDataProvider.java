package provider.nx;

import java.io.IOException;

import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataProvider;
import provider.nx.core.NXFile;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataProvider implements MapleDataProvider {
	private final NXFile file;

	public NXDataProvider(String path) throws IOException {
		this.file = new NXFile(path, NXFile.LibraryMode.MAPPED_AND_PARSED);
	}

	@Override
	public MapleData getData(String path) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public MapleDataDirectoryEntry getRoot() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}
