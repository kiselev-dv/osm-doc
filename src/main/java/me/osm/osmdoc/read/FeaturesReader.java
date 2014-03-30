package me.osm.osmdoc.read;

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
