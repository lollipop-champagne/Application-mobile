package org.gl.jmd.view.etudiant.listing;

import java.util.*;

import org.gl.jmd.Constantes;
import org.gl.jmd.R;
import org.gl.jmd.dao.EtudiantDAO;
import org.gl.jmd.model.*;
import org.gl.jmd.model.enumeration.*;
import org.gl.jmd.view.etudiant.StatsAnnee;
import org.gl.jmd.view.etudiant.create.SaisieNoteE;
import org.gl.jmd.view.list.*;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.*;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * Activit� correspondant � la vue de listing des UE d'une ann�e (en local).
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class ListeUEE extends Activity {

	private int positionDip = 0;

	private int positionAnn = 0;

	private Etudiant etud = EtudiantDAO.load();

	private Annee ann;

	private DecoupageYearType d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.etudiant_liste_ue);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		positionDip = getIntent().getExtras().getInt("positionDip");
		positionAnn = getIntent().getExtras().getInt("positionAnn");

		ann = etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn);

		d = (DecoupageYearType) getIntent().getExtras().getSerializable("type");

		if (d != null) {
			RelativeLayout r = (RelativeLayout) findViewById(R.id.etudiant_liste_ue_bandeau);
			r.setVisibility(View.GONE);

			initListe(d);
		} else {
			initListe();
			initTitleView();
		}
	}

	private void initTitleView() {
		TextView tvTitre = (TextView) findViewById(R.id.etudiant_liste_ue_titre);

		if (ann.getNom().length() > Constantes.LIMIT_TEXT) {
			tvTitre.setText(ann.getNom().substring(0, Constantes.LIMIT_TEXT) + "...");
		} else {
			tvTitre.setText(ann.getNom());
		}
	}

	/**
	 * M�thode d�clench�e lors d'un click sur le bouton de simulation.
	 * 
	 * @param view La vue lors du click sur le bouton de simulation.
	 */
	public void simulerObtentionAnnee(View view) {
		Intent i = new Intent(ListeUEE.this, StatsAnnee.class);
		i.putExtra("annee", ann);

		startActivity(i);
	}

	private void initListe() {
		if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().size() == 0) {
			ListView liste = (ListView) findViewById(R.id.listUEEtu);
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("titre", "Aucune UE.");

			listItem.add(map);		

			liste.setAdapter(new SimpleAdapter (getBaseContext(), listItem, R.layout.simple_list, new String[] {"titre"}, new int[] {R.id.titre}));
			liste.setOnItemClickListener(null);
		} else {
			List<Item> items = new ArrayList<Item>();

			if (ann.getDecoupage() == DecoupageType.NULL) {
				for (int s = 0; s < etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().size(); s++) {
					items.add(new Header(etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getNom()));	

					for (int b = 0; b < etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getListeMatieres().size(); b++) {
						items.add(new ListItemMatiere(etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getListeMatieres().get(b), s, b));	
					}

					if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getMoyenne(ann.getListeRegles()) != -1.0) {
						if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).estAjourne(ann.getListeRegles())) {
							items.add(new Footer("D�faillant."));
						} else if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getMoyenne(ann.getListeRegles()) < 10) {
							items.add(new Footer("Non valid�e."));
						} else {
							items.add(new Footer("Valid�e."));
						}
					}
				}
			} 

			final TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);

			final ListView liste = (ListView) findViewById(R.id.listUEEtu);

			liste.setAdapter(adapter);

			liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
					try {
						Header h = ((Header) adapter.getItem(position));

						if (h != null) {
							return;
						}
					} catch(Exception e) {
						// Do nothing.
					}

					try {
						Footer f = ((Footer) adapter.getItem(position));

						if (f != null) {
							return;
						}
					} catch(Exception e) {
						// Do nothing.
					}

					if (((ListItemMatiere) adapter.getItem(position)).getMatiere() == null) {
						// Do nothing.
					} else {
						Intent newIntent = new Intent(ListeUEE.this, SaisieNoteE.class);
						newIntent.putExtra("matiere", ((ListItemMatiere) adapter.getItem(position)).getMatiere());

						startActivity(newIntent);
					}
				}
			});
		}
	}

	private void initListe(DecoupageYearType d) {
		if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().size() == 0) {
			ListView liste = (ListView) findViewById(R.id.listUEEtu);
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("titre", "Aucune UE.");

			listItem.add(map);		

			liste.setAdapter(new SimpleAdapter (getBaseContext(), listItem, R.layout.simple_list, new String[] {"titre"}, new int[] {R.id.titre}));
			liste.setOnItemClickListener(null);
		} else {
			List<Item> items = new ArrayList<Item>();

			for (int s = 0; s < etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().size(); s++) {
				if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getDecoupage() == d) {
					items.add(new Header(etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getNom()));	

					if (!etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getListeMatieres().isEmpty()) {
						for (int b = 0; b < etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getListeMatieres().size(); b++) {
							items.add(new ListItemMatiere(etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getListeMatieres().get(b), s, b));	
						}
					} else {
						items.add(new ListItemMatiere());	
					}

					if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getMoyenne(ann.getListeRegles()) != -1.0) {
						if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).estAjourne(ann.getListeRegles())) {
							items.add(new Footer("D�faillant."));
						} else if (etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getMoyenne(ann.getListeRegles()) < 10) {
							items.add(new Footer("Non valid�e."));
						} else {
							items.add(new Footer("Valid�e (" + etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn).getListeUE().get(s).getMoyenne(ann.getListeRegles()) + "/20)."));
						}
					}
				}
			}

			final TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);

			final ListView liste = (ListView) findViewById(R.id.listUEEtu);

			liste.setAdapter(adapter);

			liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
					try {
						Header h = ((Header) adapter.getItem(position));

						if (h != null) {
							return;
						}
					} catch(Exception e) {
						// Do nothing.
					}

					try {
						Footer f = ((Footer) adapter.getItem(position));

						if (f != null) {
							return;
						}
					} catch(Exception e) {
						// Do nothing.
					}

					if (((ListItemMatiere) adapter.getItem(position)).getMatiere() == null) {
						// Do nothing.
					} else {
						Intent newIntent = new Intent(ListeUEE.this, SaisieNoteE.class);
						newIntent.putExtra("matiere", ((ListItemMatiere) adapter.getItem(position)).getMatiere());

						startActivity(newIntent);
					}
				}
			});
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
	 * M�thode ex�cut�e lorsque l'activit� est relanc�e.
	 */
	@Override
	public void onRestart() {
		etud = EtudiantDAO.load();
		ann = etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn);

		if (d != null) {
			RelativeLayout r = (RelativeLayout) findViewById(R.id.etudiant_liste_ue_bandeau);
			r.setVisibility(View.GONE);

			initListe(d);
		} else {
			initListe();
			initTitleView();
		}

		super.onRestart();
	} 

	/**
	 * M�thode ex�cut�e lorsque l'activit� est relanc�e.
	 */
	@Override
	public void onResume() {
		etud = EtudiantDAO.load();
		ann = etud.getListeDiplomes().get(positionDip).getListeAnnees().get(positionAnn);

		if (d != null) {
			RelativeLayout r = (RelativeLayout) findViewById(R.id.etudiant_liste_ue_bandeau);
			r.setVisibility(View.GONE);

			initListe(d);
		} else {
			initListe();
			initTitleView();
		}

		super.onResume();
	} 
}