package ua.edu.ratos.security;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ua.edu.ratos.dao.entity.Student;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString(callSuper = true)
public class AuthenticatedUser extends User {

    private Long userId;

    private String name;

    private String surname;

    protected AuthenticatedUser(Long userId, String name, String surname, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.name = name;
        this.surname = surname;
    }

    public static AuthenticatedUser create(@NonNull final Student student) {
        return new AuthenticatedUser(
                student.getStudId(),
                student.getUser().getName(),
                student.getUser().getSurname(),
                student.getUser().getEmail(),
                new String(student.getUser().getPassword()),
                getAuthorities(student.getUser())
        );
    }

    static Set<GrantedAuthority> getAuthorities(ua.edu.ratos.dao.entity.User user){
        Set<GrantedAuthority> authorities =  new HashSet<>();
        if (user.getRoles()==null || user.getRoles().isEmpty()) return authorities;
        user.getRoles().forEach(a -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(a);
            authorities.add(grantedAuthority);
        });
        return authorities;
    }
}
