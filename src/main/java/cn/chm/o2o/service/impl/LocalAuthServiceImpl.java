package cn.chm.o2o.service.impl;

import cn.chm.o2o.dao.LocalAuthDao;
import cn.chm.o2o.dao.PersonInfoDao;
import cn.chm.o2o.dto.LocalAuthExecution;
import cn.chm.o2o.entity.LocalAuth;
import cn.chm.o2o.entity.PersonInfo;
import cn.chm.o2o.enums.LocalAuthStateEnum;
import cn.chm.o2o.exceptions.LocalAuthOperationException;
import cn.chm.o2o.service.LocalAuthService;
import cn.chm.o2o.util.ImageUtil;
import cn.chm.o2o.util.MD5;
import cn.chm.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;


    @Override
    public LocalAuth getLocalAuthByUsernameAndPws(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) {
        // 判空，看传入的参数合不合法
        if (localAuth == null || localAuth.getPassword() == null
                || localAuth.getUsername() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            // 合法了就开始创建账号
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            // 新用户
            if (localAuth.getPersonInfo() != null
                    && localAuth.getPersonInfo().getUserId() == null) {
                if (profileImg != null) {
                    localAuth.getPersonInfo().setCreateTime(new Date());
                    localAuth.getPersonInfo().setLastEditTime(new Date());
                    localAuth.getPersonInfo().setEnableStatus(1);
                    try {
                        // 添加个人图片，并赋值地址给localAuth
                        addProfileImg(localAuth, profileImg);
                    } catch (Exception e) {
                        throw new LocalAuthOperationException("addUserProfileImg error: "
                                + e.getMessage());
                    }
                }
                try {
                    PersonInfo personInfo = localAuth.getPersonInfo();
                    int effectedNum = personInfoDao
                            .insertPersonInfo(personInfo);
                    if (effectedNum <= 0) {
                        throw new RuntimeException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("insertPersonInfo error: "
                            + e.getMessage());
                }
            }
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectedNum <= 0) {
                throw new LocalAuthOperationException("帐号创建失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,
                        localAuth);
            }
        } catch (Exception e) {
            throw new RuntimeException("insertLocalAuth error: "
                    + e.getMessage());
        }
    }

    private void addProfileImg(LocalAuth localAuth, CommonsMultipartFile profileImg) {
        String dest = PathUtil.getPersonInfoImagePath();
        String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
        localAuth.getPersonInfo().setProfileImg(profileImgAddr);
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
        // 非空判断
        if (userId != null && username != null && password != null
                && newPassword != null && !password.equals(newPassword)) {
            try {
                // 调用Dao层接口修改密码
                int effectedNum = localAuthDao.updateLocalAuth(userId,
                        username, MD5.getMd5(password),
                        MD5.getMd5(newPassword), new Date());
                if (effectedNum <= 0) {
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new LocalAuthOperationException("更新密码失败:" + e.toString());
            }
        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
