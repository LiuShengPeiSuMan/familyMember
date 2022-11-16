package test;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.pojo.FamilyMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


public class test {

    @Autowired
    public static RedisTemplate redisTemplate;
    public static void main(String[] args) {
        Object name = redisTemplate.opsForValue().get("name");
        System.err.println(name);
    }
}
