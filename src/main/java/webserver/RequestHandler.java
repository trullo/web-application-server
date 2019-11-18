package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtppRequestCHSUtil;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private String url;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World".getBytes();

            log.debug("-------------------------- info -------------------------");
            url = "";
            try {
                getHtmlRequestInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(1);
//            if(url != null && !url.isEmpty()){
//                System.out.println(1-2);
//                body = Files.readAllBytes(new File("./webapp" + url).toPath());
//                System.out.println(new File("./webapp" + url).toPath());
//            }
//            System.out.println(2);
            response200Header(dos, body.length);
//            System.out.println(3);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void getHtmlRequestInfo() throws IOException {
        HtppRequestCHSUtil httpUtil = new HtppRequestCHSUtil();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";
        int i = 1;
        while ((line = br.readLine()) != null) {
            System.out.println(i + " / " + line);
            if (i == 1) url = httpUtil.getUrl(line);
            i++;
        }
    }
}
