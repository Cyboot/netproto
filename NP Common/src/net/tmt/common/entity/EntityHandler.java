package net.tmt.common.entity;

import java.util.Map;

public interface EntityHandler {

	public EntityFactory getFactory();

	public Map<Long, Entity> getEntityMap();

	public EntityHandler getSelf();

}
