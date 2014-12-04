package org.gl.jmd.view.admin.create;

import java.io.*;
import java.net.URLEncoder;
import java.util.regex.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gl.jmd.Constantes;
import org.gl.jmd.R;
import org.gl.jmd.model.Matiere;
import org.gl.jmd.utils.*;
import org.gl.jmd.view.Accueil;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.*;

/**
 * Activit� correspondant � la vue de cr�ation d'une mati�re.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class CreationMatiere extends Activity {
	
	private Activity activity;

	private Toast toast;
	
	private int idUE = 0;
	
	private EditText NOM;
	
	private EditText COEFF;
	
	private CheckBox IS_OPTION;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.administrateur_creation_matiere);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
		
		NOM = (EditText) findViewById(R.id.admin_creation_matiere_nom);
		COEFF = (EditText) findViewById(R.id.admin_creation_matiere_coefficient);
		IS_OPTION = (CheckBox) findViewById(R.id.admin_creation_matiere_checkbox_option);
		
		idUE = getIntent().getExtras().getInt("idUE");
	}
	
	/**
	 * M�thode permettant de cr�er une mati�re (d�clench�e lors d'un click sur le bouton "cr�er").
	 * 
	 * @param view La vue lors du click sur le bouton de cr�ation.
	 */
	public void creerMatiere(View view) {
		if ((NOM.getText().toString().length() != 0) && (COEFF.getText().toString().length() != 0)) {		
			Pattern pattern = Pattern.compile("^[0-9]*$");
			Matcher matcher = pattern.matcher(COEFF.getText().toString());
			
			if (!matcher.matches()) {
				COEFF.setBackgroundResource(R.drawable.border_edittext_error);
				
				toast.setText("Le coefficient doit �tre un nombre.");
				toast.show();
				
				return;
			} else {
				COEFF.setBackgroundResource(R.drawable.border_edittext);
			}
			
			Matiere m = new Matiere();
			m.setNom(NOM.getText().toString());
			m.setCoefficient(Integer.parseInt(COEFF.getText().toString()));
			m.setIsOption(IS_OPTION.isChecked());
			
			ProgressDialog progress = new ProgressDialog(activity);
			progress.setMessage("Chargement...");
			new CreerMatiere(progress, Constantes.URL_SERVER + "matiere" +
					"?nom=" + URLEncoder.encode(m.getNom()) +
					"&coefficient=" + m.getCoefficient() +
					"&isOption=" + m.isOption() +
					"&idUE=" + idUE +
					"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
					"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
					"&timestamp=" + new java.util.Date().getTime()).execute();	
		} else {
			boolean isCoeffOK = true;
			boolean isNomOK = true;
			
			String txtToast = "";
			
			if (COEFF.getText().toString().length() == 0) {
				COEFF.setBackgroundResource(R.drawable.border_edittext_error);
				isCoeffOK = false;
			} else {
				COEFF.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (NOM.getText().toString().length() == 0) {
				NOM.setBackgroundResource(R.drawable.border_edittext_error);
				isNomOK = false;
			} else {
				NOM.setBackgroundResource(R.drawable.border_edittext);
			}
			
			if (!isNomOK && !isCoeffOK) {
				txtToast = "Les deux champs sont vides.";
			} else if (!isCoeffOK) {
				txtToast = "Le champ \"Coefficient\" est vide.";
			} else if (!isNomOK) {
				txtToast = "Le champ \"Nom\" est vide.";
			} 
			
			toast.setText(txtToast);
			toast.show();
		}
	}
	
	/* Classes internes. */
	
	private class CreerMatiere extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public CreerMatiere(ProgressDialog progress, String pathUrl) {
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
		    HttpPut httppost = new HttpPut(pathUrl);

		    try {
		        HttpResponse response = httpclient.execute(httppost);
		        
		        if (response.getStatusLine().getStatusCode() == 200) {
		        	toast.setText("Mati�re cr��e.");
		        	toast.show();
		        	
		        	finish();
		        } else if (response.getStatusLine().getStatusCode() == 401) {
					File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
					File fileToken = new File("/sdcard/cacheJMD/token.jmd");
					
					filePseudo.delete();
					fileToken.delete();
		        	
					activity.finishAffinity();
		        	startActivity(new Intent(CreationMatiere.this, Accueil.class));	
		        	
		        	toast.setText("Session expir�e.");	
					toast.show();
		        } else if (response.getStatusLine().getStatusCode() == 500) {
		        	toast.setText("Une erreur est survenue au niveau de la BDD.");	
					toast.show();
		        } else {
		        	toast.setText("Erreur inconnue. Veuillez r�essayer.");	
					toast.show();
		        }
		    } catch (ClientProtocolException e) {
		    	CreationMatiere.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationMatiere.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationMatiere.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    } catch (IOException e) {
		    	CreationMatiere.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationMatiere.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationMatiere.this.finish();
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
	@Override
	public void onBackPressed() {		
		if ((NOM.getText().toString().length() != 0) || (COEFF.getText().toString().length() != 0)) {
			AlertDialog.Builder confirmQuitter = new AlertDialog.Builder(this);
			confirmQuitter.setTitle("Annulation");
			confirmQuitter.setMessage("Voulez-vous vraiment annuler ?");
			confirmQuitter.setCancelable(false);
			confirmQuitter.setPositiveButton("Oui", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			});

			confirmQuitter.setNegativeButton("Non", null);
			confirmQuitter.show();
		} else {
			finish();
		}
	}
}
