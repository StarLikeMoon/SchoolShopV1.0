package cn.chm.o2o.service.impl;

import cn.chm.o2o.cache.RedisUtil;
import cn.chm.o2o.dao.AreaDao;
import cn.chm.o2o.entity.Area;
import cn.chm.o2o.exceptions.AreaOperationException;
import cn.chm.o2o.service.AreaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    @Transactional
    public List<Area> getAreaLsit() {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        // 将list转换成string，利用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        if (!redisUtil.containsKey(key)) {
            // 如果不存在这个缓存,就从数据库取
            areaList = areaDao.queryArea();
            // 转换成string缓存
            String jsonValue = null;
            try {
                jsonValue = objectMapper.writeValueAsString(areaList);
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
                    .constructParametricType(ArrayList.class, Area.class);
            try {
                areaList = objectMapper.readValue(jsonValue, javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("Json转换失败" + e.toString());
                // 由于开启了事务，这里需要抛异常来触发回滚
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }
}
