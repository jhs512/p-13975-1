package com.back.global.security

import com.back.global.rsData.RsData
import com.back.standard.util.Ut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter,
    private val customOAuth2LoginSuccessHandler: CustomOAuth2LoginSuccessHandler,
    private val customOAuth2AuthorizationRequestResolver: CustomOAuth2AuthorizationRequestResolver
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            // 요청 경로별 인가 설정
            authorizeHttpRequests {
                authorize("/favicon.ico", permitAll)
                authorize("/h2-console/**", permitAll)
                authorize(HttpMethod.GET, "/api/*/posts/{id:\\d+}", permitAll)
                authorize(HttpMethod.GET, "/api/*/posts", permitAll)
                authorize(HttpMethod.GET, "/api/*/posts/{postId:\\d+}/comments", permitAll)
                authorize(HttpMethod.GET, "/api/*/posts/{postId:\\d+}/comments/{id:\\d+}", permitAll)
                authorize("/api/*/members/login", permitAll)
                authorize("/api/*/members/logout", permitAll)
                authorize(HttpMethod.POST, "/api/*/members", permitAll)
                authorize("/api/*/adm/**", hasRole("ADMIN"))
                authorize("/api/*/**", authenticated)
                authorize(anyRequest, permitAll)
            }

            // iframe 허용
            headers {
                frameOptions { sameOrigin = true }
            }

            // CSRF, FormLogin, Logout, HttpBasic 비활성화
            csrf { disable() }
            formLogin { disable() }
            logout { disable() }
            httpBasic { disable() }

            // 세션 정책: STATELESS
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            // OAuth2 로그인 설정
            oauth2Login {
                authenticationSuccessHandler = customOAuth2LoginSuccessHandler

                authorizationEndpoint {
                    authorizationRequestResolver = customOAuth2AuthorizationRequestResolver
                }
            }

            // 커스텀 인증 필터 등록
            addFilterBefore<UsernamePasswordAuthenticationFilter>(customAuthenticationFilter)

            // 인증 실패, 권한 부족 처리
            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint { _, response, _ ->
                    response.contentType = "application/json;charset=UTF-8"
                    response.status = 401
                    response.writer.write(
                        Ut.json.toString(
                            RsData<Void>("401-1", "로그인 후 이용해주세요.")
                        )
                    )
                }

                accessDeniedHandler = AccessDeniedHandler { _, response, _ ->
                    response.contentType = "application/json;charset=UTF-8"
                    response.status = 403
                    response.writer.write(
                        Ut.json.toString(
                            RsData<Void>("403-1", "권한이 없습니다.")
                        )
                    )
                }
            }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            // 허용할 오리진
            allowedOrigins = listOf("https://cdpn.io", "http://localhost:3000")
            // 허용할 HTTP 메서드
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")
            // 쿠키/인증 정보 포함 허용
            allowCredentials = true
            // 허용할 헤더
            allowedHeaders = listOf("*")
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/api/**", configuration)
        }
    }
}