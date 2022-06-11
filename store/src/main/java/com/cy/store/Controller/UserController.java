package com.cy.store.Controller;

import com.cy.store.Controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.cy.store.Controller.BaseController.OK;

//@Controller
@RestController  //组合注解 ==  @Controller + @ResponseBody
//凡是以users开头的请求都会被拦截到UserController类中
@RequestMapping("users")
public class UserController extends BaseController{

    //控制层依赖于Service层
    @Autowired
    private IUserService userService;

/*
    @RequestMapping("reg")
    //@ResponseBody  //此方法结果以JSON格式进行数据响应给前端
    public JsonResult<Void> reg(User user){
        //创建响应结果对象
        JsonResult<Void> result = new JsonResult<>();

        try {
            userService.reg(user);
            result.setState(200);
            result.setMessage("注册成功");
        } catch (UsernameDuplicatedException e) {
            result.setState(4000);
            result.setMessage("用户名被占用");
        } catch (InsertException e){
            result.setState(5000);
            result.setMessage("注册时产生未知异常");
        }
        return result;
    }

 */

    /**
     * 约定大于配置的开发理念，省略大量的配置甚至注解的编写
     *1.接收数据方式一：请求处理方法的参数值列表设置为pojo类型来接收前端数据，
     * SpringBoot会将前端的url地址中的参数名和pojo类的属性名进行比较，若
     * 这俩名称相同，则将值注入到pojo类中对应的属性上
     */
    @RequestMapping("reg")
    public JsonResult<Void> reg(User user){
        userService.reg(user);
        return new JsonResult<Void>(OK);
    }

    /**
     *2.接收数据方式二：请求处理方法的参数值列表设置为非pojo类型，
     * SpringBoot会直接将请求的参数名和方法的参数名直接进行比较，
     *若名称相同则自动完成值的依赖注入
     */
    @RequestMapping("login")
    public JsonResult<User> login(String username,
                                  String password,
                                  HttpSession session) {
        User data = userService.login(username,password);

        //向session对象中完成数据的绑定（session全局）
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());

        //获取session中绑定的数据
//        System.out.println(getuidFromSession(session));
//        System.out.println(getUsernameFromSession(session));

        return new JsonResult<User>(OK,data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,
                                           String newPassword,
                                           HttpSession session) {
        // 调用session.getAttribute("")获取uid和username
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);

        // 调用业务对象执行修改密码
        userService.changePassword(uid, username, oldPassword, newPassword);
        // 返回成功
        return new JsonResult<Void>(OK);
    }


    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session) {
      User data  =
              userService.getByUid(getuidFromSession(session));
      return new JsonResult<>(OK,data);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user,
                                       HttpSession session) {
        //user对象四个部分:username,phone,email,gender
        //uid数据需要再次封装到user对象中
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid,username,user);
        return new JsonResult<>(OK);
    }



    /**设置上传文件的最大值*/
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    /**限制上传文件的类型*/
    public static final List<String> AVATAR_TYPES = new ArrayList<String>();

    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");

    }

    /**
     * MultipartFile接口：是SpringMVC提供的一个接口，这个接口为我们包装了获取文件类型的数据
     * （任何类型的file都可以接收），SpringBoot整合了SpringMVC,只需在处理请求的方法参数列表上
     * 声明一个参数类型为MultipartFile的参数，SpringBoot会自动将传递给服务的文件数据赋值给这个参数
     *
     * @RequestParam 表示请求中的参数，将请求中的参数注入请求处理方法的某个参数上，
     * 若名称不一致，可使用@RequestParam注解进行标记和映射
     * @param session
     * @param file
     * @return
     */
    @PostMapping("change_avatar")
    public JsonResult<String> changeAvatar(
            HttpSession session,
            @RequestParam("file")MultipartFile file){
        //判断文件是否为null
        if (file.isEmpty()) {
            throw new FileEmptyException("文件为空");
        }
        if (file.getSize() > AVATAR_MAX_SIZE) {
            throw new FileSizeException("文件容量太大了");
        }
        //判断文件类型是否符合规定和后缀类型
        String contentType = file.getContentType();
        //若集合包含某个元素则返回值true
        if (!AVATAR_TYPES.contains(contentType)) {
            throw new FileTypeException("文件类型不支持");
        }

        //上传文件.../upload/文件.png
        String parent =
                session.getServletContext().getRealPath("upload");
        //File对象指向该路径，File是否存在
        File dir = new File(parent);
        if (!dir.exists()) {  //检测目录是否存在
            dir.mkdir();  //创建当前目录
        }

        //获取到该文件名称，使用UUID工具来生成一个新的字符串作为文件名
        //eg: avatar01.png
        String originalFilename = file.getOriginalFilename();
        System.out.println("OriginalFilename=" + originalFilename);
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String filename = UUID.randomUUID().toString().toUpperCase()
                + suffix;

        File dest = new File(dir,filename); //是一个空文件

        try {
            file.transferTo(dest); //将file文件中的数据写入dest文件
        } catch (FileStateException e) {
            throw new FileStateException("文件状态异常");
        } catch (IOException e) {
            throw new FileUploadIOException("文件读写异常");
        }

        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        //返回头像的路径/upload/test.png
        String avatar = "/upload/" + filename;
        userService.changeAvatar(uid,avatar,username);
        //返回头像路径给前端页面，将来用于头像展示使用
        return new JsonResult<>(OK,avatar);
    }
}
