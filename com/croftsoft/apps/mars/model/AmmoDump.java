     package com.croftsoft.apps.mars.model;

     /*********************************************************************
     * An ammo dump.
     *
     * @version
     *   2003-04-14
     * @since
     *   2003-04-14
     * @author
     *   <a href="http://www.croftsoft.com/">David Wallace Croft</a>
     *********************************************************************/

     public interface  AmmoDump
       extends Model, AmmoDumpAccessor, Damageable
     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     {

     public void  setAmmo ( double  ammo );

     //////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////
     }