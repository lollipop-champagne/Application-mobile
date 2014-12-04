package org.gl.jmd.dao;

import java.io.*;

import org.gl.jmd.model.Parametre;

import android.os.Environment;

/**
 * Objet permettant de manipuler des param�tres.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class ParametreDAO {
	
	/**
	 * Le fichier o� sont enregistr�s les param�tres de l'application.
	 */
	private static File fileParam = new File(Environment.getExternalStorageDirectory().getPath() + "/cacheJMD/param.config");
	
	/**
	 * Constructeur par d�faut du DAO.<br />
	 * Le priv� emp�che son instanciation (classe statique).
	 */
	private ParametreDAO() {
		
	}
	
	/**
	 * M�thode retournant le fichier associ� � l'objet "Parametre".
	 * 
	 * @return Le fichier de l'objet "Parametre".
	 */
	public static File getFile() {
		return fileParam;
	}

	/**
	 * M�thode permettant de s�rialiser (i.e, sauvegarder) les param�tres de l'application.
	 * 
	 * @param params L'objet � sauvegarder.
	 * 
	 * @return <b>true</b> si les param�tres ont bien �t� sauvegard�s.<br />
	 * <b>false</b> sinon.
	 */
	public static boolean save(Parametre params) {
		boolean result = false;
		
		try {
			FileOutputStream fichier = new FileOutputStream(fileParam);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			
			oos.writeObject(params);
			oos.flush();
			oos.close();
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;    
	}

	/**
	 * M�thode permettant de charger les param�tres de l'application.
	 * 
	 * @return Un objet "Parametre" contenant les param�tres de l'application.
	 */
	public static Parametre load() {        
        if (fileParam.exists()) {
            try {
                FileInputStream fichier = new FileInputStream(fileParam);
                ObjectInputStream ois = new ObjectInputStream(fichier);
                
                Parametre e = ((Parametre) ois.readObject());
                
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
        }
        
        return null;
    }
}
