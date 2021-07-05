package buaa.cs.caowenhua.musicweb.controller;

import buaa.cs.caowenhua.musicweb.service.Impl.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author CaoWenHua
 * @create 2021-07-02  9:45
 */
@Slf4j
@Controller
public class CommentController {
    @Autowired
    MyService myService;

    //返回所有的评论页面
    @GetMapping("/comment")
    String Comment( @RequestParam(value = "songid", required = true) int songid,
                    HttpSession session,Map map
                    ){
        int userid = (int)session.getAttribute("userid");
        myService.Comment(songid,userid,null,map);
        return"comments/comment_list";

    }

    @PostMapping("/comment")
    String Comment2( @RequestParam(value = "song", required = true) int songid,
                     @RequestParam(value = "comment", required = true) String body,
                     HttpSession session,Map map
                     ){
        int userid = (int)session.getAttribute("userid");
        myService.Comment(songid,userid,body,map);
        return"comments/comment_list";
    }
}
