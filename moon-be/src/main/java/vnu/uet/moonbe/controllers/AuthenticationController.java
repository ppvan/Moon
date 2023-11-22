package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vnu.uet.moonbe.dto.AuthenticationDto;
import vnu.uet.moonbe.dto.RegisterDto;
import vnu.uet.moonbe.dto.TokenResponseDto;
import vnu.uet.moonbe.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<TokenResponseDto> register(
      @RequestBody RegisterDto request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<TokenResponseDto> authenticate(
      @RequestBody AuthenticationDto request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
}
