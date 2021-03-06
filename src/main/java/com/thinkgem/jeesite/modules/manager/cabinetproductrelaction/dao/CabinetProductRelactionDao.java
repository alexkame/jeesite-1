/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.manager.cabinetproductrelaction.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.manager.cabinetproductrelaction.entity.CabinetProductRelaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 柜子商品关系配置DAO接口
 *
 * @author yt
 * @version 2017-08-19
 */
@MyBatisDao
public interface CabinetProductRelactionDao extends CrudDao<CabinetProductRelaction> {

    CabinetProductRelaction findBydrawerIdAndProductId(@Param("cabinetNo")String cabinetNo,@Param("drawerId") String drawerId, @Param("productId") String productId);

    CabinetProductRelaction findByDrawerNoAndProductId(@Param("cabinetNo")String cabinetNo,@Param("drawerNo") String drawerNo, @Param("productId") String productId);


    List<CabinetProductRelaction> findListByDrawerNo(@Param("cabinetNo") String cabinetNo,@Param("drawerNo") String drawerNo);

    Integer deleteByDrawerNo(@Param("cabinetNo") String cabinetNo,@Param("drawerNo") String drawerNo);


    List<CabinetProductRelaction> getSaleProductByCabinetId(@Param("cabinetId") String cabinetId);


    List<CabinetProductRelaction> findListByCabinetIdAndProductId(@Param("productId") String productId, @Param("cabinetId") String cabinetId);

    CabinetProductRelaction findByCabinetIdAndDrawerId(@Param("cabinetId") String cabinetId, @Param("drawerId") String drawerId);

    Integer updateProduct(@Param("id") String id, @Param("productId") String productId, @Param("productName") String productName);

}