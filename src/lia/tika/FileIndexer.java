package lia.tika;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

public class FileIndexer {
	protected FileHandler fileHandler;
	
	public FileIndexer(Properties props) throws IOException {
		fileHandler = new ExtensionFileHandler(props);
	}

}
