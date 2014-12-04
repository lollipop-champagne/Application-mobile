package org.gl.jmd.view.admin.listing;

import java.io.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.gl.jmd.*;
import org.gl.jmd.model.*;
import org.gl.jmd.model.enumeration.DecoupageType;
import org.gl.jmd.utils.*;
import org.gl.jmd.view.Accueil;
import org.gl.jmd.view.admin.create.CreationAnnee;
import org.gl.jmd.view.list.SwipeDismissListViewTouchListener;
import org.json.*;


import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.View;
import android.widget.*;

/**
 * Activit� correspondant � la vue de listing des ann�es d'un dipl�me.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class ListeAnneeA extends Activity {

	private Activity activity;

	private Diplome d = null;

	private Toast toast;

	private ArrayList<Annee> listeAnnees = new ArrayList<Annee>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.administrateur_liste_annee);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		d = (Diplome) getIntent().getExtras().getSerializable("diplome");

		activity = this;
		toast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

		initTextView();
		actualiserListe();
	}

	private void initTextView() {
		TextView tvTitre = (TextView) findViewById(R.id.admin_liste_annee_titre);
		tvTitre.setText(d.getNom());
	}

	private void actualiserListe() {	
		listeAnnees.clear();

		ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Chargement...");
		new ListerAnnees(progress, Constantes.URL_SERVER + "annee/getAnneesFollowedByDiplome" +
				"?idDiplome=" + d.getId() +
				"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
				"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
				"&timestamp=" + new java.util.Date().getTime()).execute();	
	}

	private void initListe() {
		final ListView liste = (ListView) findViewById(android.R.id.list);

		if (listeAnnees.size() > 0) {
			final ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;

			for(int s = 0; s < listeAnnees.size(); s++) {
				map = new HashMap<String, String>();

				map.put("id", "" + listeAnnees.get(s).getId());
				map.put("titre", listeAnnees.get(s).getNom());
				map.put("decoupage", listeAnnees.get(s).getDecoupage().name());
				map.put("description", listeAnnees.get(s).getEtablissement().getNom() + " - " + listeAnnees.get(s).getEtablissement().getVille());
				map.put("isLastYear", "" + listeAnnees.get(s).isLast());

				if (listeAnnees.get(s).isFollowed()) {
					map.put("img", String.valueOf(R.drawable.star_list));
				} else {
					map.put("img", null);
				}
				
				listItem.add(map);		
			}

			final SimpleAdapter adapter = new SimpleAdapter (getBaseContext(), listItem, R.layout.administrateur_liste_annee_list, new String[] {"titre", "description", "img"}, new int[] {R.id.titre, R.id.description, R.id.img});

			liste.setAdapter(adapter); 

			SwipeDismissListViewTouchListener touchListener =
					new SwipeDismissListViewTouchListener(liste,
							new SwipeDismissListViewTouchListener.DismissCallbacks() {
								@Override
								public boolean canDismiss(int position) {
									return true;
								}

								@Override
								public void onDismiss(ListView listView, int[] reverseSortedPositions) {
									for (final int position : reverseSortedPositions) {
										AlertDialog.Builder confirmSuppr = new AlertDialog.Builder(ListeAnneeA.this);
										confirmSuppr.setTitle("Suppression");
										confirmSuppr.setMessage("Voulez-vous vraiment supprimer cet �l�ment ?");
										confirmSuppr.setCancelable(false);
										confirmSuppr.setPositiveButton("Oui", new AlertDialog.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												ProgressDialog progress = new ProgressDialog(activity);
												progress.setMessage("Chargement...");
												new DeleteAnnee(progress, Constantes.URL_SERVER + "annee" +
														"?id=" + listeAnnees.get(position).getId() +
														"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
														"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
														"&timestamp=" + new java.util.Date().getTime()).execute();

												adapter.notifyDataSetChanged();
											}
										});
										
										confirmSuppr.setNegativeButton("Non", null);
										confirmSuppr.show();
									}
									
									adapter.notifyDataSetChanged();
								}
							}
					);
			
			liste.setOnTouchListener(touchListener);

			liste.setOnScrollListener(touchListener.makeScrollListener());

			liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
					Intent newIntent = new Intent(ListeAnneeA.this, ListeUERegleA.class);
					newIntent.putExtra("annee", listeAnnees.get(position));
					startActivity(newIntent);
				}
			});

			liste.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {
					if (listItem.get(arg2).get("img") == null) {
						AlertDialog.Builder confirmSuppr = new AlertDialog.Builder(ListeAnneeA.this);
						confirmSuppr.setTitle("Suivre l'ann�e");
						confirmSuppr.setMessage("Voulez-vous vraiment suivre cette ann�e ?");
						confirmSuppr.setCancelable(false);
						confirmSuppr.setPositiveButton("Oui", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ProgressDialog progress = new ProgressDialog(activity);
								progress.setMessage("Chargement...");
								new FollowAnnee(progress, Constantes.URL_SERVER + "admin/follow" +
										"?idAnnee=" + listeAnnees.get(arg2).getId() +
										"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
										"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
										"&timestamp=" + new java.util.Date().getTime()).execute();	
							}
						});
	
						confirmSuppr.setNegativeButton("Non", null);
						confirmSuppr.show();
					} else {
						AlertDialog.Builder confirmSuppr = new AlertDialog.Builder(ListeAnneeA.this);
						confirmSuppr.setTitle("Ne plus suivre l'ann�e");
						confirmSuppr.setMessage("Voulez-vous vraiment ne plus suivre cette ann�e ?");
						confirmSuppr.setCancelable(false);
						confirmSuppr.setPositiveButton("Oui", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ProgressDialog progress = new ProgressDialog(activity);
								progress.setMessage("Chargement...");
								new UnfollowAnnee(progress, Constantes.URL_SERVER + "admin/unfollow" +
										"?idAnnee=" + listeAnnees.get(arg2).getId() +
										"&token=" + FileUtils.readFile("/sdcard/cacheJMD/token.jmd") + 
										"&pseudo=" + FileUtils.readFile("/sdcard/cacheJMD/pseudo.jmd") +
										"&timestamp=" + new java.util.Date().getTime()).execute();	
							}
						});
	
						confirmSuppr.setNegativeButton("Non", null);
						confirmSuppr.show();
					}
					
					return true;
				}
			}); 
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("titre", "Aucune ann�e.");

			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
			listItem.add(map);

			liste.setAdapter(new SimpleAdapter (getBaseContext(), listItem, R.layout.simple_list, new String[] {"titre"}, new int[] {R.id.titre})); 
		}
	}

	public void creerAnnee(View view) {
		Intent newIntent = new Intent(ListeAnneeA.this, CreationAnnee.class);
		newIntent.putExtra("idDiplome", d.getId());

		startActivity(newIntent);
	}

	/* Classes internes. */

	private class ListerAnnees extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public ListerAnnees(ProgressDialog progress, String pathUrl) {
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
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			HttpGet httpGet = null;
			String response = null;
			
			try {
				httpGet = new HttpGet(pathUrl);
				httpResponse = httpClient.execute(httpGet);
				
				httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				
				if (httpEntity != null) {
				    try {
				        httpEntity.consumeContent();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {            	
				try {
					JSONArray annees = new JSONArray(response);
					Annee a = null;
					
					for (int i = 0; i < annees.length(); i++) {
						JSONObject c = annees.getJSONObject(i);

						a = new Annee();
						a.setId(c.getInt("idAnnee"));
						a.setNom(c.getString("nom"));
						a.setIsLast(c.getBoolean("isLastYear"));
						a.setDecoupage(DecoupageType.valueOf(c.getString("decoupage")));
						a.setIsFollowed(c.getBoolean("isFollowed"));
						
						JSONObject etaFromAnnee = c.getJSONObject("etablissement");
						Etablissement e = new Etablissement();
						e.setId(etaFromAnnee.getInt("idEtablissement"));
						e.setNom(etaFromAnnee.getString("nom"));
						e.setVille(etaFromAnnee.getString("ville"));

						a.setEtablissement(e);

						listeAnnees.add(a);
					}

					ListeAnneeA.this.runOnUiThread(new Runnable() {
						public void run() {    						
							initListe();
						}
					});
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			} else if (httpResponse.getStatusLine().getStatusCode() == 401) { 
				File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
	        	File fileToken = new File("/sdcard/cacheJMD/token.jmd");

	        	filePseudo.delete();
	        	fileToken.delete();

	        	activity.finishAffinity();
	        	startActivity(new Intent(ListeAnneeA.this, Accueil.class));	

	        	toast.setText("Session expir�e.");	
	        	toast.show();
			}

			return null;
		}
	}

	private class DeleteAnnee extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public DeleteAnnee(ProgressDialog progress, String pathUrl) {
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
			HttpDelete httpDelete = new HttpDelete(pathUrl);

			try {
				final HttpResponse response = httpclient.execute(httpDelete);

				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {    			
						if (response.getStatusLine().getStatusCode() == 200) {
							ListeAnneeA.this.runOnUiThread(new Runnable() {
								public void run() {    						
									actualiserListe();
								}
							});

							toast.setText("Ann�e supprim�e.");
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 401) {
							File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
							File fileToken = new File("/sdcard/cacheJMD/token.jmd");

							filePseudo.delete();
							fileToken.delete();

							activity.finishAffinity();
							startActivity(new Intent(ListeAnneeA.this, Accueil.class));	

							toast.setText("Session expir�e.");	
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 500) {				        	
							toast.setText("Une erreur est survenue au niveau de la BDD.");	
							toast.show();
						} else {
							toast.setText("Erreur inconnue. Veuillez r�essayer.");	
							toast.show();
						}

						return;
					}
				});
			} catch (ClientProtocolException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} catch (IOException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
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

	private class FollowAnnee extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public FollowAnnee(ProgressDialog progress, String pathUrl) {
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
				final HttpResponse response = httpclient.execute(httpGet);

				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {    			
						if (response.getStatusLine().getStatusCode() == 200) {
							ListeAnneeA.this.runOnUiThread(new Runnable() {
								public void run() {    						
									actualiserListe();
								}
							});
							
							toast.setText("Vous suivez d�sormais cette ann�e.");
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 401) {
							File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
							File fileToken = new File("/sdcard/cacheJMD/token.jmd");

							filePseudo.delete();
							fileToken.delete();

							activity.finishAffinity();
							startActivity(new Intent(ListeAnneeA.this, Accueil.class));	

							toast.setText("Session expir�e.");	
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 500) {				        	
							toast.setText("Une erreur est survenue au niveau de la BDD.");	
							toast.show();
						} else {
							toast.setText("Erreur inconnue. Veuillez r�essayer.");	
							toast.show();
						}

						return;
					}
				});
			} catch (ClientProtocolException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} catch (IOException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
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
	
	private class UnfollowAnnee extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private String pathUrl;

		public UnfollowAnnee(ProgressDialog progress, String pathUrl) {
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
				final HttpResponse response = httpclient.execute(httpGet);

				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {    			
						if (response.getStatusLine().getStatusCode() == 200) {
							ListeAnneeA.this.runOnUiThread(new Runnable() {
								public void run() {    						
									actualiserListe();
								}
							});
							
							toast.setText("Vous ne suivez plus cette ann�e.");
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 401) {
							File filePseudo = new File("/sdcard/cacheJMD/pseudo.jmd");
							File fileToken = new File("/sdcard/cacheJMD/token.jmd");

							filePseudo.delete();
							fileToken.delete();

							activity.finishAffinity();
							startActivity(new Intent(ListeAnneeA.this, Accueil.class));	

							toast.setText("Session expir�e.");	
							toast.show();
						} else if (response.getStatusLine().getStatusCode() == 500) {				        	
							toast.setText("Une erreur est survenue au niveau de la BDD.");	
							toast.show();
						} else {
							toast.setText("Erreur inconnue. Veuillez r�essayer.");	
							toast.show();
						}

						return;
					}
				});
			} catch (ClientProtocolException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
							}
						});

						AlertDialog error = builder.create();
						error.show();
					}
				});
			} catch (IOException e) {
				ListeAnneeA.this.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeAnneeA.this);
						builder.setMessage("Erreur - V�rifiez votre connexion");
						builder.setCancelable(false);
						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								ListeAnneeA.this.finish();
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
	 * M�thode ex�cut�e lorsque l'activit� est relanc�e.<br />
	 * Ici, �a permet d'actualiser la liste des ann�es lorsqu'une ann�e vient d'�tre cr�� et que l'application ram�ne l'utilisateur sur cette vue de listing.
	 */
	@Override
	public void onRestart() {		
		actualiserListe();

		super.onRestart();
	} 
}