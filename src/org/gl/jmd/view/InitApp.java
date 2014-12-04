package org.gl.jmd.view;

import java.io.File;

import org.gl.jmd.R;
import org.gl.jmd.dao.ParametreDAO;
import org.gl.jmd.model.*;
import org.gl.jmd.model.enumeration.ParamType;
import org.gl.jmd.view.admin.*;
import org.gl.jmd.view.etudiant.AccueilE;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.CheckBox;

/**
 * Activit� permettant de choisir si l'utilisateur est un administrater ou un �tudiant.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class InitApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ecran_choix_user);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		initCheckbox(); 
	}
	
	private void initCheckbox() {
		Parametre p = ParametreDAO.load();
		CheckBox checkbox = (CheckBox) findViewById(R.id.accueil_choix_user_se_souvenir);
		
		if (p != null) {
			if (p.get(ParamType.IS_ADMIN) != null) {
				checkbox.setChecked(true);
			}
		}
	}
	
	/**
	 * M�thode permettant de naviguer vers l'accueil administrateur.
	 * 
	 * @param view La vue lors du click sur le bouton.
	 */
	public void navigateToAdminScreen(View view) {		
		CheckBox checkbox = (CheckBox) findViewById(R.id.accueil_choix_user_se_souvenir);
		 
		if (checkbox.isChecked()) {
			Parametre p = new Parametre();
			p.addParam(ParamType.IS_ADMIN, "true");
			
			ParametreDAO.save(p);
		}
		
		finish();
		
		File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
		File fileToken = new File("/sdcard/cacheJMD/token.jmd");
		
		if (filePseudo.exists() && fileToken.exists()) {
			startActivity(new Intent(InitApp.this, AccueilA.class));
		} else {
			startActivity(new Intent(InitApp.this, ConnexionA.class));
		}
	}
	
	/**
	 * M�thode permettant de naviguer vers l'accueil �tudiant.
	 * 
	 * @param view La vue lors du click sur le bouton.
	 */
	public void navigateToEtudiantScreen(View view) {		
		CheckBox checkbox = (CheckBox) findViewById(R.id.accueil_choix_user_se_souvenir);
		 
		if (checkbox.isChecked()) {
			Parametre p = new Parametre();
			p.addParam(ParamType.IS_ADMIN, "false");
			
			ParametreDAO.save(p);
		}
		
		finish();
		startActivity(new Intent(InitApp.this, AccueilE.class));
	}
	
	/* M�thodes h�rit�es de la classe Activity. */
	
	/**
	 * M�thode permettant d'emp�cher la reconstruction de la vue lors de la rotation de l'�cran. 
	 * 
	 * @param newConfig L'�tat de la vue avant la rotation.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * M�thode d�clench�e lors d'un click sur le bouton virtuel Android de retour.
	 */
	public void onBackPressed() {
		AlertDialog.Builder confirmQuitter = new AlertDialog.Builder(this);
		confirmQuitter.setTitle("Fermeture");
		confirmQuitter.setMessage("Voulez-vous vraiment quitter ?");
		confirmQuitter.setCancelable(false);
		confirmQuitter.setPositiveButton("Oui", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				InitApp.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		
		confirmQuitter.setNegativeButton("Non", null);
		confirmQuitter.show();
	}
}
