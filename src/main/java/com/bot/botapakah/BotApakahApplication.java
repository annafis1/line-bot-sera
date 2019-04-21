package com.bot.botapakah;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Random;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {
    public Random random = new Random();
    public String fgokey = "null";

    @Autowired
    public LineMessagingClient lineMessagingClient;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotApakahApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(BotApakahApplication.class, args);
    }


    public void replyChat(String replyToken, String answer) {
        TextMessage answerInMessage = new TextMessage(answer);
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, answerInMessage)).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }

    public void processChat(MessageEvent<TextMessageContent> event, String output) {
        String replyToken = event.getReplyToken();
        replyChat(replyToken, output);
    }

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        String msg = messageEvent.getMessage().getText().toLowerCase();
        String[] msgSplit = msg.split(" ");
        if (msgSplit[0].equals("/apakah")) {
            String answer = getYesNo();
            processChat(messageEvent, answer);
        } if (msgSplit[0].equals("/help")) {
            String answer = getInfo();
            processChat(messageEvent, answer);
        } if (msgSplit[0].equals("/lihatbmi")) {
            String category = msgSplit[1].toLowerCase();
            String answer = getImageLink(category);
            processChat(messageEvent, answer);
        } if (msgSplit[0].equals("/fgokey")) {
            processChat(messageEvent, fgokey);
        } if (msgSplit[0].equals("/setfgokey")) {
            String newKey = msgSplit[1];
            String answer = setFgoKey(newKey);
            processChat(messageEvent, answer);
        }
    }

    public String setFgoKey(String newKey) {
        fgokey = newKey;
        return String.format("Key berhasil diubah, key kamu sekarang adalah:" +
                "\n\n%s",fgokey);
    }

    public String getImageLink(String query) {
        String links =  "https://images-na.ssl-images-amazon.com/images/I/41FAM8Tx18L._SX466_.jpg," +
                        "https://cdn11.bigcommerce.com/s-hfhomm5/images/stencil/1280x1280/products/180/451/Solid_Red_Sized__25214.1507754519.jpg?c=2&imbypass=on," +
                        "https://stoffe.kawaiifabric.com/images/product_images/large_img/solid-yellow-fabric-Robert-Kaufman-USA-Citrus-179483-1.JPG";
        String res[] = links.split(",");
        int num = random.nextInt(res.length);
        return res[num];
    }

    public String getInfo() {
        return "Berikut beberapa instruksi yang bisa ku jalankan ^_^:" +
                "\n - /help" +
                "\n - /apakah <statement yang kamu ingin tanya>" +
                "\n Selamat mencoba";
    }

    public String getYesNo() {
        String answers = "Iya,Tidak,Mungkin";
        String[] listAnswer = answers.split(",");
        int num = random.nextInt(listAnswer.length);
        return listAnswer[num];
    }





}
