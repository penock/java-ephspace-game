     package com.croftsoft.core.awt.image;

     import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.croftsoft.core.lang.NullArgumentException;

     /*********************************************************************
     * Static method library for manipulating Image objects.
     *
     * @version
     *   2003-07-17
     * @since
     *   1997-02-14
     * @author
     *   <a href="http://www.croftsoft.com/">David Wallace Croft</a>
     *********************************************************************/

     public final class  ImageLib
     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     {

     public static Image  crop (
       Image   image,
       int     x,
       int     y,
       int     w,
       int     h,
       Applet  applet )
     //////////////////////////////////////////////////////////////////////
     // p274, Java in a Nutshell, 1st Edition
     //////////////////////////////////////////////////////////////////////
     {
       ImageFilter  cropper = new CropImageFilter ( x, y, w, h );

       ImageProducer  prod
         = new FilteredImageSource ( image.getSource ( ), cropper );

       if ( applet != null )
       {
         return applet.createImage ( prod );
       }
       else
       {
         return Toolkit.getDefaultToolkit ( ).createImage ( prod );
       }
     }

     /*********************************************************************
     * Loads an automatic image from a resource file.
     *
     * @param  imageFilename
     *
     *    The path/filename of the resource image, usually within a JAR.
     *
     * @param  transparency
     *
     *    Transparency.BITMASK, .OPAQUE, or .TRANSLUCENT.
     *
     * @param  component
     *
     *    The image will be compatible with this Component.
     *
     * @param  classLoader
     *
     *    If null, component.getClass ( ).getClassLoader ( ) is used.
     *
     * @param  dimension
     *
     *    If null, the image will not be scaled.
     *********************************************************************/
     public static BufferedImage  loadAutomaticImage (
       String       imageFilename,
       int          transparency,
       Component    component,
       ClassLoader  classLoader,
       Dimension    dimension )
       throws IOException
     //////////////////////////////////////////////////////////////////////
     {
       NullArgumentException.check ( imageFilename );

       NullArgumentException.check ( component );

       if ( classLoader == null )
       {
         classLoader = component.getClass ( ).getClassLoader ( );
       }

       BufferedImage  bufferedImage
         = loadBufferedImage ( imageFilename, classLoader );

       GraphicsConfiguration  graphicsConfiguration
         = component.getGraphicsConfiguration ( );

       if ( graphicsConfiguration == null )
       {
         throw new IllegalStateException ( "null graphicsConfiguration" );
       }

       int  width, height;

       if ( dimension == null )
       {
         width  = bufferedImage.getWidth  ( );

         height = bufferedImage.getHeight ( );
       }
       else
       {
         width  = dimension.width;

         height = dimension.height;

         if ( width < 1 )
         {
           throw new IllegalArgumentException (
             "dimension.width < 1:  " + width );
         }

         if ( height < 1 )
         {
           throw new IllegalArgumentException (
             "dimension.height < 1:  " + height );
         }
       }

       BufferedImage  automaticImage
         = graphicsConfiguration.createCompatibleImage (
         width, height, transparency );

       Graphics  graphics = automaticImage.getGraphics ( );

       if ( dimension == null )
       {
         graphics.drawImage ( bufferedImage, 0, 0, null );
       }
       else
       {
         graphics.drawImage ( bufferedImage, 0, 0, width, height, null );
       }

       graphics.dispose ( );

       bufferedImage.flush ( );

       return automaticImage;
     }

     public static BufferedImage  loadBufferedImage (
       String       imageFilename,
       ClassLoader  classLoader )
       throws IOException
     //////////////////////////////////////////////////////////////////////
     {
       // ImageIO.read(URL) seems to be buggy when running within an applet
       // on Linux/Netscape so ImageIO.read(InputStream) is used instead.
       // The problem may be that the InputStream is not flushed or closed.
        
       InputStream  inputStream
         = classLoader.getResourceAsStream ( imageFilename );

       if ( inputStream == null )
       {
         return null;
       }

       BufferedInputStream  bufferedInputStream
         = new BufferedInputStream ( inputStream );

       BufferedImage  bufferedImage = ImageIO.read ( bufferedInputStream );

       bufferedInputStream.close ( );

       return bufferedImage;
     }

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////

     private  ImageLib ( ) { }

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     }
