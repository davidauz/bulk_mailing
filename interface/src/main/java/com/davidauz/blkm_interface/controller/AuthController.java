package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_interface.entity.User;
import com.davidauz.blkm_interface.impl.UserDetailsServiceImpl;
import com.davidauz.blkm_interface.repository.UserValidationRepository;
import com.davidauz.blkm_interface.service.IdentityServiceException;
import com.davidauz.blkm_interface.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl user_details_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserValidationRepository userValidationRepository;


    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping(path = "/register/confirm", produces = MediaType.TEXT_HTML_VALUE)
    public String register_new
    (   @RequestParam("token") String token
    ,   Model model
    ) {
        try {
            userService.confirmUser(token).orElseThrow(() -> new IdentityServiceException("Bad token"));
            model.addAttribute("s_message", "Registration is complete!" +
                    "Thank you for your time and patience." +
                    "You can now proceed to login with your email and password."
                    );
            return "login";
        }catch(Exception e){
            logger.error(e.getMessage());
            model.addAttribute("s_error", "There's something wrong with your data,\n" +
                    "Sorry!"
            );
            return "login";
        }
    }


    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration
    (   @Valid @ModelAttribute("user") User user
    ,   BindingResult result
    ,   Model model
    ){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "auth/register";
        }
        userService.saveUser(user);
        return "redirect:auth/register?success";
    }


    @PostMapping(path="/login/do_forgot") // answers to POST
    public String handle_forgot_pwd_form
    (   @RequestParam(name = "email") String email
            ,   Model model
            ,   HttpServletResponse response
    ) { //  the model can supply attributes used for rendering views
        try {
            userService.send_forgot_password(email);
        }catch(Exception e){
// not giving clues to the user if the email entered was real or not
        }
        return show_forgot_page("An email has been sent to your address (if found in our DB).  Follow the instructions to set a new password.", model);
    }



    @GetMapping(path="/login/forgot")
    public String show_forgot_page
    (   @RequestParam(name = "smessage", defaultValue = "") String smessage
    ,   Model model
    ) { //  the model can supply attributes used for rendering views
        if(0<smessage.length()){
            model.addAttribute("message", true);
            model.addAttribute("messageString", smessage);
        }
        return "auth/forgot"; // -> forgot.html
    }


    @GetMapping("/register/new_password")
    public String showNewPwdForm
    (   @RequestParam("token") String token
    ,   Model model
    ){
        try {
            User user=userService.userPasswordToBeReset(token).orElseThrow(() -> new IdentityServiceException("Bad token"));
            model.addAttribute("token", token);
        } catch (IdentityServiceException e) {
            model.addAttribute("s_error", "There's something wrong with your data, Sorry!");
        }
        return "newpwd";
    }



    @PostMapping(path = "/register/newpwd", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String manageNewPwdForm
    (   @RequestParam("token") String token
    ,   @RequestParam("password1") String password1
    ,   @RequestParam("password2") String password2
    ,   Model model
    ){
        try {
            User user=userService.confirmUserNewPwd(token).orElseThrow(() -> new IdentityServiceException("Bad token"));
            if (!password1.equals(password2))
                return "<p>The password you supplied do not match...</p><p>Please try again</p>";
            user.setPassword(password1);
            userService.updateUser(user);
            return "<p>Thank you!  You may now proceed to the login page.</p>";
        } catch (IdentityServiceException e) {
            return ("There's something wrong with your data, Sorry!");
        }
    }

    @PostMapping(path = "/register/data", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String register_new
    (   @RequestParam("email") String email
    ,   @RequestParam("password1") String password1
    ,   @RequestParam("password2") String password2
    ,   Model model
    ) {
        try {
            if (!password1.equals(password2))
                return "<p>The password you supplied do not match...</p><p>Please try again</p>";
            userService.register_new_user(email, password1);
            return "<p>Thank you!  Please follow the instructions in the email we just sent you.</p>" +
                    "<p>(P.S. it's just a link you have to click)</p>"; // returning literal string because of @ResponseBody
        }catch(Exception e){
            logger.error(e.getMessage());
            return "<p>Something went wrong... sorry!</p>" +
                    "<p>The error message is: "+e.getMessage()+"</p>";
        }
    }


}
