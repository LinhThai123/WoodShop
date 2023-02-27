package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.entity.Token;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.model.request.LoginReq;
import com.example.woodshop_studio.model.request.RegisterReq;
import com.example.woodshop_studio.model.request.UserReq;
import com.example.woodshop_studio.repository.TokenRepository;
import com.example.woodshop_studio.repository.UserRepository;
import com.example.woodshop_studio.service.AuthService;
import com.example.woodshop_studio.service.MailService;
import com.example.woodshop_studio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AuthServiceIpml implements AuthService {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private UserService userService ;

    @Autowired
    private TokenRepository tokenRepository ;

    @Autowired
    private MailService mailService ;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserReq login(LoginReq req, HttpSession session) {
        // tạo đối tượng dựa trên thông tin xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(req.getEmail() , req.getPassword()) ;

        // Tiến hành xác thực (@Bean trong config)
        Authentication authentication = authenticationManager.authenticate(token) ;

        // Lưu thông tin user đăng nhập
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(authentication.getName());
        // Lưu thông tin trong session
        session.setAttribute("MY_SESSION" , authentication.getName());

        return userService.infoUserByEmail(req.getEmail());
    }

    @Override
    public String register(RegisterReq req) {
        User userExists = userRepository.findByEmail(req.getEmail());
        if(userExists != null) {
            throw new RuntimeException("Email do exits") ;
        }
        // mã hóa password
        String hash = BCrypt.hashpw(req.getPassword() , BCrypt.gensalt(12));

        // Tạo user lưu vào database
        User user = new User(req.getName(), req.getEmail(),hash,req.getAddress(),req.getPhone(), new ArrayList<>(List.of("USER"))) ;
        userRepository.save(user) ;
        return generateTokenAndSenEmail(user);
    }

    @Override
    public String logout(HttpSession session) {
        if(session != null) {
            session.invalidate();
            return "Logout Successfull";
        }
        else {
            return "Logout Fail";
        }
    }
    @Override
    public String generateTokenAndSenEmail(User user) {
        // Sinh ra token
        String tokenStr  = UUID.randomUUID().toString();

        // tạo ra token và lưu vào database
        Token token = new Token(tokenStr,LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),user);

        tokenRepository.save(token) ;

        String link = "https://woodshop.com/api/auth/confirm?" + tokenStr ;
        mailService.send(user.getEmail(), "Chúc mừng bạn đã đăng ký thành công tài khoản WoodShop <3 " ,
                "Nhấn vào link xác thực tài khoản " + link );
        return link ;
    }
}
