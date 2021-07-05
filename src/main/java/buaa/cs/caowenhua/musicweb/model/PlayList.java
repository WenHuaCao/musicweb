package buaa.cs.caowenhua.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoWenHua
 * @create 2021-06-25  8:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayList {
    private int id;
    private  String builddate;
    private  int user_id;
    private  String picture;
    private  String name;

    public PlayList(String builddate, int user_id, String picture, String name) {
        this.builddate = builddate;
        this.user_id = user_id;
        this.picture = picture;
        this.name = name;
    }
    public boolean isDefaultPlayList(String username){
        return (username + "的默认收藏歌单").equals(name);
    }
    public boolean isUploadPlayList(String username){
        return (username +"上传的音乐").equals(name);
    }
//    @Override
//    public String toString() {
//        return "{" +
//                "\"id\":" + id +
//                ", \"picture\":'" + picture + '\'' +
//                ", \"name\":'" + name + '\'' +
//                '}';
//    }
}

