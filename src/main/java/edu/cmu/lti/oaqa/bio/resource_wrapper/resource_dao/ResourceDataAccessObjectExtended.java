package edu.cmu.lti.oaqa.bio.resource_wrapper.resource_dao;

import java.util.Collection;

import edu.cmu.lti.oaqa.bio.resource_wrapper.Entity;
import edu.cmu.lti.oaqa.bio.resource_wrapper.Relation;
import edu.cmu.lti.oaqa.bio.species_mapper.Species;

public interface ResourceDataAccessObjectExtended extends ResourceDataAccessObject {
	
	/**
	 */
	Collection<Entity> getEntities(String query, String aType);
	
	/**
	 */
	Collection<Entity> getEntities(String query, Species species);
	
	/**
	 */
	Collection<Entity> getRelatedEntities(Entity eObj, Relation relObj);	
}
