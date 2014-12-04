package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

/**
 * Classe repr�sentant un �tudiant.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Etudiant implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * La liste des dipl�mes de l'�tudiant.
	 */
	private ArrayList<Diplome> listeDiplomes = new ArrayList<Diplome>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Etudiant() {
		
	}
	
	/**
	 * M�thode retournant la liste des dipl�mes de l'�tudiant.
	 * 
	 * @return La liste des dipl�mes de l'�tudiant.
	 */
	public ArrayList<Diplome> getListeDiplomes() {
		return this.listeDiplomes;
	}
	
	/**
	 * M�thode permettant de modifier la liste des dipl�mes de l'�tudiant.
	 * 
	 * @param listeDiplomes La nouvelle liste des dipl�mes de l'�tudiant.
	 */
	public void setListeDiplomes(ArrayList<Diplome> listeDiplomes) {
		this.listeDiplomes = listeDiplomes;
	}
}
