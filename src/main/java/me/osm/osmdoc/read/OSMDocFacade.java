package me.osm.osmdoc.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.osm.osmdoc.model.Feature;
import me.osm.osmdoc.model.Tag;
import me.osm.osmdoc.model.Tags;

import org.apache.commons.lang3.StringUtils;


public class OSMDocFacade {
	
	
	private TagsDecisionTreeImpl dTree;
	private DOCReader docReader;
	private Set<Feature> excludedFeatures;
	
	
	/*
	 * tagKey -> [tagVal, tagVal, tagVal] 
	 * 	  tagVal -> [featureType, featureType] 
	 * 
	 * eg:
	 * 
	 * amenity:[parking, place_of_worship]
	 * 
	 * place_of_worship: [place_of_worship, place_of_worship_christian, place_of_worship_jewish, ...]
	 * 
	 */
	private Map<String, Map<String, List<Feature>>> key2values = new HashMap<String, Map<String,List<Feature>>>();
	
	public OSMDocFacade(String osmXML, List<String> exclude) {
		
		docReader = new DOCReader(osmXML);
		
		List<Feature> features = docReader.getFeatures();
		
		excludedFeatures = getBranches(exclude);
		
		for(Feature f : features) {
			
			if(excludedFeatures.contains(f)){
				continue;
			}
			
			//synonyms
			for(Tags synonym : f.getTags()) {
				
				//our feature should match all of them
				List<Tag> tagsCombination = synonym.getTag();
				
				for(Tag t : tagsCombination) {
					//TODO: support key match
					String tagKey = t.getKey().getValue();
					if(!t.isExclude()) {
						if(!key2values.containsKey(tagKey)) {
							key2values.put(tagKey, new HashMap<String, List<Feature>>());
						}
						
						for(Tag.Val val : t.getVal()) {
							String tagVal = val.getValue();
							if(key2values.get(tagKey).get(tagVal) == null) {
								key2values.get(tagKey).put(tagVal, new ArrayList<Feature>());
							}
							
							key2values.get(tagKey).get(tagVal).add(f);
						}
					}
				}
			}
		}
		
		dTree = new TagsDecisionTreeImpl(key2values);
	}

	public Set<Feature> getBranches(List<String> exclude) {
		String hierarcyName = null;
		boolean singleHierarcy = docReader.getDoc().getHierarchy().size() == 1;
		if(singleHierarcy) {
			hierarcyName = docReader.getDoc().getHierarchy().get(0).getName();
		}
		
		return getBranches(exclude, hierarcyName, singleHierarcy);
	}
	
	private Set<Feature> getBranches(List<String> exclude, String hierarcyName,
			boolean singleHierarcy) {
		Set<Feature> result = new HashSet<Feature>();
		if(exclude != null) {
			for(String ex : exclude) {
				String[] split = StringUtils.split(ex, ':');
				if(singleHierarcy && split.length == 1) {
					result.addAll(docReader.getHierarcyBranch(hierarcyName, ex));
				}
				else {
					result.addAll(docReader.getHierarcyBranch(split[0], split[1]));
				}
			}
		}
		return result;
	}

	public TagsDecisionTreeImpl getPoiClassificator() {
		return dTree;
	}
	
}
