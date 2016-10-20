package net.virtualviking.dunesbuggy.vro;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class JSONHelper {
	static DateFormat dateFormat = new ISO8601DateFormat();
	
	@SuppressWarnings("unchecked")
	public static Object getObject(Map<String, Object> data, String name) {
		StringTokenizer st = new StringTokenizer(name, ".");
		Map<String, Object> here = data;
		Object result = null;
		while(st.hasMoreTokens()) {
			String simpleName = st.nextToken();
			result = here.get(simpleName);
			if(result == null)
				return null;
			if(st.hasMoreTokens())
				here = (Map<String, Object>) result;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getComplex(Map<String, Object> data, String name) {
		return (Map<String, Object>) getObject(data, name);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getListOfComplex(Map<String, Object> data, String name) {
		return (List<Map<String, Object>>) getObject(data, name);
	}
	
	@SuppressWarnings("unchecked")
	public static Object getIndexed(Map<String, Object> data, String name, int index) {
		List<Object> list = (List<Object>) JSONHelper.getObject(data, name);
		try {
			return list != null ? list.get(index) : null;
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getIndexedComplex(Map<String, Object> data, String name, int index) {
		return (Map<String, Object>) JSONHelper.getIndexed(data, name, index);
	}
	
	public static Map<String, Object> fixDates(Map<String, Object> json) {
		for(Map.Entry<String, Object> entry : json.entrySet()) {
			Object o = entry.getValue();
			entry.setValue(fixDate(o));
		}
		return json;
	}
	
	public static String getString(Map<String, Object> data, String name) {
		return (String) getObject(data, name);
	}
	
	public static String encode(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}
	
	@SuppressWarnings("unchecked")
	private static Object fixDate(Object o) {
		if(o == null)
			return null;
		if(o instanceof Map)
			return fixDates((Map<String, Object>) o);
		else if(o instanceof List) 
			return fixDates((List<Object>) o);
		else {
			try {
				String s = o.toString();
				return dateFormat.parseObject(s);
			} catch(ParseException e) {
				return o;
			}
		}
	}
	
	private static List<Object> fixDates(List<Object> objects) {
		ArrayList<Object> result = new ArrayList<Object>(objects.size());
		for(Object o : objects) {
			result.add(fixDate(o));
		}
		return result;
	}
	
}
