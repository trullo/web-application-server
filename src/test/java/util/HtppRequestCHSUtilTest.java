package util;

import org.junit.Test;

public class HtppRequestCHSUtilTest {
    @Test
    public void getUrl(){
        String line = "GET /index.html HTTP/1.1";

        HtppRequestCHSUtil util = new HtppRequestCHSUtil();
        String url = util.getUrl(line);
        System.out.println(url);
    }
}
