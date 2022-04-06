package trycb.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

@Scope("_default")
@Collection("profile")
public class Profile {
  @Id
  @GeneratedValue
  private UUID id;
  private String firstName, lastName;
  private Byte age;
  private String address;

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setFirstName(String name) {
    this.firstName = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setAge(byte age) {
    this.age = age;
  }

  public byte getAge() {
    return age;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }
}
