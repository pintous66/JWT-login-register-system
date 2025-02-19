package health.mental.domain.User;

import health.mental.infra.security.PermissionService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.ConstructorParameters;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")

@Getter
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(of = {"id"})

public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;




    @Column(name = "login", unique = true)
    private String login;


    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private UserRole role;

    public User(String login, String password, String role) {
        this.login = login;
        this.password = password;
        try{
            this.role = UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role, please use one of the following: " + List.of(UserRole.values()));
        }

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        PermissionService permissionService = new PermissionService();
        return permissionService.getHierarchyMap().get(role);
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
