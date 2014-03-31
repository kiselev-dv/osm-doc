package me.osm.osmdoc.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import me.osm.osmdoc.model.Fref;
import me.osm.osmdoc.model.Group;
import me.osm.osmdoc.model.Hierarchy;

public class DOCReader {
	
	private DocPart doc;

	private Map<String, Feature> featureByName = new HashMap<String, Feature>();
	
	public DOCReader(String osmDocXML) {
		try {
			
			InputStream is = null;
			if("jar".equals(osmDocXML)) {
				is = DOCReader.class.getResourceAsStream("/osm-doc.xml");
			}
			else {
				is = new FileInputStream(new File(osmDocXML));
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance("me.osm.osmdoc.model", 
					me.osm.osmdoc.model.ObjectFactory.class.getClassLoader());
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			doc = (DocPart) unmarshaller.unmarshal(is);
			
			for(Feature f : doc.getFeature()) {
				featureByName.put(f.getName(), f);
			}
			
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Feature> getFeatures() {
		return new ArrayList<Feature>(doc.getFeature());
	}
	
	public DocPart getDoc() {
		return doc;
	}

	public Collection<? extends Feature> getHierarcyBranch(
			String hierarchyName, String branch) {
		
		Set<String> excluded = new HashSet<String>();
		
		Map<String, Hierarchy> hierarchy2Name = new HashMap<String, Hierarchy>();
		for(Hierarchy h : doc.getHierarchy()) {
			hierarchy2Name.put(h.getName(), h);
		}
		
		Hierarchy hierarchy = hierarchy2Name.get(hierarchyName);
		
		for(Fref fref : hierarchy.getFref()) {
			
			String ref = fref.getRef();
			if(ref.equals(branch)) {
				excluded.add(ref);
				break;
			}
			
			for(Group g : hierarchy.getGroup()) {
				traverseGroup(branch, g, excluded, g.getName().equals(branch));
			}
		}
		
		Set<Feature> result = new HashSet<Feature>();
		for(String ex : excluded) {
			Feature feature = featureByName.get(ex);
			result.add(feature);
		}
		
		
		return result;
	}

	private void traverseGroup(String branch, Group g, Set<String> excluded, boolean add) {
		
		if(add) {
			for(Fref fref : g.getFref()) {
				excluded.add(fref.getRef());
			}
			
			for(Group cg : g.getGroup()) {
				traverseGroup(branch, cg, excluded, true);
			}
		}
		else {
			for(Fref fref : g.getFref()) {
				if(fref.getRef().equals(branch)) {
					excluded.add(fref.getRef());
					return;
				}
			}
			
			for(Group cg : g.getGroup()) {
				traverseGroup(branch, cg, excluded, cg.getName().equals(branch));
			}
		}
	}
}
