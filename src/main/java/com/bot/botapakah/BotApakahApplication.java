package com.bot.botapakah;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import com.linecorp.bot.model.event.message.ImageMessageContent;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {
    public Random random = new Random();
    public String fgokey = "null";
    public int talkCounter = 0;
    public int touchCounter = 0;
    int idxHealth = 0;    // index for health meter
    
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

    public void replyImage(MessageEvent<TextMessageContent> event, String url) {
        ImageMessage image_msg = new ImageMessage(url, url);
        String replyToken = event.getReplyToken();
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, image_msg)).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Cant get image");
        }
    }

    public void processTextEvent(MessageEvent<TextMessageContent> event, String output) {
        String replyToken = event.getReplyToken();
        replyChat(replyToken, output);
    }

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        String msg = messageEvent.getMessage().getText();   // input di chat
        String[] msgSplit = msg.split(" ");
        String command = msgSplit[0].toLowerCase();

        if (command.equals("/apakah")) {
            String answer = getYesNo();
            processTextEvent(messageEvent, answer);
        } if (msg.toLowerCase().contains("/help")) {
            String answer = getInfo();
            processTextEvent(messageEvent, answer);
        } if (command.equals("/talk")) {
            String answer = talk();
            processTextEvent(messageEvent, answer);
        } if (command.equals("/touch")) {
            String respond = touch();
            processTextEvent(messageEvent, respond);
        } if (command.equals("/rin")) {
            String url = getRinURL();
            replyImage(messageEvent, url);
        } if (command.equals("/shrug")) {
            String backslash = "\\";
            String shrug = "¯" + backslash + "_(ツ)_/¯";
            processTextEvent(messageEvent, shrug);
        } if (command.equals("/hacktober")) {
            String msg = "Celebrating Hacktoberfest 2021";
            msg = msg + "!!";
            processTextEvent(messageEvent, msg);
        } if (command.equals("/hacktober image")) {
            String img_url = "https://hacktoberfest.digitalocean.com/_nuxt/img/logo-hacktoberfest-full.f42e3b1.svg";
            processTextEvent(messageEvent, img_url);
        }
    }
    
    public String getRinURL() {
        String urls = 
            "https://ih0.redbubble.net/image.204577888.6515/pp,550x550.jpg;" +
            "https://i.pinimg.com/originals/3a/d0/fa/3ad0faf0bdf0afe02e9118cc286bbc8f.png;" +
            "https://i.ebayimg.com/images/g/dQoAAOSwnpBbRH1F/s-l300.jpg";
        String listURL[] = urls.split(";");
        int idx = random.nextInt(listURL.length);
        return listURL[idx];
    }

    public String touch() {
            String chats = "N-not THERE!;" +
                    "Nnnhh, s-stop;" +
                    "Hya?! W-Wh-WHERE do you think you are touching!;" +
                    "P-please stop;" +
                    "Y-you are the worst;" +
                    "N-not now.." +
                    "Nhaaa! No!;" +
                    "PERVERT!;" +
                    "Eeeep!;";
            String[] chatList = chats.split(";");
            int num = random.nextInt(chatList.length);
            return chatList[num];

    }

    public String talk() {
        String res = "";
        String chats = "Let's do our best today ^_^;" +
                "Something I like? Video games, anime, those kind of stuffs I like them ^_^;" +
                "I don't really like to hang out with other people, but I'd love to hang out with you :);" +
                "Never give up on something you want to achieve ^_^;" +
                "Don't be sad, I'm right here :);" +
                "Let's play video games together someday :D ;" +
                "I'm hungry, gimme food :<";
        String[] chatList = chats.split(";");
//        if (talkCounter < chatList.length - 1) {
//            res = chatList[talkCounter];
//            talkCounter++;
//            return res;
//        }
//        res = chatList[talkCounter];
//        talkCounter = 0; return res;
        int num = random.nextInt(chatList.length); res = chatList[num];
        return res;
    }

    public String getInfo() {
        return "These are the instructions that I can do for u ^_^:" +
                "\n - /help -> Melihat apasaja yang bisa ku lakukan" +
                "\n - /apakah [statement yang kamu ingin tanya]" +
                "\n - /talk -> interractive talk with me :D" +
                "\n - /touch -> Hmph" +
                "\n - /rin -> Get random image of Rin Kagamine from Vocaloid" +
                "\n Have fun trying";
    }

    public String getYesNo() {
        String answers = "Iya,Tidak,Mungkin";
        String[] listAnswer = answers.split(",");
        int num = random.nextInt(listAnswer.length);
        return listAnswer[num];
    }





}
