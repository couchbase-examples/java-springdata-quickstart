package trycb.controller;

import java.util.List;
import java.util.UUID;

import com.couchbase.client.core.error.DocumentNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trycb.model.Profile;
import trycb.repository.ProfileRepository;

@RestController
public class ProfileController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
  @Autowired
  private ProfileRepository profileRepository;

  @GetMapping("/profile")
  public ResponseEntity<List<Profile>> listProfiles(@RequestParam(required = false) String query, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int page) {
    if (pageSize < 1 || pageSize > 10) pageSize = 10;
    PageRequest pageRequest = PageRequest.of(page, pageSize);
    List<Profile> result;
    if (query != null && query.length() > 0) {
      result = profileRepository.findByQuery(query, pageRequest).toList();
    } else {
      result = profileRepository.findAll(pageRequest).toList();
    }

    if (result != null && result.size() > 0) {
      return ResponseEntity.ok(result);
    }

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<Profile> getProfileById(@PathVariable("id") UUID id) {
    if (id == null) {
      return ResponseEntity.status(400).build();
    }
    Profile result = profileRepository.findById(id).orElse(null);
    if (result == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }

  @PostMapping("/profile")
  public ResponseEntity<Profile> saveProfile(@RequestBody Profile profile) {
    profile = profileRepository.save(profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(profile);
  }

  @DeleteMapping("/profile/{id}")
  public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
    try {
    profileRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (DataRetrievalFailureException e) {
      LOGGER.error("Document not found", e);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/test/**")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("test");
  }
}
