package org.antop.board.login

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class LoginProcessingProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()
        val user = userDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("Invalid password")
        }

        return UsernamePasswordAuthenticationToken(
            username,
            user.password,
            user.authorities,
        )
    }

    override fun supports(authentication: Class<*>?): Boolean = authentication == UsernamePasswordAuthenticationToken::class.java
}
