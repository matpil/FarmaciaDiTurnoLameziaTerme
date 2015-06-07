package com.matpil.farmaciaditurno;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.matpil.farmaciaditurno.adapter.FarmaciaAdapter;
import com.matpil.farmaciaditurno.model.Farmacia;
import com.matpil.farmaciaditurno.model.Schedule;
import com.matpil.farmaciaditurno.source.InputDataSource;

public class MainActivity extends Activity {

	private Map<String, Schedule> master = null;
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
		// updateDateSearch();
		Button dateTo = (Button) findViewById(R.id.dateToSearch);
		dateTo.setText(String.format("GIORNO: %s", dateForSearch));
		View finder = findViewById(R.id.finder);
		retrieveDate(dateTo);
		findPharmacy(finder);
	}

	private void findPharmacy(View finder) {
		finder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cleanLayout();
				Schedule s = master.get(dateForSearch);
				if (s == null) {
					showMessageError(v.getContext());
				} else {
					// List<Farmacia> festivi = s.getFestivi();
					// final List<Farmacia> notturno = s.getNotturno();
					final Map<String, Farmacia> schedule = s.getSchedule();
					findViewById(R.id.LinearLayoutList).setVisibility(TextView.VISIBLE);
					if (schedule.isEmpty()) {
						Toast.makeText(v.getContext(), "NESSUNA FARMACIA APERTA TROVATA", Toast.LENGTH_LONG).show();
					} else {
						final ListView lView = (ListView) findViewById(R.id.listView);
						FarmaciaAdapter fAdapter = new FarmaciaAdapter(v.getContext(), schedule);
						lView.setAdapter(fAdapter);
						lView.setVisibility(ListView.VISIBLE);
						lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								@SuppressWarnings("unchecked")
								Map.Entry<String, Farmacia> entry = (Map.Entry<String, Farmacia>) parent.getItemAtPosition(position);
								Farmacia farmacia = entry.getValue();
								showPharmInfo(farmacia);
							}

						});
					}
				}
			}

			private void showMessageError(Context context) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("DATA NON VALIDA");
				alertDialog.setMessage("INTERVALLO DATA VALIDO: 01/01/2015 - 30/11/2015");
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				alertDialog.show();
			}
		});
	}

	private void showPharmInfo(final Farmacia pharm) {
		int idx = farmacie.indexOf(pharm);
		if (idx > -1) {
			findViewById(R.id.LinearLayoutMaps).setVisibility(TextView.VISIBLE);
			final String address = farmacie.get(idx).getWay();
			final String phone = farmacie.get(idx).getPhone();
			// Toast.makeText(getApplicationContext(), msg,
			// Toast.LENGTH_SHORT).show();
			TextView addressView = (TextView) findViewById(R.id.address);
//			addressView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					Intent callIntent = new Intent(Intent.ACTION_VIEW);
//					Uri parse = Uri.parse("geo:"+address.trim());
//					Log.i("PARSE", parse.toString());
//					callIntent.setData(parse);
//					startActivity(callIntent);
//				}
//			});
			TextView phoneView = (TextView) findViewById(R.id.phone);
			phoneView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent callIntent = new Intent(Intent.ACTION_DIAL);
					Uri parse = Uri.parse(phone.trim());
					Log.i("PARSE", parse.toString());
					callIntent.setData(parse);
					startActivity(callIntent);
				}
			});
			addressView.setText(address);
			addressView.setVisibility(TextView.VISIBLE);
			phoneView.setText(phone.toUpperCase(Locale.ITALIAN));
			phoneView.setVisibility(TextView.VISIBLE);
		} else {
			Toast.makeText(getApplicationContext(), "NESSUN INDIRIZZO TROVATO", Toast.LENGTH_SHORT).show();
		}
	}

	private void cleanLayout() {
		ListView lview = (ListView) findViewById(R.id.listView);
		lview.setVisibility(ListView.INVISIBLE);
		lview.setAdapter(null);
		findViewById(R.id.address).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.LinearLayoutList).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.LinearLayoutMaps).setVisibility(TextView.INVISIBLE);
	}

	private void retrieveDate(Button dateTo) {
		dateTo.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
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

	protected DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
			master = InputDataSource.readTextFile(this, getResources().getString(R.string.nomeFile));
		if (farmacie == null) {
			farmacie = InputDataSource.readTextFilePharm(this, getResources().getString(R.string.nomeFileFarmacie));
		}
		Toast.makeText(this, "Caricamento completato", Toast.LENGTH_LONG).show();
	}

}
