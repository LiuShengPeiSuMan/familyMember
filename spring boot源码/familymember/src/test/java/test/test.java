package test;

import com.alibaba.fastjson.JSONObject;
import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.dao.FamilyMemberImageDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.pojo.FamilyMemberImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest
public class test {

    @Autowired
    public static RedisTemplate redisTemplate;

    @Autowired
    private FamilyMemberImageDao image;

    @Test
    void findImage() {
        FamilyMemberImage image1 = image.selectImage("9d6fb91c-bda2-4df3-af83-88e68420");
        Object o = JSONObject.toJSON(image1);
        System.err.println(o);
    }

}
