package com.matpil.farmaciaditurno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.matpil.farmaciaditurno.model.Farmacia;
import com.matpil.farmaciaditurno.model.Schedule;

public class MainActivity extends Activity {

	private HashMap<String, Schedule> master = null;
	private List<Farmacia> farmacie = null;
	private String dateForSearch = null;
	protected int mYear;
	protected int mMonth;
	protected int mDay;
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		loadInputFile();
		createTodayStringkey();
//		updateDateSearch();
		Button dateTo = (Button) findViewById(R.id.dateToSearch);
		dateTo.setText(String.format("GIORNO: %s", dateForSearch));
		View finder =  findViewById(R.id.finder);
		retrieveDate(dateTo);
		findPharmacy(finder);
	}

//	private void updateDateSearch() {
//		TextView today = (TextView) findViewById(R.id.daySearch);
//		today.setText("GIORNO RICERCA: " + dateForSearch);
//	}

	private void findPharmacy(View finder) {
		finder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cleanLayout();
				Schedule s = master.get(dateForSearch);
				if (s == null) {
					showMessageError(v.getContext());
				} else {
					List<Farmacia> festivi = s.getFestivi();
					List<Farmacia> notturno = s.getNotturno();
					findViewById(R.id.LinearLayout2).setVisibility(TextView.VISIBLE);
					if (festivi.isEmpty() && notturno.isEmpty()) {
						TextView message = (TextView) findViewById(R.id.notturno);
						message.setText("NESSUNA FARMACIA APERTA TROVATA");
						message.setVisibility(TextView.VISIBLE);
					}
					if (!notturno.isEmpty()) {
						Farmacia pharm = notturno.get(0);
						TextView message = (TextView) findViewById(R.id.notturno);
						message.setText("NOTTURNO: " + pharm.getName());
						message.setVisibility(TextView.VISIBLE);
						showPharmDetail(message, pharm);
					}
					if (!festivi.isEmpty()) {
						Farmacia fest1 = festivi.get(0);
						TextView festivo1 = (TextView) findViewById(R.id.festivo1);
						festivo1.setText("FESTIVO: " + fest1.getName());
						festivo1.setVisibility(TextView.VISIBLE);
						showPharmDetail(festivo1, fest1);
						if (festivi.size() == 2) {
							Farmacia fest2 = festivi.get(1);
							TextView festivo2 = (TextView) findViewById(R.id.festivo2);
							festivo2.setText("FESTIVO: " + fest2.getName());
							festivo2.setVisibility(TextView.VISIBLE);
							showPharmDetail(festivo2, fest2);
						}
					}
				}
			}

			private void showMessageError(Context context) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("DATA NON VALIDA");
				alertDialog.setMessage("INTERVALLO DATA VALIDO: 01/01/2015 - 30/11/2015");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialog.show();
			}
		});
	}

	private void cleanLayout() {
		findViewById(R.id.notturno).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.festivo1).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.festivo2).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.address).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.LinearLayout2).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.LinearLayoutMaps).setVisibility(TextView.INVISIBLE);
	}

	private void retrieveDate(Button dateTo) {
		dateTo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cleanLayout();
				showDialog(0);
			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	protected void showPharmDetail(TextView tView, final Farmacia pharm) {
		tView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int idx = farmacie.indexOf(pharm);
				if (idx > -1) {
					findViewById(R.id.LinearLayoutMaps).setVisibility(
							TextView.VISIBLE);
					String msg = farmacie.get(idx).getWay();
					// Toast.makeText(getApplicationContext(), msg,
					// Toast.LENGTH_SHORT).show();
					TextView address = (TextView) findViewById(R.id.address);
					address.setText(msg);
					address.setVisibility(TextView.VISIBLE);
				} else {
					Toast.makeText(getApplicationContext(),
							"NESSUN INDIRIZZO TROVATO", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
	}

	protected DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			String day = null;
			String month = null;
			if (mDay < 10)
				day = "0" + mDay;
			else
				day = "" + mDay;
			if (mMonth < 10)
				month = "0" + mMonth;
			else
				month = "" + mMonth;
			dateForSearch = String.format("%s/%s/%s", day, month, mYear);
			Button dateTo = (Button) findViewById(R.id.dateToSearch);
			dateTo.setText(String.format("GIORNO: %s", dateForSearch));
		}
	};

	private void createTodayStringkey() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
		dateForSearch = sdf.format(new Date());
	}

	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void loadInputFile() {
		if (master == null)
			master = new HashMap<String, Schedule>();
		readTextFile(this, getResources().getString(R.string.nomeFile));
		if (farmacie == null) {
			farmacie = new ArrayList<Farmacia>();
		}
		readTextFilePharm(this,
				getResources().getString(R.string.nomeFileFarmacie));
		Toast.makeText(this, "Caricamento completato", Toast.LENGTH_LONG)
				.show();
	}

	private void readTextFile(Context context, String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(fileName)));
			String line;
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",", true);
				while (st.hasMoreTokens()) {
					Schedule s = new Schedule();
					String day = checkToken(st);
					addFarmacia(s, checkToken(st), "F");
					addFarmacia(s, checkToken(st), "F");
					addFarmacia(s, checkToken(st), "N");
					master.put(day, s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	private void readTextFilePharm(Context context, String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(fileName)));
			String line;
			Farmacia f = new Farmacia();
			boolean start = true;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				// System.out.println(line);
				if ("".equals(line)) {
					f.setWay(sb.toString());
					farmacie.add(f);
					f = new Farmacia();
					start = true;
					sb = new StringBuffer();
				} else if (start) {
					start = false;
					f.setName(line);
				} else {
					sb.append(line + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	private void addFarmacia(Schedule s, String farName, String tipo) {
		if (farName != null && !"".equals(farName)) {
			Farmacia farmacia = new Farmacia();
			farmacia.setName(farName);
			if ("F".equals(tipo))
				s.addFarmaciaInGiorniFestivi(farmacia);
			else if ("N".equals(tipo))
				s.addFarmaciaNotturno(farmacia);
		}

	}

	private String checkToken(StringTokenizer st) {
		if (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (",".equals(token)) {
				return "";
			} else {
				if (st.hasMoreTokens())
					st.nextToken();
				return token;
			}
		}
		return null;
	}

}
