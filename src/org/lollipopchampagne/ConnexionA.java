package org.lollipopchampagne;

import java.io.IOException;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.lollipopchampagne.utils.Constantes;
import org.lollipopchampagne.utils.FileUtils;
import org.lollipopchampagne.utils.SecurityUtils;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConnexionA extends Activity {

private Activity activity;
	
	private Toast toast;
	private long back_pressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_connexion);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
	}
	
	/**
	 * Méthode redirigeant l'utilisateur vers la vue d'inscription.
	 * 
	 * @param view La vue lors du click sur le bouton.
	 */
	public void inscription(View view) {
		//startActivity(new Intent(ConnexionA.this, InscriptionA.class));			
	}
	
	/**
	 * Méthode permettant de connecter l'utilisateur.
	 * 
	 * @param view La vue lors du click sur le bouton de connexion.
	 */
	public void connexion(View view) {
		final EditText PSEUDO = (EditText) findViewById(R.id.connex_zone_pseudo);
		final EditText PASSWORD = (EditText) findViewById(R.id.connex_zone_mdp);
		
		if ((PSEUDO.getText().toString().length() != 0) && (PASSWORD.getText().toString().length() != 0)) {
			Admin a = new Admin();
			a.setPseudo(PSEUDO.getText().toString());
			a.setPassword(SecurityUtils.sha256(PASSWORD.getText().toString()));
			
			String URL = Constantes.URL_SERVER + "admin/login";

			ProgressDialog progress = new ProgressDialog(activity);
			progress.setMessage("Chargement...");
			new SeConnecter(progress, URL, a).execute();	
		} else {
			if (PSEUDO.getText().toString().length() == 0) {
				PSEUDO.setBackgroundResource(R.drawable.border_edittext_error);
			} else {
				PSEUDO.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (PASSWORD.getText().toString().length() == 0) {
				PASSWORD.setBackgroundResource(R.drawable.border_edittext_error);

			} else {
				PASSWORD.setBackgroundResource(R.drawable.border_edittext);
			}
			
			toast.setText("Au moins un des champs est vide.");
			toast.show();
		}
	}
	
	/**
	 * Méthode permettant de rediriger l'utilisateur vers la vue de récupération du mot de passe.
	 * 
	 * @param view La vue lors du click sur le lien de récupération du mot de passe.
	 */
	public void resetPassword(View view) {
		//startActivity(new Intent(ConnexionA.this, RecupMDPA.class));
	}
	
	/* Classe interne. */
	
	private class SeConnecter extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;
		private Admin a;

		public SeConnecter(ProgressDialog progress, String pathUrl, Admin a) {
			this.progress = progress;
			this.pathUrl = pathUrl;
			this.a = a;
		}

		public void onPreExecute() {
			progress.show();
		}

		public void onPostExecute(Void unused) {
			progress.dismiss();
		}

		protected Void doInBackground(Void... arg0) {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(pathUrl);

		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", a.getPseudo()));
				nameValuePairs.add(new BasicNameValuePair("password", a.getPassword()));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        HttpResponse response = httpclient.execute(httppost);
		        
		        if (response.getStatusLine().getStatusCode() == 200) {
		        	String responseBody = EntityUtils.toString(response.getEntity());
		        	
		        	FileUtils.writeFile(responseBody, "/sdcard/cacheJMD/token.jmd");
		        	FileUtils.writeFile(a.getPseudo(), "/sdcard/cacheJMD/pseudo.jmd");
		    		
		        	finish();
					//startActivity(intent);
		        } else if (response.getStatusLine().getStatusCode() == 401) {
		        	toast.setText("Mauvais identifiants. Veuillez réessayer.");	
					toast.show();
		        } else if (response.getStatusLine().getStatusCode() == 500) {
		        	toast.setText("Une erreur est survenue au niveau de la BDD.");	
					toast.show();
		        } else {
		        	toast.setText("Erreur inconnue. Veuillez réessayer.");	
					toast.show();
		        }
		    } catch (ClientProtocolException e) {
		    	ConnexionA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionA.this);
						builder.setMessage("Erreur - Vérifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ConnexionA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    } catch (IOException e) {
		    	ConnexionA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionA.this);
						builder.setMessage("Erreur - Vérifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ConnexionA.this.finish();
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
	
	/* Méthodes héritées de la classe Activity. */
	
	/**
	 * Méthode permettant d'empécher la reconstruction de la vue lors de la rotation de l'écran. 
	 * 
	 * @param newConfig L'état de la vue avant la rotation.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * Méthode déclenchée lors d'un click sur le bouton virtuel Android de retour.
	 */
	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis()) {
			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			Toast.makeText(getBaseContext(), "Appuyez encore une fois sur \"Retour\" pour quitter l'application.", Toast.LENGTH_SHORT).show();
		}
		
        back_pressed = System.currentTimeMillis();
	}
}
