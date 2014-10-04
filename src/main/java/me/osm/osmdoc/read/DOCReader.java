package me.osm.osmdoc.read;

import java.util.Collection;
import java.util.List;

import me.osm.osmdoc.model.Feature;
import me.osm.osmdoc.model.Hierarchy;

public interface DOCReader {

	public List<Feature> getFeatures();

	public Collection<? extends Feature> getHierarcyBranch(
			String hierarchyName, String branch);

	public abstract List<Hierarchy> listHierarchies();

	public abstract Hierarchy getHierarchy(String name);
	

}