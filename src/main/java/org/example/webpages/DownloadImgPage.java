package org.example.webpages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class DownloadImgPage {
    public static String getImage(String path){
        String  base64Str = "";
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            base64Str = "data:image/png;base64,"+ encoder.encodeToString(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<img src="+base64Str+" width=\"700\" height=\"427\" />";
    }
}
