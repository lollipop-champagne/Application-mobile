package org.lollipopchampagne.utils;

import java.io.*;

/**
 * Classe utilitaire permettant de simplifier la manipulation des fichiers dans l'application.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class FileUtils {
	
	/**
	 * Constructeur priv� de la classe.<br />
	 * Il est <i>private</i> pour emp�cher son instanciation.
	 */
	private FileUtils() {
		
	}
	
	/**
	 * M�thode permettant de lire un fichier et de renvoyer son contenu.
	 * 
	 * @param path Le chemin du fichier � lire.
	 * @return Le contenu du fichier, ou une chaine vide si le fichier donn� en argument n'existe pas.
	 */
	public static String readFile(String path) {
		String contenuFichier = "";

		try {
			FileInputStream fis = new FileInputStream(path);
			int n;

			while((n = fis.available()) > 0) {
				byte[] b = new byte[n]; 
				int result = fis.read(b);
				
				if(result == -1) {
					break; 
				}
				
				contenuFichier = new String(b);
			}
			
			fis.close();
		} catch (Exception err) {
			return "";
		} 

		return contenuFichier;
	}
	
	/**
	 * M�thode permettant d'�crire un texte dans un fichier.
	 * 
	 * @param content Le contenu � �crire dans le fichier.
	 * @param path L'adresse du fichier.
	 * 
	 * @return <b>true</b> si le texte a bien �t� �crit dans le fichier.<br />
	 * <b>false</b> sinon.
	 */
	public static boolean writeFile(String content, String path) {
		try { 
			FileWriter lu = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(lu);
			
			out.write(content); 
			out.close(); 
		} catch (IOException b) {
			return false;
		}

		return true;
	}
}
