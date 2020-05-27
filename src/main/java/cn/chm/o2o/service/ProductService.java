package cn.chm.o2o.service;

import cn.chm.o2o.dto.ProductExecution;
import cn.chm.o2o.entity.Product;
import cn.chm.o2o.exceptions.ProductOperationException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface ProductService {

    /**
     * 添加商品及其图片
     *
     * @param product     实体类
     * @param thumbnail   商品缩略图
     * @param productImgs 商品详情图片
     * @return
     * @throws RuntimeException
     */
    ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs)
            throws ProductOperationException;

    /**
     * 通过productId获取商品详情
     *
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 更新商品信息以及图片处理
     *
     * @param product
     * @param thumbnail
     * @param productImgs
     * @return
     * @throws RuntimeException
     */
    ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
                                   List<CommonsMultipartFile> productImgs) throws ProductOperationException;

    /**
     * 获取指定条件的商品列表
     *
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
