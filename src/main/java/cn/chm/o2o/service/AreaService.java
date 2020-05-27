package cn.chm.o2o.service;

import cn.chm.o2o.entity.Area;

import java.util.List;

public interface AreaService {

    // 缓存进Redis的Key
    public static final String AREALISTKEY = "arealist";

    List<Area> getAreaLsit();
}
