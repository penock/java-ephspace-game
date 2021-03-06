     package com.croftsoft.core.io;

     import java.io.IOException;
import java.io.InputStream;

     /*********************************************************************
     *
     * A generic interface for Object parsers.
     *
     * <P>
     *
     * @see
     *   Encoder
     * @version
     *   2003-05-14
     * @since
     *   2000-03-23
     * @author
     *   <A HREF="http://www.alumni.caltech.edu/~croft/">David W. Croft</A>
     *********************************************************************/

     public interface  Parser
     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     {

     /*********************************************************************
     * @param  contentLength
     *
     *   -1 if unknown.
     *********************************************************************/
     public Object  parse (
       InputStream  inputStream,
       int          contentLength )
       throws IOException;

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     }
