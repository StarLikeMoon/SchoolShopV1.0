package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.Product;
import cn.chm.o2o.entity.ProductCategory;
import cn.chm.o2o.entity.Shop;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @Ignore
    public void testInsertProduct() {

        Product product = new Product();
        product.setProductName("奶茶");
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);
        product.setEnableStatus(1);
        productDao.insertProduct(product);
    }

    @Test
    @Ignore
    public void testQueryProductByProductId() {
        Product product = productDao.queryProductByProductId(1L);
        System.out.println(product.getProductName());
    }

    @Test
    @Ignore
    public void testUpdateProduct() {
        Product product = new Product();
        product.setProductName("奶茶更新");
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);
        product.setProductId(1L);
        productDao.updateProduct(product);

    }

    @Test
    @Ignore
    public void testQueryProductList() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        Product product = new Product();
        product.setShop(shop);
        List<Product> products = productDao.queryProductLsit(product, 0, 5);
        System.out.println(products.size());
    }

    @Test
    @Ignore
    public void testQueryProductCount() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        Product product = new Product();
        product.setShop(shop);
        int i = productDao.queryProductCount(product);
        System.out.println(i);
    }

    @Test
    public void testUpdateProductCategoryToNull() {
        productDao.updateProductCategoryToNull(4L);
    }
}
