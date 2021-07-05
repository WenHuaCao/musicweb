package buaa.cs.caowenhua.musicweb.mapper;

import buaa.cs.caowenhua.musicweb.model.PlayList;
import buaa.cs.caowenhua.musicweb.model.PlayListSong;
import buaa.cs.caowenhua.musicweb.model.Song;
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
public interface SongMapper {
    //插入歌曲
    @Insert("INSERT INTO song(duration_time, singer, picture, album_name, song_words,is_valid,user_id,songname,song_file_url)  " +
            "VALUES(#{duration_time}, #{singer}, #{picture}, #{album_name}, #{song_words},#{is_valid},#{user_id},#{songname},#{song_file_url})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insertSong(Song song);
    //搜索歌曲
    @Select("select *  " +
            "from  song  " +
            "where songname like concat('%',#{name},'%') ")
    List<Song> selectSong1(String name);
    //使用id搜索歌曲
    @Select("select *  " +
            "from  song  " +
            "where id = #{id} ")
    List<Song> selectSong2(int id);
    //删除用户上传的歌曲，需要数据库级联删除和歌单关联的关系。
    @Delete("delete from Song  " +
            "where id = #{songid} and user_id =#{userid} ")
    void deleteSong(int songid,int userid);
    //歌单添加歌曲
    @Insert("insert into playlist_song  " +
            " values( null,#{songid},#{playlistid})")
    void collectSong(int playlistid,int songid);
    //移除歌曲
    @Delete("delete from playlist_song  " +
            " where song_id = #{songid} and playlist_id= #{playlistid}")
    void removeSong(int playlistid,int songid);
    //返回一个歌单收藏的所有的歌曲
    @Select("select * from song  " +
            " where id in ( " +
            "select song_id from playlist_song where playlist_id = #{playlistid} )")
    List<Song> selectSongFromPlayList(int playlistid);

    //判断是否被收藏
//    @Select("select * from playlist_song where song_id= #{songid} and playlist_id=#{playlistid}")
//    List<PlayListSong> isCollect(String songid ,String playlistid);
    @Select("select COUNT(id) from playlist_song where song_id= #{songid} and playlist_id=#{playlistid}")
    int isCollect(int songid ,int playlistid);
    //返回歌单中歌曲的数量
    @Select("select count(id) from playlist_song where  playlist_id=#{playlistid}")
    int lengthOfPlayList(int playlistid);

    @Update("update song set duration_time = #{duration}  ,picture = #{picture} ,song_words = #{word}  " +
            " ,song_file_url = #{file} where id =#{id}" )
    void  update0(long duration,String picture,String word,String file,long id);


}
