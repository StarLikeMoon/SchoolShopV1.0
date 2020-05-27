package cn.chm.o2o.dao;

import cn.chm.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    /**
     * 返回queryShopList中商铺的总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);

    /**
     * 分页查询店铺，可输入的条件有:店铺名（模糊），店铺状态，店铺类别，区域id，owner
     * 加入Param是因为参数有好几个
     * @param shopCondition
     * @param rowIndex  从第几行取
     * @param pageSize  取几行
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     * 通过店铺的id查询店铺信息
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);

    /**
     * 新增店铺功能
     * @param shop
     * @return 影响的行数
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return 影响的行数
     */
    int updateShop(Shop shop);
}
