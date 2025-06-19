// package com.telia.Lease_management.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import jakarta.transaction.Transactional;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;

// import com.telia.Lease_management.entity.Users;
// import com.telia.Lease_management.services.UsersService;

// import java.security.Key;
// import java.util.Date;
// import java.util.Map;
// import java.util.function.Function;

// @Transactional
// @AllArgsConstructor
// @Service
// @Slf4j
// public class JwtService {
//     private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
//     private UsersService usersService;
//     //private JwtRepository jwtRepository;

    
//       //Rechercher le token fournie par le user dans la BDD
//     //   public Jwt tokenByValue (String valeur){
//     //     return this.jwtRepository.findByValeur(valeur) 
//     //             .orElseThrow(() -> new RuntimeException("Aucun Token Trouvé! "));
//     // }

//     public Map<String, String> generate(String username) {
//         // log.info("*************************** Resultat-username {}", username);
//         UserDetails user = this.usersService.loadUserByUsername(username);

//         //Génération du Token en fonction de l'utilisateur connecté
//         final Map<String, String> jwtMap = this.generateJwt(user);

//         //Créer une instance du token généré afin de le stocker
//         // final Jwt jwt= Jwt.builder()
//         //                 .valeur(jwtMap.get("bearer"))
//         //                 .desactive(false)
//         //                 .expire(false)
//         //                 .build();
        
//         // this.jwtRepository.save(jwt);

//         return jwtMap;
//     }

//     public String extractUsername(String token) {
//         return this.getClaim(token, Claims::getSubject);
//     }

//     public boolean isTokenExpired(String token) {
//         Date expirationDate = getExpirationDateFromToken(token);
//         return expirationDate.before(new Date());
//     }

//     private Date getExpirationDateFromToken(String token) {
//         return this.getClaim(token, Claims::getExpiration);
//     }

//     private <T> T getClaim(String token, Function<Claims, T> function) {
//         Claims claims = getAllClaims(token);
//         return function.apply(claims);
//     }

//     private Claims getAllClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(this.getKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     private Map<String, String> generateJwt(UserDetails users) {
        
//         final long currentTime = System.currentTimeMillis();
//         // final long expirationTime = currentTime + 30 * 60 * 1000;  // 30 minutes
//         final long expirationTime = currentTime + 12 * 60 * 60 * 1000; // 12 Heures

//         final Map<String, Object> claims = Map.of(
//                 "nom", users.toString(),
//                 Claims.EXPIRATION, new Date(expirationTime),
//                 Claims.SUBJECT, users.getUsername()
//         );

//         final String bearer = Jwts.builder()
//                 .setIssuedAt(new Date(currentTime))
//                 .setExpiration(new Date(expirationTime))
//                 .setSubject(users.getUsername())
//                 .setClaims(claims)
//                 .signWith(getKey(), SignatureAlgorithm.HS256)
//                 .compact();
//         return Map.of("bearer", bearer);
//     }

//     private Key getKey() {
//         final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
//         return Keys.hmacShaKeyFor(decoder);
//     }

//     public void deconnexion() {
        
//     }

// }
