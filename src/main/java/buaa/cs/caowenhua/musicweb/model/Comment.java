package buaa.cs.caowenhua.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoWenHua
 * @create 2021-07-02  9:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private String create_time;
    private  int user_id;
    private  int song_id;

    private  String body;

    public Comment(String body, int userid, int songid, String builddate) {
        this.body = body;
        this.user_id = userid;
        this.song_id = songid;
        this.create_time = builddate;
    }
}
