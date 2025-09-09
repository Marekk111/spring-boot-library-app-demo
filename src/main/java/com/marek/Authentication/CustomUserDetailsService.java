package com.marek.Authentication;

import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

      Collection<? extends GrantedAuthority> authorities =
              user.getRoles()
                      .stream()
                      .map(role -> new SimpleGrantedAuthority(role.toString()))
                      .toList();

    return new User(user.getUsername(), user.getPassword(), authorities);
  }

  public String registerUser(UserDTO userDTO) {
      UserEntity user = userMapper.toEntity(userDTO);
      if (userRepository.existsByUsername(user.getUsername())) {
          return "Error: Username is already taken!";
      }
      UserEntity newUser = new UserEntity(
              null,
              user.getUsername(),
              encoder.encode(user.getPassword()),
              user.getRoles(),
              null
      );
      userRepository.save(newUser);
      return "User registered successfully!";
  }
}
