package com.dist.bdf.manager.ecm.teamspace.p8;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.collection.BooleanList;
import com.filenet.api.collection.DateTimeList;
import com.filenet.api.collection.Float64List;
import com.filenet.api.collection.IdList;
import com.filenet.api.collection.Integer32List;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Containable;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.util.Id;
import com.ibm.ecm.util.DateUtil;
import com.ibm.ecm.util.p8.P8Connection;
import com.ibm.ecm.util.p8.P8Util;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DistP8Util extends P8Util {

	private static boolean compareListProperties(List newValues, List currentValues) {
		if (newValues.size() != currentValues.size()) {
			return false;
		}
		for (int index = 0; index < newValues.size(); index++) {
			if (!newValues.get(index).equals(currentValues.get(index))) {
				return false;
			}
		}
		return true;
	}

	public static IndependentlyPersistableObject getPersistableObject(ObjectStore os, P8Connection connection,
			String docId) {

		return os != null ? getPersistableObject(os, connection, docId) : null;
	}

	public static void setProperties(ObjectStore os, P8Connection connection, IndependentObject item,
			JSONArray criterias, ClassDescription classDescription, ModifyType modifyType) throws Exception {
		String methodName = "setProperties";

		JSONObject criteria = null;

		PropertyDescriptionList propertyDescriptionList = classDescription.get_PropertyDescriptions();

		Map propDescsMap = new HashMap();
		PropertyDescription propertyDescription = null;
		for (int i = 0; i < propertyDescriptionList.size(); i++) {
			propertyDescription = (PropertyDescription) propertyDescriptionList.get(i);
			propDescsMap.put(propertyDescription.get_SymbolicName(), propertyDescription);
		}

		// Logger.logDebug(P8Util.class, methodName, request, "criterias.size()"
		// + criterias.size());
		// Logger.logDebug(P8Util.class, methodName, request, "adding the
		// attribute values for the newly created item");

		for (int iCriteriaCounter = 0; iCriteriaCounter < criterias.size(); iCriteriaCounter++) {
			criteria = (JSONObject) criterias.get(iCriteriaCounter);
			String criteriaName = criteria.get("name").toString();

			JSONArray valuesJsonArray = null;

			Object valuesJson = criteria.get("value");

			if (valuesJson == null) {
				valuesJson = criteria.get("values");
			}

			if (valuesJson != null) {
				if ((valuesJson instanceof JSONArray)) {
					valuesJsonArray = (JSONArray) valuesJson;
				} else {
					valuesJsonArray = new JSONArray();
					valuesJsonArray.add(valuesJson.toString());
				}
			} else
				valuesJsonArray = new JSONArray();

			String criteriaValue = (valuesJsonArray.isEmpty()) || (valuesJsonArray.get(0) == null) ? ""
					: valuesJsonArray.get(0).toString();

			// Logger.logDebug(P8Util.class, methodName, request, "update Item:
			// attrName=" + criteriaName + " attrValue=" + criteriaValue);
			propertyDescription = (PropertyDescription) propDescsMap.get(criteriaName);

			// Logger.logDebug(P8Util.class, methodName, request,
			// "criteriaName=" + criteriaName + " criteriaValue=" +
			// criteriaValue);
			Properties itemProperties = item.getProperties();
			if ((criteriaName.equalsIgnoreCase("FolderName")) && ((item instanceof Folder))) {
				if (modifyType == ModifyType.EDIT) {
					Property folderNameProperty = itemProperties.get("FolderName");
					String folderNameValue = folderNameProperty.getStringValue();
					if (!folderNameValue.equals(criteriaValue))
						((Folder) item).set_FolderName(criteriaValue);
				} else {
					((Folder) item).set_FolderName(criteriaValue);
				}
			} else if ((propertyDescription != null) && (!propertyDescription.get_IsReadOnly().booleanValue())
					&& (criteriaValue != null)) {
				boolean modifiable = true;
				if (modifyType == ModifyType.CHECKIN) {
					PropertySettability propertySettability = propertyDescription.get_Settability();
					if (propertySettability == PropertySettability.SETTABLE_ONLY_ON_CREATE) {
						// Logger.logDebug(P8Util.class, methodName, request,
						// "Property settable only on create.");
						modifiable = false;
					}
				}
				String propertyName = propertyDescription.get_SymbolicName();
				boolean isPresent = itemProperties.isPropertyPresent(propertyName);
				boolean isModified = !isPresent;

				if ((modifiable) && (isPresent)) {
					modifiable = itemProperties.get(propertyName).isSettable();
				}
				// Logger.logDebug(P8Util.class, methodName, request, "Property
				// modifiable = " + modifiable);

				if (modifiable) {
					TypeID type = propertyDescription.get_DataType();
					Cardinality cardinality = propertyDescription.get_Cardinality();

					if (cardinality.getValue() == 2) {
						ChoiceList choiceList = propertyDescription.get_ChoiceList();
						if (choiceList != null) {
							if (choiceList.get_DataType().equals(TypeID.STRING)) {
								StringList list = Factory.StringList.createList();
								if (criteriaValue.length() > 0) {
									for (int j = 0; j < valuesJsonArray.size(); j++) {
										String value = (String) valuesJsonArray.get(j);
										list.add(value);
									}
								}

								if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
									if (isPresent) {
										StringList currentValues = itemProperties.getStringListValue(propertyName);
										isModified = !compareListProperties(list, currentValues);
									}

									if (isModified) {
										if (criteriaValue.length() < 1)
											itemProperties.putObjectValue(propertyName, null);
										else {
											itemProperties.putObjectValue(propertyName, list);
										}
									}
								} else if (criteriaValue.length() < 1) {
									itemProperties.putObjectValue(propertyName, null);
								} else {
									itemProperties.putObjectValue(propertyName, list);
								}
							} else if (choiceList.get_DataType().equals(TypeID.LONG)) {
								Integer32List list = Factory.Integer32List.createList();
								if (criteriaValue.length() > 0) {
									for (int j = 0; j < valuesJsonArray.size(); j++) {
										if (valuesJsonArray.get(j) != null) {
											String value = valuesJsonArray.get(j).toString();
											list.add(new Integer(value));
										}
									}
								}

								if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
									if (isPresent) {
										Integer32List currentValues = itemProperties
												.getInteger32ListValue(propertyName);
										isModified = !compareListProperties(list, currentValues);
									}

									if (isModified) {
										if (criteriaValue.length() < 1)
											itemProperties.putObjectValue(propertyName, null);
										else {
											itemProperties.putObjectValue(propertyName, list);
										}
									}
								} else if (criteriaValue.length() < 1) {
									itemProperties.putObjectValue(propertyName, null);
								} else {
									itemProperties.putObjectValue(propertyName, list);
								}
							}

						} else if (type == TypeID.BOOLEAN) {
							BooleanList list = Factory.BooleanList.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(new Boolean(value));
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									BooleanList currentValues = itemProperties.getBooleanListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}
						} else if (type == TypeID.DATE) {
							DateTimeList list = Factory.DateTimeList.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(DateUtil.parseISODate(value, null));
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									DateTimeList currentValues = itemProperties.getDateTimeListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}
						} else if (type == TypeID.LONG) {
							Integer32List list = Factory.Integer32List.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(new Integer(value));
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									Integer32List currentValues = itemProperties.getInteger32ListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}
						} else if (type == TypeID.DOUBLE) {
							Float64List list = Factory.Float64List.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(new Double(value));
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									Float64List currentValues = itemProperties.getFloat64ListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}
						} else if (type == TypeID.GUID) {
							IdList list = Factory.IdList.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(new Id(value));
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									IdList currentValues = itemProperties.getIdListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}
						} else if (type != TypeID.OBJECT) {
							StringList list = Factory.StringList.createList();
							if (criteriaValue.length() > 0) {
								for (int j = 0; j < valuesJsonArray.size(); j++) {
									if (valuesJsonArray.get(j) != null) {
										String value = valuesJsonArray.get(j).toString();
										list.add(value);
									}
								}
							}
							if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
								if (isPresent) {
									StringList currentValues = itemProperties.getStringListValue(propertyName);
									isModified = !compareListProperties(list, currentValues);
								}

								if (isModified) {
									if (criteriaValue.length() < 1)
										itemProperties.putObjectValue(propertyName, null);
									else {
										itemProperties.putObjectValue(propertyName, list);
									}
								}
							} else if (criteriaValue.length() < 1) {
								itemProperties.putObjectValue(propertyName, null);
							} else {
								itemProperties.putObjectValue(propertyName, list);
							}

						}

					} else if ((type != TypeID.OBJECT) && (criteriaValue.length() < 1)) {
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Object value = property.getObjectValue();
							if (value != null)
								itemProperties.putObjectValue(propertyName, null);
						} else {
							itemProperties.putObjectValue(propertyName, null);
						}
					} else if (type == TypeID.DATE) {
						Date value = DateUtil.parseISODate(criteriaValue, null);
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Date propertyValue = property.getDateTimeValue();
							if (!value.equals(propertyValue))
								itemProperties.putObjectValue(propertyName, value);
						} else {
							itemProperties.putObjectValue(propertyName, value);
						}
					} else if (type == TypeID.DOUBLE) {
						Double value = new Double(criteriaValue);
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Double propertyValue = property.getFloat64Value();
							if (!value.equals(propertyValue))
								itemProperties.putObjectValue(propertyName, value);
						} else {
							itemProperties.putObjectValue(propertyName, value);
						}
					} else if (type == TypeID.LONG) {
						Integer value = new Integer(criteriaValue);
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Integer propertyValue = property.getInteger32Value();
							if (!value.equals(propertyValue))
								itemProperties.putObjectValue(propertyName, value);
						} else {
							itemProperties.putObjectValue(propertyName, value);
						}
					} else if (type == TypeID.GUID) {
						Id value = new Id(criteriaValue);
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Id propertyValue = property.getIdValue();
							if (!value.equals(propertyValue))
								itemProperties.putObjectValue(propertyName, value);
						} else {
							itemProperties.putObjectValue(propertyName, value);
						}
					} else if (type == TypeID.OBJECT) {
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Object propertyValue = property.getObjectValue();

							String id = "";

							if (propertyValue != null) {
								if ((propertyValue instanceof IndependentlyPersistableObject))
									id = ((IndependentlyPersistableObject) propertyValue).getObjectReference()
											.getObjectIdentity();
								else if ((propertyValue instanceof Containable)) {
									id = ((Containable) propertyValue).get_Id().toString();
								}
							}
							if (!id.equals(getObjectIdentity(criteriaValue))) {
								IndependentlyPersistableObject value = null;
								if ((criteriaValue != null) && (!criteriaValue.isEmpty())) {
									value = getPersistableObject(os, connection, criteriaValue);
								}
								itemProperties.putObjectValue(propertyName, value);
							}
						} else {
							IndependentlyPersistableObject value = null;
							if ((criteriaValue != null) && (!criteriaValue.isEmpty())) {
								value = getPersistableObject(os, connection, criteriaValue);

								itemProperties.putObjectValue(propertyName, value);
							}
						}
					} else if (type == TypeID.BOOLEAN) {
						Boolean value = new Boolean(criteriaValue);
						if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
							Property property = itemProperties.get(propertyName);
							Boolean propertyValue = property.getBooleanValue();
							if (!value.equals(propertyValue))
								itemProperties.putObjectValue(propertyName, value);
						} else {
							itemProperties.putObjectValue(propertyName, value);
						}
					} else if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
						Property property = itemProperties.get(propertyName);
						String propertyValue = property.getStringValue();
						if (!criteriaValue.equals(propertyValue))
							itemProperties.putObjectValue(propertyName, criteriaValue);
					} else {
						itemProperties.putObjectValue(propertyName, criteriaValue);
					}
				}

			}

		}

	}
}
