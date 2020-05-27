package cn.chm.o2o.dao;

import cn.chm.o2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {

    /**
     * 查询商品详情图
     *
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);

    /**
     * 批量插入商品缩略图
     *
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 根据商品id删除商品的图片
     *
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

}
