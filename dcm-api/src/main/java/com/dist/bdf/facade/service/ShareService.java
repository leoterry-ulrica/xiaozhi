
package com.dist.bdf.facade.service;

import com.dist.bdf.model.dto.system.ShareParaDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;

/**
 * @author weifj
 * @version 1.0，2016/03/12，weifj
 *    1. 创建共享服务接口
 *
 */
public interface ShareService {

	/**
	 * 添加资源共享
	 * @param shareDto dto对象
	 * @return
	 */
    /* public DcmShare addResourceShare(ShareDTO shareDto);*/
     /**
      * 共享出去
      * @param json
      * @return
      */
    Object addResourceShare(ShareParaDTO shareDto);
     /**
      * 删除资源共享
      * @param shareDto
      */
     void delResourceShare(ShareParaDTO shareDto);
     /**
      * 检测共享信息，把已经过期的信息自动设置状态（0）
      * @return
      */
     Object checkShareInfo();
     /**
      * 分页获取个人共享出去的资源
      * @param pageInfo
      * @return
      */
     Object getPersonalShare(PageSimple pageInfo);
     /**
      *  分页获取个人共享出去的资源，不单单是资源id，包括了资源其它属性信息
      * @param pageInfo
      * @return
      */
     Object getPersonalShareWholeInfo(PageSimple pageInfo);
     /**
      * 获取别人共享给我的信息
      * @param pageInfo
      * @return
      */
     Object getSharedInfoByOthers(PageSimple pageInfo);
     /**
      * 分页获取共享给我的资源，不单单是资源id，包括了资源其它属性信息
      * @param dto
      * @return
      */
	Object getSharedWholeInfoByOthers(PageSimple pageInfo);
     
}
