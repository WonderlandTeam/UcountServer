package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;

public interface UserService {

    /**
     * 用户注册
     * @param signUpVO      注册信息vo
     * @return              注册成功返回用户id
     */
    String signUp(SignUpVO signUpVO);

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
     * @return              用户信息vo
     */
    UserInfoVO login(String username,String password);
    /**
     * 修改用户信息
     * @param username        用户名
     * @param userModifyVO  用户修改信息vo
     */
    void modifyUserInfo(String username, UserInfoVO userModifyVO);
    /**
     * 修改用户信息
     * @param username        用户名
     */
    int findPasswordByMail(String username);

}
