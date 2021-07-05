package buaa.cs.caowenhua.musicweb.service.Impl;

import buaa.cs.caowenhua.musicweb.mapper.CommentMapper;
import buaa.cs.caowenhua.musicweb.mapper.PlayListMapper;
import buaa.cs.caowenhua.musicweb.mapper.SongMapper;
import buaa.cs.caowenhua.musicweb.mapper.UserMapper;
import buaa.cs.caowenhua.musicweb.model.Comment;
import buaa.cs.caowenhua.musicweb.model.PlayList;
import buaa.cs.caowenhua.musicweb.model.PlayList2;
import buaa.cs.caowenhua.musicweb.model.Song;
import buaa.cs.caowenhua.musicweb.model.User;
import buaa.cs.caowenhua.musicweb.service.MusicService;
import buaa.cs.caowenhua.musicweb.util.MyUtil;
import com.alibaba.fastjson.JSON;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
 * @create 2021-06-29  18:59
 */
@Service
@Slf4j
public class MyService  implements MusicService {
    @Autowired
    SongMapper songmapper;
    @Autowired
    UserMapper usermapper;
    @Autowired
    PlayListMapper playlistmapper;
    @Autowired
    CommentMapper commentmapper;
    @Autowired
    MyUtil myutil;

    /*
     前置：
     判断用户名对映的用户是否存在，并且密码是否正确
     后置：
     在session中存入userid
     */
    @Override
    public boolean login(String username, String password, Map map, HttpSession session) {
        List<User> result = usermapper.selectUser1(username);
        if (result == null || result.size() == 0) {
            map.put("ErrorMessage", "用户名不存在!");
            return false;
        } else if (!result.get(0).getPassword().equals(password)) {
            map.put("ErrorMessage", "用户名和密码不匹配!");
            return false;
        }
        session.setAttribute("userid", result.get(0).getId());
        session.setAttribute("username", username);
        return true;
    }

    /*
    前置：
    判断用户名是否存在却合法
    判断两次密码是否输入正确
    判断email是否符合格式
    后置：
    插入用户信息
    创建默认的歌单，和上传歌单。
    建立和默认歌单的收藏关系
     */
    @Transactional
    @Override
    public boolean register(String username, String password1, String password2, String email, Map map) {
        if (!Objects.equals(password1, password2)) {
            map.put("ErrorMessage", "两次密码输入不相同");
            return false;
        }
        if (!myutil.checkUsername(username)) {
            map.put("ErrorMessage", "用户名只是包含大小写字母和数字的长度不大于30小于3的字符串");
            return false;
        }
        if (usermapper.selectUser1(username).size() != 0) {
            map.put("ErrorMessage", "用户名已经存在");
            return false;
        }
        if (!myutil.checkEmail(email)) {
            map.put("ErrorMessage", "邮箱格式不正确，邮箱的长度应该小于100");
            return false;
        }
        User user = new User(username, password1, email, 0, 0);
        usermapper.insertUser(user);
        PlayList defaultplaylist = new PlayList(myutil.getDate(), user.getId(), "/images/system_image_file/喜欢的音乐.jpg", username + "的默认收藏歌单");
        PlayList uploadplaylist = new PlayList(myutil.getDate(), user.getId(), "/images/system_image_file/发布的音乐.jpg", username + "上传的音乐");
        playlistmapper.insertPlayList(defaultplaylist);
        playlistmapper.insertPlayList(uploadplaylist);
        //建立和默认歌单的收藏关系
        playlistmapper.collectPlayList(user.getId(), defaultplaylist.getId());
        return true;
    }

    /*
    前置：无
    后置：从所有的歌单中随机挑选，返回三个歌单信息
     */
    @Override
    public String recommend_music() {
        List<PlayList> res = playlistmapper.selectPlayList0();
        Collections.shuffle(res);
        res = res.subList(0, Math.min(res.size(), 3));
        String jres = JSON.toJSONString(res);
        return jres;
    }

