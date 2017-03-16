package me.j360.dubbo.util;

import java.util.HashMap;
import java.util.Map;

public class Params {
	
	private Map<String,Object> params = new HashMap<String,Object>();
	
	public Params(String key, Object value){
		params.put(key, value);
	}
	
	public Params append(String key,Object value){
		params.put(key, value);
		return this;
	}
	
	public Params append(String[] keys,Object[] values){
		if(keys == null || values == null){
			return this;
		}
		if(keys.length == 0 || values.length == 0){
			return this;
		}
		if(keys.length != values.length){
			throw new IllegalArgumentException();
		}
		for(int i=0;i<keys.length;i++){
			params.put(keys[i], values[i]);
		}
		return this;
	}
	
	public Map<String,Object> getResult(){
		return params;
	}
}
