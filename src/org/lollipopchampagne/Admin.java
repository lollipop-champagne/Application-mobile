package org.lollipopchampagne;

import java.io.Serializable;


public class Admin implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * L'identifiant de l'utilisateur.
	 */
	private int id;

	/**
	 * Le nom de l'utilisateur.
	 */
	private String nom;
	
	/**
	 * Le pr�nom de l'utilisateur.
	 */
	private String prenom;
	
	/**
	 * Le mot de passe de l'administrateur.
	 */
	private String pseudo;
	
	/**
	 * Le mot de passe de l'administrateur.
	 */
	private String password;
	
	/**
	 * Bool�en permettant de savoir si l'administrateur est actif ou non.
	 */
	private boolean estActive;
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Admin() {
		
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de l'utilisateur.
	 * 
	 * @return L'identifiant de l'utilisateur.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom de l'utilisateur.
	 * 
	 * @return Le nom de l'utilisateur.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant le pr�nom de l'utilisateur.
	 * 
	 * @return Le pr�nom de l'utilisateur.
	 */
	public String getPrenom() {
		return this.prenom;
	}

	/**
	 * M�thode retournant le pseudo de l'administrateur.
	 * 
	 * @return Le pseudo de l'administrateur.
	 */
	public String getPseudo() {
		return this.pseudo;
	}

	/**
	 * M�thode retournant le mot de passe de l'administrateur.
	 * 
	 * @return Le mot de passe de l'administrateur.
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * M�thode permettant de savoir si le compte est activ� ou non.
	 * 
	 * @return <b>true</b> si le compte est activ�.<br />
	 * <b>false</b> sinon.
	 */
	public boolean isActive() {
		return this.estActive;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de l'utilisateur.
	 * 
	 * @param id Le nouvel identifiant de l'utilisateur.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom de l'utilisateur.
	 * 
	 * @param nom Le nouveau nom de l'utilisateur.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier le pr�nom de l'utilisateur.
	 * 
	 * @param prenom Le nouveau pr�nom de l'utilisateur.
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * M�thode permettant de modifier le pseudo de l'administrateur.
	 * 
	 * @param pseudo Le nouveau pseudo de l'administrateur.
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * M�thode permettant de modifier le mot de passe de l'administrateur.
	 * 
	 * @param password Le nouveau mot de passe de l'administrateur.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * M�thode permettant de modifier le fait que le compte soit activ� ou non.
	 * 
	 * @param estActive La nouvelle valeur du bool�en.
	 */
	public void setIsActive(boolean estActive) {
		this.estActive = estActive;
	}
}