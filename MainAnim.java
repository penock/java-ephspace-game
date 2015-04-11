/*
 * Created on Apr 24, 2005
 * Based on code from croftsoft.src.com.croftsoft.apps.mars.Main class.
 */

/**
 * @author 05pme
 * The MainAnim class implements the necessary lifecycle functions of the program.
 * It knows about the GameWorld and AnimatedComponent (which will be running
 * the main game loop). It basically sets up the entire game: reads preferences
 * and creates the GameWorld, AnimatedComponent, UserController, and 
 * ComputerController.
 */

 

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import com.croftsoft.core.animation.AnimatedApplet;
import com.croftsoft.core.animation.AnimationInit;
import com.croftsoft.core.animation.collector.BooleanRepaintCollector;
import com.croftsoft.core.animation.painter.SpacePainter;
import com.croftsoft.core.util.loop.WindowedLoopGovernor;
    
    public class MainAnim extends AnimatedApplet
    {
 
    // String constants used to display Applet info
    private static final String  VERSION
      = "2005-05-12";

    private static final String  TITLE
      = "Lost in Asteroid Space";

    private static final String  APPLET_INFO
      = "\n" + TITLE + "\n"
      + "Copyright 2005 Ashok Pillai, Adrian Martinez, Philip Enock\n"
      + "Williams College\n"
      + "Version " + VERSION + "\n"
	  + "Image credits: \n"
	  + "Enemy ships: Ari Feldman http://www.flyingyogi.com/fun/spritelib.html\n"
	  + "User ship: Hakan Nilsson http://www.pixeljoint.com/pixels/profile.asp?id=401\n"
	  + "Asteroids: www.edwardh.com/jshooter/ help/asteroid.gif\n"
	  + "Some code also taken from David Wallace Croft's CroftSoft library"
      /*+ "Licensed under the Academic Free License version 1.2\n"*/;

    //frame constants
    private static final String  FRAME_TITLE = TITLE;
    private static final String  FRAME_ICON_FILENAME = null;
     private static final Dimension  FRAME_SIZE = new Dimension(800, 400);
    private static final String  SHUTDOWN_CONFIRMATION_PROMPT = "Close " + TITLE + "?";

    // animation constants
    
    /** frames per second */
    private static final Color   BACKGROUND_COLOR
      = new Color ( 255, 152, 109 );

    private static final Color   FOREGROUND_COLOR
      = Color.BLACK;

    private static final Font    FONT
      = new Font ( "Arioso", Font.BOLD, 10 );

    private static final Cursor  CURSOR
      = new Cursor ( Cursor.CROSSHAIR_CURSOR );

    private static final double  TIME_FACTOR_DELTA = 0.1;

	private static final double STAR_DENSITY = .001;
    
    /**	
     * Launches the MainAnim class, which launches the rest of the program.
     */
    public static void  main ( String [ ]  args ) {
      launch ( new MainAnim ( ) );
    }

    /**
     * creates and sets up a new animation initializer given the default settings constants,
     * and returns it
     * @return new AnimationInit object 
     */
    private static AnimationInit createAnimationInit() {
      
    	AnimationInit animationInit = new AnimationInit ( );

    	animationInit.setAppletInfo ( APPLET_INFO );
    	animationInit.setBackgroundColor ( BACKGROUND_COLOR );
    	animationInit.setCursor ( CURSOR );
    	animationInit.setFont ( FONT );
    	animationInit.setForegroundColor ( FOREGROUND_COLOR );
    	animationInit.setFrameIconFilename ( FRAME_ICON_FILENAME );
    	animationInit.setFrameSize ( FRAME_SIZE );
    	animationInit.setFrameTitle ( FRAME_TITLE );
    	animationInit.setShutdownConfirmationPrompt (SHUTDOWN_CONFIRMATION_PROMPT );

    	return animationInit;
    }

    /**
     * constructor: creates new AnimationInit object 
     * which is passed to superclass constructor
     *
     */
    public  MainAnim ( ) {
      super (createAnimationInit());
    }
    
    // interface Lifecycle methods
    
    /**
     * initializes animation
     */
    public void  init ( ) {
      super.init ( );

      // model

      // animation, main loop class creation
      animatedComponent.setLoopGovernor(new WindowedLoopGovernor(Constants.FRAME_RATE));
      animatedComponent.setRepaintCollector(new BooleanRepaintCollector());
      
      animatedComponent.addMouseListener (new MouseAdapter() {
        public void mousePressed(MouseEvent arg0) {
            animatedComponent.requestFocus();
        }
      });
      
      animatedComponent.addKeyListener (new KeyAdapter() {
        public void keyPressed(KeyEvent arg0) {
            animatedComponent.requestFocus();
        }
      }); 
      
      animatedComponent.requestFocus();
      
      GameWorld gameWorld = new GameWorld(animatedComponent);
      addComponentUpdater(gameWorld);
      
      addComponentPainter(new SpacePainter(null, STAR_DENSITY)); 
      addComponentPainter(gameWorld);
    }

}