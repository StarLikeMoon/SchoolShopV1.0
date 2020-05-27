package cn.chm.o2o.entity;

import java.util.Date;

/**
 * 商品详情图片实体类
 */
public class ProductImg {

    private Long productImgId;
    private String productImgAddr;
    private String productImgDesc;
    private Integer priority;
    private Date createTime;
    private Long productId;

    public Long getProductImgId() {
        return productImgId;
    }

    public void setProductImgId(Long productImgId) {
        this.productImgId = productImgId;
    }

    public String getProductImgAddr() {
        return productImgAddr;
    }

    public void setProductImgAddr(String productImgAddr) {
        this.productImgAddr = productImgAddr;
    }

    public String getProductImgDesc() {
        return productImgDesc;
    }

    public void setProductImgDesc(String productImgDesc) {
        this.productImgDesc = productImgDesc;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
