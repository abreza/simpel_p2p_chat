package sample;

import java.io.*;
import java.net.Socket;

public class Process {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public String sender = null;

    public Process(Socket socket, String sender, Controller controller) throws IOException, ClassNotFoundException {
        controller.myName.setText(sender);
        this.sender = sender;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject(sender);
        controller.setReceiver((String) objectInputStream.readObject());
    }

    public void read(Controller controller){
        new Thread(() -> {
            try {
                while(objectInputStream != null){
                    ((ChatMessage) (objectInputStream.readObject())).show(false, controller);
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendImage(String sender, byte[] image, Controller controller) throws IOException {
        sendData(new ChatMessage(image, sender), controller);
    }

    public void sendVoice(String sender, File voice, Controller controller) throws IOException {
        sendData(new ChatMessage(voice, sender), controller);
    }

    public void sendGif(String sender, File gif, Controller controller) throws IOException {
        sendData(new ChatMessage(gif, sender, 0), controller);
    }

    public void sendMessage(String sender, String message, Controller controller) throws IOException {
        sendData(new ChatMessage(message, sender), controller);
    }

    public void sendData(ChatMessage chatMessage, Controller controller) throws IOException {
        chatMessage.show(true, controller);
        objectOutputStream.writeObject(chatMessage);
    }
}