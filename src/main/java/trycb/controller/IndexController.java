package trycb.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
  @GetMapping("/")
  public ResponseEntity<Void> index() {
    // Redirecting to swagger-ui home page
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/swagger-ui/index.html");
    return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
  }
}

