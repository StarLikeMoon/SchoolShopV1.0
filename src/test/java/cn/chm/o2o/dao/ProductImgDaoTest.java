package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.ProductImg;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ProductImgDaoTest extends BaseTest {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    @Ignore
    public void testBatchInsertProductImg() {
        List<ProductImg> productImgList = new ArrayList<>();
        ProductImg productImg1 = new ProductImg();
        productImg1.setProductImgAddr("图片1地址");
        productImg1.setProductImgDesc("图片1测试");
        productImg1.setProductId(1L);
        ProductImg productImg2 = new ProductImg();
        productImg2.setProductImgAddr("图片2地址");
        productImg2.setProductImgDesc("图片2测试");
        productImg2.setProductId(1L);
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        productImgDao.batchInsertProductImg(productImgList);
    }

    @Test
    @Ignore
    public void testDeleteProductImgByProductId() {
        productImgDao.deleteProductImgByProductId(1L);
    }

    @Test
    public void testQueryProductImgList() {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(2L);
        System.out.println(productImgList.size());
    }
}
