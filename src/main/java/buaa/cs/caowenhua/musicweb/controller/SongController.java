package buaa.cs.caowenhua.musicweb.controller;

import buaa.cs.caowenhua.musicweb.mapper.SongMapper;
import buaa.cs.caowenhua.musicweb.model.PlayList2;
import buaa.cs.caowenhua.musicweb.model.Song;
import buaa.cs.caowenhua.musicweb.service.Impl.MyService;
import com.alibaba.fastjson.JSON;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author CaoWenHua
 * @create 2021-06-29  13:47
 */
@Slf4j
@Controller
public class SongController {
    @Autowired
    MyService myService;
    @GetMapping("/upload")
    String uploadMusic(Map map, HttpSession session){
        String username  = (String)session.getAttribute("User");
        map.put("username",username);
        map.put("alertBool",2);
        map.put("error","");
        map.put("style2","nav_hover");
        return "music/upload";
    }
    @PostMapping("/upload")
    String upload(Map map, HttpSession session,
                  @RequestParam(value = "songname", required = false,defaultValue = "") String songname,
                  @RequestParam(value = "singer", required = false,defaultValue = "") String singer,
                  @RequestParam(value = "album_name", required = false,defaultValue = "")String album_name,
                  @RequestPart MultipartFile song_file,
                  @RequestPart MultipartFile  song_words_file,
                  @RequestPart MultipartFile picture_file
    ) throws IOException {
        List<String> errors = new ArrayList<String>(){{
            add("");add("");add("");add("");add("");add("");add("");
        }};
        if(Objects.equals(album_name,"")) errors.set(0,"专辑名不能为空");
        if(Objects.equals(songname,"")) errors.set(1,"歌名不能为空");
        if(Objects.equals(singer,"")) errors.set(2,"歌手名不能为空");
        if(Objects.equals(song_file,null)  ) errors.set(3,"歌曲文件未选择");
        if(!Objects.equals(song_file,null) ) {
            String filename  = song_file.getOriginalFilename();
            try{
                if(filename.length() < 5||filename.toUpperCase().substring(filename.length() -4).equals(".mp3") ){
                    errors.set(3,"格式不是MP3");
                }
            }catch (Exception exc){
                log.error("上传文件的名称有误!");
                errors.set(3,"格式不是MP3");
            }
        }
        if(Objects.equals(song_words_file,null)) errors.set(4,"歌词txt文件未选择或者格式不是txt");
        if(Objects.equals(picture_file,null)) errors.set(5,"歌曲配图文件未选择");
        myService.uploadSong(map,session,songname,singer,album_name,song_file,song_words_file,picture_file,errors);
        return "music/upload";
    }
    @GetMapping(value =  "/Search")
    String search(@RequestParam(value = "value", required = true) String keyword,
            Map map, HttpSession session){
        map.put("value",keyword);
        map.put("username",(String)session.getAttribute("username"));
        myService.search(keyword,map,(int)session.getAttribute("userid"));
        return "music/Search";
    }
    //
    @GetMapping("/music_player_song")
    String musicPlayer(@RequestParam(value = "value", required = true) String keyword,
                       @RequestParam(value = "songid", required = true) int  songid,
                       Map map, HttpSession session){
        myService.musicPlayer(keyword,map);
        map.put("songid",songid);
        map.put("username",session.getAttribute("username"));
        return "music/music_player";
    }


    @GetMapping("/get_music_detail")
    @ResponseBody
    String getMusicDetail(@RequestParam(value = "id", required = true) int songid
                        ){
        return myService.getMusicDetail(songid);
    }
    @GetMapping("/music_player_playlist")
    String musicPlayerList(@RequestParam(value = "playlistid", required = true) int playlistid,
                           @RequestParam(value = "songid", required = true) int  songid,
                           Map map, HttpSession session){
        myService.musicPlayerList(playlistid,map);
        map.put("songid",songid);
        map.put("username",session.getAttribute("username"));
        return "music/music_player";
    }
    @GetMapping("/ajax_removesong")
    @ResponseBody
    String removeSong(@RequestParam(value = "songlistid", required = true) int playlistid,
                           @RequestParam(value = "songid", required = true) int  songid,
                           Map map, HttpSession session){
        int userid = (int)session.getAttribute("userid");
        String result = myService.alterSong(songid,playlistid,session,"0");
        Map<String,String> res = new HashMap<>();
        res.put("message",result);
        return JSON.toJSONString(res);

    }
    @GetMapping("/ajax_addsong")
    @ResponseBody
    String addSong(@RequestParam(value = "songlistid", required = true) int playlistid,
                           @RequestParam(value = "songid", required = true) int  songid,
                           Map map, HttpSession session){
        int userid = (int)session.getAttribute("userid");
        String result = myService.alterSong(songid,playlistid,session,"1");
        Map<String,String> res = new HashMap<>();
        res.put("message",result);
        return JSON.toJSONString(res);
    }
}
