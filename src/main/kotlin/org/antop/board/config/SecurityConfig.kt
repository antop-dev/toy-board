package org.antop.board.config

import org.antop.board.common.constants.LoginConstant
import org.antop.board.common.constants.PostConstant
import org.antop.board.login.AlreadyAuthenticatedFilter
import org.antop.board.login.HtmxLoginFailureHandler
import org.antop.board.login.HtmxLoginSuccessHandler
import org.antop.board.login.HtmxLogoutSuccessHandler
import org.antop.board.login.LoginProcessingService
import org.antop.board.member.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
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
                authorize(PostConstant.Url.SAVE_FORM, authenticated)
                authorize(PostConstant.Url.SAVE_PROCESS, authenticated)
                authorize(PostConstant.Url.EDIT_FORM, authenticated)
                authorize(PostConstant.Url.EDIT_PROCESS, authenticated)
                authorize(PostConstant.Url.REPLY_FORM, authenticated)
                authorize(PostConstant.Url.REPLY_PROCESS, authenticated)
                authorize(anyRequest, permitAll)
            }
            formLogin {
                loginPage = LoginConstant.Url.LOGIN_FORM
                loginProcessingUrl = LoginConstant.Url.LOGIN_PROCESSING
                usernameParameter = LoginConstant.Parameter.USERNAME
                passwordParameter = LoginConstant.Parameter.PASSWORD
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
                rememberMeParameter = LoginConstant.Parameter.REMEMBER_ME
                userDetailsService = userDetailsService(memberService)
            }
        }

        http.addFilterBefore(
            AlreadyAuthenticatedFilter(LoginConstant.Url.LOGIN_FORM),
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
    fun userDetailsService(memberService: MemberService): UserDetailsService = LoginProcessingService(memberService)
}
