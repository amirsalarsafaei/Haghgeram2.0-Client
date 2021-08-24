package Models.Events;

import java.time.LocalDate;

public class AuthEvent {
    public String username, password, name, family_name, bio, email, phone;
    public LocalDate birthDate;
    public byte[] image;
    public AuthEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public AuthEvent(String username, String password, String name, String family_name, String bio, String email,
                     String phone,  LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.family_name = family_name;
        this.bio = bio;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

}