    /*
    前置：无
    后置：
    查询用户创建的歌单，和用户收藏的歌单。
    查询每一个用户歌单的歌曲的数量
     */
    @Override
    public boolean userPage(Map map, HttpSession session, String success) {
        //获取用户的歌单
        try {
            int userid = (int) session.getAttribute("userid");
            String username = (String) session.getAttribute("username");
            List<PlayList> buildplaylist = playlistmapper.selectPlayList1(userid);
            List<PlayList> favorplaylist = playlistmapper.selectPlayList2(userid);
            List<PlayList2> buildplaylist2 = new ArrayList<>();
            List<PlayList2> favorplaylist2 = new ArrayList<>();
            for (PlayList p : buildplaylist) {
                PlayList2 p2 = new PlayList2(p);
                p2.setNum(songmapper.selectSongFromPlayList(p.getId()).size());
                buildplaylist2.add(p2);
            }
            for (PlayList p : favorplaylist) {
                PlayList2 p2 = new PlayList2(p);
                p2.setNum(songmapper.selectSongFromPlayList(p.getId()).size());
                favorplaylist2.add(p2);
            }
            map.put("style1", "nav_hover");
            map.put("success", success);
            map.put("build_result", JSON.toJSONString(buildplaylist2));
            map.put("collect_result", JSON.toJSONString(favorplaylist2));
            map.put("username", username);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    前置：写入文件没有错误
    后置：歌曲信息写入数据库，用户上传量加一，添加到用户上传的歌单
     */
    @Override
    public boolean uploadSong(Map map, HttpSession session, String songname, String singer, String album_name, MultipartFile song_file,
                              MultipartFile song_words_file, MultipartFile picture_file, List<String> errors) {
        String username = (String) session.getAttribute("username");
        map.put("username", username);
        User user = usermapper.selectUser1(username).get(0);
        boolean flag = true;
        for (var error : errors) {
            if (!Objects.equals(error, "")) {
                flag = false;
            }
        }
        if (flag == false) { //上传不成功
            for (int i = 0; i < 6; i++) {
                map.put("error" + i, errors.get(i));
            }
            map.put("alertBool", 0);
            map.put("username", username);
            return false;
        } else {
            int userfilenum = user.getFile_num();
            //存储mp3文件
            try {
                long duration = 0;
                String picturepath = "";
                String wordpath = "";
                String file_url = "";
                Song song = new Song(duration, singer, picturepath, album_name, wordpath, 1, user.getId(), songname, file_url);
                songmapper.insertSong(song);
                long id = song.getId();
                file_url = myutil.writeFile("/upload/" + username + "/mp3/", id + "_" + songname + ".mp3", song_file);
                Encoder encoder = new Encoder();
                MultimediaInfo info = encoder.getInfo(new File(MyUtil.class_dir + file_url));
                duration = info.getDuration();
                //存储歌词文件
                wordpath = myutil.writeFile("/upload/" + username + "/word/", id + "_" + songname + ".txt", song_words_file);
                //存储歌词图片
                picturepath = myutil.writeFile("/upload/" + username + "/pic/", id + "_" + picture_file.getOriginalFilename(), picture_file);
                //将相关的信息写入数据库
                songmapper.update0(duration, picturepath, wordpath, file_url, id);
                //用户的上传歌单中添加此关联
                PlayList defaultplaylist = playlistmapper.selectPlayList4(user.getId(), user.getUsername() + "上传的音乐").get(0);
                songmapper.collectSong(defaultplaylist.getId(), (int) song.getId());
                //用户的上传数量加一
                usermapper.updateUser1(user.getId());
            } catch (Exception e) {
                map.put("alertBool", 0);
                return false;
            }
            map.put("alertBool", 1);
            return true;
        }

    }
    /*
    前置条件：keyword合法
    后置：向map中存入，歌曲和搜索结果和歌单的搜索结果.
     */

    @Override
    public String displaySong(List<Song> songlist) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Song song : songlist) {
            Map<String, Object> map = new HashMap();
            map.put("song_id", song.getId());
            map.put("songList_songname", song.getSongname());
            map.put("songList_songauthor", song.getSinger());
            map.put("songList_album", song.getAlbum_name());
            long time = song.getDuration_time() / 1000;
            String t = "" + time / 60 + ":" + time % 60;
            map.put("songList_songtime", t);
//            //获取歌词,读取文件，转化为txt
//            map.put("words",myutil.fileToString(song.getSong_words()));
//            map.put("picture_url",song.getPicture());
//            map.put("song_url",song.getSong_file_url());
            res.add(map);
        }
        return JSON.toJSONString(res);
    }

    /*
    搜索的结果不显示默认的歌单和用户上传的歌单
     */
    @Override
    public String displayPlayList(List<PlayList> playlists, int userid) {
        List<Map<String, Object>> res = new ArrayList<>();
        String username = usermapper.id2name(userid);
        for (PlayList p : playlists) {
            //如果是默认的歌单或者上传歌单就不显示
            if (p.isDefaultPlayList(username) || p.isUploadPlayList(username)) continue;
            Map<String, Object> map = new HashMap();
            map.put("playlist_id", p.getId());
            map.put("songListT_name", p.getName());
            map.put("songLisT_num", songmapper.lengthOfPlayList(p.getId()));
            //由创建者的id获取创建者的名称
            map.put("songList_songauthor", usermapper.id2name(p.getUser_id()));
            //判断当前用户是否收藏歌曲
            map.put("playlist_flag", playlistmapper.isCollect(userid, p.getId()));
            //获得当前歌单包含的歌曲的数量

            res.add(map);
        }
        return JSON.toJSONString(res);
    }

