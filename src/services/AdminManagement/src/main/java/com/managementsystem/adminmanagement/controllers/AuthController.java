package com.managementsystem.adminmanagement.controllers;

import com.managementsystem.adminmanagement.models.ERole;
import com.managementsystem.adminmanagement.models.Role;
import com.managementsystem.adminmanagement.models.User;
import com.managementsystem.adminmanagement.payload.request.LoginRequest;
import com.managementsystem.adminmanagement.payload.request.SignupRequest;
import com.managementsystem.adminmanagement.payload.response.JwtResponse;
import com.managementsystem.adminmanagement.payload.response.MessageResponse;
import com.managementsystem.adminmanagement.repository.RoleRepository;
import com.managementsystem.adminmanagement.repository.UserRepository;
import com.managementsystem.adminmanagement.security.jwt.JwtUtils;
import com.managementsystem.adminmanagement.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Set<String> tokenBlacklist = new HashSet<>();
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Set a default expiration time for the cookie (e.g., 24 hours)
        int cookieExpirationTime = 24 * 60 * 60; // 24 hours in seconds

        // Set JWT token in a cookie
        Cookie cookie = new Cookie("JWT_TOKEN", jwt);
        cookie.setPath("/api"); // Set the cookie path to the API path
        cookie.setHttpOnly(true); // Make the cookie accessible only via HTTP (not JavaScript)
        cookie.setMaxAge(cookieExpirationTime); // Set cookie expiration time in seconds
        response.addCookie(cookie);


        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("lec")) {
                    Role modRole = roleRepository.findByName(ERole.ROLE_LECTURER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }


        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@CookieValue(name = "JWT_TOKEN") String jwtToken) {
        blacklistToken(jwtToken);

        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    // Check if token is blacklisted
    @GetMapping("/isTokenBlacklisted")
    public ResponseEntity<?> isTokenBlacklisted(@RequestParam String token) {
        if (tokenBlacklist.contains(token)) {
            return ResponseEntity.ok(new MessageResponse("Token is blacklisted"));
        }
        return ResponseEntity.ok(new MessageResponse("Token is valid"));
    }

    // Add token to blacklist and invalidate it
    private void blacklistToken(String token) {
        tokenBlacklist.add(token);

        // Get the secret key used to sign the token
        String secretKey = jwtUtils.getJwtSecret();

        // Invalidate token by setting its expiration to a past date
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);

        Claims claims = parsedToken.getBody();
        claims.setExpiration(new Date(0));

        String invalidatedToken = Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        updateJwtTokenInCookie(invalidatedToken);
    }

    // Update JWT token in the cookie
    private void updateJwtTokenInCookie(String token) {
        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .path("/api")
                .maxAge(0) // Expire cookie immediately
                .build();

        // Send the updated cookie in the response header
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }


}
