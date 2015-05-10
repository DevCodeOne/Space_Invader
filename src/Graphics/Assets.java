package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Assets {

    public static BufferedImage loadImg(String src){
        try {
            return ImageIO.read(Assets.class.getResourceAsStream("assets/" + src));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
