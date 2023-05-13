package com.manjeet.admin.config.customDetailsSerive;

import com.manjeet.common.entity.Role;
import com.manjeet.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    User user;

    public CustomUserDetails(User user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities= new ArrayList<>();

        for(Role role: roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getFullname(){
        return user.getFirstName()+" "+user.getLastName();
    }
    public void setFirstName(String firstName){
        user.setFirstName(firstName);
    }
    public void setLastName(String lastName){
        user.setLastName(lastName);
    }

    public boolean hasRole(String hasRole){
        Set<Role> roles = user.getRoles();
        for(Role role: roles){
            if(role.getName().equals(hasRole))
                return true;
        }
        return false;
    }
}
