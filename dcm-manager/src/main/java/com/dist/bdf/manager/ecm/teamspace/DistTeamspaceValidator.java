/*     */ package com.dist.bdf.manager.ecm.teamspace;
/*     */ 
/*     */ import com.ibm.ecm.serviceability.Logger;
import com.ibm.ecm.teamspace.TeamspaceDB;
import com.ibm.ecm.teamspace.TeamspaceValidator;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ListIterator;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
 public class DistTeamspaceValidator extends TeamspaceValidator
 {

/*     */   public JSONObject removeInvalidJsonFolderChildItems(JSONArray foldersArray, JSONArray childItemsArray, JSONArray jsonArray)
/*     */   {
/*  53 */     String methodName = "findJsonObjectById";
//    Logger.logEntry(this, methodName, request);
/*     */ 
//  Logger.logDebug(this, methodName, request, "foldersArray = " + foldersArray);
/*  57 */     JSONObject object = null;
/*     */ 
/*  59 */     if (foldersArray != null) {
//   Logger.logDebug(this, methodName, request, "number of folders = " + foldersArray.size());
/*  61 */       ListIterator iterator = foldersArray.listIterator();
/*  62 */       while (iterator.hasNext()) {
/*  63 */         object = (JSONObject)iterator.next();
/*  64 */         String id = (String)object.get("id");
/*  65 */         String type = (String)object.get("type");
/*  66 */         String name = (String)object.get("name");
/*  67 */         String path = (String)object.get("path");
/*  68 */         JSONObject invalidObject = new JSONObject();
/*  69 */         invalidObject.put("type", type);
/*  70 */         invalidObject.put("name", name);
/*  71 */         invalidObject.put("path", path);
/*  72 */         invalidObject.put("id", id);
//       Logger.logDebug(this, methodName, request, "id = " + id);
/*     */ 
/*  75 */         ListIterator iterator2 = childItemsArray.listIterator();
/*  76 */         while (iterator2.hasNext()) {
/*  77 */           JSONObject object2 = (JSONObject)iterator2.next();
/*  78 */           String id2 = (String)object2.get("id");
/*  79 */           if (id2.equals(id)) {
/*  80 */             jsonArray.add(object);
/*  81 */             iterator.remove();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  87 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */   public JSONObject findJsonObjectById(TeamspaceDB teamspaceDB, String objectId)
/*     */   {
/*  92 */     String methodName = "findJsonObjectById";
//     Logger.logEntry(this, methodName, request);
/*     */ 
/*  95 */     JSONArray foldersArray = teamspaceDB.getFolders();
//   Logger.logDebug(this, methodName, request, "foldersArray = " + foldersArray);
/*  97 */     JSONObject object = null;
/*     */ 
/*  99 */     if (foldersArray != null) {
//    Logger.logDebug(this, methodName, request, "number of folders = " + foldersArray.size());
/* 101 */       ListIterator iterator = foldersArray.listIterator();
/* 102 */       while (iterator.hasNext()) {
/* 103 */         object = (JSONObject)iterator.next();
/* 104 */         String id = (String)object.get("id");
//       Logger.logDebug(this, methodName, request, "id = " + id);
//      Logger.logDebug(this, methodName, request, "objectId = " + objectId);
/* 107 */         if (id.equals(objectId)) {
/* 108 */           return object;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 113 */     return object;
/*     */   }
/*     */ 
/*     */   public void cleanUpFolderDataRefs(TeamspaceDB teamspaceDB)
/*     */   {
/* 118 */     String methodName = "validateFolderData";
//   Logger.logEntry(this, methodName, request);
/* 120 */     JSONArray foldersArray = teamspaceDB.getFolders();
//    Logger.logDebug(this, methodName, request, "foldersArray = " + foldersArray);
/* 122 */     JSONArray jsonArrayOfInvalidItems = getInvalidFoldersandDocs();
/*     */ 
/* 124 */     if (foldersArray != null) {
//      Logger.logDebug(this, methodName, request, "number of folders = " + foldersArray.size());
/* 126 */       ListIterator iterator = foldersArray.listIterator();
/* 127 */       while (iterator.hasNext()) {
/* 128 */         JSONObject object = (JSONObject)iterator.next();
/* 129 */         String id = (String)object.get("id");
/* 130 */         String type = (String)object.get("type");
/* 131 */         String name = (String)object.get("name");
//       Logger.logDebug(this, methodName, request, "name = " + name + " :type = " + type);
/* 133 */         if ((type == null) || (type.equals("folder")))
/*     */         {
/* 135 */           ListIterator invalidItemsiterator = jsonArrayOfInvalidItems.listIterator();
/* 136 */           while (invalidItemsiterator.hasNext()) {
/* 137 */             JSONObject invalidItem = (JSONObject)invalidItemsiterator.next();
/* 138 */             String invalidItemId = (String)invalidItem.get("id");
//           Logger.logDebug(this, methodName, request, "invalidItemId = " + invalidItemId);
/* 140 */             JSONArray children = null;
/* 141 */             if ((object.get("children") instanceof JSONArray)) {
//             Logger.logDebug(this, methodName, request, "has array of children");
/* 143 */               children = (JSONArray)object.get("children");
/* 144 */               ListIterator referencesIterator = children.listIterator();
/* 145 */               while (referencesIterator.hasNext()) {
/* 146 */                 JSONObject reference = (JSONObject)referencesIterator.next();
/* 147 */                 String _value = (String)reference.get("_reference");
//              Logger.logDebug(this, methodName, request, "remove: " + _value);
/* 149 */                 if ((_value != null) && (_value.equals(invalidItemId)))
/* 150 */                   referencesIterator.remove();
/*     */               }
/*     */             }
/*     */             else
/*     */             {
//              Logger.logDebug(this, methodName, request, "no instance of array");
/* 156 */               JSONObject reference = (JSONObject)object.get("children");
/* 157 */               if (reference != null) {
/* 158 */                 String _value = (String)reference.get("_reference");
//                Logger.logDebug(this, methodName, request, "remove: " + _value);
/* 160 */                 if ((_value != null) && (_value.equals(invalidItemId))) {
/* 161 */                   object.put("children", new JSONArray());
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 169 */     teamspaceDB.setFolders(foldersArray);
/*     */   }
/*     */ }
