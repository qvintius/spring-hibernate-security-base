package mainpackage.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtService {

    @Value("UZ0iH2rRs32GeSatOS19IX3m3Dj0e4wjlY1fVyhQHjxFqBb")
    private String jwtSecret;

    //токен обновится засчет рефреш токена тчобы не вводить заново логин и пароль
    public JwtAuthenticationDto generateAuthToken(String email) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(getRefreshToken(email));
        return jwtDto;
    }


    //подпись для целостности и подлинности токена
    public SecretKey getSingInKey() {
//        byte[] encodedKey = Base64.getDecoder().decode(jwtSecret.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //создание токена их емейл действительного в течение минуты
    public String generateJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        String jwtToken = Jwts.builder().subject(email).expiration(date).signWith(getSingInKey()).compact();
        return jwtToken;
    }

    public String getRefreshToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().setSubject(email).signWith(getSingInKey()).compact();
    }

    //обновление после истечения времени
    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    //достать из токена емейл
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSingInKey()).build().parseClaimsJws(token).getPayload();
        return claims.getSubject();
    }

    //валидность токена
    public Boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(getSingInKey()).build().parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired jwtException {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            log.error("Unsupported jwtException {}", e.getMessage());
        } catch (MalformedJwtException e){
            log.error("Malformed jwtException {}", e.getMessage());
        } catch (SecurityException e){
            log.error("SecurityException {}", e.getMessage());
        } catch (Exception e){
            log.error("Exception {}", e.getMessage());
        }
        return false;
    }

}
