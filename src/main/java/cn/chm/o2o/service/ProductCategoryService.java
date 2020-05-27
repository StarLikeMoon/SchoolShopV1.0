package cn.chm.o2o.service;

import cn.chm.o2o.dto.ProductCategoryExecution;
import cn.chm.o2o.entity.ProductCategory;
import cn.chm.o2o.exceptions.ProductCategoryException;

import java.util.List;

/**
 * 商品类别的service层
 */
public interface ProductCategoryService {

    /**
     * 查询指定商铺下的所有商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 批量添加商品类别并返回商品类别操作信息类
     * @param productCategoryList
     * @return
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryException;

    /**
     * 将该类别下的商品的类别id置为空，再删除指定的商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId,
                                                   long shopId) throws ProductCategoryException;
}
