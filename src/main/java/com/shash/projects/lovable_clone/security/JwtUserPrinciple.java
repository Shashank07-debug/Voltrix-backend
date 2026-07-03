package com.shash.projects.lovable_clone.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtUserPrinciple (
        Long userId,
        String username,
        List<GrantedAuthority> authorities
){
}
