package net.virtualviking.dunebuggy.models;

import java.util.ArrayList;
import java.util.List;

public class CatalogTreeNode {
	private final String text;
	
	private final String icon;
	
	private final String href;

	private final String color;
	
	private final String backColor;
	
	private List<CatalogTreeNode> nodes;

	public CatalogTreeNode(String text, String icon, String href, String color, String backColor) {
		super();
		this.text = text;
		this.icon = icon;
		this.href = href;
		this.color = color;
		this.backColor = backColor;
	}

	public List<CatalogTreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<CatalogTreeNode> nodes) {
		this.nodes = nodes;
	}

	public String getText() {
		return text;
	}

	public String getIcon() {
		return icon;
	}

	public String getHref() {
		return href;
	}

	public String getColor() {
		return color;
	}

	public String getBackColor() {
		return backColor;
	}
	
	public void addNode(CatalogTreeNode node) {
		if(nodes == null)
			nodes = new ArrayList<>();
		nodes.add(node);
	}
}
