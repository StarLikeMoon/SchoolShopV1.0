package cn.chm.o2o.dao;

import cn.chm.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺中商品类别的Dao层
 */
public interface ProductCategoryDao {

    /**
     * 根据shopid返回店铺商品类别
     *
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 商品类别的批量添加
     *
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategopry(List<ProductCategory> productCategoryList);

    int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);

}
