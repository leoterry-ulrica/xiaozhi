package com.dist.bdf.manager.ecm.teamspace;
/*package com.dist.bdf.provider.dcm.teamspace;

import java.io.IOException;
import java.util.ListIterator;

import javax.servlet.ServletRequest;

import org.junit.After;

import com.ibm.ecm.serviceability.Logger;
import com.ibm.ecm.teamspace.TeamspaceDB;
import com.ibm.ecm.util.SearchTemplateBase;
import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DistTeamspaceDB extends TeamspaceDB {

	private JSONObject teamspace;
	//private ServletRequest request = null;
	private String json = null;

	public DistTeamspaceDB(String json) {
		
		super(null,json);
		
		String methodName = "TeamspaceDB";

		this.json = json;
		try {
			this.teamspace = ((JSONObject) JSON.parse(json));
			//Logger.logDebug(this, methodName, request, "teamspace: " + this.teamspace.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			// Logger.logError(this, methodName, request, e);
		} catch (IOException e) {
			e.printStackTrace();
			// Logger.logError(this, methodName, request, e);
		}
	}
		       
	                  @Override
		         public JSONArray getMembers() {
		  160      String methodName = "getMembers";
		  161      JSONArray jsonArray = (JSONArray)this.teamspace.get("members");
		       
		  163      if (jsonArray != null) {
		  164        ListIterator iterator = jsonArray.listIterator();
		  165        while (iterator.hasNext()) {
		  166          JSONObject member = (JSONObject)iterator.next();
		  167          String displayName = (String)member.get("displayName");
		  168          String shortName = (String)member.get("name");
		//        Logger.logDebug(this, methodName, this.request, "shortName: " + shortName);
		 //        Logger.logDebug(this, methodName, this.request, "displayName: " + displayName);
		  171          if (displayName == null) {
		  172            member.put("displayName", shortName);
		  173            iterator.set(member);
		 //           Logger.logDebug(this, methodName, this.request, "added short name as display name");
		               } else {
		//          Logger.logDebug(this, methodName, this.request, "had a display name");
		               }
		             }
		           }
		       
		  181      return jsonArray;
		         }
		       
		 
}
*/