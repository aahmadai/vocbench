package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StInstances  extends LightEntity {

	private static final long serialVersionUID = 2462963413848433985L;
	private StInstancesId id;

	public StInstances() {
	}

	public StInstances(StInstancesId id) {
		this.id = id;
	}

	public StInstancesId getId() {
		return this.id;
	}

	public void setId(StInstancesId id) {
		this.id = id;
	}

}
