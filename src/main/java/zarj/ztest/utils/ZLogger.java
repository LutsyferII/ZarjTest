package zarj.ztest.utils;

import zarj.ztest.ZarjTest;

import java.awt.*;

public class ZLogger {
    public static void Error(String text){
        ZarjTest.LOGGER.error("{ZarjTest} "+text, Color.RED);
    }
    public static void Text(String text){
        ZarjTest.LOGGER.info("{ZarjTest} "+text, Color.ORANGE);
    }
}
