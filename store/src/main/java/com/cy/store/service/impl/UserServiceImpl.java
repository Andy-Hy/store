package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.xml.crypto.Data;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.util.Date;
import java.util.UUID;

/**用户模块业务层的实现类 */
@Service //将当前类对象交给Spring管理，自动创建对象以及对象的维护
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {

        //通过user参数来获取传递过来的username
        String username = user.getUsername();
        //调用findByUsername(username)判断用户是否被注册过
        User result = userMapper.findByUsername(username);
        //判断结果是否不为null，则抛出用户名被占用的异常
        if (result != null) {
            //抛出异常
            throw new UsernameDuplicatedException("用户名被占用");
        }

        //密码加密处理的实现，MD5
        String oldPassword = user.getPassword();
        //(串 + password + 串) ---- MD5算法加密，连续加载三次
        //盐值 + password + 盐值 （盐值就是随机数组）
        //获取一个随机数组（盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        //补全数据，盐值的记录
        user.setSalt(salt);
        //将密码和盐值作为整体进行加密
        String md5Password = getMD5Password(oldPassword,salt);
        //将加密后的密码重新补全设置到user对象中
        user.setPassword(md5Password);

        //补全数据，is_Delete设置成0
        user.setIsDelete(0);
        //补全数据，4个日志字段信息
        user.setCreatedUser(user.getCreatedUser());
        user.setModifiedUser(user.getModifiedUser());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        //执行注册功能业务的实现(rows==1)
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("用户注册过程中出现未知错误");
        }
    }



    @Override
    public User login(String username, String password) {
        //根据用户名称来查询用户的数据是否存在，若不存在则抛出异常
        User result = userMapper.findByUsername(username);
        if (result == null) {
            throw new UserNotFoundException("用户数据不存在");
        }
        //检测用户密码是否匹配
        //1.先获取到数据库中加密后的密码
        String oldPassword = result.getPassword();
        //2.和用户传递过来的密码进行比对
        //2.1 先获取盐值:上一次注册时所自动生成的盐值
        String salt = result.getSalt();
        //2.2 将用户密码按照相同的MD5算法进行加密
        String newMd5Password = getMD5Password(password,salt);
        //3 将密码进行比对
        if (! newMd5Password.equals(oldPassword)) {
            throw new PasswordNotMatchException("用户密码错误");
        }
        //判断is_delete字段的值是否为1表示标记
        if (result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据存在");
        }

        //调用mapper层的findByUsername来查询用户的数据，提升系统的性能
        User user = userMapper.findByUsername(username);

        //当前的用户数据返回,返回的数据是为了辅助其他的页面做数据展示使用()
        return user;
    }

    @Override
    public void changePassword(Integer uid,
                               String username,
                               String oldPassword,
                               String newPassword) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1) {
            throw new UserNotFoundException("用户数据存在");
        }
        //原始密码和数据中的密码进行比对
        String oldMd5Password = getMD5Password(oldPassword,result.getSalt());
        if (!result.getPassword().equals(oldMd5Password)) {
            throw new PasswordNotMatchException("密码错误");
        }

        //将新的密码设置到数据库中，将新的密码进行加密再更新
        String newMd5Password = getMD5Password(newPassword,result.getSalt());
        Integer rows = userMapper.updatePasswordByUid(uid,newMd5Password,
                                                    username, new Date());

        if (rows != 1) {
            throw new UpdateException("更新数据产生未知异常");
        }
    }

    @Override
    public User getByUid(Integer uid) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete()==1) {
            throw new UserNotFoundException("用户数据不存在");
        }

        User user = new User();
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender(result.getGender());

        return user;
    }

    /**
     * user对象中的数据phone/email/gender,手动再将uid/username封装到user对象中
     *
     */
    @Override
    public void changeInfo(Integer uid, String username, User user) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete()==1) {
            throw new UserNotFoundException("用户数据不存在");
        }

        user.setUid(uid);
//        user.setUsername(username);
        user.setModifiedUser(username);
        user.setCreatedTime(new Date());

        Integer rows = userMapper.updateInfoByUid(user);
        if (rows != 1) {
            throw new UpdateException("更新数据时产生未知异常");
        }
    }

    @Override
    public void changeAvatar(Integer uid, String avatar, String username) {
        //查询当前用户数据是否存在
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete().equals(1)) {
            throw new UserNotFoundException("用户数据不存在");
        }
        Integer rows = userMapper.updateAvatarByUid(uid,avatar,
                username,new Date());
        if (rows != 1) {
            throw new UpdateException("更新用户头像时产生未知错误");
        }
    }


    /**
     * 定义一个MD5算法的加密处理
     */
    private String getMD5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            //MD5加密算法的调用（加密三次）
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        //返回加密之后的密码
        return password;

    }
}
