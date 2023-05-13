package com.manjeet.admin.config.customDetailsSerive;

import com.manjeet.admin.user.UserRepository;
import com.manjeet.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=repo.getUserByEmail(email);
        if(user==null)
            throw new UsernameNotFoundException("No Such User Present With Email : "+email);

        return new CustomUserDetails(user);
    }
}
