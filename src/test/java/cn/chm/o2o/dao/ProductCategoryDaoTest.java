package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.ProductCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryDaoTest extends BaseTest {

    @Autowired
    ProductCategoryDao productCategoryDao;

    @Test
    @Ignore
    public void testQueryProductCategoryList() {
        long shopId = 1;
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println(productCategories.size());
    }

    @Test
    public void testBatchInsertProductCategopry() {
        ProductCategory productCategory1 = new ProductCategory();
        ProductCategory productCategory2 = new ProductCategory();
        productCategory1.setProductCategoryName("绿茶");
        productCategory1.setPriority(10);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        productCategory2.setProductCategoryName("红茶");
        productCategory2.setPriority(5);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);
        ArrayList<ProductCategory> productCategories = new ArrayList<>();
        productCategories.add(productCategory1);
        productCategories.add(productCategory2);
        productCategoryDao.batchInsertProductCategopry(productCategories);
    }

}
