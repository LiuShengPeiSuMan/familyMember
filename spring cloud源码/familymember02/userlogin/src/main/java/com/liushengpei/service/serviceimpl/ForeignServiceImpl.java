package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.UserLoginDao;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对外提供接口
 */
@Service
public class ForeignServiceImpl implements IForeignService {

    @Autowired
    private UserLoginDao loginDao;

    /**
     * 查询登录用户
     */
    @Override
    public List<UserLogin> queryLoginList() {
        List<UserLogin> userLogins = loginDao.queryLoginList();
        return userLogins;
    }

    /**
     * 添加登录权限
     */
    @Override
    public Integer addLogin(UserLogin login) {
        Integer num = loginDao.addZzLogin(login);
        return num;
    }

    /**
     * 修改登录密码
     */
    @Override
    public Integer updatePassword(String password, String id, String updateUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("password", password);
        params.put("updateTime", new Date());
        params.put("updateUser", updateUser);
        Integer num = loginDao.updatePassword(params);
        return num;
    }

    /**
     * 解除用户登录权限
     */
    @Override
    public Integer relievePwd(String id, String updateUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("updateTime", new Date());
        params.put("updateUser", updateUser);
        Integer num = loginDao.relievePassword(params);
        return num;
    }

    /**
     * 查询登录邮箱个数
     */
    @Override
    public Integer emailCount(String email) {
        Integer count = loginDao.queryEmailCount(email);
        return count;
    }

    /**
     * 查询登录用户的详细信息
     */
    @Override
    public UserLogin queryUserLogin(String id) {
        UserLogin login = loginDao.queryUserLogin(id);
        return login;
    }
}
