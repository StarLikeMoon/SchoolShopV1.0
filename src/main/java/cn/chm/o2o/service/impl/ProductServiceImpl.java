package cn.chm.o2o.service.impl;

import cn.chm.o2o.dao.ProductDao;
import cn.chm.o2o.dao.ProductImgDao;
import cn.chm.o2o.dto.ProductExecution;
import cn.chm.o2o.entity.Product;
import cn.chm.o2o.entity.ProductImg;
import cn.chm.o2o.enums.ProductStateEnum;
import cn.chm.o2o.exceptions.ProductOperationException;
import cn.chm.o2o.service.ProductService;
import cn.chm.o2o.util.ImageUtil;
import cn.chm.o2o.util.PageCalculator;
import cn.chm.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    @Transactional
    public ProductExecution addProduct(Product product,
                                       CommonsMultipartFile thumbnail,
                                       List<CommonsMultipartFile> productImgs)
            throws ProductOperationException {
        int effectedNum = 0;
        // 1. 处理缩略图，获取缩略图路径并赋值给product实体类
        // 2. 向数据库插入product，返回product的id
        // 3. 结合商品id批量处理商品详情图
        // 4. 将详情图插入数据库

        // 空值判断
        if (product != null && product.getShop() != null
                && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认直接上架
            product.setEnableStatus(1);
            // 若商品缩略图为空则不添加
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }
            try {
                effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败" + e.toString());
            }
            // 如果商品详情图不为空就添加详情图
            if (productImgs != null && productImgs.size() > 0) {
                addProductImgs(product, productImgs);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            // 商品信息为空
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    /**
     * 1.若传了新的缩略图，就先处理缩略图：原先存在就先删除旧的，再添加新的，并将路径赋值给商品
     * 2.若上传了新的详情图，操作同缩略图
     * 3.清除商品原先的所有详情图
     * 4.更新商品
     *
     * @param product
     * @param thumbnail
     * @param productImgs
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product,
                                          CommonsMultipartFile thumbnail,
                                          List<CommonsMultipartFile> productImgs)
            throws ProductOperationException {
        // 判空
        if (product != null && product.getShop() != null
                && product.getShop().getShopId() != null) {
            // 修改时间
            product.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 如果上传了新的缩略图就更新
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    // 删除旧的缩略图
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                // 添加新的缩略图
                addThumbnail(product, thumbnail);
            }
            if (productImgs != null && productImgs.size() > 0) {
                // 上传了新的详情图，先删除
                deleteProductImgs(product.getProductId());
                // 再添加
                addProductImgs(product, productImgs);
            }
            try {
                // 更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 获取指定条件的商品列表
     *
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        // 页码转换成数据库的行数
        int rowIndex = PageCalculator.calulateRowIndex(pageIndex, pageSize);
        // 调用Dao层返回结果
        List<Product> productList = productDao.queryProductLsit(productCondition, rowIndex, pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setProductList(productList);
        productExecution.setCount(count);
        return productExecution;
    }

    /**
     * 批量删除商品的所有详情图
     *
     * @param productId
     */
    private void deleteProductImgs(Long productId) {
        // 先查到所有图
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        // 循环遍历地址删除
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getProductImgAddr());
        }
        // 清理数据库信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    /**
     * 批量添加商品详情图
     *
     * @param product
     * @param productImgs
     */
    private void addProductImgs(Product product, List<CommonsMultipartFile> productImgs) {
        List<ProductImg> productImgList = null;
        int effectedNum = 0;
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<String> imgAddrList = ImageUtil.generateNormalImgs(productImgs, dest);
        if (imgAddrList != null && imgAddrList.size() > 0) {
            productImgList = new ArrayList<>();
            // 遍历图片并添加进productImg实体类
            for (String imgAddr : imgAddrList) {
                ProductImg productImg = new ProductImg();
                productImg.setProductImgAddr(imgAddr);
                productImg.setProductId(product.getProductId());
                productImg.setCreateTime(new Date());
                productImgList.add(productImg);
            }
            try {
                effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("商品详情图创建失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("商品详情图创建失败" + e.toString());
            }
        }
    }

    /**
     * 处理商品缩略图
     *
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, CommonsMultipartFile thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }

}
