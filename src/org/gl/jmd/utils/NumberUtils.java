package org.gl.jmd.utils;

/**
 * Classe utilitaire offrant des op�rations sur les nombres.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class NumberUtils {
	
	/**
	 * Constructeur priv� de la classe.<br />
	 * Il est <i>private</i> pour emp�cher son instanciation.
	 */
	private NumberUtils() {
		
	}
	
	/**
	 * M�thode permettant d'arrondir une valeur.
	 * 
	 * @param value La valeur � arrondir.
	 * @param places Le nombre de d�cimales � garder.
	 * 
	 * @return La valeur, arrondie.
	 */
	public static double round(double value, int places) {
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    
	    return (double) tmp / factor;
	}
}
