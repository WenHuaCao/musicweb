package buaa.cs.caowenhua.musicweb.mapper;

import buaa.cs.caowenhua.musicweb.model.User;
import javafx.beans.binding.BooleanExpression;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author CaoWenHua
 * @create 2021-06-29  13:53
 */
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(username, password, email, role, file_num)   " +
            "VALUES(#{username}, #{password}, #{email}, #{role}, #{file_num})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    boolean insertUser(User user);
    //判断用户是否存在，存在就返回
    @Select("select id ,username, password, email, role, file_num  " +
            "from user   " +
            "where username  = #{username}")
    List<User> selectUser1(String username);
    //根据id查询用户
    @Select("select id ,username, password, email, role, file_num  " +
            "from user   " +
            "where id  = #{userid}")
    List<User> selectUser2(int userid);
    //
    @Select("select username from user where id = #{id}")
    String id2name(int id);
    //更新用户的上传数量
    @Update("update user  " +
            "set file_num = file_num + 1  " +
            " where id = #{userid}")
    boolean  updateUser1(int userid);
    //从数据中删除用户,暂时用不到
    boolean deleteUser(int userid);
}
