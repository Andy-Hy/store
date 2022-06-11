package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.util.Date;

//@SpringBootTest: 标注当前类是测试类,不会随项目一块打包
@SpringBootTest
//@RunWith: 启动这单元测试类(单元测试类是不能运行的),需要传递一个参数,必须是SpringRunner的实例类型
@RunWith(SpringRunner.class)
public class UserMapperTests {
    /**
     * 单元测试方法:就可以单独独立运行,不用启动整个项目,可以做单元测试,提升了代码测试效率
     * 1. 必须被@Test注解修饰
     * 2. 返回值类型必须是void
     * 3. 方法的参数列表不指定任何类型
     * 4. 方法的访问修饰符必须是public
     */

    // idea有检测的功能,接口是不能直接创建Bean的(动态代理技术来解决)
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setUsername("user05");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println("rows=" + rows);
    }


    @Test
    public void updatePasswordByUid(){
        userMapper.updatePasswordByUid(
                9, "321",
                "管理员", new Date());
    }

    @Test
    public void findByUid(){
        System.out.println(userMapper.findByUid(9));
    }

    @Test
    public void updateInfoByUid() {
        User user = new User();
        user.setUid(9);
        user.setPhone("15180808080");
        user.setEmail("test002@gmail.com");
        user.setGender(1);
        userMapper.updateInfoByUid(user);
    }


    @Test
    public void updateAvatarByUid() {
        userMapper.updateAvatarByUid(
                12,
                "/upload/avatar.png",
                "管理员",
                new Date());
    }
}
