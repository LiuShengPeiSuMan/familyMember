package util.constant;

/**
 * 自定义常量
 */
public class ConstantToolUtil {

    //email格式校验
    public static final String EMAIL_CHECK = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
    //手机号格式校验
    public static final String PHONE_CHECK = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
    //缓存key
    public static final String USER_LOGIN = "user:login:";
    //用户登录缓存时间
    public static final long USER_LOGIN_TIME = 30L;
    //获取到验证码缓存的时间
    public static final long EMAIL_CODE = 5L;
    //发件人邮箱
    public static final String EMAIL_FROM = "2726118282@qq.com";
    //添加家族成员数据
    public static final String ADD_FAMILY = "add:family:";
    //家族成员添加姓名
    public static final String CHECK_FAMILY = "check:family:";
    //添加新生儿
    public static final String ADD_BABY = "add:baby:";
    //新生儿添加姓名
    public static final String CHECK_BABY = "check:baby:";
    //添加登录权限普通成员
    public static final String PUTONG="putong";
}
