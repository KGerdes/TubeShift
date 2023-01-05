package gosm.backend;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringLocalization {

	
	private static Logger log = Logger.getLogger(StringLocalization.class.getName());
	private Map<String, Map<String, String>> mapOfConstants = new HashMap<>();
	private List<Locale> locales;
	private Locale selected;
	private Map<String, String> selmap;
	
	public StringLocalization(InputStream strm, Locale... locs) {
		locales = Arrays.asList(locs);
		if (locales.isEmpty()) {
			throw new IllegalArgumentException();
		}
		selected = locales.get(0);
		initMaps();
		importConstants(strm);
		
	}

	private void initMaps() {
		for (Locale l : locales) {
			if (l.getLanguage() != null) {
				if (!mapOfConstants.containsKey(l.getLanguage())) {
					mapOfConstants.put(l.getLanguage(),new HashMap<>());
				}
			}
		}
		selmap = mapOfConstants.get(selected.getLanguage());
		if (selmap == null) {
			throw new IllegalArgumentException("Language " + selected.getLanguage() + " not in map");
		}
	}

	private void importConstants(InputStream strm) {
		Properties p = new Properties();
		try {
			p.load(strm);
		} catch (IOException e) {
			throw new TubeShiftException(e.getMessage(), e);
		}
		for (Object okey : p.keySet()) {
			addToConstants(p, okey.toString());
		}
		checkProperties();
	}

	private void checkProperties() {
		for (Map<String, String> tmp : mapOfConstants.values()) {
			if (tmp.size() != selmap.size()) {
				throw new TubeShiftException("Locale-Maps size differs");
			}
			if (tmp != selmap) {
				for (String key : tmp.keySet()) {
					int index = key.indexOf('.');
					findCorrespondingKey(key, index);
				}
			}
		}
	}
	
	private void findCorrespondingKey(String key, int index) {
		if (index >= 0) {
			String sh = key.substring(index);
			boolean found = false;
			for (String key2 : selmap.keySet()) {
				if (key2.endsWith(sh)) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new TubeShiftException("Key nicht gefunden: " + key);
			}
		}
	}

	private void addToConstants(Properties p, String key) {
		for (Locale l : locales) {
			String chk = l.getLanguage() + ".";
			String chk2 = l.toString() + ".";
			if (key.startsWith(chk) || key.startsWith(chk2)) {
				Map<String, String> pmap = mapOfConstants.get(l.getLanguage());
				pmap.put(key, p.getProperty(key));
			}
		}
	}
	
	public String get(String key, Object... param) {
		return getByObject(null, key, null, param);
	}
	
	public String getWithDefault(String key, String def, Object... param) {
		return getByObject(null, key, def, param);
	}
	
	public String getByObject(Object object, String key, Object... param) {
		return getByObjectWithDefault(object, key, null, param);
	}
	
	public String getByObjectWithDefault(Object object, String key, String def, Object... param) {
		try {
			StringBuilder sb = new StringBuilder(".")
					.append(object != null ? object.getClass().getName() + "." : "")
					.append(key);
			return formatter(sb.toString(), def, param);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			return key;
		}
	}

	private String formatter(String postfix, String def, Object[] param) {
		String value = selmap.get(selected.toString() + postfix);
		if (value != null) {
			return String.format(value, param);
		}
		value = selmap.get(selected.getLanguage() + postfix);
		if (value != null) {
			return String.format(value, param);
		}
		if (def != null) {
			return def;
		}
		throw new TubeShiftException("key " + postfix + " not found for locale " + selected.toString());
	}

	public Locale getSelected() {
		return selected;
	}
}
