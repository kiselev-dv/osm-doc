package me.osm.osmdoc.processing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Linker {
	
	private static PrintWriter writer;
	
	public static void main(String[] args) {
		try {
			
			writer = new PrintWriter(new File(args[0]));
			
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<doc-part xmlns=\"http://map.osm.me/osm-doc-part\" xmlns:d=\"http://map.osm.me/osm-doc-part\">");
			
			linkFeatures("/features");
			linkFeatures("/hierarchies");
			linkFeatures("/traits");
			writer.println("</doc-part>");
			
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void linkFeatures(String resource) throws URISyntaxException, IOException {
		URL url = Linker.class.getResource(resource);
		URI uri = url.toURI();
		if(uri != null) {

			File dir = new File(uri);
			
			Iterator<File> filesIterator = FileUtils.iterateFiles(dir, 
					new SuffixFileFilter(".xml"), TrueFileFilter.INSTANCE);
			
			while(filesIterator.hasNext()) {
				File f = filesIterator.next();
				for(String line : (List<String>)FileUtils.readLines(f)) {
					if(!line.contains("<?xml") && !line.contains("<doc-part") 
							&& !line.contains("</doc-part>")) {
						writer.println(line);
					}
				}
			}
		}
	}
}
