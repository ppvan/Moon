package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.dto.AuthenticationDto;
import vnu.uet.moonbe.dto.ProfileDto;
import vnu.uet.moonbe.dto.RegisterDto;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.services.AuthenticationService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterDto request
  ) {
    return service.register(request);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationDto request
  ) {
    return service.authenticate(request);
  }
}