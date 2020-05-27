package cn.chm.o2o.dao;

import cn.chm.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品增删改查的Dao层
 */
public interface ProductDao {

    /**
     * 查询商品列表并分页，可以多条件查询
     *
     * @param productCondition
     * @param pageSize
     * @return
     */
    List<Product> queryProductLsit(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    /**
     * 查询对应的商品总数
     *
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 根据商品id查询商品详情
     *
     * @param productId
     * @return
     */
    Product queryProductByProductId(long productId);

    /**
     * 插入商品
     *
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 删除商品类别之前，将商品类别ID置为空
     *
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);

}
