package com.davidauz.blkm_interface.impl;

import com.davidauz.blkm_interface.entity.Role;
import com.davidauz.blkm_interface.entity.User;
import com.davidauz.blkm_interface.entity.UserValidation;
import com.davidauz.blkm_interface.service.IdentityServiceException;
import com.davidauz.blkm_interface.service.UserService;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_interface.repository.RoleRepository;
import com.davidauz.blkm_interface.repository.UserRepository;
import com.davidauz.blkm_interface.repository.UserValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
// Values
    @Value("${bulk_mailing.server.web.address}")
    private String web_address;

    @Autowired
    private blk_MailQueue mailQueue;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public User findByEmail(String email) {
        return null;
    }


    @Autowired
    private UserValidationRepository userValidationRepository;

    public UserServiceImpl
    (   UserRepository userRepository
            ,   RoleRepository roleRepository
            ,   PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }


    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    public UserValidation validation(User user) {
        return userValidationRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("Need to save the user before using validation"));
    }



    public Optional<User> userPasswordToBeReset(String passREsetToken) throws IdentityServiceException {
        UserValidation uv = userValidationRepository.findByPasswordResetToken(passREsetToken).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (20)"));

        User user = userRepository.findById(uv.getUser()).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (22)"));

        user.markTokenAsValid();
        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }


    public Optional<User> confirmUser
    (   String confirmationToken
    ) throws IdentityServiceException
    {
        UserValidation userValidation = userValidationRepository.findByToken(confirmationToken).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (21)"));

        User user = userRepository.findById(userValidation.getUser()).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (22)"));

        if (!validation(user).tokenIsCurrent())
            throw new IdentityServiceException("Token not current");

        user.markTokenAsValid();
        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }



    public Optional<User> confirmUserNewPwd(String confirmationToken) throws IdentityServiceException {

        UserValidation userValidation = userValidationRepository.findByPasswordResetToken(confirmationToken).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (21)"));

        User user = userRepository.findById(userValidation.getUser()).orElseThrow(() ->
                new IdentityServiceException("Invalid Token (22)"));

        if (!validation(user).PwdResettokenIsCurrent())
            throw new IdentityServiceException("Token not current");

        user.markTokenAsValid();
        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }



    private void checkEmailAddress(String address) throws IdentityServiceException {
        // Really, really basic validation to ensure the email address has an @ symbol that's not at the start or end
        if (!address.contains("@"))
            throw new IdentityServiceException("Invalid email address (1)");
        if (address.endsWith("@"))
            throw new IdentityServiceException("Invalid email address (2)");
        if (address.startsWith("@"))
            throw new IdentityServiceException("Invalid email address (3)");
        if (address.length() < 5)
            throw new IdentityServiceException("Invalid email address (4)");
        if (!address.contains("."))
            throw new IdentityServiceException("Invalid email address (5)");
    }



    private void checkPassword(String password) throws IdentityServiceException {
        if (password == null)
            throw new IdentityServiceException( "No password set.");

        if (password.length() < 12)
            throw new IdentityServiceException( "Password is too short.");

        if (password.length() > 200)
            throw new IdentityServiceException( "Password is too long.");

        if (!password.trim().equals(password))
            throw new IdentityServiceException( "No spaces in password.");

        if (password.contains(" "))
            throw new IdentityServiceException("No spaces in password.");

        String clean = password.replaceAll("[^\\n\\r\\t\\p{Print}]", "");
        if (!password.equals(clean))
            throw new IdentityServiceException( "No non-printable characters in password.");
    }


    private void sendConfirmationMail
    (   User user
    ) throws Exception {
            blk_MailMessage lmmp = new blk_MailMessage();
            String hLink = web_address + "/register/confirm?token=" + validation(user).getToken();

            lmmp.setRecipient(user.getEmail());
            lmmp.setSubject("Bulk Mailing: Finish Setting Up Your Account");
            lmmp.setBody("<p>Thank you for registering!</p>\n" +
                    "<p>Please click on the below link to activate your account.</p>\n" +
                    "<p><a href='" + hLink + "'>" + hLink + "</a></p>" +
                    "<p><img src='cid:logo'></p>");
            lmmp.addInline("logo", new ClassPathResource("static/images/company.logo.png"));
            mailQueue.enqueue(lmmp); // no saving to DB
    }


    public void send_forgot_password
    (   String email
    )   throws Exception
    {
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new IdentityServiceException("Email address not found"));

        if (!user.validated())
            throw new IdentityServiceException("User never activated (should resend activation email)");

        UserValidation uv = userValidationRepository.findById(user.getId()).orElseThrow(()
                -> new IdentityServiceException("No validation token found. (b)"));

        uv.newPasswordResetToken();
        uv = userValidationRepository.save(uv);
        String hLink = web_address + "/register/new_password?token="+ uv.getPasswordResetToken();

        blk_MailMessage lmmp = new blk_MailMessage();
        lmmp.setRecipient(user.getEmail());
        lmmp.setSubject("Bulk Mailing: Forgot password");
        lmmp.setBody("<p>To reset your password click on the following link:</p>\n" +
                "<p><a href='"+hLink+"'>"+hLink+"</a></p>" +
                "<p><img width=50% src='cid:logo'></p>");
        lmmp.addInline("logo", new ClassPathResource("static/images/company.logo.png"));
        mailQueue.enqueue(lmmp);
    }

    public void updateUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User register_new_user
    (   String email
    ,   String pass
    ) throws Exception {
        checkEmailAddress(email);
        checkPassword(pass);

        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent())
            throw new IdentityServiceException("Email already exists.");

        User newUser = new User();
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(pass));

        if (!passwordEncoder.matches(pass, newUser.getPassword()))
            throw new IllegalArgumentException("The passwordEncoder just failed to match an encoded password!");

        newUser = userRepository.save(newUser);

        UserValidation userValidation = new UserValidation(newUser);
        userValidation.newToken();
        userValidationRepository.save(userValidation);

        sendConfirmationMail(newUser);

        return newUser;
    }

}
