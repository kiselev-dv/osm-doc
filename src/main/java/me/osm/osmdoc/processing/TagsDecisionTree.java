package me.osm.osmdoc.processing;

import java.util.List;
import java.util.Map;

/**
 * Decision tree.
 * Decide do we support this type of features and find feature type
 * */
public interface TagsDecisionTree {
	
	/**
	 * Find feature type by it,s tags.
	 * @param tags - feature tags
	 * @returns feature types or <code>null</code> if this kind of features is not supported
	 * */
	public List<String> getType(Map<String, String> tags);
	
}
