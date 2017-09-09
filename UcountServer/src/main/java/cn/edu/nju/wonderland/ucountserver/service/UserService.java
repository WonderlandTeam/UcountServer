package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;

public interface UserService {

    /**
     * 用户注册
     * @param signUpVO      注册信息vo
     * @param userAgent     客户端类型
     * @return              注册成功返回用户id
     */
    String signUp(SignUpVO signUpVO, String userAgent);

    /**
     * 获取用户信息
     * @param username        用户名
     * @return              用户信息vo
     */
    UserInfoVO getUserInfo(String username);

    /**
     * 登录
     * @param username  	用户名
     * @param password  	密码
     * @param userAgent     客户端类型
     * @return              用户信息vo
     */
    UserInfoVO login(String username, String password, String userAgent);
    /**
     * 修改用户信息
     * @param username        用户名
     * @param userModifyVO  用户修改信息vo
     */
    void modifyUserInfo(String username, UserInfoVO userModifyVO);
    /**
     * 通过邮箱找回密码
     * @param username        用户名
     */
    String findPasswordByMail(String username);
    /**
     * 通过手机找回密码
     * @param username        用户名
     */
    String findPasswordByTel(String username);

}
