package com.example.familymembermanagement.util;


/**
 * 请求地址
 */
public class UrlApiUtil {

    private static String ip = "192.168.1.6";
    private static String port = "4314";

    /**
     * 账号密码登录
     */
    public static final String LOGIN_URL = "http://" + ip + ":" + port + "/userlogin/cacheLogin";

    /**
     * 查询族长有没有登录权限
     */
    public static final String QUERY_ZZ = "http://" + ip + ":" + port + "/userlogin/loginZz";

    /**
     * 初始化族长
     */
    public static final String ADD_ZUZHANG = "http://" + ip + ":" + port + "/userlogin/addInitZz";

    /**
     * 邮箱发送验证码
     */
    public static final String SEND_CODE = "http://" + ip + ":" + port + "/userlogin/emailCodeSend";

    /**
     * 邮箱登录
     */
    public static final String EMAIL_LOGIN = "http://" + ip + ":" + port + "/userlogin/emailLogin";

    /**
     * 首页数据请求
     */
    public static final String HOME_DATA = "http://" + ip + ":" + port + "/home/homeData";

    /**
     * 发布通知
     */
    public static final String RELEASE_NOTICE = "http://" + ip + ":" + port + "/notice/addNotice";

    /**
     * 查询所有审核记录
     */
    public static final String ALL_EXAMINE = "http://" + ip + ":" + port + "/examine/getAllExamine";

    /**
     * 查询审核详细信息
     */
    public static final String EXAMINE_DETAILED = "http://" + ip + ":" + port + "/examine/examineDetailed";

    /**
     * 修改审核结果
     */
    public static final String FINDINGS_OFAUTIT = "http://" + ip + ":" + port + "/examine/findingsOfAudit";

    /**
     * 审核记录
     */
    public static final String EXAMINE_RECORD = "http://" + ip + ":" + port + "/examine/examineRecord";

    /**
     * 删除审核记录
     */
    public static final String DELETE_EXAMINE = "http://" + ip + ":" + port + "/examine/deleteExamine";

    /**
     * 查询所有家族成员
     */
    public static final String ALL_MEMBER = "http://" + ip + ":" + port + "/member/allMember";

    /**
     * 查询每一项详细数据
     */
    public static final String FAMILY_DETAILED = "http://" + ip + ":" + port + "/member/familyDetailed";

    /**
     * 查询所有户主
     */
    public static final String ALL_HOUSE = "http://" + ip + ":" + port + "/house/queryAllHouse";

    /**
     * 查询所属户主所有家庭成员
     */
    public static final String HOUSE_ALL_FAMILY = "http://" + ip + ":" + port + "/house/queryAllPeopleHouse";

    /**
     * 添加户主
     */
    public static final String ADD_HOUSE = "http://" + ip + ":" + port + "/house/addHouse";

    /**
     * 查询家庭成员详细信息
     */
    public static final String QUERY_FAMILY_DETAILED = "http://" + ip + ":" + port + "/house/queryFamilyDetailed";

    /**
     * 查询家庭成员信息
     */
    public static final String QUERY_FAMILY_INFORMATION = "http://" + ip + ":" + port + "/family/queryFamilyInformation";

    /**
     * 查询该成员所属户主id
     */
    public static final String QUERY_HOUSE_ID = "http://" + ip + ":" + port + "/family/queryHouseId";

    /**
     * 添加家庭成员
     */
    public static final String ADD_FAMILY = "http://" + ip + ":" + port + "/family/addFamily";

    /**
     * 修改家庭成员
     */
    public static final String UPDATE_FAMILY = "http://" + ip + ":" + port + "/family/updateFamily";

    /**
     * 删除家庭成员
     */
    public static final String DELETE_FAMILY = "http://" + ip + ":" + port + "/family/deleteFamily";

    /**
     * 查询本年度出生成员
     */
    public static final String YEAR_BABY = "http://" + ip + ":" + port + "/babySituation/yearBaby";

    /**
     * 查询出生成员详细信息
     */
    public static final String BABY_DETAILED = "http://" + ip + ":" + port + "/babySituation/babyDetailed";

    /**
     * 添加出生成员
     */
    public static final String ADD_BABY = "http://" + ip + ":" + port + "/babySituation/addNewBaby";

    /**
     * 查询所有登录人权限
     */
    public static final String QUERY_ALL_LOGIN = "http://" + ip + ":" + port + "/jurisdiction/queryLogin";

    /**
     * 查询登录人详细信息
     */
    public static final String USER_LOGIN = "http://" + ip + ":" + port + "/jurisdiction/userLogin";

    /**
     * 重置（修改）用户权限
     */
    public static final String RESET_USERLOGIN = "http://" + ip + ":" + port + "/jurisdiction/resetUserLogin";

    /**
     * 解除登录权限
     */
    public static final String RELIEVE_LOGIN = "http://" + ip + ":" + port + "/jurisdiction/relieveLogin";

    /**
     * 查询姓名和邮箱
     */
    public static final String SELECT_NAME_EMAIL = "http://" + ip + ":" + port + "/jurisdiction/selectNameEmail";

    /**
     * 添加用户权限
     */
    public static final String ADD_LOGIN_PUTONG = "http://" + ip + ":" + port + "/jurisdiction/addPuTongLogin";

    /**
     * 统计图
     */
    public static final String STATISTICS = "http://" + ip + ":" + port + "/statistics/proportion";

    /**
     * 历史审核记录
     */
    public static final String QUERY_HISTORY = "http://" + ip + ":" + port + "/history/queryHistory";

}
