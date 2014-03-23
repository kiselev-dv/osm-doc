package me.osm.osmdoc.processing;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import me.osm.osmdoc.model.DocPart;
import me.osm.osmdoc.model.Feature;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FeaturesReader {
	public static List<Feature> getFeatures () {
		
		List<Feature> result = new ArrayList<Feature>();
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("me.osm.osmdoc.model", 
					me.osm.osmdoc.model.ObjectFactory.class.getClassLoader());
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			URL url = FeaturesReader.class.getResource("src/main/resources/features");
			if (url != null) {
				File dir = new File(url.toURI());
				Iterator filesIterator = FileUtils
						.iterateFiles(dir, new SuffixFileFilter(".xml"), TrueFileFilter.INSTANCE);
				
				for(File f = (File) filesIterator.next();filesIterator.hasNext();) {
					DocPart docPart = (DocPart) unmarshaller.unmarshal(f);
					for(Feature feature : docPart.getFeature()) {
						result.add(feature);
					}
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
