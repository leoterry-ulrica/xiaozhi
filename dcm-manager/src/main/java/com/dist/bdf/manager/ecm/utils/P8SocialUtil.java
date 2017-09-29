package com.dist.bdf.manager.ecm.utils;

import java.util.Iterator;
import java.util.Map;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;

public abstract class P8SocialUtil {

	protected abstract IndependentObject retrieveSummaryData(ObjectStore os, Id documentId);
	
	/**
	 * 添加social数据
	 * @param os
	 * @param className
	 * @param properties
	 * @return
	 */
	public CmAbstractPersistable addSocialDatum(ObjectStore os, String className, String documentId, Map<String, Object> properties) {
		
		properties.put("ClbDocumentId", new Id(documentId));
		CmAbstractPersistable datum = Factory.CmAbstractPersistable.createInstance(os, className);// DistSummaryData
		saveProperties(datum, properties, true);
		// ClbDocumentId
		return datum;
	}
	
	/**
	 * 编辑social数据
	 * @param os
	 * @param id
	 * @param className
	 * @param properties
	 * @return
	 */
	public CmAbstractPersistable editSocialDatum(ObjectStore os, String id, String className,
			Map<String, Object> properties) {
		CmAbstractPersistable datum = Factory.CmAbstractPersistable.fetchInstance(os, className, new Id(id), null);
		saveProperties(datum, properties, true);

		return datum;
	}
	/**
	 * 
	 * @param datum
	 * @param properties
	 * @return
	 */
	public CmAbstractPersistable editSocialDatum(CmAbstractPersistable datum, Map<String, Object> properties) {

		saveProperties(datum, properties, true);

		return datum;
	}

	/**
	 * 保存具体属性
	 * @param datum
	 * @param properties
	 * @param save
	 */
	protected void saveProperties(CmAbstractPersistable datum, Map<String, Object> properties, boolean save) {
		if (properties != null) {
			Iterator i = properties.keySet().iterator();
			while (i.hasNext()) {
				String name = (String) i.next();
				datum.getProperties().putObjectValue(name, properties.get(name));
			}
		}
		if (save)
			datum.save(RefreshMode.REFRESH);
	}

}
