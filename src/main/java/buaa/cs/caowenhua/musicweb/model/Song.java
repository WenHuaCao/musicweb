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
public class Song {
    private  long id;
    private  long duration_time;
    private  String singer;
    private  String picture;
    private String album_name;
    private  String song_words;
    private  int is_valid;
    private int user_id;
    private String songname;
    private String song_file_url;

    public Song(long duration_time, String singer, String picture, String album_name, String song_words, int is_valid, int user_id, String songname, String song_file_url) {
        this.duration_time = duration_time;
        this.singer = singer;
        this.picture = picture;
        this.album_name = album_name;
        this.song_words = song_words;
        this.is_valid = is_valid;
        this.user_id = user_id;
        this.songname = songname;
        this.song_file_url = song_file_url;
    }
}
