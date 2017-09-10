package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.UserDetector;
import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.util.MD5;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class UserServiceImpl implements UserService, UserDetector {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signUp(SignUpVO signUpVO) {
        if(userRepository.findByUsername(signUpVO.userName) != null) {
            throw new ResourceConflictException("用户名已存在");
        }
        if (signUpVO.tel != null && userRepository.findByTel(signUpVO.tel) != null) {
            throw new ResourceConflictException("手机号已被注册");
        }
        User user = new User();
        user.setUsername(signUpVO.userName);
        user.setPassword(MD5.encrypt(signUpVO.password));
        user.setTel(signUpVO.tel);
        user.setEmail(signUpVO.email);
        userRepository.save(user);
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        User user = userRepository.findByUsername(username);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.userName = user.getUsername();
        userInfoVO.email = user.getEmail();
        userInfoVO.tel = user.getTel();
        return userInfoVO;
    }

    @Override
    public void modifyUserInfo(String username, UserInfoVO userInfoVO) {
        User user = userRepository.findByUsername(username);
        if(user != null){
            user.setEmail(userInfoVO.email);
            user.setTel(userInfoVO.tel);
            user.setPassword(MD5.encrypt(userInfoVO.password));
            userRepository.save(user);
        }
    }

    @Override
    public String findPasswordByMail(String username)  {
        //生成随机数
        String chars = "0123456789";
        char[] rands = new char[6];
        for (int i = 0; i < 6; i++) {
            int rand = (int) (Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }
        String num = String.valueOf(rands);
        User user = userRepository.findByUsername(username);
        // 收件人电子邮箱
        String to = user.getEmail();

        // 发件人电子邮箱
        String from = "782024062@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("782024062@qq.com", "ioolndenegkhbbfh"); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("优财找回密码");
            // 设置消息体
            message.setText(num);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from runoob.com");
            return num;
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return  null;
    }

    @Override
    public String findPasswordByTel(String username) {
        String chars = "0123456789";
        char[] rands = new char[6];
        for (int i = 0; i < 6; i++) {
            int rand = (int) (Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }
        String num = String.valueOf(rands);
        User user = userRepository.findByUsername(username);
        if(user == null){
            return null;
        }
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "VanillaV"),new NameValuePair("Key", "10a3d7604001230f4b42"),new NameValuePair("smsMob",user.getTel()),new NameValuePair("smsText","验证码："+num)};
        post.setRequestBody(data);

        try {
            client.executeMethod(post);
            return num;
        } catch (IOException e) {
            e.printStackTrace();
        }
        org.apache.commons.httpclient.Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode);
        for(Header h : headers)
        {
            System.out.println(h.toString());
        }
        String result = null;
        try {
            result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result); //打印返回消息状态
        post.releaseConnection();
        return  null;
    }

    @Override
    public UserInfoVO login(String username, String password, String userAgent) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByTel(username);
        }
        if (user == null) {
            throw new ResourceNotFoundException("用户名不存在");
        }
        if(!user.getPassword().equals(MD5.encrypt(password))) {
            throw new ResourceNotFoundException("密码错误");
        }
        // 更新客户端类型
        if (userAgent != null && !userAgent.equals(user.getUserAgent())) {
            user.setUserAgent(userAgent);
            userRepository.save(user);
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.userName = user.getUsername();
        userInfoVO.email = user.getEmail();
        userInfoVO.tel = user.getTel();
        return userInfoVO;
    }

    @Override
    public boolean isUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

}