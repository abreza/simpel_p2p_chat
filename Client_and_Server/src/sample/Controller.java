package sample;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import com.vdurmont.emoji.EmojiParser;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class Controller {
    @FXML
    private TextField name;
    @FXML
    private TextField serverIP;
    @FXML
    private BorderPane main;
    @FXML
    private BorderPane waiting;
    @FXML
    public VBox clientStack;
    @FXML
    public VBox serverStack;
    @FXML
    public TextField clientText;
    @FXML
    public TextField serverText;
    @FXML
    public Label clientReceiver;
    @FXML
    public Label serverReceiver;
    @FXML
    public FlowPane ePane;
    @FXML
    public TextField ImageAddress;
    @FXML
    public TextField gifAddress;
    @FXML
    public Button browse;
    @FXML
    public ImageView userImage;
    @FXML
    public Button stop;
    @FXML
    public ImageView myImage;
    @FXML
    public Label myName;

    private boolean login(boolean isClient){
        if(name.getText().equals("")){
            Exception e = new Exception("please write your name");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("please write your name!");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();
            return false;
        }
        else if(isClient && serverIP.getText().equals("")){
            Exception e = new Exception("please write Server IP");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("please write Server IP!");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();
            return false;
        }
        else{
            return true;
        }
    }
    @FXML
    public void serverLogin() throws IOException {
        if(login(false)){
            ((Stage) main.getScene().getWindow()).close();

            Stage stage = new Stage();
            stage.setTitle("Waiting");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Waiting.fxml"));
            stage.setScene(new Scene(fxmlLoader.load(), 250,100));
            stage.show();
            Platform.runLater(() -> {
                try {
                    Stage stage1 = new Stage();
                    stage1.setTitle("Server");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
                    loader.setLocation(getClass().getResource("Server.fxml"));
                    stage1.setScene(new Scene(loader.load(), 600,415));
                    stage1.setMinHeight(415);
                    stage1.setMinWidth(600);
                    stage1.setMaxHeight(415);
                    stage1.setMaxWidth(600);
                    new Server(1377, name.getText(), stage1, loader.getController(), fxmlLoader.getController());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    public void clientLogin() throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        if(login(true)){
            ((Stage) main.getScene().getWindow()).close();
            Stage stage = new Stage();
            stage.setTitle("Client");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
            stage.setScene(new Scene(loader.load(), 600,415));
            stage.setMinHeight(415);
            stage.setMinWidth(600);
            stage.setMaxHeight(415);
            stage.setMaxWidth(600);
            stage.show();
            new Client(1377, name.getText(), loader.getController());
        }
    }

    @FXML
    public void send() throws IOException {
        if(Main.processes.get(0) != null){
            if(clientText != null)
                Main.processes.get(0).sendMessage(Main.processes.get(0).sender, clientText.getText(), this);
            else if(serverText != null)
                Main.processes.get(0).sendMessage(Main.processes.get(0).sender, serverText.getText(), this);
        }
        setText("");
    }

    public TextField getName() {
        return name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public TextField getServerIP() {
        return serverIP;
    }

    public void setServerIP(TextField serverIP) {
        this.serverIP = serverIP;
    }

    public BorderPane getMain() {
        return main;
    }

    public void setMain(BorderPane main) {
        this.main = main;
    }

    public BorderPane getWaiting() {
        return waiting;
    }

    public void setWaiting(BorderPane waiting) {
        this.waiting = waiting;
    }

    public void addStack(String message, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        Label lbl = new Label(EmojiParser.parseToUnicode(message));
        add(lbl, messageIsFromMe);
    }

    public void addStack(byte[] image, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setImage(convertToJavaFXImage(image, 250, 250));
        add(imageView, messageIsFromMe);
    }

    public void addStack(File voice, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        Button button = new Button("Play");
        button.setOnAction(event ->{
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(voice);
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }});
        add(button, messageIsFromMe);
    }


    Boolean isPaused = true;
    public void addStackGif(File file, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        GifDecoder gif = new GifDecoder();
        gif.read(new BufferedInputStream(new FileInputStream(file)));
        Animation animation = new Animation(gif, 1000);
        HBox root = new HBox();
        ImageView imageView = animation.getView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setEffect(new BoxBlur());
        imageView.setOnMouseClicked(event -> {
            if(isPaused){
                imageView.setEffect(null);
                animation.play();
            }
            else{
                imageView.setEffect(new BoxBlur());
                animation.pause();
            }
            isPaused = !isPaused;
        });
        root.getChildren().addAll( imageView);
        add(root, messageIsFromMe);
    }

    public class Animation extends Transition{
        private ImageView imageView;
        private int count;
        private int lastIndex;
        private Image[] sequence;

        public Animation(GifDecoder d, double durationMs) {
            sequence = new Image[ d.getFrameCount()];
            for( int i=0; i < d.getFrameCount(); i++) {
                WritableImage wimg = null;
                BufferedImage bimg = d.getFrame(i);
                sequence[i] = SwingFXUtils.toFXImage( bimg, wimg);
            }
            init(durationMs);
        }

        private void init(double durationMs) {
            this.imageView = new ImageView();
            this.imageView.setImage(sequence[0]);
            this.count = sequence.length;
            setCycleCount(100);
            setCycleDuration(Duration.millis(durationMs));
            setInterpolator(Interpolator.LINEAR);
        }

        protected void interpolate(double k) {
            final int index = Math.min((int) Math.floor(k * count), count - 1);
            if (index != lastIndex) {
                imageView.setImage(sequence[index]);
                lastIndex = index;
            }
        }

        public ImageView getView() {
            return imageView;
        }

    }

    public void setText(String message){
        if(serverText != null){
            serverText.setText(message);
        }
        else if(clientText != null){
            clientText.setText(message);
        }
    }

    public void add(javafx.scene.Node e, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        HBox hBox = new HBox();
        ImageView imageView = new ImageView();
        if(serverStack != null){
            if(messageIsFromMe){
                imageView.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p1.png")).toURI()))));
            }
            else {
                imageView.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p2.png")).toURI()))));
            }
            imageView.prefWidth(20);
            imageView.prefHeight(20);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.maxWidth(20);
            imageView.maxHeight(20);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(e);
            serverStack.getChildren().add(hBox);
            Label label = new Label();
            label.setPrefHeight(20);
            serverStack.getChildren().add(label);
        }
        else if(clientStack != null){
            if(messageIsFromMe){
                imageView.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p2.png")).toURI()))));
            }
            else {
                imageView.setImage(new Image(new FileInputStream(new File((getClass().getResource("icons/p1.png")).toURI()))));
            }
            imageView.prefWidth(20);
            imageView.prefHeight(20);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.maxWidth(20);
            imageView.maxHeight(20);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(e);
            clientStack.getChildren().add(hBox);
            Label label = new Label();
            label.setPrefHeight(20);
            clientStack.getChildren().add(label);
        }
    }

    @FXML
    public void setReceiver(String receiver) {
        if (clientReceiver != null) {
            clientReceiver.setText(receiver);
        }
        else if (serverReceiver != null) {
            serverReceiver.setText(receiver);
        }
    }

    public void emojis() throws IOException {
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 12; i <= 91; i++) {
            labels.add(new Label(EmojiParser.parseToUnicode(EmojiParser.parseToHtmlDecimal("&#1285" + i + ";"))));
        }
        Controller controller = build("Emojis", 200, 300);
        FlowPane emojiPane = controller.ePane;
        for (Label label : labels) {
            label.setOnMouseClicked(event -> {
                setText(label.getText());
                ((Stage) label.getScene().getWindow()).close();
            });
            emojiPane.getChildren().add(label);
        }
    }

    public void choosePhoto() throws IOException {
        Controller controller = build("ImageBrowse", 310, 33);
        controller.browse.setOnAction(event ->{
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(new Image(new FileInputStream(new File((getClass().getResource("images/" + controller.ImageAddress.getText() + ".png")).toURI()))), null);
                ((Stage) controller.browse.getScene().getWindow()).close();
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", s);
                byte[] res  = s.toByteArray();
                s.close();
                Main.processes.get(0).sendImage(Main.processes.get(0).sender, res, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private int numberOfVoices = 0;
    public void receiveVoice() throws IOException, LineUnavailableException, URISyntaxException {
        Recorder recorder = new Recorder();
        File file = new File((getClass().getResource("voices/voice" + numberOfVoices + ".wav")).toURI());
        recorder.beginRecording(file);
        Controller controller = build("Stop", 80, 50);
        controller.stop.setOnAction(event -> {
            recorder.endRecording();
            ((Stage) controller.stop.getScene().getWindow()).close();
            try {
                Main.processes.get(0).sendVoice(Main.processes.get(0).sender, file, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void chooseGif() throws IOException {
        Controller controller = build("GifBrowse", 300, 33);
        controller.browse.setOnAction(event ->{
            try {
                File file = new File((getClass().getResource("gifs/" + controller.gifAddress.getText() + ".gif")).toURI());
                ((Stage) controller.browse.getScene().getWindow()).close();
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                s.close();
                Main.processes.get(0).sendGif(Main.processes.get(0).sender, file, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }


    public Controller build(String fxmlName, int width, int height) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(fxmlName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
        stage.setScene(new Scene(loader.load(), width, height));
        stage.show();
        return loader.getController();
    }


    private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {}
        return image;
    }
}
