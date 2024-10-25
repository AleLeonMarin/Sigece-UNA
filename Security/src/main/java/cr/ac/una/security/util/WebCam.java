package cr.ac.una.security.util;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 *
 * @author kfonsecah
 */
public class WebCam {

    private final Webcam webcam;
    private BufferedImage lastImage;
    private final ImageView imageView;

    public WebCam(ImageView imageView) {
        webcam = Webcam.getDefault();
        this.imageView = imageView;

    }

    public void start() {
        webcam.getLock().disable();
        webcam.open();
        System.out.println("Taking photo");
        new Thread(() -> {
            //System.out.println("Taking photo");
            while (true) {

                try {
                    lastImage = webcam.getImage();
                    updateImageView();
                    Thread.sleep(30);


                } catch (InterruptedException e) {
                    e.printStackTrace();


                    break;

                }

            }
        }).start();


    }

    public void updateImageView() {
        if (lastImage != null) {
            SwingFXUtils.toFXImage(lastImage, null);
            Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(lastImage, null)));

        }
    }

    public void takePhoto() {
        try {
            if (lastImage != null) {
                File file = new File("photo.png");
                ImageIO.write(lastImage, "PNG", file);
                System.out.println("Photo saved successfully!");
            } else {
                System.out.println("No image to save!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save photo!");
        }
    }
    public void stop(){
        webcam.close();
    }
}
