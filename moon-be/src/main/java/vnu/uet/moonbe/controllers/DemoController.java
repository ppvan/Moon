package vnu.uet.moonbe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vnu.uet.moonbe.dto.APIResponse;

@RestController
@RequestMapping("/test")
public class DemoController {


    @GetMapping("/")
    public ResponseEntity<APIResponse<String>> demoRoute() {
        APIResponse<String> response = APIResponse.err(HttpStatus.BAD_REQUEST, "Bad Demo Object");
        return ResponseEntity.badRequest()
                .body(response);
    }
}
