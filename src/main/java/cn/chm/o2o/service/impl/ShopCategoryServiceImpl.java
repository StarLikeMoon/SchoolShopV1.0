package cn.chm.o2o.service.impl;

import cn.chm.o2o.cache.RedisUtil;
import cn.chm.o2o.dao.ShopCategoryDao;
import cn.chm.o2o.dto.ShopCategoryExecution;
import cn.chm.o2o.entity.Area;
import cn.chm.o2o.entity.HeadLine;
import cn.chm.o2o.entity.ShopCategory;
import cn.chm.o2o.enums.ShopCategoryStateEnum;
import cn.chm.o2o.exceptions.AreaOperationException;
import cn.chm.o2o.service.ShopCategoryService;
import cn.chm.o2o.util.ImageUtil;
import cn.chm.o2o.util.PathUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SCLISTKEY;
        List<ShopCategory> shopCategoryList = null;
        // 将list转换成string，利用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // 如果传入的shopCategoryCondition为空，说明是首页的请求，返回所有根类
        if (shopCategoryCondition == null) {
            key = key + "_allfirstlevel";
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            // 返回指定的parent下面所有子类
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition != null) {
            // 如果没有指定parent，则列出所有子类别
            key = key + "_allsecondlevel";
        }
        // 判断redis是否存在这个key
        if (!redisUtil.containsKey(key)) {
            // 如果不存在这个缓存,就从数据库取
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            // 转换成string缓存
            String jsonValue = null;
            try {
                jsonValue = objectMapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("Json转换失败" + e.toString());
                // 由于开启了事务，这里需要抛异常来触发回滚
                throw new AreaOperationException(e.getMessage());
            }
            // 没有问题就缓存
            redisUtil.cacheValue(key, jsonValue);
        } else {
            // 如果缓存中存在
            String jsonValue = redisUtil.getValue(key);
            // 定义要将json转换成的类型
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = objectMapper.readValue(jsonValue, javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("Json转换失败" + e.toString());
                // 由于开启了事务，这里需要抛异常来触发回滚
                throw new AreaOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }

    @Override
    @Transactional
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, CommonsMultipartFile thumbnail) {
        if (shopCategory != null) {
            shopCategory.setCreateTime(new Date());
            shopCategory.setLastEditTime(new Date());
            if (thumbnail != null) {
                addThumbnail(shopCategory, thumbnail);
            }
            try {
                int effectedNum = shopCategoryDao
                        .insertShopCategory(shopCategory);
                if (effectedNum > 0) {
                    // 更新了shopCategory需要清空缓存，保证缓存更新
                    redisUtil.removeKeys(SCLISTKEY);
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
                } else {
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("添加店铺类别信息失败:" + e.toString());
            }
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    private void addThumbnail(ShopCategory shopCategory, CommonsMultipartFile thumbnail) {
        String dest = PathUtil.getShopCategoryImagePath();
        String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
        shopCategory.setShopCategoryImg(thumbnailAddr);
    }
}
