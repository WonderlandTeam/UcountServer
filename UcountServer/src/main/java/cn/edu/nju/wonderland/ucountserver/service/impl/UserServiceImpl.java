package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Long signUp(SignUpVO signUpVO) {
        // TODO
        return null;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        // TODO
        return null;
    }

    @Override
    public void modifyUserInfo(Long userId, UserModifyVO userModifyVO) {
        // TODO
    }

}
