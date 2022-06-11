package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest: 标注当前类是测试类,不会随项目一块打包
@SpringBootTest
//@RunWith: 启动这单元测试类(单元测试类是不能运行的),需要传递一个参数,必须是SpringRunner的实例类型
@RunWith(SpringRunner.class)
public class UserServiceTests {
    /**
     * 单元测试方法:就可以单独独立运行,不用启动整个项目,可以做单元测试,提升了代码测试效率
     * 1. 必须被@Test注解修饰
     * 2. 返回值类型必须是void
     * 3. 方法的参数列表不指定任何类型
     * 4. 方法的访问修饰符必须是public
     */

    // idea有检测的功能,接口是不能直接创建Bean的(动态代理技术来解决)
    @Autowired
    private IUserService userService;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("user02");
            user.setPassword("1111");
            userService.reg(user);
            System.out.println("OK");
        } catch (ServiceException e) {
            //获取类的对象，再获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login(){
        User user =
                userService.login("tom001","123456");
        System.out.println(user);
    }

    @Test
    public void getByUid(){
        System.out.println(userService.getByUid(9));
    }

    @Test
    public void changeInfo(){
        User user = new User();
        user.setPhone("178404040");
        user.setEmail("12345@qq.com");
        user.setGender(0);
        userService.changeInfo(9,"管理员",user);
    }

    @Test
    public void changeAvatar(){
        userService.changeAvatar(
                11,
                "/upload/test.png",
                "小明"
        );
    }
}
