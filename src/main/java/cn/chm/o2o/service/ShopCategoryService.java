package cn.chm.o2o.service;

import cn.chm.o2o.dto.ShopCategoryExecution;
import cn.chm.o2o.entity.ShopCategory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface ShopCategoryService {

    public static final String SCLISTKEY = "shopcategorylist";

    /**
     * 查询类别列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);


    /**
     * 添加商铺类别
     * @param shopCategory
     * @param thumbnail
     * @return
     */
    ShopCategoryExecution addShopCategory(ShopCategory shopCategory,
                                          CommonsMultipartFile thumbnail);
}
