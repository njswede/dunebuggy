package net.virtualviking.dunebuggy.models;

public class CatalogObject {
	private final String name;
	
	private final String id; 
		
	private final CatalogObject parent;
	
	private final boolean isFolder;

	public CatalogObject(String name, String id, CatalogObject parent, boolean isFolder) {
		super();
		this.name = name;
		this.id = id;
		this.parent = parent;
		this.isFolder = isFolder;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public CatalogObject getParent() {
		return parent;
	}
	
	public String getParentId()
	{
		return parent != null ? parent.getId() : null; 
	}
	
	public boolean getIsFolder() {
		return isFolder;
	}
}
