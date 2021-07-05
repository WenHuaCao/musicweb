package buaa.cs.caowenhua.musicweb.controller;

import buaa.cs.caowenhua.musicweb.model.PlayList;
import buaa.cs.caowenhua.musicweb.service.Impl.MyService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author CaoWenHua
 * @create 2021-06-29  13:47
 */
@Slf4j
@Controller
@RequestMapping(value = "/")
public class UserController {
    @Autowired
//    IndexServiceImpl indexService;
    MyService myService;

    @GetMapping(value = { "/login"})
    String loginUser() {
        return "user/login";
    }

    @PostMapping(value = {"/login"})
    String login(@RequestParam(value = "username", required = true) String username,
                 @RequestParam(value = "password", required = true) String password,
                 Map map,
                 HttpSession Session) {
        if(myService.login(username,password,map,Session)){
            return "redirect:/index";
        }
        return "/user/login";
    }

    @GetMapping(value = {"/register"})
    String registerUser() {
        return "/user/registeration";
    }

    @PostMapping(value = {"/register"})
    String regiter(@RequestParam(value = "username", required = true) String username,
                   @RequestParam(value = "email", required = true) String email,
                   @RequestParam(value = "password1", required = true) String password1,
                   @RequestParam(value = "password2", required = true) String password2,
                   Map map) {

        if(myService.register(username,password1,password2,email,map) ){
             return "redirect:/login";
        }
        return "/user/registeration";
    }

    @GetMapping(value = {"/","/index"})
    String indexUser(Map map ,HttpSession Session) {
        map.put("block","user/index");
        map.put("style0","nav_hover");
        map.put("recommandMusic",myService.recommend_music());
        map.put("username",Session.getAttribute("username"));
        return "share_layout/base";
    }
    @GetMapping("/logout")
    String logout(Map map ,HttpSession session){
        session.removeAttribute("username");
        session.removeAttribute("userid");
        return "/user/login";
    }
    /**
     * 目前推荐的逻辑是推荐用户自己创建的歌单
     * @param session
     * @return
     */
//    String recommend_music(HttpSession session){
//
//        List<PlayList> res =  indexService.getPlaylists((String)session.getAttribute("User"));
//        log.info(""+ res.size());
//        String jres = JSON.toJSONString(res);
//        log.info("res:" + jres);
//        return jres;
//    }

//    使用模板传参和使用异步请求传参数
//@ResponseBody
//@GetMapping(value ="/recommend_music")
//    List<PlayList> recommend_music(HttpSession session){
//
//        List<PlayList> res =  indexService.getPlaylists((String)session.getAttribute("User"));
//        log.info(""+ res.size());
//        return res;
//    }
}
