package buaa.cs.caowenhua.musicweb.mapper;

import buaa.cs.caowenhua.musicweb.model.Comment;
import buaa.cs.caowenhua.musicweb.model.PlayList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author CaoWenHua
 * @create 2021-07-02  9:46
 */
@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment(create_time, user_id, song_id, body )   " +
            "VALUES(#{create_time}, #{user_id}, #{song_id}, #{body})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertComment(Comment comment);
    //选取一个歌曲关联的所有的评论
    @Select("select * from comment where song_id =#{songid}")
    List<Comment> select0(int songid);
}
