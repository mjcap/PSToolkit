package com.capbpm.util.logging;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.logging.hpel.LogRecordContext;
import com.ibm.websphere.logging.hpel.reader.RepositoryLogRecord;
import com.ibm.websphere.logging.hpel.reader.RepositoryReaderImpl;
import com.ibm.websphere.logging.hpel.reader.ServerInstanceLogRecordList;



public class LoggingContextHelper {
	
	
	public static void main(String args[]) throws Exception
	{
		String tmp = new LoggingContextHelper().getLog("");		
		System.out.println(tmp);
	
	}

	public String getLog(String processApp)
	{
		String retval = "";
		LoggingContextHelper instance = LoggingContextHelper.instance();
		RepositoryReaderImpl reader = new RepositoryReaderImpl("com.ibm.bpm.saas.logging.ProcessAppLogger");
		for(ServerInstanceLogRecordList oneServerList: reader.getLogLists()) {
		        oneServerList.getHeader().list(System.out);
		        for (RepositoryLogRecord record: oneServerList) {
		        	retval +=record.toString();
		        }
		}
	
		
		return retval;
		
		
	}
	
	/**
	 * The 'key' part of the 'extension' metadata
	 */
	public static final String PROCESS_APP_EXTENSION_NAME = "bpmProcessApp";
	
	/**
	 * Make this a singleton in context of the classloader.  Might as well
	 * as the cleanest way to invoke from the javascript is through static methods.
	 */
	private static LoggingContextHelper instance = null;
	
	/**
	 * The Log4J logger that will be used for all log records.
	 */
	private Logger bpmLogger = null;
	
	/**
	 * Enforce this as a singleton.
	 */
	private LoggingContextHelper() {
		bpmLogger = Logger.getLogger("com.ibm.bpm.saas.logging.ProcessAppLogger"); 
	}
	
	/**
	 * Retrieve the singleton instance of this LoggingContextHelper
	 * 
	 * @return The singleton instance
	 */
	private static LoggingContextHelper instance() {
		if (instance == null) {
			instance = new LoggingContextHelper();
		}
		return instance;
	}
	
	/**
	 * Set the PROCESS_APP_EXTENSION_NAME extension value on the thread
	 *  
	 * @param processApp The acronym of the process app
	 */
	private void setContext(final String processApp) {
		LogRecordContext.registerExtension(PROCESS_APP_EXTENSION_NAME, new LogRecordContext.Extension() {
			public String getValue() {
				return processApp;
			}
		});
	}

	/**
	 * Clear the PROCESS_APP_EXTENSION_NAME extension value on the thread
	 */
	private void clearContext() {
		LogRecordContext.unregisterExtension(PROCESS_APP_EXTENSION_NAME);
	}
	
	/**
	 * Set the context, log the message, and clear the context
	 * 
	 * @param acronym The acronym of the process app 
	 * @param logMessage The message to log
	 * @param level The logging level
	 */
	private static void log(String acronym, String logMessage, Level level) {
		LoggingContextHelper instance = LoggingContextHelper.instance();
		instance.setContext(acronym);
		instance.bpmLogger.log(level, logMessage);
		instance.clearContext();
	}
	
	/**
	 * Log an error message
	 * 
	 * @param acronym The acronym of the process app
	 * @param logMessage The message to log
	 */
	public static void error(String acronym, String logMessage) {
		log(acronym, logMessage, Level.SEVERE);
	}
	
	/**
	 * Log a debug message
	 * 
	 * @param acronym The acronym of the process app
	 * @param logMessage The message to log
	 */
	public static void debug(String acronym, String logMessage) {
		log(acronym, logMessage, Level.FINEST);
	}
	
	/**
	 * Log a warning message
	 * 
	 * @param acronym The acronym of the process app
	 * @param logMessage The message to log
	 */
	public static void warn(String acronym, String logMessage) {
		log(acronym, logMessage, Level.WARNING);
	}
	
	/**
	 * Log an info message
	 * 
	 * @param acronym The acronym of the process app
	 * @param logMessage The message to log
	 */
	public static void info(String acronym, String logMessage) {
		log(acronym, logMessage, Level.INFO);
	}
}