package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class GraphicsConverter implements TextGraphicsConverter {

    private int width;
    private int height;
    private double ratio;
    private TextColorSchema schema = new ColorSchema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int widthImage = img.getWidth(); // ширина исходной картинки
        int heightImage = img.getHeight(); // высота исходной картинки
        double ratioImage = widthImage > heightImage ? widthImage / heightImage : heightImage / widthImage;

        if (ratioImage > getMaxRatio()) {
            throw new BadImageSizeException(ratioImage, getMaxRatio());
        }

        int newWidth = 0;
        int newHeight = 0;

        boolean mark;
        if (widthImage >= heightImage) {
            mark = true;
        } else {
            mark = false;
        }

        // обе стороны больше заданных значений
        if (mark && (widthImage > getWidth()) && (heightImage > getHeight())) {
            int proportion = widthImage / getWidth();
            newWidth = widthImage / proportion;
            newHeight = heightImage / proportion;

            // хотя бы одна больше заданных параметров
        } else if (mark && (widthImage > getWidth() || heightImage > getHeight())) {
            int proportion = widthImage / getWidth();
            newWidth = widthImage / proportion;
            newHeight = heightImage / proportion;

        } else if (mark && (widthImage < getWidth() || heightImage < getHeight())) {
            newWidth = widthImage;
            newHeight = heightImage;

            //высота больше ширины
        } else {
            int proportion = heightImage / getHeight();
            newWidth = widthImage / proportion;
            newHeight = heightImage / proportion;
        }


        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH); //новая суженая картинка
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder sb = new StringBuilder();
        char[][] arrayChar = new char[newHeight][newWidth];

        char c;
        String result = null;

        sb.append("<div style=\"white-space: nowrap;\">");
        for (int i = 0; i < arrayChar.length; i++) {
            for (int j = 0; j < arrayChar[i].length; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                c = schema.convert(color);

                sb
                        .append(c)
                        .append(c);
            }
            sb
                    .append("\n");
            result = sb.toString();
        }
        sb.append("</div>");
        return result;
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.ratio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getMaxRatio() {
        return ratio;
    }
}
