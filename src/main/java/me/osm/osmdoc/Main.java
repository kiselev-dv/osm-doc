package me.osm.osmdoc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.osm.osmdoc.commands.ExpStrings;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;


public class Main {
	
	private static final String COMMAND = "command";
	
	/**
	 * Command line command description
	 * */
	public static interface CommandDescription {
		
		/**
		 * Name of command, will be used as executable.jar long-coomand-name
		 * */
		public String longName();
		
		/**
		 * Command description
		 * */
		public String help(); 
	}

	/**
	 * Supported commands
	 * */
	private enum Command implements CommandDescription {
		
		EXP_STRINGS {
			@Override
			public String longName() {return name().toLowerCase();}
			@Override
			public String help() {return "Generate csv file with features, "
					+ "tags, etc. names and translations for them";}
		}

	}

	private static Subparser generateTranslations;;
	
	public static void main(String[] args) {
		
		ArgumentParser parser = getArgumentsParser();
		
		try {
			Namespace namespace = parser.parseArgs(args);
			
			String catalogPath = namespace.getString("catalog");
			
			if(namespace.get(COMMAND).equals(Command.EXP_STRINGS)) {
				new ExpStrings(catalogPath).run();
			}
			
		}
		catch (ArgumentParserException e) {
			parser.handleError(e);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ArgumentParser getArgumentsParser() {
		ArgumentParser parser = ArgumentParsers.newArgumentParser("gazetter")
                .defaultHelp(true)
                .description("Create alphabetical index of osm file features");

		parser.version("0.1");
		
		parser.addArgument("--catalog").required(true)
			.help("Path to doc file or folder with catalog features.")
			.setDefault("catalog");
        
        Subparsers subparsers = parser.addSubparsers();
        
        //GENERATE_TRANSLATIONS
        {
        	Command command = Command.EXP_STRINGS;
			generateTranslations = subparsers.addParser(command.longName())
        			.setDefault(COMMAND, command)
					.help(command.help());
        }
        
        return parser;
	}
	
	/**
	 * Returns string list or empty list for null 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<String> list( List list) {
		if(list == null) {
			return Collections.emptyList();
		}
		return list;
	}
}
