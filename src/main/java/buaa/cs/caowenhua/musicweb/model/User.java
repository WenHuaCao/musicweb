package buaa.cs.caowenhua.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoWenHua
 * @create 2021-06-25  8:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private  int id;
    private  String username;
    private  String Password;
    private  String email;
    private  int role;
    private int file_num ;

    public User( String username, String password, String email, int role, int file_num) {
        this.username = username;
        Password = password;
        this.email = email;
        this.role = role;
        this.file_num = file_num;
    }
}
