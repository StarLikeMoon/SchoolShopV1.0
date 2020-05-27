package cn.chm.o2o.dao;

import cn.chm.o2o.entity.PersonInfo;

public interface PersonInfoDao {

    /**
     * 通过userId来查询用户信息
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户
     *
     * @param
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);

    /**
     * 修改用户信息
     *
     * @param
     * @return
     */
    int updatePersonInfo(PersonInfo personInfo);

}
