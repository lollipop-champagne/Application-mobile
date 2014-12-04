package org.gl.jmd.model;

import java.io.Serializable;

/**
 * Classe repr�sentant une note.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Note implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant de la note.
	 */
	private int id;
	
	/**
	 * Le nom de l'�preuve associ�e � la note.
	 */
	private String nom;
	
	/**
	 * La valeur de la note.
	 */
	private double note = -1.0;
	
	/**
	 * Le coefficient de la note SI elle est de type "Contr�le Continu".
	 */
	private int coefficient;
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Note() {
		
	}
	
	/* Getters. */

	/**
	 * M�thode retournant l'identifiant de la note.
	 * 
	 * @return L'identifiant de la note.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom de l'�preuve associ�e � la note.
	 * 
	 * @return Le nom de l'�preuve associ�e � la note.
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * M�thode retournant la valeur de la note.
	 * 
	 * @return La valeur de la note.
	 */
	public double getNote() {
		return this.note;
	}
	
	/**
	 * M�thode retournant le coefficient de la note SI elle est de type "Contr�le Continu".
	 * 
	 * @return Le coefficient de la note SI elle est de type "Contr�le Continu".
	 */
	public int getCoefficient() {
		return this.coefficient;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de la note.
	 * 
	 * @param id Le nouvel identifiant de la note.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom de l'�preuve associ�e � la note.
	 * 
	 * @param nom Le nouveau nom de l'�preuve associ�e � la note.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * M�thode permettant de modifier la valeur de la note.
	 * 
	 * @param note La nouvelle valeur de la note.
	 */
	public void setNote(double note) {
		this.note = note;
	}
	
	/**
	 * M�thode permettant de modifier le coefficient de la note SI elle est de type "Contr�le Continu".
	 * 
	 * @param coefficient Le nouveau coefficient de la note SI elle est de type "Contr�le Continu".
	 */
	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}
}
