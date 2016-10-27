package net.virtualviking.dunesbuggy.models;

import java.util.ArrayList;
import java.util.List;

public class TableRow {
	private List <Object[]> rows = new ArrayList<>();
	
	public TableRow() {
	}
	
	public void addRow(Object...data) {
		rows.add(data);
	}
	
	public List<Object[]> getRows() {
		return rows;
	}
}
