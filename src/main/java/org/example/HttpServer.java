package org.example;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HttpServer {
    public static void main(String[] args) {
        Map<String, String> urls = new HashMap<>();
        Javalin app = Javalin
                            .create(config -> config.addStaticFiles("/public"))
                            .start(9000);



        app.get("/", ctx -> ctx
                                    .contentType("text/html")
                                    .render("/public/index.html"));

        app.post("/", ctx -> {
            String url = ctx.formParam("url");
            String shortUrl = generateShortUrl(12);
            urls.put(shortUrl, url);
            System.out.println(url + "    " + shortUrl);

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Bitly</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <p>Short URL</p>\n" +
                    "<a href=\"" + "http://localhost:9000/" + shortUrl + "\">" +
                    "http://localhost:9000/" + shortUrl + "</a\n" +
                    "</body>\n" +
                    "</html>";


            ctx.contentType("text/html")
                    .result(html);
        });

        app.get("/:short", ctx -> {
            String key = ctx.pathParam("short");
            String url = urls.get(key);

            if(key.equals("list")) {
                String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Bitly</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <p>list of URL's</p>\n" +
                    "placeholder" +
                    "</body>\n" +
                    "</html>";

            StringBuilder urlsTableHtml = new StringBuilder();
            urls.forEach((k, v) -> {
                urlsTableHtml.append("<div>")
                        .append("<a href=\"").append("http://localhost:9000/").append(k).append("\">")
                        .append("http://localhost:9000/").append(k).append("</a")
                        .append("</div>\n");
            });
            html = html.replaceAll("placeholder", urlsTableHtml.toString());

            ctx.contentType("text/html")
                    .result(html);
            return;
            }

            ctx.contentType("text/html");
            ctx.redirect(url);
        });


//        app.get("/list", ctx -> {
//            String html = "<!DOCTYPE html>\n" +
//                    "<html lang=\"en\">\n" +
//                    "<head>\n" +
//                    "    <meta charset=\"UTF-8\">\n" +
//                    "    <title>Bitly</title>\n" +
//                    "</head>\n" +
//                    "<body>\n" +
//                    "    <p>list of URL's</p>\n" +
//                    "placeholder" +
//                    "</body>\n" +
//                    "</html>";
//
//            StringBuilder urlsTableHtml = new StringBuilder();
//            urls.forEach((k, v) -> {
//                urlsTableHtml.append("<div>")
//                        .append("<a href=\"").append("http://localhost:9000/").append(k).append("\">")
//                        .append("http://localhost:9000/").append(k).append("</a")
//                        .append("</div>\n");
//            });
//            html = html.replaceAll("placeholder", urlsTableHtml.toString());
//
//            ctx.contentType("text/html")
//                    .result(html);
//        });

    }

    private static String generateShortUrl(int length) {
        char[] chars = new char[length];
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            char c = (char) ((random.nextInt(26) + 1) | 64);
            chars[i] = c;
        }
        System.out.println(new String(chars));
        return new String(chars);
    }
}
