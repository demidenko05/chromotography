package org.demidenko05.android.chromatography.model;

public abstract class AbstractEntityWithName extends AbstractEntity {
	private String name;
	@Override
	public String toString() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
