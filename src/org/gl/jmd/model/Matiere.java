package org.gl.jmd.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.gl.jmd.utils.NumberUtils;

/**
 * Classe repr�sentant une mati�re.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Matiere implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant de la mati�re.
	 */
	private int id;
	
	/**
	 * Le nom de la mati�re.
	 */
	private String nom;
	
	/**
	 * Le coefficient de la mati�re.
	 */
	private float coefficient;
	
	/**
	 * Bool�en permettant de savoir si la mati�re est une option, ou non.
	 */
	private boolean isOption;
	
	/**
	 * Le coefficient du partiel.
	 */
	private double coeffPartiel = 0;
	
	/**
	 * La note de premi�re session.
	 */
	private Note noteSession1 = new Note();
	
	/**
	 * La note de seconde session.
	 */
	private Note noteSession2 = new Note();
	
	/**
	 * Les notes de contr�le continu de la mati�re.
	 */
	private ArrayList<Note> listeNotesCC = new ArrayList<Note>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Matiere() {
		
	}
	
	/**
	 * M�thode permettant de calculer la note finale de la mati�re.
	 * 
	 * @return La note finale de la mati�re.
	 */
	public double getNoteFinale() {
		double res = -1.0;
		
		if ((this.noteSession1 != null) && 
				(this.noteSession1.getNote() != -1.0)) {
			
			res = this.noteSession1.getNote();
		} 
		
		if ((this.noteSession1 != null) && 
				(this.noteSession2 != null) &&
					(this.noteSession2.getNote() != -1.0)) {
			
			res = this.noteSession2.getNote();
		}
		
		if (this.listeNotesCC.size() > 0) {
			double sommeNoteCoeff = 0;
			double sommeCoeff = 0;
			
			for (int i = 0; i < this.listeNotesCC.size(); i++) {
				sommeNoteCoeff +=  this.listeNotesCC.get(i).getNote() * this.listeNotesCC.get(i).getCoefficient();
				sommeCoeff += this.listeNotesCC.get(i).getCoefficient();
			}
			
			double moyenneCC = sommeNoteCoeff / sommeCoeff;
			double coeff = (this.coeffPartiel / (this.coeffPartiel + 1));
			double coeffCC = (1.0 / (this.coeffPartiel + 1));
			
			res = NumberUtils.round((res * coeff) + (moyenneCC * coeffCC), 2);
		} 
		
		return res;
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de la mati�re.
	 * 
	 * @return L'identifiant de la mati�re.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * M�thode retournant le nom de la mati�re.
	 * 
	 * @return Le nom de la mati�re.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant le coefficient de la mati�re.
	 * 
	 * @return Le coefficient de la mati�re.
	 */
	public double getCoefficient() {
		return this.coefficient;
	}
	
	/**
	 * M�thode permettant de savoir si la mati�re est une option, ou non.
	 * 
	 * @return <b>true</b> si la mati�re est une option.<br />
	 * <b>false</b> sinon.
	 */
	public boolean isOption() {
		return this.isOption;
	}
	
	/**
	 * M�thode retournant le coefficient du partiel de la mati�re.
	 * 
	 * @return Le coefficient du partiel de la mati�re.
	 */
	public double getCoeffPartiel() {
		return this.coeffPartiel;
	}
	
	/**
	 * M�thode retournant la note de premi�re session de la mati�re.
	 * 
	 * @return La note de premi�re session de la mati�re.
	 */
	public Note getNoteSession1() {
		return this.noteSession1;
	}
	
	/**
	 * M�thode retournant la note de seconde session de la mati�re.
	 * 
	 * @return La note de seconde session de la mati�re.
	 */
	public Note getNoteSession2() {
		return this.noteSession2;
	}

	/**
	 * M�thode retournant les notes de contr�le continu de la mati�re.
	 * 
	 * @return Les notes de contr�le continu de la mati�re.
	 */
	public ArrayList<Note> getListeNotesCC() {
		return this.listeNotesCC;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de la mati�re.
	 * 
	 * @param id Le nouvel identifiant de la mati�re.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * M�thode permettant de modifier le nom de la mati�re.
	 * 
	 * @param nom Le nouveau nom de la mati�re.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier le coefficient de la mati�re.
	 * 
	 * @param coefficient Le nouveau coefficient de la mati�re.
	 */
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	
	/**
	 * M�thode permettant de modifier le fait que la mati�re soit une option, ou non. 
	 * 
	 * @param isOption Le bool�an repr�sentant le fait que la mati�re soit une option, ou non.
	 */
	public void setIsOption(boolean isOption) {
		this.isOption = isOption;
	}
	
	/**
	 * M�thode permettant de modifier le coefficient du partiel de la mati�re.
	 * 
	 * @param coeffPartiel Le nouveau coefficient du partiel de la mati�re.
	 */
	public void setCoeffPartiel(double coeffPartiel) {
		this.coeffPartiel = coeffPartiel;
	}

	/**
	 * M�thode permettant de modifier la note de premi�re session de la mati�re.
	 * 
	 * @param noteSession1 La nouvelle note de premi�re session de la mati�re.
	 */
	public void setNoteSession1(Note noteSession1) {
		this.noteSession1 = noteSession1;
	}
	
	/**
	 * M�thode permettant de modifier la note de seconde session de la mati�re.
	 * 
	 * @param noteSession1 La nouvelle note de seconde session de la mati�re.
	 */
	public void setNoteSession2(Note noteSession2) {
		this.noteSession2 = noteSession2;
	}
	
	/**
	 * M�thode permettant de modifier les notes de contr�le continu de la mati�re.
	 * 
	 * @param listeNotes Les nouvelle notes de contr�le continu de la mati�re.
	 */
	public void setListeNotesCC(ArrayList<Note> listeNotesCC) {
		this.listeNotesCC = listeNotesCC;
	}
}
