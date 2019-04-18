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

    @Autowired
    public LineMessagingClient lineMessagingClient;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotApakahApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApakahApplication.class, args);
    }


    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        String msg = messageEvent.getMessage().getText().toLowerCase();
        String[] msgSplit = msg.split(" ");
        if (msgSplit[0].equals("/apakah")) {
            String answer = getRandomAnswer();
            String replyToken = messageEvent.getReplyToken();
            replyChatWithRandomAnswer(replyToken, answer);
        } if (msgSplit[0].equals("/help")) {
            String answer = getInfo();
            String replyToken = messageEvent.getReplyToken();
            replyChatWithRandomAnswer(replyToken, answer);
        }
    }

    public String getInfo() {
        return "Berikut beberapa instruksi yang bisa ku jalankan ^_^:" +
                "\n - /help" +
                "\n - /apakah <statement yang kamu ingin tanya>" +
                "\n Selamat mencoba";
    }

    public String getRandomAnswer() {
        String res = "";
        Random random = new Random();
        int num = random.nextInt();
        if (num % 2 == 0) {
            return "ya";
        } else {
            return "tidak";
        }
    }

    public void replyChatWithRandomAnswer(String replyToken, String answer) {
        TextMessage answerInMessage = new TextMessage(answer);
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, answerInMessage)).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }



}
