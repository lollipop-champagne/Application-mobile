package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

import org.gl.jmd.model.enumeration.ParamType;

/**
 * Classe repr�sentant les param�tres de l'application.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Parametre implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Ensemble des param�tres de l'application.
	 */
	private Map<ParamType, String> params = new HashMap<ParamType, String>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Parametre() {
		
	}
	
	/**
	 * M�thode permettant d'ajouter un param�tre � l'application.
	 * 
	 * @param name Le type du param�tre � ajouter.
	 * @param value La valeur du param�tre � ajouter.
	 */
	public void addParam(ParamType name, String value) {
		params.put(name, value);
	}
	
	/**
	 * M�thode permettant de supprimer un param�tre de l'application.
	 * 
	 * @param key La cl� du param�tre � supprimer (i.e, le type).
	 */
	public void removeParam(ParamType key) {
		params.remove(key);
	}
	
	/**
	 * M�thode permettant de rechercher un param�tre dans les param�tres de l'application.
	 * 
	 * @param key Le param�tre recherch�.
	 * 
	 * @return L'�l�ment recherch�, s'il existe.<br />
	 * <b>null</b> sinon.
	 */
	public String get(ParamType key) {
		return params.get(key);
	}
}
