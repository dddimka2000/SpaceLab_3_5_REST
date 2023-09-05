package org.example.security.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserEntity userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserEntity userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    public String generateRefreshToken(UserEntity userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    private String buildToken(Map<String, Object> extraClaims, UserEntity userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token, UserEntity userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getLogin())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        //TODO МБ ТУТ?
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

//@Component
//@Log4j2
//public class JwtTokenProvider {
//    @Value("${jwt.token.secret}")
//    private String secret;
//    @Value("${jwt.token.expired}")
//    private long validityInMilliseconds;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @PostConstruct
//    protected void init() {
//        secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }
//
//    public String createToken(String username, List<UserRole> roles) {
//
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("roles", getRoleNames(roles));
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//        return Jwts.builder()//
//                .setClaims(claims)//
//                .setIssuedAt(now)//
//                .setExpiration(validity)//
//                .signWith(SignatureAlgorithm.HS256, secret)//
//                .compact();
//    }
//
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    public String getUsername(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public String resolveToken(HttpServletRequest req) {
//        String authHeader = req.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Basic ")) {
//            log.info("authHeader: " + authHeader);
//            String token = authHeader.substring(6); // Извлекаем токен без префикса "Basic "
//            log.info("Извлеченный токен: " + token);
//            return token;
//        }
//
//        return null;
//    }
//    public boolean validateToken(String token) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//
//            if (claims.getBody().getExpiration().before(new Date())) {
//                return false;
//            }
//
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new JwtAuthenticationException("JWT token is expired or invalid");
//        }
//    }
//    private List<String> getRoleNames(List<UserRole> userRoles) {
//        List<String> result = new ArrayList<>();
//
//        userRoles.forEach(role -> {
//            result.add(role.name());
//        });
//
//        return result;
//    }
//}