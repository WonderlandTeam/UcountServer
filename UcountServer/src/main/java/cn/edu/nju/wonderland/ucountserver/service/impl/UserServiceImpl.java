package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.UserService;
import cn.edu.nju.wonderland.ucountserver.vo.SignUpVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.UserModifyVO;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Properties;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	
    @Override
    public String signUp(SignUpVO signUpVO) {
    	if(userRepository.findByUsername ( signUpVO.userName ) != null){
    		return null;
    	}
        User user = new User();
        user.setUsername(signUpVO.userName);
        user.setPassword(MD5(signUpVO.password));
        user.setTel(signUpVO.tel);
        user.setEmail(signUpVO.email);
        userRepository.save(user);
        return user.getUsername();
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
        user.setEmail(userInfoVO.email);
        user.setTel(userInfoVO.tel);
        user.setPassword(userInfoVO.password);
    }

    @Override
    public int findPasswordByMail(String username)  {
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
            return Integer.valueOf(num);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return  0;
    }

    @Override
	public UserInfoVO login(String username, String password) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username);
		if((user.getUsername() == username || user.getTel() == username )&& user.getPassword() == MD5(password)

                ){
	        UserInfoVO userInfoVO = new UserInfoVO();
	        userInfoVO.userName = user.getUsername();
	        userInfoVO.email = user.getEmail();
	        userInfoVO.tel = user.getTel();
	        return userInfoVO;
		}
		return null;
	}
	private String MD5(String str) {
        try{
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	md.update(str.getBytes());
        	return new BigInteger(1, md.digest()).toString(16);
        }catch(Exception exception){
        	exception.printStackTrace();
        }
        return null;
	}

}
