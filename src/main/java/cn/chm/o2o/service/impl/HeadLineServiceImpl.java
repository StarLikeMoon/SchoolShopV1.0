package cn.chm.o2o.service.impl;

import cn.chm.o2o.cache.RedisUtil;
import cn.chm.o2o.dao.HeadLineDao;
import cn.chm.o2o.entity.Area;
import cn.chm.o2o.entity.HeadLine;
import cn.chm.o2o.exceptions.AreaOperationException;
import cn.chm.o2o.service.HeadLineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        String key = HLLISTKEY;
        List<HeadLine> headLineList = null;
        // 将list转换成string，利用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // 拼接出redis的Key,取出被禁用的或者可用的列表
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        // 判断redis是否存在这个key
        if (!redisUtil.containsKey(key)) {
            // 如果不存在这个缓存,就从数据库取
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            // 转换成string缓存
            String jsonValue = null;
            try {
                jsonValue = objectMapper.writeValueAsString(headLineList);
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
                    .constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = objectMapper.readValue(jsonValue, javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("Json转换失败" + e.toString());
                // 由于开启了事务，这里需要抛异常来触发回滚
                throw new AreaOperationException(e.getMessage());
            }
        }
        return headLineList;
    }
}
