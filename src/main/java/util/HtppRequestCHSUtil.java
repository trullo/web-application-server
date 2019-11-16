package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtppRequestCHSUtil {
    public String getUrl(String line){
        String url = "";
        Matcher matcher = Pattern.compile("/(.+).+(html)+").matcher(line);
        if (matcher.find()) {
            url = matcher.group(0);
        }
        return url;
    }
}
