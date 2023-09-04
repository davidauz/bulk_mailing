package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.service.AppLog;
import com.davidauz.blkm_interface.entity.User;
import com.davidauz.blkm_interface.entity.UserValidation;
import com.davidauz.blkm_interface.impl.UserDetailsServiceImpl;
import com.davidauz.blkm_interface.repository.UserRepository;
import com.davidauz.blkm_interface.repository.UserValidationRepository;
import com.davidauz.blkm_interface.service.IdentityServiceException;
import com.davidauz.blkm_interface.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

	public static final long TOKEN_TYPE_SUBSCRIBE = 56
	,   TOKEN_TYPE_PASSWORD_RESET=57
	;
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

    @Autowired
    private UserDetailsServiceImpl user_details_service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserValidationRepository userValidationRepository;

	@Autowired private AppLog applog;

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
		}catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("s_error", "POPOOOOO   There's something wrong with your data,\n" +
					"Sorry!"
			);
		}
		UserValidation userValidation = userValidationRepository.findByTokenAndTokenType(token, TOKEN_TYPE_SUBSCRIBE).get();
		applog.log("User `"+userValidation.getUser()+"` validated");
		return "login";
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
		User existing = userRepo.findByEmail(user.getEmail()).orElseThrow(()->new IllegalArgumentException("Unknown user `"+user.getEmail()+"`"));
		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "auth/register";
		}
		userRepo.save(user);
		applog.log("User `"+user.getEmail()+"` registered");
		return "redirect:auth/register?success";
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
			if(0==password1.length() || 0==password2.length())
				return "<p>Invalid password</p>";
			if (!password1.equals(password2))
				return "<p>The password you supplied do not match...</p><p>Please try again</p>";
			userService.register_new_user(email, password1);
			applog.log("New user `"+email+"`");
			return "<p>Thank you!  Please follow the instructions in the email we just sent you.</p>" +
					"<p>(P.S. it's just a link you have to click)</p>"; // returning literal string because of @ResponseBody
		}catch(Exception e){
			logger.error(e.getMessage());
			return "<p>Something went wrong... sorry!</p>" +
					"<p>The error message is: "+e.getMessage()+"</p>";
		}
	}


	//------------------------  NEW PASSWORD    ------------------------
	@GetMapping("/register/req_npwd")
	public String showNewPwdForm
	(   Model model
	){
		return "newpwd";
	}



	// ------------------------ forgot password ------------------------
	@PostMapping(path="/login/do_forgot")
	public String handle_forgot_pwd_form
	(   @RequestParam(name = "email") String email
	,   Model model
	,   HttpServletResponse response
	) { //  the model can supply attributes used for rendering views
		try {
			userService.send_forgot_password(email);
		}catch(Exception e){
// not giving clues to the user if the email entered was real or not
			logger.info(email+":"+e.getMessage());
		}
		applog.log("User `"+email+"` sent forgot pwd email");
		return show_forgot_page("An email has been sent to your address." +
				"Click the link to set a new password (within 24 hours).", model);
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
		return "auth/forgot"; // -> /login/do_forgot
	}


	@GetMapping("register/new_password")
	public String new_password
	(   @RequestParam("token") String token
	,   Model model
	) {
		try {
			userService.confirmPwd(token).orElseThrow(() -> new IdentityServiceException("Bad token"));
			model.addAttribute("s_message", "Password has been reset." +
					"Please insert a new one."
			);
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			model.addAttribute("token", token);
			return "auth/reset_pwd";
		}catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("s_error", "ZIPOOOO    There's something wrong with your data,\n" +
					"Sorry!"
			);
		}
		return "Error 12";
	}



	@PostMapping(path = "/register/newpwd", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody // returning raw content and not a template name
	public String manageNewPwdForm
	(   @RequestParam("orig_pwd") String orig_pwd
	,   @RequestParam("password1") String password1
	,   @RequestParam("password2") String password2
	,   Model model
	){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			if ( ! userService.user_pwd_matches(userDetails.getUsername(),orig_pwd) )
				throw new IdentityServiceException("Invalid password");
			return do_changepwd(userDetails.getUsername(), password1,password2,model);
		} catch (Exception e) {
			return ("<p>CACOOOOO   There's something wrong with your data, Sorry!</p><p>"+e.getMessage()+"</p>");
		}
	}

	private String do_changepwd
	(   String userName
	,   String password1
	,   String password2
	,   Model model
	) throws IdentityServiceException {
		if (!password1.equals(password2))
			throw new IdentityServiceException("The passwords you supplied do not match...</p><p>Please try again");
		User user = userRepo.findByEmail(userName).orElseThrow(()->new IllegalArgumentException("User not found"));
		user.setPassword(passwordEncoder.encode(password1)); // TODO: this should be in UserService
		userRepo.save(user);
		return "<p>Thank you!  You may now proceed to the login page.</p>";
	}


	@PostMapping(path = "/register/pwd_reset", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody // returning raw content and not a template name
	public String managePwdReset
	(   @RequestParam("password1") String password1
	,   @RequestParam("password2") String password2
	,   @RequestParam("datoken") String datoken
	,   Model model
	){
		try {
			UserValidation uv = userValidationRepository.findByTokenAndTokenType(datoken, TOKEN_TYPE_PASSWORD_RESET).get(); //.orElseThrow(()-> new IdentityServiceException("Bad token")); //get();
			User user=userRepo.findById(uv.getUser()).get(); // .orElseThrow(()-> new IdentityServiceException("Bad token"));
			return do_changepwd(user.getEmail(), password1,password2,model);
		} catch (Exception e) {
			return ("<p>CIPOOOO There's something wrong with your data, Sorry!</p><p>"+e.getMessage()+"</p>");
		}
	}

}
