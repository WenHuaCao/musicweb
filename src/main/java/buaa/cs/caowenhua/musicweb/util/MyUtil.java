package buaa.cs.caowenhua.musicweb.util;

import buaa.cs.caowenhua.musicweb.model.PlayList;
import buaa.cs.caowenhua.musicweb.model.Song;
import com.alibaba.fastjson.JSON;
import javafx.beans.binding.BooleanExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CaoWenHua
 * @create 2021-06-29  19:21
 */
@Component
public class MyUtil {
    public static String class_dir =new File("src/main/resources/static/").getAbsolutePath();
    public Boolean checkUsername(String username){
        //暂时防止注册
//        if(1==1)return false;
        if(username.length() > 30 || username.length() < 3)return false;
        String regEx = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    public  boolean checkEmail(String email){
        if(email.length() > 100)return false;
        String regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public String getDate() {
        String time = null;
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = dateformat.format(date);
        return time;
    }
    public Date toDate(String str)  {
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return dateformat.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return new Date();
        }
    }
    public void createParentPath(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public String writeFile(String uploadpath, String filename, MultipartFile file) throws IOException {
        String file_url = uploadpath+ filename;
        createParentPath(class_dir + uploadpath);
//                Files.copy(song_file,source);
        file.transferTo(new File(class_dir + uploadpath, filename));
        System.out.println(uploadpath+ filename);
        return uploadpath+ filename;
    }

    public static void main(String[] args) {
        String res = new MyUtil().fileToString("upload/sheyin/word/10_光年之外.txt");
        System.out.println(res);
    }

    public boolean isImage(String filepath,String filename){
        File file = new File(class_dir + filepath, filename);
        try {
            // 通过ImageReader来解码这个file并返回一个BufferedImage对象
            // 如果找不到合适的ImageReader则会返回null，我们可以认为这不是图片文件
            // 或者在解析过程中报错，也返回false
            Image image = ImageIO.read(file);
            return image != null;
        } catch(IOException ex) {
            return false;
        }

    }


    public String fileToString(String filepath) {
        filepath =class_dir + filepath;
        filepath = filepath.replace("\\","/");
        File file = new File(filepath);
        String res = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), Charset.forName("GBK")));
            StringBuilder sb = new StringBuilder();
            String str;
            while((str = br.readLine()) != null){
                sb.append("\n" + str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
}
