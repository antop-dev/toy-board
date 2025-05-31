package org.antop.board.login

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val id: Long,
    val email: String,
    private val password: String,
    val nickname: String,
    val avatar: String?,
) : UserDetails {
    companion object {
        private const val DEFAULT_ROLE_NAME = "ROLE_USER"
        val DEFAULT_ROLE = SimpleGrantedAuthority(DEFAULT_ROLE_NAME)
    }

    override fun getAuthorities() = mutableListOf(DEFAULT_ROLE)

    override fun getPassword() = password

    override fun getUsername() = email
}
