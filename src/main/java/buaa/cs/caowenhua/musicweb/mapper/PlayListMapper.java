package buaa.cs.caowenhua.musicweb.mapper;

import buaa.cs.caowenhua.musicweb.model.PlayList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author CaoWenHua
 * @create 2021-06-29  14:19
 */
@Mapper
public interface PlayListMapper {
    //用户创建歌单
    @Insert("INSERT INTO playlist(builddate, user_id, picture, name )  " +
            "VALUES(#{builddate}, #{user_id}, #{picture}, #{name})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertPlayList(PlayList playist);
    //用户删除歌单，所有收藏过该歌单的用户，全部变为未收藏。级联删除。

    @Delete("delete from  Playlist  " +
            "where id= #{playlistid} and user_id = #{userid}")
    int deletePlayList(int userid,int playlistid);

    @Select("select * from playlist limit 50")
    List<PlayList> selectPlayList0();


    //返回用户创建的歌单
    @Select("select * from playlist  " +
            "where user_id =#{userid}")
    List<PlayList> selectPlayList1(int userid);

    //返回用户创建的歌单
    @Select("select * from playlist  " +
            "where user_id =#{userid} and name =#{name}")
    List<PlayList> selectPlayList4(int userid,String name);

    //返回歌单id对映的歌单列表
    @Select("select * from playlist where id = #{id}")
    List<PlayList> selectPlayList5(int id);
    //返回用户收藏的歌单
    @Select("select * from playlist  " +
            "where id in (   " +
            "select playlist_id from user_playlist  " +
            "where user_id=#{userid} )" )
    List<PlayList> selectPlayList2(int userid);
    //搜索歌单
    @Select("select * from playlist  " +
            "where name like concat('%',#{name},'%') ")
    List<PlayList> selectPlayList3(String name);


    //用户收藏指定的歌单
    @Insert("insert into user_playlist values(null,#{userid},#{playlistid})")
    Boolean collectPlayList(int userid,int playlistid);
    //用户移除指定的歌单
    @Delete("delete from user_playlist where user_id =#{userid} and playlist_id=#{playlistid}")
    Boolean removePlayList(int userid,int playlistid);
    //判断用户是否收藏指定的歌单
    @Select("select count(id) from user_playlist where user_id =#{userid} and playlist_id=#{playlistid}")
    int isCollect(int userid,int playlistid);
    @Update("update playlist set picture = #{pic} where id =#{id}")
    boolean update0(String pic,int id);




}
