package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

/**
 * Classe repr�sentant un dipl�me.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Diplome implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identifiant du dipl�me.
	 */
	private int id;
	
	/**
	 * Le nom du dipl�me.
	 */
	private String nom;
	
	/**
	 * La liste des ann�es du dipl�me.
	 */
	private ArrayList<Annee> listeAnnees = new ArrayList<Annee>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Diplome() {
		
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant du dipl�me.
	 * 
	 * @return L'identifiant du dipl�me.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom du dipl�me.
	 * 
	 * @return Le nom du dipl�me.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant la liste des ann�es du dipl�me.
	 * 
	 * @return La liste des ann�es du dipl�me.
	 */
	public ArrayList<Annee> getListeAnnees() {
		return this.listeAnnees;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant du dipl�me.
	 * 
	 * @param id Le nouvel identifiant du dipl�me.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom du dipl�me.
	 * 
	 * @param nom Le nouveau nom du dipl�me.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier la liste des ann�es du dipl�me.
	 * 
	 * @param listeAnnees La nouvelle liste des ann�es du dipl�me.
	 */
	public void setListeAnnees(ArrayList<Annee> listeAnnees) {
		this.listeAnnees = listeAnnees;
	}
}