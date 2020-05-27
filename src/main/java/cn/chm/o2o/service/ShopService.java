package cn.chm.o2o.service;

import cn.chm.o2o.dto.ShopExecution;
import cn.chm.o2o.entity.Shop;
import cn.chm.o2o.exceptions.ShopOperationException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

public interface ShopService {

    /**
     * 根据shopCondition分页，返回相应的店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    /**
     * 通过shopId获取Shop实体类对象
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息
     * @param shop
     * @param shopImg
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) throws ShopOperationException;

    /**
     * 注册店铺
     * @param shop
     * @param shopImg
     * @return
     */
    ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) throws ShopOperationException;
}
