package com.davidauz.blkm_interface.service;

import com.davidauz.blkm_interface.entity.User;

import java.util.Optional;

public interface UserService {
    void saveUser(User userDto);

    public Optional<User> confirmUser(String confirmationToken) throws IdentityServiceException;

    public Optional<User> confirmUserNewPwd(String confirmationToken) throws IdentityServiceException;

    public void send_forgot_password(String email) throws Exception;

    public boolean user_pwd_matches(String email, String paswd);
    public User register_new_user(String email, String pass) throws Exception;

    void updateUser(User user);
}
