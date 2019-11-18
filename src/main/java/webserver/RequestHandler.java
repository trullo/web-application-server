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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug("request line : {}", line);

            if (line == null) {
                return;
            }

            String[] tokens = line.split(" ");

            while (!line.equals("")) {
                line = br.readLine();
                log.debug("header : {}", line);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());

//            log.debug("-------------------------- info -------------------------");
//            url = "";
//            try {
//                getHtmlRequestInfo();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(url != null && !url.isEmpty()){
//                body = Files.readAllBytes(new File("./webapp" + url).toPath());
//                System.out.println(new File("./webapp" + url).toPath());
//            }
            response200Header(dos, body.length);
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

        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line = "";
        int i = 1;
        while (!"".equals(line = br.readLine())) {
            if (line == null) return;
            log.debug(line);
            if (i == 1) {
                url = httpUtil.getUrl(line);
                log.debug(url);
            }
            i++;
        }
    }
}
