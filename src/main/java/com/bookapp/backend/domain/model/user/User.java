package com.bookapp.backend.domain.model.user;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel implements UserDetails {

    private String username;
    private String email;
    private String password;
    private LocalDate birthdayDate;
    private NonNegativeInteger followersCount = new NonNegativeInteger(0);
    private NonNegativeInteger followingCount = new NonNegativeInteger(0);

    public User(String username, String email, String password, LocalDate birthdayDate) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Benutzername darf nicht leer sein");
        }
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthdayDate = birthdayDate;
    }
    public User(Long id){
        this.id = id;
    }

    public User(
            Long id,
            String username,
            String email,
            String password,
            LocalDate birthdayDate,
            NonNegativeInteger followersCount,
            NonNegativeInteger followingCount
    ) {
        super(id);
        this.username = username;
        this.email = email; // String → Email VO
        this.password = password;
        this.birthdayDate = birthdayDate;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public void follow(User other) {
        if (other == null) {
            throw new IllegalArgumentException("Zu verfolgender Benutzer muss angegeben werden");
        }
        if (this.equals(other)) {
            throw new IllegalStateException("Sie können sich nicht selbst folgen");
        }
        this.followingCount = this.followingCount.increment();
        other.followersCount = other.followersCount.increment();
    }

    public void unfollow(User other) {
        if (other == null) {
            throw new IllegalArgumentException("Zu entfolgender Benutzer muss angegeben werden");
        }
        if (this.equals(other)) {
            throw new IllegalStateException("Sie können Ihre eigene Verfolgung nicht aufgeben");
        }
        this.followingCount = this.followingCount.decrement();
        other.followersCount = other.followersCount.decrement();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // örnek sabit rol
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
        // If user can disable his acc
    }
}
