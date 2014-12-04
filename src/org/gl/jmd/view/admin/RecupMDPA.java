package org.gl.jmd.view.admin;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gl.jmd.Constantes;
import org.gl.jmd.R;
import org.gl.jmd.model.Admin;

import android.app.*;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.*;

/**
 * Activit� correspondant � la vue de r�cup�ration d'un mot de passe pour les administrateurs.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class RecupMDPA extends Activity {
	
	private Activity activity;
	
	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.administrateur_mdp_oublie);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
	}
	
	/**
	 * M�thode permettant de lancer la r�cup�ration du mot de passe.
	 * 
	 * @param view La vue lors du click sur le bouton de validation.
	 */
	public void recupPassword(View view) {
		final EditText PSEUDO = (EditText) findViewById(R.id.mdp_oublie_zone_pseudo);
		
		if (PSEUDO.getText().toString().length() != 0) {
			PSEUDO.setBackgroundResource(R.drawable.border_edittext);
			
			Admin a = new Admin();
			a.setPseudo(PSEUDO.getText().toString());
			
			String URL = Constantes.URL_SERVER + "admin/passwordOublie" +
						 "?pseudo=" + a.getPseudo();

			ProgressDialog progress = new ProgressDialog(activity);
			progress.setMessage("Chargement...");
			new RecupMDP(progress, URL).execute();	
		} else {
			if (PSEUDO.getText().toString().length() == 0) {
				PSEUDO.setBackgroundResource(R.drawable.border_edittext_error);
			} 
			
			toast.setText("Le champ \"Pseudo\" est vide.");
			toast.show();
		}
	}
	
	/* Classe interne. */
	
	private class RecupMDP extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public RecupMDP(ProgressDialog progress, String pathUrl) {
			this.progress = progress;
			this.pathUrl = pathUrl;
		}

		public void onPreExecute() {
			progress.show();
		}

		public void onPostExecute(Void unused) {
			progress.dismiss();
		}

		protected Void doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(pathUrl);

		    try {
		        HttpResponse response = httpclient.execute(httpGet);
		        
		        if (response.getStatusLine().getStatusCode() == 200) {
		        	toast.setText("Les instructions vous ont �t� envoy�es par mail.");
		        	toast.show();
		        	
		        	finish();
		        } else if (response.getStatusLine().getStatusCode() == 404) {
		        	toast.setText("Le pseudo sp�cifi� n'existe pas.");	
					toast.show();
		        } else if (response.getStatusLine().getStatusCode() == 500) {
		        	toast.setText("Une erreur est survenue au niveau de la BDD.");	
					toast.show();
		        } else {
		        	toast.setText("Erreur inconnue. Veuillez r�essayer.");	
					toast.show();
		        }
		    } catch (ClientProtocolException e) {
		    	RecupMDPA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(RecupMDPA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								RecupMDPA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    } catch (IOException e) {
		    	RecupMDPA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(RecupMDPA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								RecupMDPA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    }				

			return null;
		}
	}
	
	/* M�thode h�rit�e de la classe Activity. */
	
	/**
	 * M�thode permettant d'emp�cher la reconstruction de la vue lors de la rotation de l'�cran. 
	 * 
	 * @param newConfig L'�tat de la vue avant la rotation.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