    @Override
    public String displayPlayList2(List<PlayList> playlists) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (PlayList p : playlists) {
            //如果是默认的歌单或者上传歌单就不显示
//            if(p.isDefaultPlayList(username) || p.isUploadPlayList(username)) continue;
            Map<String, Object> map = new HashMap();
            map.put("playlist_id", p.getId());
            map.put("playList_name", p.getName());
            map.put("picture_url", p.getPicture());
            map.put("playList_date", p.getBuilddate());
//            map.put("songLisT_num",songmapper.lengthOfPlayList(p.getId()));
            //由创建者的id获取创建者的名称
            map.put("playList_build_user", usermapper.id2name(p.getUser_id()));
            //判断当前用户是否收藏歌曲
            //获得当前歌单包含的歌曲的数量

            res.add(map);
        }
        return JSON.toJSONString(res);
    }

    @Override
    public boolean search(String keyword, Map map, int userid) {
        //检验keyword合法
        if (keyword.length() < 2 || keyword.length() > 200) {
            map.put("ErrorMessage", "搜索的关键词的长度应该大于2且小于200");
            map.put("songs_result", "");
            map.put("playlists_result", "");
            return false;
        }
        List<Song> songs_result = songmapper.selectSong1(keyword);
        List<PlayList> playLists_result = playlistmapper.selectPlayList3(keyword);
        map.put("songs_result", displaySong(songs_result));
        map.put("playlists_result", displayPlayList(playLists_result, userid));
        return true;
    }

    @Override
    public boolean musicPlayer(String keyword, Map map) {
        if (keyword.length() < 2 || keyword.length() > 200) {
            map.put("ErrorMessage", "搜索的关键词的长度应该大于2且小于200");
            return false;
        }
        List<Song> songs_result = songmapper.selectSong1(keyword);
        map.put("songs_result", displaySong(songs_result));
        return true;
    }

    @Override
    public boolean musicPlayerList(int playlistid, Map map) {
        //获得歌单的所有歌曲
        List<Song> songs = songmapper.selectSongFromPlayList(playlistid);
        map.put("songs_result", displaySong(songs));
        return true;
    }

    @Override
    public boolean single_playlist_info(int playlistid, Map map) {
        //获取id对映的歌单
        List<PlayList> temp = playlistmapper.selectPlayList5(playlistid);
        if (temp.size() == 0) {
            return false;
        }
        PlayList play = temp.get(0);
        //获取一个歌单的所有的歌曲
        List<Song> songs = songmapper.selectSongFromPlayList(play.getId());
        map.put("playlists_result", displayPlayList2(temp));
        map.put("songs_result", displaySong(songs));
        return true;
    }

    @Override
    public String getMusicDetail(int songid) {
        List<Song> temp = songmapper.selectSong2(songid);
        if (temp.size() == 0) return "";
        Map<String, Object> map = new HashMap<>();
        Song song = temp.get(0);


        map.put("songname", song.getSongname());
        map.put("album_name", song.getAlbum_name());
        map.put("song_url", song.getSong_file_url());
        map.put("words", myutil.fileToString(song.getSong_words()));
        map.put("picture_url", song.getPicture());
//            map.put("words",myutil.fileToString(song.getSong_words()));
//            map.put("picture_url",song.getPicture());
//            map.put("song_url",song.getSong_file_url());

        map.put("song_url", song.getSong_file_url());
        return JSON.toJSONString(map);
    }

    @Override
    public boolean createList(String name, MultipartFile file, HttpSession session, Map map) {
        int userid = (int) session.getAttribute("userid");
        String username = (String) session.getAttribute("username");
        List<PlayList> result = playlistmapper.selectPlayList4(userid, name);
        if (result.size() != 0) {
            map.put("ErrorMessage", "歌单名称已经存在!");
            map.put("alertBool", 0);
            map.put("username", 0);
            return false;
        }
        try {

            PlayList playlist = new PlayList(myutil.getDate(), userid, "", name);
            playlistmapper.insertPlayList(playlist);
            String picturepath = myutil.writeFile("/upload/" + username + "/pic2/", playlist.getId() + "_" + file.getOriginalFilename(), file);
            playlistmapper.update0(picturepath, playlist.getId());
            //添加到用户收藏的歌单中
            playlistmapper.collectPlayList(userid, playlist.getId());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            map.put("ErrorMessage", "图片写入错误!");
            map.put("alertBool", 0);
            map.put("username", 0);
            return false;
        }
    }

    //删除或者移除歌单
    @Override
    public int removePlayList(int id, HttpSession session, Map map) {
        String username = (String) session.getAttribute("username");
        int userid = (int) session.getAttribute("userid");
        List<PlayList> temp = playlistmapper.selectPlayList5(id);
        if (temp.size() == 0) return 0;
        PlayList playlist = temp.get(0);
        //默认的歌单无法删除，上传歌单无法删除
        if (playlist.isDefaultPlayList(username) || playlist.isUploadPlayList(username)) return 0;
        //如果是用户自己创建的
        if (playlist.getUser_id() == userid) {
            //从数据库中删除
            playlistmapper.deletePlayList(userid, playlist.getId());
            return 1;
        }
        playlistmapper.removePlayList(userid, playlist.getId());
        return 2;
    }


    @Override
    public String alterPlayList(int id, int userid, String action) {
        if (action.equals("1")) {
            if (playlistmapper.isCollect(userid, id) == 0) {
                playlistmapper.collectPlayList(userid, id);
                return "添加成功!";
            } else {
                return "已经存在，添加失败!";
            }
        } else if (action.equals("0")) {
            if (playlistmapper.isCollect(userid, id) == 1) {
                playlistmapper.removePlayList(userid, id);
                return "删除成功!";
            } else {
                return "不存在关联，删除失败!";
            }
        }
        return "";
    }

    @Override
    public String alterSong(int songid, int listid, HttpSession session, String action) {
        String username = (String) session.getAttribute("username");
        int id = (int) session.getAttribute("userid");
        List<PlayList> playlist = playlistmapper.selectPlayList5(listid);
        if (playlist.size() == 0) return "歌单不存在";
        if (playlist.get(0).getUser_id() != id) {
            return "不是歌单的拥有者!";
        }
        if (playlist.get(0).isUploadPlayList(username)) {
            return "不能删除上传歌单中的歌曲";
        }
        if (action.equals("1")) {
            if (songmapper.isCollect(songid, listid) == 0) {
                songmapper.collectSong(listid, songid);
                return "收藏成功!";
            } else {
                return "已经存在，不要重复收藏!";
            }
        } else {
            if (songmapper.isCollect(songid, listid) == 1) {
                songmapper.removeSong(listid, songid);
                return "取消收藏成功!";
            } else {
                return "不存在，取消收藏失败!";
            }
        }

    }

    @Override
    public String addSongList(int id, int userid) {
        Map<String, String> map = new HashMap<>();
        if (playlistmapper.isCollect(userid, id) == 1) {
            playlistmapper.collectPlayList(userid, id);
            map.put("message", "你成功收藏该歌单!");
        } else {
            playlistmapper.removePlayList(userid, id);
            map.put("message", "你已取消收藏该歌单!");
        }
        return JSON.toJSONString(map);
    }

    @Override
    public String songList(int userid, String username) {
        List<PlayList> playlists = playlistmapper.selectPlayList1(userid);
        List<Map<String, Object>> res = new ArrayList<>();
        for (PlayList p : playlists) {
            //排除上传的歌单
            if (p.isUploadPlayList(username)) continue;
            Map<String, Object> temp = new HashMap<>();
            temp.put("play_listid", p.getId());
            temp.put("list_img", p.getPicture());
            temp.put("list_num", songmapper.lengthOfPlayList(p.getId()));
            temp.put("list_name", p.getName());
            res.add(temp);
        }
        return JSON.toJSONString(res);
    }

    @Override
    public boolean Comment(int songid, int userid, String body, Map map) {
        if (body != null) {
            Comment comment = new Comment(body, userid, songid, myutil.getDate());
            commentmapper.insertComment(comment);
        }
        //检查song
        List<Song> songs = songmapper.selectSong2(songid);
        if (songs.size() == 0) return true;
        Song song = songs.get(0);
        List<Comment> result = commentmapper.select0(songid);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Comment comment : result) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("body", comment.getBody());
            temp.put("time", comment.getCreate_time());
            temp.put("author", usermapper.id2name(comment.getUser_id()));
            res.add(temp);
        }
        Collections.sort(result, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return myutil.toDate(o2.getCreate_time()).compareTo(myutil.toDate(o1.getCreate_time()));
            }
        });
        map.put("songname", song.getSongname());
        map.put("album_name", song.getAlbum_name());
        map.put("picture_url", song.getPicture());
        map.put("singer", song.getSinger());
        map.put("songid", song.getId());
        map.put("username", usermapper.id2name(userid));
        map.put("comments_result", res);
        map.put("counts", res.size());
        return false;
    }

}
