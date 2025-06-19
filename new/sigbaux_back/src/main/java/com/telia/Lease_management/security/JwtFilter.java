// package com.telia.Lease_management.security;

// import jakarta.servlet.ServletException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.telia.Lease_management.entity.Users;
// import com.telia.Lease_management.services.UsersService;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;

// @Service
// public class JwtFilter extends OncePerRequestFilter {

//     private UsersService usersService;
//     private JwtService jwtService;

 
//     public JwtFilter(UsersService usersService, JwtService jwtService) {
//         this.usersService = usersService;
//         this.jwtService = jwtService;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         String token = null;
//         String username = null;
//         boolean isTokenExpired = true;
//         //Jwt tokenDansLaBdd = null;

//         // Bearer eyJhbGciOiJIUzI1NiJ9.eyJub20iOiJBY2hpbGxlIE1CT1VHVUVORyIsImVtYWlsIjoiYWNoaWxsZS5tYm91Z3VlbmdAY2hpbGxvLnRlY2gifQ.zDuRKmkonHdUez-CLWKIk5Jdq9vFSUgxtgdU1H2216U
//         final String authorization = request.getHeader("Authorization");
//         if(authorization != null && authorization.startsWith("Bearer ")){
//             token = authorization.substring(7);
//             //tokenDansLaBdd= this.jwtService.tokenByValue(token);
//             isTokenExpired = jwtService.isTokenExpired(token);
//             username = jwtService.extractUsername(token);
//         }

//         if(!isTokenExpired 
//                     && username != null 
//                     //&& tokenDansLaBdd.getUtilisateur().getEmail().equals(username)
//                     && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = usersService.loadUserByUsername(username);
//             UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//             SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//         }

//         filterChain.doFilter(request, response);
//     }
// }

