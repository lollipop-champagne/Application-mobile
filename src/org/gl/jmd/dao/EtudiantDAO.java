package org.gl.jmd.dao;

import java.io.*;
import java.util.ArrayList;

import org.gl.jmd.model.*;

/**
 * Objet permettant de manipuler des �tudiants.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class EtudiantDAO {
	
	/**
	 * Le fichier o� est enregistr� l'�tudiant.
	 */
	private static File fileProfil = new File("/sdcard/cacheJMD/etudiant.cfg");
	
	/**
	 * Constructeur par d�faut du DAO.<br />
	 * Le priv� emp�che son instanciation (classe statique).
	 */
	private EtudiantDAO() {
		
	}

	/**
	 * M�thode permettant de sauver (s�rialiser) un �tudiant.
	 * 
	 * @return <b>true</b> si l'�tudiant a bien �t� sauvegard�.<br />
	 * <b>false</b> sinon.
	 */
	public static boolean save(Etudiant etudiant) {
		boolean result = false;
		
		try {
			FileOutputStream fichier = new FileOutputStream(fileProfil);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			
			oos.writeObject(etudiant);
			oos.flush();
			oos.close();
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;    
	}
	
	/**
	 * M�thode permettant de supprimer le profil �tudiant enregistr�.
	 * 
	 * @return <b>true</b> si le profil a bien �t� supprim�.<br />
	 * <b>false</b> sinon.
	 */
	public static boolean deleteProfil() {
		return fileProfil.delete();
	}
	
	/**
	 * M�thode permettant de changer l'�tudiant.
	 * 
	 * @return Un objet �tudiant si le fichier existe.<br />
	 * <b>null</b> sinon.
	 */
	public static Etudiant load() { 		
        if (fileProfil.exists()) {
            try {
                FileInputStream fichier = new FileInputStream(fileProfil);
                ObjectInputStream ois = new ObjectInputStream(fichier);
                
                Etudiant e = ((Etudiant) ois.readObject());
                
                ois.close();
                fichier.close();
                
                return e;
            } catch (InvalidClassException e){
                e.printStackTrace();
            } catch (java.io.IOException e) {                
                e.printStackTrace();
            } catch (ClassNotFoundException e) {                
                e.printStackTrace();
            }
        } else {
        	Etudiant newEtudiant = new Etudiant();
			ArrayList<Diplome> listeDiplomes = new ArrayList<Diplome>();
			newEtudiant.setListeDiplomes(listeDiplomes);
			
			return newEtudiant;
        }
        
        return null;
    }
}