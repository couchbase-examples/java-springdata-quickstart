package org.couchbase.quickstart.springdata.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

import lombok.Getter;
import lombok.Setter;

@Scope("_default")
@Collection("profile")
@Getter
@Setter
public class Profile {
  @Id
  @GeneratedValue
  private UUID id;
  private String firstName;
  private String lastName;
  private Byte age;
  private String address;
 
}
