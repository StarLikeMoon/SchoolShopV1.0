package cn.chm.o2o.service;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class AreaServiceTest extends BaseTest {
    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList(){
        List<Area> areaLsit = areaService.getAreaLsit();
        System.out.println(areaLsit.size());
    }

}
