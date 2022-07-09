package fr.pixteam.pixcms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.pixteam.pixcms.Application;
import fr.pixteam.pixcms.managers.AuthManager;

import java.util.Date;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Username cannot be null")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Email cannot be null")
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String usernameLowerCase;

    @NotNull(message = "Account status cannot be null")
    private AccountStatus accountStatus;

    @NotNull(message = "Password cannot be null")
    private String password;

    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    private Date dateCreated;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.usernameLowerCase = username.toLowerCase(Locale.ROOT);
        this.email = email.toLowerCase(Locale.ROOT);
        this.accountStatus = Application.getMailManager().isMailingEnabled() ? AccountStatus.WAITING_VERIFICATION : AccountStatus.VERIFIED;
        this.password = AuthManager.encodePassword(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameLowerCase() {
        return usernameLowerCase;
    }

    public void setUsernameLowerCase(String usernameLowerCase) {
        this.usernameLowerCase = usernameLowerCase;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", usernameLowerCase='" + usernameLowerCase + '\'' +
                ", accountStatus=" + accountStatus +
                ", password='" + password + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
