package buaa.cs.caowenhua.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoWenHua
 * @create 2021-06-28  20:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayList2 {
    private int id;
    private  String picture;
    private  String name;
    private  String builddate;
    private  int  user_id;
    private  int num = 0;
   public  PlayList2 (PlayList playList){
        id = playList.getId();
        picture = playList.getPicture();
        name = playList.getName();
        builddate = playList.getBuilddate();
        user_id = playList.getUser_id();
        num = 0;
    }
}
