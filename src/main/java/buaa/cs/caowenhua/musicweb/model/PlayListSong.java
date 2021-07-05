package buaa.cs.caowenhua.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoWenHua
 * @create 2021-06-30  13:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayListSong {
    private int  id;
    private int song_id;
    private int playlist_id;
}
