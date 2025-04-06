package org.antop.board.login

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val id: Long,
    val email: String,
    private val _password: String,
    val nickname: String,
) : UserDetails {
    companion object {
        private const val DEFAULT_ROLE_NAME = "ROLE_USER"
        val DEFAULT_ROLE = SimpleGrantedAuthority(DEFAULT_ROLE_NAME)
    }

    override fun getAuthorities() = mutableListOf(DEFAULT_ROLE)

    override fun getPassword() = _password

    override fun getUsername() = email
}
