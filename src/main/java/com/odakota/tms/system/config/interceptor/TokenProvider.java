package com.odakota.tms.system.config.interceptor;

import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class TokenProvider {

    private final UserSession userSession;
    @Value("${auth.token.secret-key}")
    private String secretKey;
    @Value("#{new Integer('${auth.token.expiration-time}')}")
    private int tokenExpiration;
    @Value("${auth.token.subject}")
    private String subject;
    @Value("${auth.token.issuer}")
    private String issuer;

    @Autowired
    public TokenProvider(UserSession userSession) {
        this.userSession = userSession;
    }

    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuer(issuer)
                   .setExpiration(new Date(generateTimeExpiration()))
                   .signWith(SignatureAlgorithm.HS512, secretKey)
                   .claim(Constant.TOKEN_CLAIM_USER_ID, userId)
                   .claim(Constant.TOKEN_CLAIM_USER_NAME, username)
                   .compact();
    }

    public long generateTimeExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, tokenExpiration);
        return calendar.getTimeInMillis();
    }

    void parseTokenInfoToUserSession(String token) throws UnAuthorizedException {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            userSession.setUserId(Long.parseLong(claims.get(Constant.TOKEN_CLAIM_USER_ID).toString()));
            userSession.setUsername(claims.get(Constant.TOKEN_CLAIM_USER_NAME).toString());
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        }
    }
}
