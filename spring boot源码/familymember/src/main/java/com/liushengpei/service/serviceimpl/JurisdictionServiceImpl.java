package com.liushengpei.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.dao.UserLoginDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.Jurisdication;
import com.liushengpei.pojo.UserLogin;
import com.liushengpei.service.IJurisdictionService;
import com.liushengpei.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.liushengpei.util.ConstantToolUtil.*;

/**
 * 权限管理
 */
@Service
public class JurisdictionServiceImpl implements IJurisdictionService {

    @Autowired
    private UserLoginDao loginDao;
    @Autowired
    private FamilyMemberDao familyMemberDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //序列化
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 查询所有用户权限
     */
    @Override
    public List<UserLogin> queryLogin(String name) {
        List<UserLogin> userLogins = loginDao.queryUserLogin(name);
        return userLogins;
    }

    /**
     * 重置用户密码
     */
    @Override
    public String resetUserLogin(String id, String loginName, String account) {
        //查询缓存是否有数据
        String user = (String) redisTemplate.opsForValue().get(USER_LOGIN + account);
        if (user != null) {
            //删除缓存数据
            redisTemplate.delete(USER_LOGIN + account);
        }
        Map<String, Object> params = new HashMap<>();
        String pwd = UUID.randomUUID().toString().substring(0, 8);
        params.put("password", pwd);
        params.put("updateTime", new Date());
        params.put("updateUser", loginName);
        params.put("id", id);
        loginDao.resetPassword(params);
        return "重置密码成功！";
    }

    /**
     * 添加普通成员登录权限
     */
    @Override
    public String addPuTongLogin(Map<String, Object> params) {
        //登录人id
        //String loginId = (String) params.get("loginId");
        //登录人姓名
        String loginName = (String) params.get("loginName");
        //普通家族成员姓名（唯一）
        String puTongName = (String) params.get("puTongName");
        //普通成员邮箱，可以为空
        String loginEmail = (String) params.get("email");
        //查看是否已添加权限
        Integer count = loginDao.isLogin(puTongName);
        if (count == null || count > 0) {
            return "此成员已添加登录权限";
        }
        //添加成员登录权限
        UserLogin login = new UserLogin();
        login.setId(UUID.randomUUID().toString().substring(0, 32));
        String account = ToolUtil.toPinYin(puTongName);
        login.setAccount(PUTONG + account);
        login.setPassword(UUID.randomUUID().toString().substring(0, 8));
        login.setNickname(puTongName);
        if (loginEmail != null && !loginEmail.matches(EMAIL_CHECK)) {
            return "邮箱格式不正确";
        }
        //登录邮箱不等于null，并且格式都正确
        if (loginEmail != null && loginEmail.matches(EMAIL_CHECK)) {
            login.setLoginEmail(loginEmail);
        }
        //普通成员角色
        login.setRole(3);
        login.setCreateTime(new Date());
        login.setCreateUser(loginName);
        login.setUpdateTime(new Date());
        login.setUpdateUser(loginName);
        login.setDelFlag(0);
        loginDao.addUser(login);
        return "普通成员登录权限添加成功！";
    }

    /**
     * 解除成员登录权限
     */
    @Override
    public String relieveLogin(String id, String loginName, String account) {
        //删除缓存数据
        redisTemplate.delete(USER_LOGIN + account);
        //删除登录权限
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("updateTime", new Date());
        params.put("updateUser", loginName);
        Integer integer = loginDao.relieveUserLogin(params);
        if (integer != null && integer > 0) {
            return "解除登录权限成功！";
        }
        return "此成员登录权限已解除！";
    }

    /**
     * 查询登录详细信息
     */
    @Override
    public UserLogin login(String id) {
        UserLogin query = loginDao.query(id);
        return query;
    }

    /**
     * 查询所有成员
     */
    @Override
    public List<Jurisdication> queryAll() {
        List<FamilyMember> familyMemberList = familyMemberDao.selectAll(null);
        //存储姓名和邮箱
        List<Jurisdication> jurisd = new ArrayList<>();
        if (familyMemberList.size() > 0) {
            for (FamilyMember f : familyMemberList) {
                Jurisdication ju = new Jurisdication();
                ju.setName(f.getName());
                ju.setEmail(f.getEmail());
                jurisd.add(ju);
            }
        }
        //查询已有登录权限的用户
        List<UserLogin> userLogins = loginDao.queryUserLogin(null);
        List<Jurisdication> login = new ArrayList<>();
        if (!userLogins.isEmpty()) {
            for (UserLogin u : userLogins) {
                Jurisdication ju = new Jurisdication();
                ju.setName(u.getNickname());
                ju.setEmail(u.getLoginEmail());
                login.add(ju);
            }
        }
        //筛选出没有登录权限的用户
        List<Jurisdication> screen = new ArrayList<>();
        boolean off = false;
        for (Jurisdication j : jurisd) {
            for (Jurisdication k : login) {
                if (j.getName().equals(k.getName())) {
                    off = true;
                    break;
                }
            }
            if (!off) {
                screen.add(j);
            } else {
                off = false;
            }
        }
        return screen;
    }
}
