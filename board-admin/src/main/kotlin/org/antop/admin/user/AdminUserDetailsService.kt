package org.antop.admin.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdminUserDetailsService(
    private val adminService: AdminService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val admin =
            adminService.findAdmin(username)
                ?: throw UsernameNotFoundException("User not found: $username")
        return User
            .builder()
            .username(admin.id)
            .password(admin.password)
            .roles("ADMIN")
            .build()
    }
}
