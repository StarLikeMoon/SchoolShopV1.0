package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.LocalAuth;
import cn.chm.o2o.entity.PersonInfo;
import org.apache.ibatis.session.LocalCacheScope;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LocalAuthDaoTest extends BaseTest {
    @Autowired
    private LocalAuthDao localAuthDao;

    @Test
    @Ignore
    public void testQueryLocalByUserNameAndPwd(){
        String username = "testusername";
        String password = "testpassword";
        String newPassword = "newpassword";
        localAuthDao.updateLocalAuth(1L, username, password, newPassword, new Date());
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, newPassword);
        System.out.println(localAuth.getPersonInfo().getUserId());

    }

    @Test
    public void testInsertLocalAuth(){
        String username = "newAuth";
        String password = "test";
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(2L);
        localAuth.setPersonInfo(personInfo);
        localAuthDao.insertLocalAuth(localAuth);
        LocalAuth localAuth2 = localAuthDao.queryLocalByUserNameAndPwd(username, password);
        System.out.println(localAuth2.getPersonInfo().getUserId());
    }

}
