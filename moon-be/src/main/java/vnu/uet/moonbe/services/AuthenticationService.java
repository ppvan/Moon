package vnu.uet.moonbe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vnu.uet.moonbe.dto.AuthenticationDto;
import vnu.uet.moonbe.dto.RegisterDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.dto.TokenResponseDto;
import vnu.uet.moonbe.exceptions.EmailAlreadyExistsException;
import vnu.uet.moonbe.models.Role;
import vnu.uet.moonbe.models.Token;
import vnu.uet.moonbe.models.TokenType;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.repositories.TokenRepository;
import vnu.uet.moonbe.repositories.UserRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity<?> register(RegisterDto request) {
    if (repository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException("Email already exists");
    }
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);

    ResponseDto responseDto = new ResponseDto();
    responseDto.setStatusCode(HttpStatus.OK.value());
    responseDto.setMessage("User registered success");

    return ResponseEntity.ok(responseDto);
  }

  public ResponseEntity<?> authenticate(AuthenticationDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    TokenResponseDto tokenResponseDto = new TokenResponseDto();
    tokenResponseDto = tokenResponseDto.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();

    return ResponseEntity.ok(tokenResponseDto);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
          .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var tokenResponse = TokenResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), tokenResponse);
      }
    }
  }
}
