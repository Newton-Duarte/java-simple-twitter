package com.newtonduarte.simple_twitter.config;

import com.newtonduarte.simple_twitter.entities.Role;
import com.newtonduarte.simple_twitter.entities.User;
import com.newtonduarte.simple_twitter.repositories.RoleRepository;
import com.newtonduarte.simple_twitter.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var adminRole = roleRepository.findById(Role.Values.ADMIN.getRoleId());
        var adminUser = userRepository.findByName("admin");

        adminUser.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin already exist!");
                },
                () -> {
                    var user = new User();
                    user.setName("admin");
                    user.setPassword(passwordEncoder.encode("adminadmin"));
                    user.setRoles(Set.of(adminRole.get()));
                    userRepository.save(user);
                }
        );
    }
}
