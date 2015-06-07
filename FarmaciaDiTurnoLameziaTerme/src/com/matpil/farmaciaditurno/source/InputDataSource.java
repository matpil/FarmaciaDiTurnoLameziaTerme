package com.matpil.farmaciaditurno.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;

import com.matpil.farmaciaditurno.model.Farmacia;
import com.matpil.farmaciaditurno.model.Schedule;

public class InputDataSource {
	
	private static Map<String, Schedule> master = new HashMap<String, Schedule>();
	private static List<Farmacia> farmacie = new ArrayList<Farmacia>();
	
	public static Map<String, Schedule> readTextFile(Context context, String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
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
		return master;
	}

	public static List<Farmacia> readTextFilePharm(Context context, String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
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
				} else if (line.startsWith("tel")) {
					f.setPhone(line);
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
		return farmacie;
	}

	private static void addFarmacia(Schedule s, String farName, String tipo) {
		if (farName != null && !"".equals(farName)) {
			Farmacia farmacia = new Farmacia();
			farmacia.setName(farName);
			if ("F".equals(tipo))
				s.addFarmaciaInGiorniFestivi(farmacia);
			else if ("N".equals(tipo))
				s.addFarmaciaNotturno(farmacia);
		}

	}

	private static String checkToken(StringTokenizer st) {
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
