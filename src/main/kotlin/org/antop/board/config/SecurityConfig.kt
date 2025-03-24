package org.antop.board.config

import org.antop.board.common.constants.LoginConsts
import org.antop.board.login.AlreadyAuthenticatedFilter
import org.antop.board.login.HtmxLoginFailureHandler
import org.antop.board.login.HtmxLoginSuccessHandler
import org.antop.board.login.HtmxLogoutSuccessHandler
import org.antop.board.login.LoginProcessingProvider
import org.antop.board.login.LoginProcessingService
import org.antop.board.member.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val memberService: MemberService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf {
                csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
            }
            authorizeHttpRequests {
                authorize("/posts/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            formLogin {
                loginPage = LoginConsts.Url.LOGIN_FORM
                loginProcessingUrl = LoginConsts.Url.LOGIN_PROCESSING
                usernameParameter = LoginConsts.Parameter.USERNAME
                passwordParameter = LoginConsts.Parameter.PASSWORD
                authenticationSuccessHandler = loginSuccessHandler()
                authenticationFailureHandler = loginFailureHandler()
                permitAll()
            }
            logout {
                logoutUrl = "/logout"
                logoutSuccessHandler = logoutSuccessHandler()
                permitAll()
            }
            rememberMe {
                rememberMeParameter = LoginConsts.Parameter.REMEMBER_ME
                userDetailsService = loginService(memberService)
            }
        }

        http.addFilterBefore(
            AlreadyAuthenticatedFilter(LoginConsts.Url.LOGIN_FORM),
            UsernamePasswordAuthenticationFilter::class.java,
        )

        return http.build()
    }

    @Bean
    fun loginSuccessHandler() = HtmxLoginSuccessHandler()

    @Bean
    fun loginFailureHandler() = HtmxLoginFailureHandler()

    @Bean
    fun logoutSuccessHandler() = HtmxLogoutSuccessHandler()

    @Bean
    fun loginProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): LoginProcessingProvider = LoginProcessingProvider(userDetailsService, passwordEncoder)

    @Bean
    fun loginService(memberService: MemberService) = LoginProcessingService(memberService)
}
