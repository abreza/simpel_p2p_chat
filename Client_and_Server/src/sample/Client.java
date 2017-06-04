package sample;

import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

public class Client {

    public Client(int port, String name, Controller controller) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        controller.userImage.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p1.png")).toURI()))));
        controller.myImage.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p2.png")).toURI()))));
        Main.processes.add(new Process(new Socket("localhost", port), name, controller));
        Main.processes.get(Main.processes.size() - 1).read(controller);
    }
}

