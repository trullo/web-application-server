package util;

import model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtppRequestCHSUtil {
    public String getUrl(String line){
        String url = "";
        Matcher matcher = Pattern.compile("/(.+).+\\s").matcher(line);
        if (matcher.find()) {
            url = matcher.group(0);
        }
        return url.trim();
    }

    public User userDefaultSetting(String lineToken) {


        return new User("1","2","A","B");
    }
}
