package cr.ac.una.wssigeceuna.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kendall
 */
public class JwTokenHelper {

    private static JwTokenHelper jwTokenHelper = null;
    private static final long EXPIRATION_LIMIT = 1;
    private static final long EXPIRATION_RENEWAL_LIMIT = 5;
    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private JwTokenHelper() {
    }

    public static JwTokenHelper getInstance() {
        if (jwTokenHelper == null) {
            jwTokenHelper = new JwTokenHelper();
        }
        return jwTokenHelper;
    }

    public String generatePrivateKey(String username) {
        return AUTHENTICATION_SCHEME + Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(false))
                .claim("rnt", AUTHENTICATION_SCHEME + Jwts
                        .builder()
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(getExpirationDate(true))
                        .claim("rnw", true)
                        .signWith(key)
                        .compact())
                .signWith(key)
                .compact();
    }

    public Claims claimKey(String privateKey) throws ExpiredJwtException, MalformedJwtException {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(privateKey)
                .getBody();
    }

    private Date getExpirationDate(boolean renewal) {
        long currentTimeInMillis = System.currentTimeMillis();
        long expMilliSeconds = TimeUnit.MINUTES.toMillis(EXPIRATION_LIMIT);
        if (renewal) {
            expMilliSeconds = TimeUnit.MINUTES.toMillis(EXPIRATION_RENEWAL_LIMIT);
        }
        return new Date(currentTimeInMillis + expMilliSeconds);
    }
}
