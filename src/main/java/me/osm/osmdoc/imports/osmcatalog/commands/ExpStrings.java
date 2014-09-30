package me.osm.osmdoc.imports.osmcatalog.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.osm.osmdoc.model.Feature;
import me.osm.osmdoc.model.LangString;
import me.osm.osmdoc.read.DOCFileReader;
import me.osm.osmdoc.read.DOCFolderReader;
import me.osm.osmdoc.read.DOCReader;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class ExpStrings {

	private String catalogPath;
	private List<String> langs;
	private List<String> types;

	public ExpStrings(String catalogPath, List<String> langs, List<String> types) {
		
		this.catalogPath = catalogPath;
		this.langs = langs;
		this.types = types;
		
	}

	public void run() throws IOException {
		CsvListWriter csvListWriter = new CsvListWriter(new PrintWriter(System.out), 
				new CsvPreference.Builder('$', '\t', "\n").build());
		
		try {
			DOCReader reader = null;
			
			if(catalogPath.equals("jar") || catalogPath.endsWith(".xml")) {
				reader = new DOCFileReader(catalogPath);
			}
			else {
				reader = new DOCFolderReader(catalogPath);
			}
			
			for(Feature f : reader.getFeatures()) {
				
				List<String> row = new ArrayList<String>();
				Map<String, List<String>> titles = new HashMap<String, List<String>>();
				
				for(LangString ls : f.getTitle()) {
					if(titles.get(ls.getLang()) == null) {
						titles.put(ls.getLang(), new ArrayList<String>());
					}
					titles.get(ls.getLang()).add(ls.getValue()); 
				}
				
				int ttls = 0;
				for(Entry<String, List<String>> e : titles.entrySet()) {
					ttls = Math.max(ttls, e.getValue().size());
				}
				
				for(int i = 0; i < ttls; i++) {
					row.add(f.getName() + ".title");
					for(String lk : langs) {
						row.add(titles.get(lk).get(0));
					}
					
					csvListWriter.write(row);
				} 
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			csvListWriter.flush();
			csvListWriter.close();
		}
		
	}

}
