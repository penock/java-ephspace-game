     package com.croftsoft.apps.mars.model;

     /*********************************************************************
     * A bullet.
     *
     * @version
     *   2003-04-14
     * @since
     *   2003-04-14
     * @author
     *   <a href="http://www.croftsoft.com/">David Wallace Croft</a>
     *********************************************************************/

     public interface  Bullet
       extends Model, BulletAccessor
     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     {

     public void  fire (
       double  originX,
       double  originY,
       double  heading );

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     }