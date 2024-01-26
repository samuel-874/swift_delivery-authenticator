package com.swiftdelivery.authenticator.model.user;

import com.swiftdelivery.authenticator.auth.jwt.JwtUtils;
import com.swiftdelivery.authenticator.mappers.Mapper;
import com.swiftdelivery.authenticator.model.mail.MailService;
import com.swiftdelivery.authenticator.model.mail.MailTemplates;
import com.swiftdelivery.authenticator.model.tokens.TokensService;
import com.swiftdelivery.authenticator.model.user.enums.AccStatus;
import com.swiftdelivery.authenticator.response.UserRegistrationDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static com.swiftdelivery.authenticator.response.CustomResponse.response;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokensService tokenService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MailService mailService;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private HttpServletRequest servletRequest;


    record AuthenticationRequest(@NotBlank(message = "Email's required") String email,
                                 @NotBlank(message = "Password's required") String password){}
    record AuthenticationResponse(String jwt){}

    @Override
    public ResponseEntity registerUser(UserRegistrationDTO userDTO) {
        UsersEntity users = Mapper.mapUserEntity(userDTO);

        if(userDTO.getProvider() != UserProvider.SWIFT_DELIVERY && userDTO.getPassword() == null){
            return response(403,"Password is required",null);
        } else if (userDTO.getProvider() == UserProvider.GOOGLE) {
            users.setAccStatus(AccStatus.VERIFIED);
        }else{
            users.setAccStatus(AccStatus.UN_VERIFIED);
            sendVerificationMail(userDTO.getEmail());
        }

        var key = generateAPIKey(userDTO.getEmail());
        users.setAPIKey(key);
        var token = jwtUtils.generateToken(userDTO.getEmail());
        usersRepository.save(users);

        //   send verification mail

        return response(201,"Registration Successful",token);
    }

    @Override
    public ResponseEntity sendVerificationMail(String email){
        var token = tokenService.generateToken(email);
        var byteToken = "swift_delivery".concat(token).concat(email).getBytes();
        var encoded = Base64.getEncoder().encodeToString(byteToken);
        var frontend_url = System.getenv("FRONTEND_URL");
        var validation_link = frontend_url + "?token="+ encoded;
//         TODO send email
        try{
            MailTemplates templates = new MailTemplates();
            mailService.sendMail(email, "Swift Delivery - Activation", templates.verificationMail(validation_link));
        }catch (Exception e){
            return response(500,"Could'nt send mail",null);
        }

        return response(200,"Token Generated",null);
    }

    @Override
    public ResponseEntity reSendVerificationMail(String email){
        var token = tokenService.generateToken(email);
        var byteToken = "swift_delivery".concat(token).concat(email).getBytes();
        var encoded = Base64.getEncoder().encodeToString(byteToken);
        var frontend_url = System.getenv("FRONTEND_URL");
        var validation_link = frontend_url + "?token="+ encoded;
//         TODO send email
        try{
            MailTemplates templates = new MailTemplates();
            mailService.sendMail(email, "Swift Delivery - Activation", templates.verificationMail(validation_link));
        }catch (Exception e){
            return response(500,"Could'nt send mail",null);
        }

        return response(200,"Token Generated",null);
    }

    @Override
    public ResponseEntity validateVerificationMail(String email, String token){
        try{
            var isValid = tokenService.validateToken(email, token);
            if(isValid){
                UsersEntity users = usersRepository.findByEmail(email).get();
                users.setAccStatus(AccStatus.VERIFIED);
                usersRepository.save(users);
            }
        }catch (Exception e){
            return response(403,"Invalid Token",null);
        }
        return response(200,"Validation Successful",null);
    }



    @Override
    public ResponseEntity login(AuthenticationRequest request){

        String email = request.email();
        String password = request.password();

        UsersEntity user;

        try{
            user = usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
            if(user.getAccStatus() != AccStatus.VERIFIED){
                return response(403,"Registration not completed",null);
            }

            if(user.getProvider() == UserProvider.GOOGLE){
                return response(401,"User Signed up With Google",null);
            }

            String userPassword = user.getPassword();

            Boolean isCorrectPassword = passwordEncoder.matches(password,userPassword);
            if(!isCorrectPassword){
                return  response(402,"Incorrect password", null);
            }
            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            user.setLastLogin(System.currentTimeMillis());
            usersRepository.save(user);
            Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,request.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (NullPointerException e){
            return  response(404,"User not found", null);
        }
        final String jwt = jwtUtils.generateToken(user.getEmail());
        return  ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    private String generateAPIKey(String email){
        var dommieString = "EF2CCfXsde";
        var byteData = dommieString.concat(email).getBytes();
        var key = Base64.getEncoder().encodeToString(byteData);

        return key;
    }

    @Override
    public ResponseEntity getUserDetails(){
        var user = userUtils.extractUser(servletRequest);

        return response(201,"Feteched Successfully",user);
    }

}
