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
    Long signUp(SignUpVO signUpVO);

    /**
     * 获取用户信息
     * @param userId        用户id
     * @return              用户信息vo
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 修改用户信息
     * @param userId        用户id
     * @param userModifyVO  用户修改信息vo
     */
    void modifyUserInfo(Long userId, UserModifyVO userModifyVO);

}
