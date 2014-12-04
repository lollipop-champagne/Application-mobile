package org.gl.jmd.model;

import java.io.Serializable;

/**
 * Classe repr�sentant un �tablissement.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Etablissement implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant de l'�tablissement.
	 */
	private int id;

	/**
	 * Le nom de l'�tablissement.
	 */
	private String nom;
	
	/**
	 * La ville de l'�tablissement.
	 */
	private String ville;
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Etablissement() {
		
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de l'�tablissement.
	 * 
	 * @return L'identifiant de l'�tablissement.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom de l'�tablissement.
	 * 
	 * @return Le nom de l'�tablissement.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant la ville de l'�tablissement.
	 * 
	 * @return La ville de l'�tablissement.
	 */
	public String getVille() {
		return this.ville;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de l'�tablissement.
	 * 
	 * @param id Le nouvel identifiant de l'�tablissement.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom de l'�tablissement.
	 * 
	 * @param nom Le nouveau nom de l'�tablissement.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier la ville de l'�tablissement.
	 * 
	 * @param ville La nouvelle ville de l'�tablissement.
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}
}
