package cl.bice.vida.botonpago.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


public class ImagenesUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ImagenesUtil.class);

    /**
     * Genera Timbre de Pago
     * @param fecha
     * @throws IOException
     */
    public static void generarTimbrePagado(String fecha, String formato, URL urlfileoriginal, String fileabsolutepath) throws IOException {
        logger.debug("generarTimbrePagado(String fecha=" + fecha + ", String formato=" + formato + ", String fileabsolutepath=" + fileabsolutepath + ") - iniciando");    
        //Crea y images y la mete dentro de un buffer
        BufferedImage images = ImageIO.read(urlfileoriginal);        
        int w = images.getWidth(), h = images.getHeight();
        BufferedImage total = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = total.createGraphics();
        g.drawImage(images, 0, 0, Color.WHITE, null);
        
        //Agrego un texto dentro de la imagen        
        String text = fecha;
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setPaint(Color.GRAY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);    
        TextLayout tl = new TextLayout(text, g.getFont(), g.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        float x = 25;
        float y = 95;
        //Posicion de pegado del texto
        tl.draw(g, x, y);
        g.dispose();
        
        //Rotar Buffer de Imagen en 30 Grados
        BufferedImage nuevo = rotate(total, 30, Color.white);        

        //Grabar Buffer en un Archivo
        FileOutputStream salida =  new FileOutputStream(fileabsolutepath);
        ImageIO.write(nuevo, formato, salida);
        
        // Liberando 
        images.flush();
        total.flush();
        nuevo.flush();
        salida.close();
        
        logger.debug("generarTimbrePagado(String fecha=" + fecha + ", String formato=" + formato + ", String fileabsolutepath=" + fileabsolutepath + ") - termina");
    }


    public static BufferedImage rotate (BufferedImage bi, double degrees, Color backgroundColor)    {
            logger.debug("rotate(BufferedImage bi=" + bi + ", double degrees=" + degrees + ", Color backgroundColor=" + backgroundColor + ") - iniciando");
            
            // adjust the angle that was passed so it's between 0 and 360 degrees
            double positiveDegrees = (degrees % 360) + ((degrees < 0) ? 360 : 0);
            double degreesMod90 = positiveDegrees % 90;
            double radians = Math.toRadians(positiveDegrees);
            double radiansMod90 = Math.toRadians(degreesMod90);
            
            // don't bother with any of the rest of this if we're not really rotating
            if (positiveDegrees == 0)
                    return null;
            
            // figure out which quadrant we're in (we'll want to know this later)
            int quadrant = 0;
            if (positiveDegrees < 90)
                    quadrant = 1;
            else if ((positiveDegrees >= 90) && (positiveDegrees < 180))
                    quadrant = 2;
            else if ((positiveDegrees >= 180) && (positiveDegrees < 270))
                    quadrant = 3;
            else if (positiveDegrees >= 270)
                    quadrant = 4;
            
            // get the height and width of the rotated image (you can also do this
            // by applying a rotational AffineTransform to the image and calling
            // getWidth and getHeight against the transform, but this should be a
            // faster calculation)
            int height = bi.getHeight();
            int width = bi.getWidth();
            double side1 = (Math.sin(radiansMod90) * height) + (Math.cos(radiansMod90) * width);
            double side2 = (Math.cos(radiansMod90) * height) + (Math.sin(radiansMod90) * width);
            
            double h = 0;
            int newWidth = 0, newHeight = 0;
            if ((quadrant == 1) || (quadrant == 3)) {
                    h = (Math.sin(radiansMod90) * height);
                    newWidth = (int)side1;
                    newHeight = (int)side2;
            } else {
                    h = (Math.sin(radiansMod90) * width);
                    newWidth = (int)side2;
                    newHeight = (int)side1;
            }
            
            
            // figure out how much we need to shift the image around in order to
            // get the origin where we want it
            int shiftX = (int)(Math.cos(radians) * h) - ((quadrant == 3) || (quadrant == 4) ? width : 0);
            int shiftY = (int)(Math.sin(radians) * h) + ((quadrant == 2) || (quadrant == 3) ? height : 0);
                        
            // create a new BufferedImage of the appropriate height and width and
            // rotate the old image into it, using the shift values that we calculated
            // earlier in order to make sure the new origin is correct
            BufferedImage newbi = new BufferedImage(width, height, bi.getType());
            Graphics2D g2d = newbi.createGraphics();
            g2d.setBackground(backgroundColor);
            g2d.clearRect(0, 0, newWidth, newHeight);
            g2d.rotate(radians);
            //g2d.drawImage(bi, shiftX, -shiftY, null);
            g2d.drawImage(bi, 25, -45, null);
            logger.debug("rotate(BufferedImage bi=" + bi + ", double degrees=" + degrees + ", Color backgroundColor=" + backgroundColor + ") - termina");
            return newbi;
    }
    
    public static void main(String[] args) throws IOException {
        logger.info("main(String[] args=" + args + ") - iniciando");
        generarTimbrePagado("01-09-2008","PNG",new URL("file:D:\\Java\\wks\\jdeveloper\\bice\\webpagobicepublico\\public_html\\images\\styles\\transaccion_pagada_90grados.jpg"),"c:\\Temp\\timbre.png");
        logger.info("main(String[] args=" + args + ") - termina");
    }
    
}




