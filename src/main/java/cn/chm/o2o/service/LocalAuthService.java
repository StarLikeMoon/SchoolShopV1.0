package cn.chm.o2o.service;

import cn.chm.o2o.dto.LocalAuthExecution;
import cn.chm.o2o.entity.LocalAuth;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 本地账号系统的Service层
 */
public interface LocalAuthService {
    /**
     * 通过账号密码获取账号信息
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth getLocalAuthByUsernameAndPws(String username, String password);

    /**
     * 通过userId获取账号信息
     *
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 注册本地账号
     *
     * @param localAuth
     * @return
     */
    LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg);

    /**
     * 修改本地账号的密码
     *
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username,
                                       String password, String newPassword);
}
