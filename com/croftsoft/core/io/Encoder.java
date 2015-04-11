     package com.croftsoft.core.io;

     import java.io.IOException;

     /*********************************************************************
     * A generic interface for Object encoders.
     *
     * @see
     *   Parser
     * @version
     *   2003-05-13
     * @since
     *   2000-03-23
     * @author
     *   <a href="http://www.croftsoft.com/">David Wallace Croft</a>
     *********************************************************************/

     public interface  Encoder
     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     {

     public byte [ ]  encode ( Object  object )
       throws IOException;

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     }
