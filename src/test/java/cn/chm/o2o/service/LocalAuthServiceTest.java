package cn.chm.o2o.service;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.LocalAuth;
import cn.chm.o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalAuthServiceTest extends BaseTest {
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void test(){
        String username = "newAuth";
        String password = "test";
        String newPassword = "newtest";
        localAuthService.modifyLocalAuth(2L,username,password,newPassword);

    }

}
