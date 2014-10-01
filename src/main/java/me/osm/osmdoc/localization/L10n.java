package me.osm.osmdoc.localization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class L10n {
	
	public static final String L10N_PREFIX = "l10n.";
	
	private L10n() {
		
	}
	
	private L10n(Locale locale) {
		rbundle = ResourceBundle.getBundle("localization.strings");
	}

	private ResourceBundle rbundle;
	
	private static final Map<String, L10n> instances = new HashMap<String, L10n>(); 
	
	public static String tr(String key, Locale locale) {
		
		if(locale == null) {
			locale = Locale.getDefault();
		}
		
		if(key.startsWith(L10N_PREFIX)) {
			
			if(instances.get(locale.getDisplayName()) == null) {
				synchronized (instances) {
					if(instances.get(locale.getDisplayName()) == null) {
						instances.put(locale.getDisplayName(), new L10n(locale));
					}
				}
			}
			
			return instances.get(locale.getDisplayName()).rbundle.getString(key);
		}
		else {
			return key;
		}
		
	}
}
