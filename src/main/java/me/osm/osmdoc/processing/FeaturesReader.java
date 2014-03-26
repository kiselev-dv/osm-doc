package me.osm.osmdoc.processing;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import me.osm.osmdoc.model.DocPart;
import me.osm.osmdoc.model.Feature;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FeaturesReader {
	
	public static void main(String[] args) {
		try {
			System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			System.out.println("<doc-part xmlns=\"http://map.osm.me/osm-doc-part\">");
			
			URL url = FeaturesReader.class.getResource("/features");
			URI uri = url.toURI();
			if(uri != null) {
				File dir = new File(uri);
				Iterator filesIterator = FileUtils
						.iterateFiles(dir, new SuffixFileFilter(".xml"), TrueFileFilter.INSTANCE);
				
				while(filesIterator.hasNext()) {
					File f = (File) filesIterator.next();
					for(String line : (List<String>)FileUtils.readLines(f)) {
						if(!line.contains("<?xml") && !line.contains("<doc-part") 
								&& !line.contains("</doc-part>")) {
							System.out.println(line);
						}
					}
				}
				
			}
			
			System.out.println("</doc-part>");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Feature> getFeatures (String folder) {
		
		List<Feature> result = new ArrayList<Feature>();
		try {
			if("jar".equals(folder)) {
				JAXBContext jaxbContext = JAXBContext.newInstance("me.osm.osmdoc.model", 
						me.osm.osmdoc.model.ObjectFactory.class.getClassLoader());
				
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				DocPart docPart = (DocPart) unmarshaller.unmarshal(
						FeaturesReader.class.getResourceAsStream("/osm-ru-features.xml"));
				
				for(Feature feature : docPart.getFeature()) {
					result.add(feature);
				}
			}
			else {
				readFromFolder(folder + "/features", result);
			}
		
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static void readFromFolder(String folder, List<Feature> result) throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance("me.osm.osmdoc.model", 
				me.osm.osmdoc.model.ObjectFactory.class.getClassLoader());

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		File dir = new File(folder);
		Iterator filesIterator = FileUtils
				.iterateFiles(dir, new SuffixFileFilter(".xml"), TrueFileFilter.INSTANCE);
		
		while(filesIterator.hasNext()) {
			File f = (File) filesIterator.next();
			DocPart docPart = (DocPart) unmarshaller.unmarshal(f);
			for(Feature feature : docPart.getFeature()) {
				result.add(feature);
			}
		}
	}
	
}
