
package com.example.residential_management.service;

import jakarta.mail.internet.MimeMessage;
import com.example.residential_management.entity.User;
import com.example.residential_management.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public boolean updateRole(Long userId, String roleName) {
        return userMapper.updateRole(userId, roleName) > 0;
    }

    public boolean deleteById(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return userMapper.login(username.trim(), password);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    public boolean resetPasswordAndSendEmail(String email) {
        User user = userMapper.findByEmail(email);

        if (user == null) {
            return false;
        }

        String newPassword = generateRandomPassword(10);

        userMapper.updatePassword(user.getUserId(), newPassword);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Mật khẩu mới - PhoneStore");

            String html =
                    "<div style='font-family:Arial,sans-serif;max-width:600px;margin:auto;"
                            + "padding:20px;border:1px solid #e5e7eb;border-radius:10px;'>"
                            + "<h2 style='color:#2563eb;text-align:center;'>"
                            + "Khôi phục mật khẩu thành công</h2>"
                            + "<p>Xin chào <b>" + user.getFullName() + "</b>,</p>"
                            + "<p>Mật khẩu mới của bạn:</p>"
                            + "<h1 style='color:#dc2626;'>" + newPassword + "</h1>"
                            + "<p>Vui lòng đăng nhập và thay đổi mật khẩu.</p>"
                            + "<p>Trân trọng,<br><b>Phone Store Team</b></p>"
                            + "</div>";

            helper.setText(html, true);
            mailSender.send(message);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String checkDuplicate(String username, String email) {
        if (userMapper.findByUsername(username) != null) {
            return "Tên đăng nhập \"" + username
                    + "\" đã được sử dụng. Vui lòng chọn tên khác.";
        }

        if (userMapper.findByEmail(email) != null) {
            return "Email \"" + email
                    + "\" đã được đăng ký. Bạn có thể đăng nhập hoặc lấy lại mật khẩu.";
        }

        return null;
    }

    public boolean register(User user) {
        try {
            return userMapper.insert(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changePassword(Long userId, String newPassword) {
        userMapper.updatePassword(userId, newPassword);
    }
}
