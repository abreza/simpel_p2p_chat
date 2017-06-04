package sample;

import javafx.application.Platform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

public class ChatMessage implements Serializable{
    private String message;
    private String sender;
    private byte[] image;
    private File gif;
    private MessageType messageType;
    private File voice;

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.messageType = MessageType.TEXT;
    }

    public ChatMessage(byte[] image, String sender) {
        this.image = image;
        this.sender = sender;
        this.messageType = MessageType.IMAGE;
    }

    public ChatMessage(File gif, String sender, int x) {
        this.gif = gif;
        this.sender = sender;
        this.messageType = MessageType.GIF;
    }

    public ChatMessage(File voice, String sender) {
        this.voice = voice;
        this.sender = sender;
        this.messageType = MessageType.VOICE;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void show(boolean messageIsFromMe, Controller controller){
        switch (messageType){
            case TEXT:
                textShow(messageIsFromMe, controller);
                break;
            case IMAGE:
                imageShow(messageIsFromMe, controller);
                break;
            case VOICE:
                voiceShow(messageIsFromMe, controller);
                break;
            case GIF:
                gifShow(messageIsFromMe, controller);
                break;
        }
    }

    public void textShow(boolean messageIsFromMe, Controller controller) {
        try{
            if (messageIsFromMe) {
                controller.addStack(getMessage(), messageIsFromMe);
                System.out.println("-    " + getMessage());
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getMessage(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getMessage());
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void imageShow(boolean messageIsFromMe, Controller controller){
        try{
            if (messageIsFromMe) {
                controller.addStack(getImage(), messageIsFromMe);
                System.out.println(getSender() + " send a image");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getImage(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a image");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
    public void voiceShow(boolean messageIsFromMe, Controller controller){
        try{
            if (messageIsFromMe) {
                controller.addStack(getVoice(), messageIsFromMe);
                System.out.println(getSender() + " send a voice");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getVoice(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a voice");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void gifShow(boolean messageIsFromMe, Controller controller){
        try{
            if (messageIsFromMe) {
                controller.addStackGif(getGif(), messageIsFromMe);
                System.out.println(getSender() + " send a gif");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStackGif(getGif(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a gif");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public byte[] getImage() {
        return image;
    }

    public File getVoice() {
        return voice;
    }

    public File getGif() {
        return gif;
    }
}

