package cn.chm.o2o.service.impl;

import cn.chm.o2o.dao.ProductCategoryDao;
import cn.chm.o2o.dao.ProductDao;
import cn.chm.o2o.dto.ProductCategoryExecution;
import cn.chm.o2o.entity.ProductCategory;
import cn.chm.o2o.enums.ProductCategoryStateEnum;
import cn.chm.o2o.exceptions.ProductCategoryException;
import cn.chm.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryException {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            // 传入的值有效
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategopry(productCategoryList);
                if (effectedNum <= 0) {
                    // 类别添加失败
                    throw new ProductCategoryException("商品类别添加失败");
                } else {
                    // 添加成功
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryException("batchAddProductCategory error: " + e.getMessage());
            }
        } else {
            // 传入的值无效
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId,
                                                          long shopId)
            throws ProductCategoryException {
        // 删除商品类别之前先将该类别的商品的类别置为空
        try {
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
        } catch (Exception e) {
            throw new ProductCategoryException("updateProductCategoryToNull error: " + e.getMessage());
        }
        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new ProductCategoryException("商品类别删除失败");
            } else {
                return new ProductCategoryExecution(
                        ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryException("deleteProductCategory error: "
                    + e.getMessage());
        }
    }


}
