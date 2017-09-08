package cn.edu.nju.wonderland.ucountserver.service;

public interface UserDetector {

    /**
     * 判断用户名是否存在
     * @param username      用户名
     * @return              true or false
     */
    boolean isUserExists(String username);

}
