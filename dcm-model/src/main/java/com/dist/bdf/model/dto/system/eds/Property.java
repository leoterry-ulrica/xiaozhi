package com.dist.bdf.model.dto.system.eds;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class Property implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String symbolicName;
	private boolean hidden = false;
	/**
	 * 是否必须
	 */
	private boolean required = true;
	//private String value;
	/**
	 * 表示这个属性改变后，是否级联更新依赖此属性的其他属性
	 */
	private boolean hasDependentProperties = false;
	private ChoiceList choiceList = new ChoiceList();
	
/*	private Object[]customInvalidItems = {1,2,3};*/
	
	public Property(String symbolicName, boolean hidden, boolean required, boolean hasDependentProperties, String choicListName, Map<?, ?> data, String value){
		
		this.symbolicName = symbolicName;
		this.hidden = hidden;
		this.required = required;
		this.hasDependentProperties = hasDependentProperties;
		this.choiceList.setDisplayName(choicListName);
		//this.value = value;
		
		if(data != null && !data.isEmpty()){
			addChoices(data);
		}
		
	}
	public String getSymbolicName() {
		return symbolicName;
	}
	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isHasDependentProperties() {
		return hasDependentProperties;
	}
	public void setHasDependentProperties(boolean hasDependentProperties) {
		this.hasDependentProperties = hasDependentProperties;
	}
	public ChoiceList getChoiceList() {
		return choiceList;
	}
	public void setChoiceList(ChoiceList choiceList) {
		this.choiceList = choiceList;
	}
	
	@SuppressWarnings("rawtypes")
	private void addChoices(Map data){
		
		Iterator entries = data.entrySet().iterator();  	  
		while (entries.hasNext()) {  
		  
		    Map.Entry entry = (Map.Entry) entries.next();    
		    String key = entry.getKey().toString();  
		    String value = entry.getValue().toString();  
		    addChoice(value,key);
		}  
	}
	private void addChoice(String displayName, String name) {
		
		Choice ch = new Choice(displayName, name);
		this.choiceList.addChoiceEntry(ch);
		
	}
/*	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	*/
	/*public Object[] getCustomInvalidItems() {
		return customInvalidItems;
	}
	public void setCustomInvalidItems(Object[] customInvalidItems) {
		this.customInvalidItems = customInvalidItems;
	}*/
}
