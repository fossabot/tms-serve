package com.odakota.tms.system.config.interceptor;

import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.auth.Client;
import com.odakota.tms.enums.auth.TokenType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String SIGNING_KEY = "tms-app-signing-key";
    private static final String SUBJECT = "tms-authentication";
    private static final String ISSUER = "odakota";

    private final UserSession userSession;

    @Value("${auth.token.admin.access-expire}")
    private int adminAccessExpire;

    @Value("${auth.token.admin.refresh-expire}")
    private int adminRefreshExpire;

    @Value("${auth.token.customer.access-expire}")
    private int customerAccessExpire;

    @Value("${auth.token.customer.refresh-expire}")
    private int customerRefreshExpire;

    @Autowired
    public TokenProvider(UserSession userSession) {
        this.userSession = userSession;
    }

    public String generateToken(TokenType tokenType, Client client, String tokenId, Map<String, Object> data) {
        Claims claims = Jwts.claims();
        claims.setId(tokenId);
        claims.setSubject(SUBJECT);
        claims.setIssuer(ISSUER);
        claims.setExpiration(new Date(generateTimeExpiration(tokenType, client)));
        claims.put(Client.class.getSimpleName(), client);
        claims.put(TokenType.class.getSimpleName(), tokenType);
        if (tokenType.equals(TokenType.ACCESS)) {
            if (client.equals(Client.ADMIN)) {
                claims.put(Constant.TOKEN_CLAIM_USER_ID, data.get(Constant.TOKEN_CLAIM_USER_ID));
                claims.put(Constant.TOKEN_CLAIM_ROLE_ID, data.get(Constant.TOKEN_CLAIM_ROLE_ID));
                claims.put(Constant.TOKEN_CLAIM_BRANCH_ID, data.get(Constant.TOKEN_CLAIM_BRANCH_ID));
                claims.put(Constant.TOKEN_CLAIM_BRAND_ID, data.get(Constant.TOKEN_CLAIM_BRAND_ID));
            } else {
                claims.put(Constant.TOKEN_CLAIM_CUSTOMER_ID, data.get(Constant.TOKEN_CLAIM_USER_ID));
            }
        }
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, SIGNING_KEY).setClaims(claims).compact();
    }

    public long generateTimeExpiration(TokenType tokenType, Client client) {
        Calendar calendar = Calendar.getInstance();
        if (tokenType.equals(TokenType.ACCESS)) {
            if (client.equals(Client.ADMIN)) {
                calendar.add(Calendar.MINUTE, adminAccessExpire);
            } else {
                calendar.add(Calendar.MINUTE, customerAccessExpire);
            }
        } else {
            if (client.equals(Client.ADMIN)) {
                calendar.add(Calendar.MINUTE, adminRefreshExpire);
            } else {
                calendar.add(Calendar.MINUTE, customerRefreshExpire);
            }
        }
        return calendar.getTimeInMillis();
    }

    void parseTokenInfoToUserSession(String token) throws UnAuthorizedException {
        try {
            Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
            userSession.setUserId(claims.get(Constant.TOKEN_CLAIM_USER_ID, Long.class));
            userSession.setRoleIds(Arrays.stream(claims.get(Constant.TOKEN_CLAIM_ROLE_ID, String.class).split(",")).map(
                    Long::parseLong).collect(Collectors.toList()));
            userSession.setTokenId(claims.getId());
            userSession.setBranchId(claims.get(Constant.TOKEN_CLAIM_BRANCH_ID, Long.class));
            userSession.setBrandId(claims.get(Constant.TOKEN_CLAIM_BRAND_ID, Long.class));
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        }
    }
}
