package org.gl.jmd.model;

import java.io.Serializable;

/**
 * Classe repr�sentant une r�gle de gestion.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Regle implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * L'identifiant de la r�gle.
	 */
	private int id;
	
	/**
	 * La r�gle (1 - NB_OPT_MINI, 2 - NOTE_MINIMALE).
	 */
	private int regle;
	
	/**
	 * L'op�rateur de la r�gle (>, <, <=, ...).
	 */
	private int operateur;
	
	/**
	 * La valeur de la r�gle.
	 */
	private int valeur;
	
	/**
	 * L'identifiant de l'UE sur laquelle s'applique la r�gle.
	 */
	private int idUE;
	
	/**
	 * L'identifiant de l'ann�e sur laquelle s'applique la r�gle.
	 */
	private int idAnnee;
	
	/**
	 * L'identifiant de la mati�re sur laquelle s'applique la r�gle.
	 */
	private int idMatiere;
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Regle() {
		
	}

	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de la r�gle.
	 * 
	 * @return L'identifiant de la r�gle.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * M�thode retournant la r�gle (1 - NB_OPT_MINI, 2 - NOTE_MINIMALE).
	 * 
	 * @return La r�gle (1 - NB_OPT_MINI, 2 - NOTE_MINIMALE).
	 */
	public int getRegle() {
		return this.regle;
	}

	/**
	 * M�thode retournant l'op�rateur de la r�gle (>, <, <=, ...).
	 * 
	 * @return L'op�rateur de la r�gle.
	 */
	public int getOperateur() {
		return this.operateur;
	}

	/**
	 * M�thode retournant la valeur de la r�gle.
	 * 
	 * @return La valeur de la r�gle.
	 */
	public int getValeur() {
		return this.valeur;
	}
	
	/**
	 * M�thode retournant l'identifiant de l'UE sur laquelle s'applique la r�gle.
	 * 
	 * @return L'identifiant de l'UE sur laquelle s'applique la r�gle.
	 */
	public int getIdUE() {
		return this.idUE;
	}

	/**
	 * M�thode retournant l'identifiant de l'ann�e sur laquelle s'applique la r�gle.
	 * 
	 * @return L'identifiant de l'ann�e sur laquelle s'applique la r�gle.
	 */
	public int getIdAnnee() {
		return this.idAnnee;
	}
	
	/**
	 * M�thode retournant l'identifiant de la mati�re sur laquelle s'applique la r�gle.
	 * 
	 * @return L'identifiant de la mati�re sur laquelle s'applique la r�gle.
	 */
	public int getIdMatiere() {
		return this.idMatiere;
	}
	
	/* Setters. */

	/**
	 * M�thode permettant de modifier l'identifiant de la r�gle.
	 * 
	 * @param id Le nouvel identifiant de la r�gle.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * M�thode permettant de modifier la r�gle.
	 * 
	 * @param regle La nouvelle r�gle (1 - NB_OPT_MINI, 2 - NOTE_MINIMALE).
	 */
	public void setRegle(int regle) {
		this.regle = regle;
	}

	/**
	 * M�thode permettant de modifier l'op�rateur de la r�gle.
	 * 
	 * @param operateur Le nouvel op�rateur de la r�gle.
	 */
	public void setOperateur(int operateur) {
		this.operateur = operateur;
	}

	/**
	 * M�thode permettant de modifier la valeur de la r�gle.
	 * 
	 * @param valeur La nouvelle valeur de la r�gle.
	 */
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	/**
	 * M�thode permettant de modifier l'identifiant de l'UE sur laquelle s'applique la r�gle.
	 * 
	 * @param idUE Le nouvel identifiant de l'UE sur laquelle s'applique la r�gle.
	 */
	public void setIdUE(int idUE) {
		this.idUE = idUE;
	}

	/**
	 * M�thode permettant de modifier l'identifiant de l'ann�e sur laquelle s'applique la r�gle.
	 * 
	 * @param idUE Le nouvel identifiant de l'ann�e sur laquelle s'applique la r�gle.
	 */
	public void setIdAnnee(int idAnnee) {
		this.idAnnee = idAnnee;
	}
	
	/**
	 * M�thode permettant de modifier l'identifiant de la mati�re sur laquelle s'applique la r�gle.
	 * 
	 * @param idUE Le nouvel identifiant de la mati�re sur laquelle s'applique la r�gle.
	 */
	public void setIdMatiere(int idMatiere) {
		this.idMatiere = idMatiere;
	}
}