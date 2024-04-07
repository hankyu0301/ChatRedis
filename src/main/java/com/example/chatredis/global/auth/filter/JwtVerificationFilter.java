package com.example.chatredis.global.auth.filter;

import com.example.chatredis.global.auth.error.AuthenticationError;
import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.auth.utils.AccessTokenRenewalUtil;
import com.example.chatredis.global.auth.utils.Token;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import com.example.chatredis.global.util.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final AccessTokenRenewalUtil accessTokenRenewalUtil;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String jws = jwtTokenizer.getHeaderAccessToken(request);
            if (redisService.hasKeyBlackList(jws))
                throw new CustomException(ExceptionCode.ALREADY_LOGGED_OUT_TOKEN_EXCEPTION);

            Map<String, Object> claims = jwtTokenizer.verifyJws(jws);
            jwtTokenizer.setAuthenticationToContext(claims);
            filterChain.doFilter(request, response);
            /*  액세스 토큰이 만료된 경우 진입*/
        } catch (CustomException ce){
            log.error("### 이미 로그아웃된 토큰입니다.");
            AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "이미 로그아웃된 토큰입니다.", response);
        }catch (ExpiredJwtException eje) {
            try {
                log.error("### 토큰이 만료됐습니다.");
                Token token = accessTokenRenewalUtil.renewAccessToken(request);

                jwtTokenizer.setHeaderAccessToken(response, token.getAccessToken());
                jwtTokenizer.setHeaderRefreshToken(response, token.getRefreshToken());

                Map<String, Object> claims = jwtTokenizer.verifyJws(token.getAccessToken());
                jwtTokenizer.setAuthenticationToContext(claims);
                filterChain.doFilter(request, response);
                /*  리프레쉬 토큰을 찾을 수 없는 경우 */
            } catch (CustomException e) {
                log.error("### 리프레쉬 토큰을 찾을 수 없음");
                AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "리프레쉬 토큰을 찾을 수 없습니다.", response);
            } catch (ExpiredJwtException je) {
                /*  리프레쉬 토큰이 만료된 경우 진입*/
                log.error("### 리프레쉬 토큰이 만료됐습니다.");
                jwtTokenizer.resetHeaderRefreshToken(response);
                AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "만료된 리프레쉬 토큰입니다.", response);
            }
        } catch (MalformedJwtException mje) {
            log.error("### 올바르지 않은 토큰 형식입니다.");
            AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "올바르지 않은 토큰 형식입니다.", response);
        } catch (SignatureException se) {
            log.error("### 토큰의 서명이 잘못 됐습니다. 변조 데이터일 가능성이 있습니다.");
            AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰 비밀키 입니다.", response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }
}