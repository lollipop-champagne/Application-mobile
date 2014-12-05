package org.lollipopchampagne;

import java.io.*;
import java.util.regex.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.lollipopchampagne.utils.Constantes;
import org.lollipopchampagne.utils.SecurityUtils;

import android.app.*;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

/**
 * Activit� correspondant � la vue d'inscription � l'application.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class CreationActivity extends Activity {

	private Activity activity;

	private Toast toast;

	private Pattern pattern;

	private Matcher matcher;
	
	private EditText NOM;
	
	private EditText ADRESSE;
	
	private EditText VILLE;
	
	private EditText CODE_POSTAL;
	
	private EditText LOGIN;
	
	private EditText PASSWORD;
	
	private EditText PASSWORD_AGAIN;
	
	private EditText LATITUDE;
	
	private EditText LONGITUDE;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_creation);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
		
		NOM = (EditText) findViewById(R.id.activity_creation_nom);
		ADRESSE = (EditText) findViewById(R.id.activity_creation_adresse);
		VILLE = (EditText) findViewById(R.id.activity_creation_ville);
		PASSWORD = (EditText) findViewById(R.id.activity_creation_password);
		PASSWORD_AGAIN = (EditText) findViewById(R.id.activity_creation_passwordAgain);
		CODE_POSTAL = (EditText) findViewById(R.id.activity_creation_code_postal);
		LOGIN = (EditText) findViewById(R.id.activity_creation_login);
		LATITUDE = (EditText) findViewById(R.id.activity_creation_latitude);
		LONGITUDE = (EditText) findViewById(R.id.activity_creation_longitude);
	}

	/**
	 * M�thode permettant de tenter d'inscrire l'utilisateur.
	 * 
	 * @param view La vue lors du click sur le bouton d'inscription.
	 */
	public void inscription(View view) {
		if ((VILLE.getText().toString().length() != 0) && (PASSWORD.getText().toString().length() != 0) && 
				(PASSWORD_AGAIN.getText().toString().length() != 0) && (CODE_POSTAL.getText().toString().length() != 0) &&
				(NOM.getText().toString().length() != 0) && (ADRESSE.getText().toString().length() != 0) 
				&& (LOGIN.getText().toString().length() != 0) && (LATITUDE.getText().toString().length() != 0) 
				&& (LONGITUDE.getText().toString().length() != 0)) {

			if (PASSWORD.getText().toString().equals(PASSWORD_AGAIN.getText().toString())) {
					ProgressDialog progress = new ProgressDialog(activity);
					progress.setMessage("Chargement...");
					new InscriptionAdmin(progress, Constantes.URL_SERVER + 
								 "centre" + 
								 "?nom=" + NOM.getText().toString() + 
								 "&adresse=" + ADRESSE.getText().toString() + 
								 "&ville=" + VILLE.getText().toString() + 
								 "&codePostal=" + CODE_POSTAL.getText().toString() + 
								 "&login=" + LOGIN.getText().toString() + 
								 "&latitude=" + LATITUDE.getText().toString() + 
								 "&longitude=" + LONGITUDE.getText().toString() + 
								 "&password=" + SecurityUtils.sha256(PASSWORD.getText().toString())).execute();	
			} else {
				PASSWORD.setBackgroundResource(R.drawable.border_edittext_error);
				PASSWORD_AGAIN.setBackgroundResource(R.drawable.border_edittext_error);
				
				toast.setText("Les mots de passe saisis ne sont pas identiques.");
				toast.show();
			}
		} else {
			boolean isPseudoOK = true;
			boolean isPasswordOK = true;
			boolean isPasswordAgainOK = true;
			boolean isNomOK = true;
			
			String txtToast = "";
			
			if (VILLE.getText().toString().length() == 0) {
				VILLE.setBackgroundResource(R.drawable.border_edittext_error);
				isPseudoOK = false;
			} else {
				VILLE.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (PASSWORD.getText().toString().length() == 0) {
				PASSWORD.setBackgroundResource(R.drawable.border_edittext_error);
				isPasswordOK = false;
			} else {
				PASSWORD.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (PASSWORD_AGAIN.getText().toString().length() == 0) {
				PASSWORD_AGAIN.setBackgroundResource(R.drawable.border_edittext_error);
				isPasswordAgainOK = false;
			} else {
				PASSWORD_AGAIN.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (CODE_POSTAL.getText().toString().length() == 0) {
				CODE_POSTAL.setBackgroundResource(R.drawable.border_edittext_error);
			} else {
				CODE_POSTAL.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (NOM.getText().toString().length() == 0) {
				NOM.setBackgroundResource(R.drawable.border_edittext_error);
				isNomOK = false;
			} else {
				NOM.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (ADRESSE.getText().toString().length() == 0) {
				ADRESSE.setBackgroundResource(R.drawable.border_edittext_error);
			} else {
				ADRESSE.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (!isPseudoOK && !isPasswordOK && !isPasswordAgainOK && !isNomOK) {
				txtToast = "Plusieurs champs sont vides.";
			} else {
				if (!isPseudoOK) {
					txtToast = "Le champ \"Pseudo\" est vide.\n";
				} 
				
				if (!isPasswordOK) {
					txtToast += "Le champ \"Mot de passe\" est vide.\n";
				}
				
				if (!isPasswordAgainOK) {
					txtToast += "Le deuxième champ \"Mot de passe\" est vide.\n";
				} 
				
				if (!isNomOK) {
					txtToast += "Le champ \"Nom\" est vide.\n";
				} 
				
			}
			
			toast.setText(txtToast);
			toast.show();
		}
	}

	/* Classe interne. */
	
	private class InscriptionAdmin extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public InscriptionAdmin(ProgressDialog progress, String pathUrl) {
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
		    HttpPut httpPut = new HttpPut(pathUrl);

		    try {
		        HttpResponse response = httpclient.execute(httpPut);
		        
		        if (response.getStatusLine().getStatusCode() == 200) {
		        	toast.setText("Création effectuée.");
		        	toast.show();
		        	
		        	finish();
		        } else if (response.getStatusLine().getStatusCode() == 403) {
		        	toast.setText("Un centre avec ce nom existe déjà.");	
					toast.show();
		        } else if (response.getStatusLine().getStatusCode() == 500) {
		        	toast.setText("Une erreur est survenue au niveau de la BDD.");	
					toast.show();
		        } else {
		        	toast.setText("Erreur inconnue. Veuillez réessayer.");	
					toast.show();
		        }
		    } catch (ClientProtocolException e) {
		    	CreationActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationActivity.this);
						builder.setMessage("Erreur - Vérifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationActivity.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    } catch (IOException e) {
		    	CreationActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationActivity.this);
						builder.setMessage("Erreur - Vérifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationActivity.this.finish();
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

	/* Méthode h�rit�e de la classe Activity. */

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