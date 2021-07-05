package buaa.cs.caowenhua.musicweb.controller;

import buaa.cs.caowenhua.musicweb.service.Impl.MyService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CaoWenHua
 * @create 2021-06-29  13:47
 */
@Slf4j
@Controller
public class PlayListController {
    @Autowired
    MyService myService;
    @GetMapping("/mymusic")
    String userPage(Map map, HttpSession session,@RequestParam(value = "success", defaultValue = "4") String success ){
        myService.userPage(map,session,success);
        return "music/playlist.html";
    }


    @GetMapping("/single_playlist_info")
    String single_playlist_info(@RequestParam(value = "id", required = true) int  playlistid,
                                Map map, HttpSession session){
        myService.single_playlist_info(playlistid,map);
        map.put("username",session.getAttribute("username"));
        return "music/single_playlist_info";

    }
    @GetMapping("/mymusic/createlist")
    String createList(Map map,HttpSession session){
        map.put("alertBool",2);
        map.put("username",session.getAttribute("username"));
        return "music/createlist";
    }
    //新建歌单
    @PostMapping("/mymusic/createlist")
    String createList(@RequestParam(value = "playlistname", required = true) String name,
                      @RequestPart MultipartFile picture_file,
                      HttpSession session,Map map){
        if(myService.createList(name,picture_file,session,map) ) {
            return "redirect:/mymusic?success=3";
        }
        return "music/createlist";
    }

    //删除歌单，包含取消收藏和删除自建的歌单
    @GetMapping("/remove_playlist")
    String removePlayList(@RequestParam(value = "id", required = true) int id,
                          HttpSession session,Map map){
        switch (myService.removePlayList(id,session,map)) {
            case 0:
             return "forward:/mymusic?success=0";
            case 1:
            return "forward:/mymusic?success=1";
            case 2:
            return "forward:/mymusic?success=2";
        }
        return "forward:/mymusic?success=0";
    }

    //添加歌单，删除歌单
    @GetMapping("/alterPlayList")
    @ResponseBody
    String alterPlayList(@RequestParam(value = "playlist", required = true) int id,
                          @RequestParam(value = "action", required = true) String  action,
                          HttpSession session,Map map) {
       String result = myService.alterPlayList(id,(int)session.getAttribute("userid"),action);
       Map<String ,String > res = new HashMap<>();
       res.put("Action",action.equals("1")?"Add":"Delete");
       res.put("result","Success");//考虑放入result
       return JSON.toJSONString(res);
    }


    @GetMapping("/ajax_addsonglist")
    @ResponseBody
    String addSongList(@RequestParam(value = "songlistid", required = true) int id,
                         HttpSession session,Map map) {
        return myService.addSongList(id,(int)session.getAttribute("userid"));
    }

    //返回除了上传歌单之外的所有歌单
    @GetMapping("/ajax_songlist")
    @ResponseBody
    String songList(HttpSession session,Map map) {
        int userid = (int)session.getAttribute("userid");
        String username = (String )session.getAttribute("username");
        return myService.songList(userid,username);
    }

    }


