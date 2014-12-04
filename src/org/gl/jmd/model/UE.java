package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

import org.gl.jmd.model.enumeration.*;
import org.gl.jmd.utils.NumberUtils;

import android.util.Log;

/**
 * Classe repr�sentant une UE (d'une ann�e).
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class UE implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant de l'UE.
	 */
	private int id;
	
	/**
	 * Le nom de l'UE.
	 */
	private String nom;
	
	/**
	 * Le type dont fait partie l'UE (semestre, ue, rien).
	 */
	private DecoupageYearType decoupageYearType;
	
	/**
	 * L'identifiant de l'ann�e li�e � l'UE.
	 */
	private int idAnnee;
	
	/**
	 * La liste des mati�res de l'UE.
	 */
	private ArrayList<Matiere> listeMatieres = new ArrayList<Matiere>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public UE() {
		
	}
	
	/**
	 * M�thode permettant de r�cup�rer la somme totale des coefficients des mati�res 
	 * pr�sentes dans l'UE.
	 * 
	 * @return La somme des coefficients des mati�res de l'UE.
	 */
	public double getTotalCoeff() {
		double res = 0;
		
		for (int i = 0; i < this.listeMatieres.size(); i++) {
			res += this.listeMatieres.get(i).getCoefficient();
		}
		
		return res;
	}
	
	/**
	 * M�thode permettant de calculer la moyenne de l'UE.
	 * 
	 * @return La moyenne de l'UE.
	 */
	public double getMoyenne(ArrayList<Regle> listeRegles) {
		double res = -1.0;
		
		int coeffGlobalUE = 0;
		double produitMatiereCoeff = 0.0;
		
		// R�cup�ration des options.
		int nbOptionMini = 0;
		
		for (int i = 0; i < listeRegles.size(); i++) {
			if ((listeRegles.get(i).getRegle() == RegleType.NB_OPT_MINI) &&
					(listeRegles.get(i).getIdUE() == this.id)) {
				
				nbOptionMini = listeRegles.get(i).getValeur();
			}
		}
		
		ArrayList<Matiere> listOptions = new ArrayList<Matiere>();
		
		for (int i = 0; i < this.listeMatieres.size(); i++) {
			if ((this.listeMatieres.get(i).getNoteFinale() != -1.0) &&
					(this.listeMatieres.get(i).isOption())) {
				
				listOptions.add(this.listeMatieres.get(i));
			}
		}
		
		// Tri de la liste des options.
		for (int i = listOptions.size() - 1; i >= 0; i--) {
	        for (int j = 0; j < i; j++) {
	            if (listOptions.get(j).getNoteFinale() < listOptions.get(j + 1).getNoteFinale()) {
	                Matiere temp = listOptions.get(j);
	                listOptions.set(j, listOptions.get(j + 1));
	                listOptions.set(j + 1, temp);
	            }
	        }
	    }
		
		// Moyenne des n meilleurs options.
		if (listOptions.size() > nbOptionMini) {	
			Log.e("UE", "---");
			
			for (int i = 0; i < nbOptionMini; i++) {
				if (listOptions.get(i).getNoteFinale() != -1.0) {
					Log.e("UE", listOptions.get(i).getNom());
					
					coeffGlobalUE += this.listeMatieres.get(i).getCoefficient();
					produitMatiereCoeff += this.listeMatieres.get(i).getNoteFinale() * this.listeMatieres.get(i).getCoefficient();
				}
			}
		}
		
		// Calcul de la moyenne.
		for (int i = 0; i < this.listeMatieres.size(); i++) {
			if ((this.listeMatieres.get(i).getNoteFinale() != -1.0) &&
					(!this.listeMatieres.get(i).isOption())) {
				
				coeffGlobalUE += this.listeMatieres.get(i).getCoefficient();
				produitMatiereCoeff += this.listeMatieres.get(i).getNoteFinale() * this.listeMatieres.get(i).getCoefficient();
			}
		}
		
		if ((coeffGlobalUE != 0) && (produitMatiereCoeff != 0.0)) {
			res = produitMatiereCoeff / coeffGlobalUE;
			res = NumberUtils.round(res, 2);
		}
		
		return res;
	}
	
	public boolean estAjourne(ArrayList<Regle> listeRegles) {
		boolean res = false;
		
		for (int i = 0; i < listeRegles.size(); i++) {			
			if ((listeRegles.get(i).getRegle() == RegleType.NOTE_MINIMALE) &&
					(listeRegles.get(i).getIdUE() == this.id)) {
				
				for (int k = 0; k < this.listeMatieres.size(); k++) {	
					if ((this.listeMatieres.get(k).getNoteFinale() != -1.0) && 
							(this.listeMatieres.get(k).getNoteFinale() < listeRegles.get(i).getValeur())) {
						
						res = true;
						break;
					}
				}
			} 
		}
		
		return res;
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de l'UE.
	 * 
	 * @return L'identifiant de l'UE.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom de l'UE.
	 * 
	 * @return Le nom de l'UE.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 * 
	 * @return Le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 */
	public DecoupageYearType getDecoupage() {
		return this.decoupageYearType;
	}
	
	/**
	 * M�thode retournant l'identifiant de l'ann�e li�e � l'UE.
	 * 
	 * @return L'identifiant de l'ann�e li�e � l'UE.
	 */
	public int getIdAnnee() {
		return this.idAnnee;
	}
	
	/**
	 * M�thode retournant la liste des mati�res de l'UE.
	 * 
	 * @return La liste des mati�res de l'UE.
	 */
	public ArrayList<Matiere> getListeMatieres() {
		return this.listeMatieres;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de l'UE.
	 * 
	 * @param id Le nouvel identifiant de l'UE.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom de l'UE.
	 * 
	 * @param nom Le nouveau nom de l'UE.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 * 
	 * @param decoupageYearType Le nouveau type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 */
	public void setDecoupage(DecoupageYearType decoupageYearType) {
		this.decoupageYearType = decoupageYearType;
	}
	
	/**
	 * M�thode permettant de modifier l'identifiant de l'ann�e li�e � l'UE.
	 * 
	 * @param idAnnee Le nouvel identifiant de l'ann�e li�e � l'UE.
	 */
	public void setIdAnnee(int idAnnee) {
		this.idAnnee = idAnnee;
	}
	
	/**
	 * M�thode permettant de modifier la liste des mati�res de l'UE.
	 * 
	 * @param listeMatieres La nouvelle liste des mati�res de l'UE.
	 */
	public void setListeMatieres(ArrayList<Matiere> listeMatieres) {
		this.listeMatieres = listeMatieres;
	}
}
