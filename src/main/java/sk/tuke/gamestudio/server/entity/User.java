package sk.tuke.gamestudio.server.entity;

import javax.persistence.*;

@Entity
@Table(name = "player")
@NamedQueries({
    @NamedQuery(
        name = "User.login",
        query = "SELECT u FROM User u WHERE u.username=:username AND u.passwd=:passwd"
    ),
    @NamedQuery(
        name = "User.check",
        query = "SELECT u FROM User u WHERE u.username=:username"
    )
})
public class User {
    @Id
    @GeneratedValue
    private int ident;

    @Column(unique = true)
    private String username;

    private String passwd;

    public User() {

    }

    public User(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public int getId() {
        return ident;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + ident +
                ", username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
