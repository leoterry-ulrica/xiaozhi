package com.dist.bdf.model.dto.system.eds;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ChoiceList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String displayName;
	private List<Choice> choices = new LinkedList<Choice>();
	
	public void addChoiceEntry(Choice choice){
		choices.add(choice);
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<Choice> getChoices() {
		return choices;
	}
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	
	
}
