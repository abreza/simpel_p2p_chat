package sample;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;

public class Server{
    private ServerSocket serverSocket;

    public Server(int port, String name, Stage stage,Controller controller, Controller waitingController) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server set up successfully");
        for (int i = 0; i < 1; i++) {
            Main.processes.add(new Process(serverSocket.accept(), name, controller));
            ((Stage) waitingController.getWaiting().getScene().getWindow()).close();
            controller.userImage.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p2.png")).toURI()))));
            controller.myImage.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p1.png")).toURI()))));
            stage.show();
            Main.processes.get(Main.processes.size() - 1).read(controller);
        }
    }
}
