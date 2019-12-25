package com.odakota.tms.system.config.interceptor;

import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String FILE_PRIVATE_KEY = "privatekey.jks";
    private static final String STORE_PASS = "secret";
    private static final String ALIAS = "odakota";

    private final UserSession userSession;

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

    public String generateToken(Long userId, String username, List<Long> roleIds, String tokenId) {
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuer(issuer)
                   .setExpiration(new Date(generateTimeExpiration()))
                   .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                   .claim(Constant.TOKEN_CLAIM_USER_ID, userId)
                   .claim(Constant.TOKEN_CLAIM_USER_NAME, username)
                   .claim(Constant.TOKEN_CLAIM_ROLE_ID, StringUtils.join(roleIds, ","))
                   .claim(Constant.TOKEN_CLAIM_JTI, tokenId)
                   .compact();
    }

    public long generateTimeExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, tokenExpiration);
        return calendar.getTimeInMillis();
    }

    void parseTokenInfoToUserSession(String token) throws UnAuthorizedException {
        try {
            Claims claims = Jwts.parser().setSigningKey(getPrivateKey()).parseClaimsJws(token).getBody();
            userSession.setUserId(Long.parseLong(claims.get(Constant.TOKEN_CLAIM_USER_ID).toString()));
            userSession.setUsername(claims.get(Constant.TOKEN_CLAIM_USER_NAME).toString());
            userSession.setRoleIds(Arrays.stream(claims.get(Constant.TOKEN_CLAIM_ROLE_ID).toString().split(",")).map(
                    Long::parseLong).collect(
                    Collectors.toList()));
            userSession.setTokenId(claims.get(Constant.TOKEN_CLAIM_JTI).toString());
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        }
    }

    private PrivateKey getPrivateKey() {
        return new KeyStoreKeyFactory(new ClassPathResource(FILE_PRIVATE_KEY),
                                      STORE_PASS.toCharArray()).getKeyPair(ALIAS).getPrivate();
    }
}
