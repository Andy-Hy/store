package com.cy.store.Controller;

import com.cy.store.Controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/**控制层的基类*/
public class BaseController {
    /**操作成功的状态码*/
    public static final int OK = 200;

    //请求处理方法，该方法返回值就是需要传递前端的数据
    //自动将异常对象传递给此方法的参数列表上
    @ExceptionHandler({ServiceException.class,FileUploadException.class})  //统一处理抛出的异常
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException) {
            result.setState(4000);
            result.setMessage("用户名已占用的异常");
        } else if (e instanceof UserNotFoundException) {
            result.setState(5001);
            result.setMessage("用户数据不存在的异常");
        } else if (e instanceof AddressNotFoundException) {
                result.setState(4004);
                result.setMessage("用户的收货地址数据不存在的异常");
            } else if (e instanceof AccessDeniedException) {
            result.setState(4005);
            result.setMessage("收货地址数据非法访问的异常");
        }  else if (e instanceof CartNotFoundException) {
                result.setState(4006);
                result.setMessage("购物车数据不存在的异常的异常");
        }else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
            result.setMessage("用户密码错误的异常");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("插入数据时产生未知异常");
        }else if (e instanceof UpdateException) {
            result.setState(5001);
            result.setMessage("更新数据时产生未知异常");
        } else if (e instanceof DeleteException) {
                result.setState(5003);
                result.setMessage("删除数据时产生未知异常");
        }else if (e instanceof FileEmptyException) {
            result.setState(6000);
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
        } else if (e instanceof FileStateException) {
            result.setState(6003);
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
        }
        return result;
    }


    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登录的用户uid的值
     */
    protected final Integer getuidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid")
                .toString());
    }

    /**
     * 获取当前用户的username
     * @param session session对象
     * @return 当前登录用户的用户名
     *  在实现类中重写父类的toString(),不是句柄信息的输出
     */
    protected final String getUsernameFromSession(
            HttpSession session) {
        return session.getAttribute("username").toString();
    }
}
