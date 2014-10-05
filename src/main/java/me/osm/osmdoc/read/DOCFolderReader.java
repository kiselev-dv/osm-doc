package me.osm.osmdoc.read;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import me.osm.osmdoc.model.DocPart;
import me.osm.osmdoc.model.Feature;
import me.osm.osmdoc.model.Hierarchy;

public class DOCFolderReader extends AbstractReader {
	
	private Map<String, Feature> featureByName;
	private Map<String, Hierarchy> hierarchies = new HashMap<String, Hierarchy>();

	public DOCFolderReader(String path) {
		
		featureByName = new HashMap<String, Feature>();
		
		File root = new File(path);
		iterateOverFiles(root);
	}

	private void iterateOverFiles(File root) {
		if(root.isFile()) {
			if(root.getName().endsWith(".xml")) {
				parse(root);
			}
		}
		else if(root.isDirectory()){
			for(File f : root.listFiles()) {
				iterateOverFiles(f);
			}
		}
	}

	private void parse(File root) {
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("me.osm.osmdoc.model", 
					me.osm.osmdoc.model.ObjectFactory.class.getClassLoader());
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			DocPart doc = (DocPart) unmarshaller.unmarshal(root);
			
			for(Feature f : doc.getFeature()) {
				featureByName.put(f.getName(), f);
			}
			
			for(Hierarchy h : doc.getHierarchy()) {
				hierarchies.put(h.getName(), h);
			}
			
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Feature> getFeatures() {
		return new ArrayList<Feature>(featureByName.values());
	}

	@Override
	public Collection<? extends Feature> getHierarcyBranch(
			String hierarchyName, String branch) {
		
		Set<String> excluded = new HashSet<String>();
		Hierarchy hierarchy = getHierarchy(hierarchyName);
		
		return getHierarcyBranch(branch, excluded, hierarchy, featureByName);
	}

	@Override
	public List<Hierarchy> listHierarchies() {
		return new ArrayList<Hierarchy>(hierarchies.values());
	}

	@Override
	public Hierarchy getHierarchy(String name) {
		
		if(name==null && hierarchies.size() == 1) {
			return hierarchies.entrySet().iterator().next().getValue();
		}
		
		return hierarchies.get(name);
	}
}
