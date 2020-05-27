package cn.chm.o2o.service.impl;

import cn.chm.o2o.dao.ShopDao;
import cn.chm.o2o.dto.ShopExecution;
import cn.chm.o2o.entity.Shop;
import cn.chm.o2o.enums.ShopStateEnum;
import cn.chm.o2o.exceptions.ShopOperationException;
import cn.chm.o2o.service.ShopService;
import cn.chm.o2o.util.ImageUtil;
import cn.chm.o2o.util.PageCalculator;
import cn.chm.o2o.util.PathUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;
import java.util.List;

// Service层实现类，负责业务逻辑
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    /**
     * 根据shopCondition分页，返回相应的店铺列表
     *
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        // 将页码转换为数据库的行
        int rowIndex = PageCalculator.calulateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList != null) {
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        } else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    /**
     * Service层，通过shopId获取到shop信息的方法
     *
     * @param shopId
     * @return
     */
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    /**
     * Service层的修改店铺
     *
     * @param shop    前端传回的shop信息
     * @param shopImg 前端传回的shop缩略图
     * @return
     * @throws ShopOperationException
     */
    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg)
            throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                // 1.判断是否需要处理图片
                if (shopImg != null) {
                    // 先取到shop的原本数据
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    // 判断原本是否有缩略图，原本就有就需要删除旧的
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    // 删除之后再重新添加
                    addShopImg(shop, shopImg);
                }
                // 2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error: " + e.getMessage());
            }
        }
    }

    /**
     * Service层的添加店铺
     *
     * @param shop    前端用户输入的shop信息
     * @param shopImg 前端用户上传的shop图片
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) {
        // 空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            // 初始化店铺参数,0审核中
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            // 向数据库中添加店铺
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                // 抛出RuntimeException或者继承自RuntimeException的异常，事务才会回滚
                throw new ShopOperationException("店铺创建失败");
            } else {
                // 添加成功后,存储图片
                if (shopImg != null) {
                    try {
                        addShopImg(shop, shopImg);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error" + e.getMessage());
                    }
                    // 更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
        // 获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        // 存储图片并返回图片新的路径
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
        shop.setShopImg(shopImgAddr);
    }
}
