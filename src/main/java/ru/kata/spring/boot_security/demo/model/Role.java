package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @JsonProperty("id")
    private Long id;
    
    @Column(name = "role")
    @JsonProperty("role")
    private String role;
    
    @Column(name = "users")
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;
    
    
    public Role() {}
    
    public Role(String role) {
        this.role = role;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
    
    @Override
    public String getAuthority() {
        return getRole();
    }
}
