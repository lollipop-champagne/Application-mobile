package org.gl.jmd.view.admin.create;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gl.jmd.*;
import org.gl.jmd.model.*;
import org.gl.jmd.model.enumeration.DecoupageType;
import org.gl.jmd.utils.*;
import org.gl.jmd.view.Accueil;
import org.json.*;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.AdapterView.*;
import android.widget.*;

/**
 * Activit� correspondant � la vue de cr�ation d'une ann�e d'un dipl�me.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class CreationAnnee extends Activity {
	
	private Activity activity;

	private Toast toast;
	
	private int idDiplome = 0;
	
	private Etablissement selectedEta = null;
	
	private CheckBox LAST_YEAR;
	
	private EditText NOM;
	
	private EditText ETA;
	
	private DecoupageType decoupage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.administrateur_creation_annee);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
		
		LAST_YEAR = (CheckBox) findViewById(R.id.administrateur_creation_annee_is_last_year);		
		NOM = (EditText) findViewById(R.id.admin_creation_annee_nom);		
		ETA = (EditText) findViewById(R.id.admin_creation_annee_nom_eta);
		
		idDiplome = getIntent().getExtras().getInt("idDiplome");
		
		initListeDecoupage();
	}
	
	private void initListeDecoupage() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.decoupage_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner spinner = (Spinner) findViewById(R.id.admin_creation_annee_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		if (parent.getItemAtPosition(pos).toString().equals("Aucun")) {
        			decoupage = DecoupageType.NULL;
        		} else if (parent.getItemAtPosition(pos).toString().equals("Trimestre")) {
        			decoupage = DecoupageType.TRIMESTRE;
        		} else if (parent.getItemAtPosition(pos).toString().equals("Semestre")) {
        			decoupage = DecoupageType.SEMESTRE;
        		} 
        	}
        	
        	@Override
        	public void onNothingSelected(AdapterView<?> parent) {
        		// Empty
        	}
		});
	}
	
	public void openListEta(View view) {
		ProgressDialog progress = new ProgressDialog(activity);
		progress.setMessage("Chargement...");
		new InitEtablissement(progress, Constantes.URL_SERVER + "etablissement/getAll").execute();	
	}
	
	/**
	 * M�thode permettant de cr�er une ann�e (d�clench�e lors d'un click sur le bouton "cr�er").
	 * 
	 * @param view La vue lors du click sur le bouton de cr�ation.
	 */
	public void creerAnnee(View view) {
		if ((NOM.getText().toString().length() != 0) && (ETA.getText().toString().length() != 0)) {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s]*$");
			Matcher matcher = pattern.matcher(NOM.getText().toString());
			
			if (!matcher.matches()) {
				NOM.setBackgroundResource(R.drawable.border_edittext_error);
				
				toast.setText("Le nom ne peut contenir que des chiffres et des lettres.");
				toast.show();
				
				return;
			} else {
				NOM.setBackgroundResource(R.drawable.border_edittext);
			}
			
			Annee a = new Annee();
			a.setNom(NOM.getText().toString());
			a.setDecoupage(decoupage);
			a.setIsLast(LAST_YEAR.isChecked());
			
			Etablissement etablissement = selectedEta;
			
			if (etablissement == null) {
				ETA.setBackgroundResource(R.drawable.border_edittext_error);
				
				toast.setText("L'�tablissement entr� n'est pas valide.");
				toast.show();
				
				return;
			}
			
			a.setEtablissement(etablissement);	
			
			ProgressDialog progress = new ProgressDialog(activity);
			progress.setMessage("Chargement...");
			new CreerAnnee(progress, Constantes.URL_SERVER + "annee" +
					"?nom=" + URLEncoder.encode(a.getNom()) +
					"&idEtablissement=" + a.getEtablissement().getId() +
					"&idDiplome=" +  idDiplome +
					"&decoupage=" + a.getDecoupage().name() +
					"&isLastYear" + a.isLast() +
					"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
					"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
					"&timestamp=" + new java.util.Date().getTime()).execute(); 
		} else {
			boolean isNomOK = true;
			boolean isEtaOK = true;
			
			String txtToast = "";
			
			if (NOM.getText().toString().length() == 0) {
				NOM.setBackgroundResource(R.drawable.border_edittext_error);
				isNomOK = false;
			}
			
			if (ETA.getText().toString().length() == 0) {
				ETA.setBackgroundResource(R.drawable.border_edittext_error);
				isEtaOK = false;
			}
			
			if (!isNomOK && !isEtaOK) {
				txtToast = "Les deux champs sont vides.";
			} else if (!isEtaOK) {
				txtToast = "Le champ \"Etablissement\" est vide.";
			} else if (!isNomOK) {
				txtToast = "Le champ \"Nom\" est vide.";
			} 
			
			toast.setText(txtToast);
			toast.show();
		}
	}
	
	/* Classes internes. */
	
	private class InitEtablissement extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public InitEtablissement(ProgressDialog progress, String pathUrl) {
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
            String jsonStr = "";
            
			try {
				jsonStr = WebUtils.call(pathUrl, WebUtils.GET);
			} catch (SocketTimeoutException e1) {
				CreationAnnee.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationAnnee.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationAnnee.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} catch (ClientProtocolException e1) {
				CreationAnnee.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationAnnee.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationAnnee.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} catch (IOException e1) {
				CreationAnnee.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationAnnee.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationAnnee.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} 
            
            final ArrayList<Etablissement> listeEtablissements = new ArrayList<Etablissement>();
            final ArrayList<String> listeEtablissementsString = new ArrayList<String>();
            Etablissement e = null;
            
            if (jsonStr.length() > 0) {            	
                try {
                    JSONArray diplomes = new JSONArray(jsonStr);
 
                    for (int i = 0; i < diplomes.length(); i++) {
                    	JSONObject c = diplomes.getJSONObject(i);
                        
                        e = new Etablissement();
                        e.setId(c.getInt("idEtablissement"));
                        e.setNom(c.getString("nom"));
                        e.setVille(c.getString("ville"));
                        
                        listeEtablissementsString.add(e.getNom());
                        listeEtablissements.add(e);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } 
			
			CreationAnnee.this.runOnUiThread(new Runnable() {
				public void run() {
					if (listeEtablissements.size() > 0) {
						final Dialog dialog = new Dialog(activity);
					    dialog.setTitle("Liste des �tablissements");
					    dialog.setCancelable(true);
					    
					    ListView listView = new ListView(activity);
					    listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listeEtablissementsString));
					    
					    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					    	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
								ETA.setText(listeEtablissementsString.get(position));
								selectedEta = listeEtablissements.get(position);
								dialog.hide();
							}

						});
					    
					    dialog.setContentView(listView);
					    dialog.show();
					} 
				}
			});

			return null;
		}
	}
	
	private class CreerAnnee extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public CreerAnnee(ProgressDialog progress, String pathUrl) {
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
		        	toast.setText("Ann�e cr��e.");
		        	toast.show();
		        	
		        	finish();
		        } else if (response.getStatusLine().getStatusCode() == 403) {
		        	toast.setText("Une ann�e avec ce nom existe d�j�.");
		        	toast.show();
		        } else if (response.getStatusLine().getStatusCode() == 401) {
					File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
					File fileToken = new File("/sdcard/cacheJMD/token.jmd");
					
					filePseudo.delete();
					fileToken.delete();
		        	
					finishAffinity();
		        	startActivity(new Intent(CreationAnnee.this, Accueil.class));	
		        	
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
		    	CreationAnnee.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationAnnee.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationAnnee.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
		    } catch (IOException e) {
		    	CreationAnnee.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(CreationAnnee.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CreationAnnee.this.finish();
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
		if (NOM.getText().toString().length() != 0) {
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
