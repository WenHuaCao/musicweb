package buaa.cs.caowenhua.musicweb.service;

import buaa.cs.caowenhua.musicweb.model.Comment;
import buaa.cs.caowenhua.musicweb.model.PlayList;
import buaa.cs.caowenhua.musicweb.model.PlayList2;
import buaa.cs.caowenhua.musicweb.model.Song;
import buaa.cs.caowenhua.musicweb.model.User;
import buaa.cs.caowenhua.musicweb.util.MyUtil;
import com.alibaba.fastjson.JSON;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.experimental.var;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author CaoWenHua
 * @create 2021-07-05  10:27
 */
public interface MusicService {
    public boolean login(String username, String password, Map map, HttpSession session);
    @Transactional
    public boolean register(String username, String password1, String password2, String email, Map map);
    public String recommend_music();
    public boolean userPage(Map map, HttpSession session, String success);
    public boolean uploadSong(Map map, HttpSession session, String songname, String singer, String album_name, MultipartFile song_file,
                              MultipartFile song_words_file, MultipartFile picture_file, List<String> errors);
    public String displaySong(List<Song> songlist);
    public String displayPlayList(List<PlayList> playlists, int userid);
    public String displayPlayList2(List<PlayList> playlists);
    public boolean search(String keyword, Map map, int userid);
    public boolean musicPlayer(String keyword, Map map);
    public boolean musicPlayerList(int playlistid, Map map);
    public boolean single_playlist_info(int playlistid, Map map);
    public String getMusicDetail(int songid);
    public boolean createList(String name, MultipartFile file, HttpSession session, Map map);
    public int removePlayList(int id, HttpSession session, Map map);
    public String alterPlayList(int id, int userid, String action);
    public String alterSong(int songid, int listid, HttpSession session, String action);
    public String addSongList(int id, int userid);
    public String songList(int userid, String username);
    public boolean Comment(int songid, int userid, String body, Map map);
}



