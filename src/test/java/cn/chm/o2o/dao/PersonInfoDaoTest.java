package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PersonInfoDaoTest extends BaseTest {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testinsert() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("张三");
        personInfo.setEmail("test");
        personInfo.setGender("男");
        personInfo.setUserType(2);
        personInfo.setEnableStatus(1);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfoDao.insertPersonInfo(personInfo);
    }

}
