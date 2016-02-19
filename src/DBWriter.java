


public class DBWriter {
	public static String dropSQL="";
	public static String insertSQL="";
	public static String createSQL="";
	public static String updateSQL="";
	public static void main(String[] args) {

		//checklist meta join table
	/*	String[] vals0  ={"checkListId:INTEGER", "name","VALUE"};
		String tmp = initTable("CheckList_Meta",vals0,false);	
	
		
		//checklist table
		String[] vals= {"name", "orderNumber:INTEGER","percentDone:DECIMAL", "timeRange","numberRequired:INTEGER","department","role","scheduledEnd","scheduledStart","category","isRandom"};	
		tmp = initTable("checkList",vals);	
		
		//task table
		String[] vals2  ={"name", "orderNumber:INTEGER","isNum","isRequired"};
		tmp = initTable("task",vals2);	
		
			
		//subtask table
		String[] vals3  ={"name", "orderNumber:INTEGER","criticalLimit"};
		tmp = initTable("subTask",vals3);	

		//taskaction table
		String[] vals4  ={"name","orderNumber:INTEGER","criticalLimit","correctiveAction","isSimple","opOne","argOne","opTwo","argTwo","unit", "isComplete","dueTime","trueTaskId:INTEGER","falseTaskId:INTEGER"};
		tmp = initTable("taskAction",vals4);	
		
		//bulletin table
		String[] vals6= {"BULLETINID:INTEGER","NAME", "VALUE"};	
		tmp = initTable("BULLETIN",vals6,false);	

		
		//bulletin table
		String[] vals7= {"SURVEYID:INTEGER","QUESTION", "OP1","OP2", "OP3"};	
		tmp = initTable("SURVEY",vals7,false);	
		
////////
		//checkList_TASK table
		String[] vals25  ={"checkListId:INTEGER", "taskId:INTEGER"};
		tmp = initTable("checkList_task",vals25,false);	
////////
		String[] vals35  ={"task:INTEGER","subTaskId:INTEGER"};
		tmp = initTable("task_subTask",vals35,false);				
////////
		//task_taskaction table
		String[] vals5  ={"subTaskId:INTEGER", "taskActionId:INTEGER"};
		tmp = initTable("subTask_taskAction",vals5,false);		
	*/


		//checklist meta join table
		String[] vals8  ={"task", "temp","dept", "checklist", "check","subcheck","timestamp", "userName"};
	 String tmp = initTable("TaskReport",vals8,false);	
		
		//System.out.println(dropSQL);
		System.out.println(createSQL);
		System.out.println(insertSQL);
		//System.out.println(updateSQL);
	}
	public static void mainBack(String[] args) {
		// TODO Auto-generated method stub
		
		//checklist table
		String[] vals= {"NAME", "ORDERNUMBER:INTEGER","PERCENTDONE:DECIMAL", "TIMERANGE","NUMBERREQUIRED:INTEGER","DEPARTMENT","ROLE","SCHEDULEEND","SCHEDULESTART","CATEGORY","ISRANDOM"};	
		String tmp = initTable("CHECKLIST",vals);	
		System.out.println(tmp);

		//task table
		String[] vals2  ={"NAME", "ORDERNUMBER:INTEGER","PARENTID:INTEGER", "DEPARTMENT",
				"GROUPING", "ISAUTOSPAWN","CRITICALLIMIT","CORRECTIVEACTION",
				"ISNUM","OPONE","ARGONE","OPTWO","ARGTWO","UNIT", "ISCOMPLETE","DUETIME","ISREQUIRED"};
		tmp = initTable("TASK",vals2);	
		System.out.println(tmp);		

		
		//checklist meta join table
		String[] vals3  ={"CHECKLISTID:INTEGER", "NAME","VALUE"};
		tmp = initTable("CHECKLIST_META",vals3,false);	
		System.out.println(tmp);			
		
		//location
		String[] vals4  ={ "FORMAT","BU","DIV","REGION","MARKET","STORE"};
		tmp = initTable("LOCATION",vals4);	
		System.out.println(tmp);

		//checklist location join table
		String[] vals8  ={"CHECKLISTID:INTEGER", "LOCATIONID:INTEGER"};
		tmp = initTable("CHECKLIST_LOCATION",vals8,false);	
		System.out.println(tmp);

		//checklist task join table
		String[] vals5  ={"CHECKLISTID:INTEGER", "TASKID:INTEGER", "ORDERNUMBER:INTEGER","ROLE","SCHEDULE"};
		tmp = initTable("CHECKLIST_TASK",vals5,false);	
		System.out.println(tmp);			

		//bulletin table
		String[] vals6= {"BULLETINID:INTEGER","NAME", "VALUE"};	
		tmp = initTable("BULLETIN",vals6,false);	
		System.out.println(tmp);
		
		//bulletin table
		String[] vals7= {"SURVEYID:INTEGER","QUESTION", "OP1","OP2", "OP3"};	
		tmp = initTable("SURVEY",vals7,false);	
		System.out.println(tmp);

		//checklist meta join table
		String[] vals9  ={"PARENTTASKID:INTEGER", "CHILDTASKID:INTEGER","VALUE"};
		tmp = initTable("TASK_CHILD",vals9,false);	
		System.out.println(tmp);

		//taskaction table
		String[] vals10  ={"NAME","ORDERNUMBER:INTEGER","CRITICALLIMIT","CORRECTIVEACTION","ISSIMPLE","OPONE","ARGONE","OPTWO","ARGTWO","UNIT", "ISCOMPLETE","DUETIME","TRUETASKID:INTEGER","FALSETASKID:INTEGER"};
		tmp = initTable("TASKACTION",vals10);	
		System.out.println(tmp);
		
		//task_taskaction table
		String[] vals11  ={"TASKID:INTEGER", "TASKACTIONID:INTEGER","VALUE"};
		tmp = initTable("TASK_TASKACTION",vals11,false);	
		System.out.println(tmp);
		
		System.out.println(tmp);
		
		System.out.println(dropSQL);
		System.out.println(createSQL);
		System.out.println(insertSQL);
		
	}

	public static String initTable(String tableName,  String[] vals)
	{
		String retval = initTable(tableName,vals,true);	
		return retval;
		
	}
	
	public static String initTable(String tableName,  String[] vals, boolean hasId)
	{
		String retval = "";//dropTable(tableName);
		dropSQL += dropTable(tableName);
		if (hasId == true)
		{
			//retval +=dropSeq(tableName);
			//retval += createSeq(tableName);
		}
		createSQL += createTable(tableName,vals,hasId);
		
		insertSQL += createInsertIntoTable(tableName,vals,hasId);
		
		updateSQL+= createUpdate(tableName,vals);
		
		return retval;
		
	}

	public static String dropTable(String tableName)
	{
		String retval = "DROP TABLE "+ tableName.toUpperCase()  +";\n";
		return retval;
	}
	
	public static String dropSeq(String tableName)
	{
		String  retval = "DROP SEQUENCE "+ tableName  +"_SEQ;\n";
		return retval;
	}
	public static String createSeq(String tableName)
	{
		String  retval ="CREATE SEQUENCE "+ tableName  +"_SEQ START WITH 1 CACHE 100;\n";
		return retval;
	}
	
	
	public static String createTable(String tableName,  String[] vals)
	{
		return createTable( tableName,   vals,  true);
	}
	public static String createTable(String tableName,  String[] vals, boolean hasId)
	{

		
		String retval = "CREATE TABLE "+ tableName.toUpperCase()  +"\n";
		String id  = (tableName +"ID").toUpperCase();
		String tabs = "\t\t";
		if (id.length()>12)
		{
			tabs="\t";
				
		}
		else if (id.length()<=6)
		{
			tabs="\t\t\t";
		}		
		String idLine = "";
		String body="";

		if (hasId)
	//		idLine ="\t"  + id+tabs+"INTEGER NOT NULL " + ",\n";//GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),\n";
		idLine ="\t"  + id+tabs+"INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),\n";
		
		for (int i=0; i<vals.length;i++)
		{
			String name = vals[i].split(":")[0];
			tabs="\t\t";
			if (name.length()>12)
			{
				tabs="\t";
					
			}
			else if (name.length()<=6)
			{
				tabs="\t\t\t";
			}
			
			String type = "VARCHAR(512)";
			

			
			if (vals[i].split(":").length>1)
			{
				
				type = vals[i].split(":")[1];
			}
			if (i+1 == vals.length) 
			{
				body+="\t"+ name.toUpperCase() +tabs+type+"\n";
			}
			else 
			{
				body+="\t"+ name.toUpperCase() +tabs+type+",\n";
			}
				
		}
		
		retval = retval +"(\n"+ idLine+ body +");\n";
		
		return retval;
	}
	public static String createInsertIntoTable(String tableName,  String[] vals, boolean hasId)
	{
		return createInsert2( tableName,   vals,true);//,  hasId, true);
	}
	public static String createUpdate(String tableName, String[] vals)
	{
		String retval="";
	
		
		String baseSQL="\n//update "+tableName.toUpperCase()+"\ntw.local.sqlStatements = new tw.object.listOf.SQLStatement();\n";
		baseSQL+="for (var i=0;i< tw.local.checkList.listLength;i++)\n";
		baseSQL+="{\n";
		baseSQL+="\ttw.local.sqlStatements[i] = new tw.object.SQLStatement();\n";
		    
		baseSQL+="\ttw.local.sqlStatements[i].sql=\"UPDATE @TABLE \" ;\n";
		baseSQL+="\ttw.local.sqlStatements[i].sql+=\"SET \" ;\n";
		baseSQL+="@VALUES\n";
		baseSQL+="\ttw.local.sqlStatements[i].sql+=\" WHERE\" ;\n";
		baseSQL+="\ttw.local.sqlStatements[i].sql+=\"\t@TABLEID=\"+@ID ;\n}";

		
		baseSQL = baseSQL.replaceAll("@TABLE",tableName.toUpperCase());
		
		String values = "";
		
		for (int i=0; i<vals.length;i++)
		{
			String[] tokems=vals[i].split(":");
			String key ="";
			String v = "";
			
			String quoteStart="\"'\" + ";		
			String quoteEnd="+ \"',\"";
			
			
			if (tokems.length ==2)
			{
				quoteStart="";	
				quoteEnd="+\",\"";
			}
			
			//don't put a comma on the last one
			if (i+1== vals.length)
			{
				quoteEnd= quoteEnd.replaceAll("\\+ \"',\"", "\\+ \"'\"");			
				quoteEnd= quoteEnd.replaceAll(",", "");
			}
			
			key = tokems[0].toUpperCase();
			v = "tw.local."+tableName+"[i]."+tokems[0] +"";	
			
			values +="\ttw.local.sqlStatements[i].sql+=\t\""+ key +"\"+\"=\"+ "+ quoteStart + v + quoteEnd+";\n";
		}
		
		retval = baseSQL.replaceAll("@VALUES", values);
		String idString = "tw.local."+tableName+"[i]." + tableName+"Id" +"";	
		retval = retval.replaceAll("@ID", idString);
		
		updateSQL +=retval;
		return retval;
	}
	public static String createInsert(String tableName, String[] vals)
	{
		String retval="";	
		String valz="";
		String baseSQL="\n//INSERT INTO "+tableName.toUpperCase()+"\ntw.local.sqlStatements = new tw.object.listOf.SQLStatement();\n";
		baseSQL+="for (var i=0;i< tw.local.checkList.listLength;i++)\n";
		baseSQL+="{\n";
		baseSQL+="\ttw.local.sqlStatements[i] = new tw.object.SQLStatement();\n";
		    
		baseSQL+="\ttw.local.sqlStatements[i].sql=\"INSERT INTO @TABLE \" ;\n";
		baseSQL+="@KEYS";
		baseSQL+="@VALUES";
		baseSQL+="\ttw.local.sqlStatements[i].sql+=\"\\n}\";\n}";
	
		baseSQL = baseSQL.replaceAll("@TABLE",tableName.toUpperCase());
		String keyz="";		
		String values = "";
		
		for (int i=0; i<vals.length;i++)
		{
			String[] tokems=vals[i].split(":");
			String key ="";

			String v = "";
			String commma=",";
			
			String quoteStart="\"'\" + ";		
			String quoteEnd="+ \"',\"";
			
			
			if (tokems.length ==2)
			{
				quoteStart="";	
				quoteEnd="+\",\"";
			}
			
			//don't put a comma on the last one
			if (i+1== vals.length)
			{
				quoteEnd= quoteEnd.replaceAll("\\+ \"',\"", "\\+ \"'\"");			
				quoteEnd= quoteEnd.replaceAll(",", "");
				commma="";
			}
			
			key = tokems[0].toUpperCase();
			v = "tw.local."+tableName+"[i]."+tokems[0] +"";	
			
			keyz +=	key+commma;
			
			values +="\ttw.local.sqlStatements[i].sql+=\t" + quoteStart + v + quoteEnd+";\n";

		}
		keyz ="\ttw.local.sqlStatements[i].sql+=\"(" + keyz+")\";\n";
		retval = baseSQL.replaceAll("@VALUES", values);
		retval = retval.replaceAll("@KEYS", keyz);
		String idString = "tw.local."+tableName+"[i]." + tableName+"Id" +"";	
		retval = retval.replaceAll("@ID", idString);
		
		//updateSQL +=retval;
		return retval;
	}
	public static String createInsertIntoTable(String tableName,  String[] vals, boolean hasId, boolean isCompact)
	{
		String retval = "INSERT INTO  "+ tableName.toUpperCase()  +" \";\n";
		String id  = tableName.toUpperCase() +"ID";
		String tabs = "\t\t";
		
		String lineBreak="\n";
		//lead tab for list of attributes and values 
		String preTab ="\t";

		if (isCompact)
		{
			lineBreak="";
			preTab="";
		}
		if (id.length()>12)
		{
			tabs="\t";
				
		}
		else if (id.length()<=6)
		{
			tabs="\t\t\t";
		}		
		String idLine = "";
		String body="";
		String values="";
	
		for (int i=0; i<vals.length;i++)
		{
			String[] tokens =  vals[i].split(":");
			String name =tokens[0];
			String currentValue  ="\"+\"'\"+tw.local."+ tableName + "."+ tokens[0].toLowerCase()+"+\"'\"+\"";// "'"+name+"'";
			if (tokens.length>1)
			{
				if (tokens[1].equalsIgnoreCase("INTEGER") || tokens[1].equalsIgnoreCase("SMALLINT"))
				{
					currentValue = "\"+tw.local."+ tableName + "."+ tokens[0].toLowerCase()+"+\"";// "-1";
				}
			}
	
			tabs="\t\t";
			if (name.length()>12)
			{
				tabs="\t";
					
			}
			else if (name.length()<=6)
			{
				tabs="\t\t\t";
			}
			
			if (isCompact)
			{
				tabs="";
			}
			
			if (i+1 == vals.length) 
			{
				body+=preTab+ name+tabs+lineBreak;
				values +=preTab+ currentValue  +"\n\t"+lineBreak;
			}
			else 
			{
				body+=preTab+ name+tabs+","+lineBreak;
				values +=preTab+currentValue +",\n\t"+lineBreak;
			}
				
		}
		
		retval = "tw.local.sql=\""+retval +"tw.local.sql+=\"("+lineBreak+ idLine+ body +") \";\ntw.local.sql +=\"" + "VALUES("+values+"\n);\"\n\n";
		
		return retval;
	}

	public static void main2(String[] args) {
		// TODO Auto-generated method stub
		
		//checklist table
		String[] vals= {"NAME", "ORDERNUMBER:INTEGER","PERCENTDONE:DECIMAL", "TIMERANGE","NUMBERREQUIRED:INTEGER","DEPARTMENT"};	
		String tmp = initTable("CHECKLIST",vals);	
		System.out.println(tmp);

		//task table
		String[] vals2  ={"NAME", "ORDERNUMBER:INTEGER","PARENTID:INTEGER", "DEPARTMENT",
				"GROUPING", "ISAUTOSPAWN","CRITICALLIMIT","CORRECTIVEACTION",
				"ISNUM","OPONE","ARGONE","OPTWO","ARGTWO","UNIT", "ISCOMPLETE","DUETIME","ISREQUIRED"};
		tmp = initTable("TASK",vals2);	
		System.out.println(tmp);		

		
		//checklist meta join table
		String[] vals3  ={"CHECKLISTID:INTEGER", "NAME","VALUE"};
		tmp = initTable("CHECKLIST_META",vals3,false);	
		System.out.println(tmp);			
		
		//location
		String[] vals4  ={ "FORMAT","BU","DIV","REGION","MARKET","STORE"};
		tmp = initTable("LOCATION",vals4);	
		System.out.println(tmp);

		//checklist location join table
		String[] vals8  ={"CHECKLISTID:INTEGER", "LOCATIONID:INTEGER"};
		tmp = initTable("CHECKLIST_LOCATION",vals8,false);	
		System.out.println(tmp);

		//checklist task join table
		String[] vals5  ={"CHECKLISTID:INTEGER", "TASKID:INTEGER", "ORDERNUMBER:INTEGER","ROLE","SCHEDULE"};
		tmp = initTable("CHECKLIST_TASK",vals5,false);	
		System.out.println(tmp);			

		//bulletin table
		String[] vals6= {"BULLETINID:INTEGER","NAME", "VALUE"};	
		tmp = initTable("BULLETIN",vals6,false);	
		System.out.println(tmp);
		
		//bulletin table
		String[] vals7= {"SURVEYID:INTEGER","QUESTION", "OP1","OP2", "OP3"};	
		tmp = initTable("SURVEY",vals7,false);	
		System.out.println(tmp);

		//checklist meta join table
		String[] vals9  ={"PARENTTASKID:INTEGER", "CHILDTASKID:INTEGER","VALUE"};
		tmp = initTable("TASK_CHILD",vals9,false);	
		System.out.println(tmp);

		//taskaction table
		String[] vals10  ={"NAME","ORDERNUMBER:INTEGER","CRITICALLIMIT","CORRECTIVEACTION","ISSIMPLE","OPONE","ARGONE","OPTWO","ARGTWO","UNIT", "ISCOMPLETE","DUETIME","TRUETASKID:INTEGER","FALSETASKID:INTEGER"};
		tmp = initTable("TASKACTION",vals10);	
		System.out.println(tmp);
		
		//task_taskaction table
		String[] vals11  ={"TASKID:INTEGER", "TASKACTIONID:INTEGER","VALUE"};
		tmp = initTable("TASK_TASKACTION",vals11,false);	
		System.out.println(tmp);
		
		System.out.println(tmp);
		
		System.out.println(dropSQL);
		System.out.println(createSQL);
		System.out.println(insertSQL);
		
	}

	public static String createInsert2(String tableName, String[] vals, boolean isJoin)
	{
		String retval="";
		
		String fields="\ttw.local.sqlStatements[i].sql+=" + "\"("+vals[0].split(":")[0].toUpperCase()+","+ vals[1].split(":")[0].toUpperCase()+"\")\";\n";

	
		
		String baseSQL="\n//INSERT "+tableName.toUpperCase()+"\ntw.local.sqlStatements = new tw.object.listOf.SQLStatement();\n";
		baseSQL+="for (var i=0;i< tw.local.checkList.listLength;i++)\n";
		baseSQL+="{\n";
		baseSQL+="\ttw.local.sqlStatements[i] = new tw.object.SQLStatement();\n";
		    
		baseSQL+="\ttw.local.sqlStatements[i].sql=\"INSERT INTO @TABLE \" ;\n";
		baseSQL +=fields;
		
		baseSQL+="@VALUES\n";
		//baseSQL+="\ttw.local.sqlStatements[i].sql+=\" WHERE\" ;\n";
		baseSQL+="\n}";

		
		baseSQL = baseSQL.replaceAll("@TABLE",tableName.toUpperCase());
		
		String values = "";
		
		for (int i=0; i<vals.length;i++)
		{
			String[] tokems=vals[i].split(":");
			String key ="";
			String v = "";
			
			String quoteStart="\"'\" + ";		
			String quoteEnd="+ \"',\"";
			
			
			if (tokems.length ==2)
			{
				quoteStart="";	
				quoteEnd="+\",\"";
			}
			
			//don't put a comma on the last one
			if (i+1== vals.length)
			{
				quoteEnd= quoteEnd.replaceAll("\\+ \"',\"", "\\+ \"'\"");			
				quoteEnd= quoteEnd.replaceAll(",", "");
			}
			
			key = tokems[0].toUpperCase();
			v = "tw.local."+tableName+"[i]."+tokems[0] +"";	
			
			if (i==0)
			{
				v = "\" VALUES(\"+" + v;
			}
			if (i+1== vals.length)
			{
				v = v + ")\";";
			}
			
			values +="\ttw.local.sqlStatements[i].sql+=\t" + quoteStart + v + quoteEnd+";\n";
		}
		
		retval = baseSQL.replaceAll("@VALUES", values);
		String idString = "tw.local."+tableName+"[i]." + tableName+"Id" +"";	
		retval = retval.replaceAll("@ID", idString);
		
		updateSQL +=retval;
		return retval;
	}
	
	public static String stripHTMLTags(String in, boolean isFormated)
	{
		String retval="";
		retval = in.replaceAll("&amp;", "");
		retval = retval.replaceAll("&quot;", "\"");
		retval = retval.replaceAll("&nbsp;", "");
		retval = retval.replaceAll("&lt;", "");
		retval = retval.replaceAll("&gt;", "");


		retval = retval.replaceAll("quot;", "\"");
		retval = retval.replaceAll("nbsp;", "");
		retval = retval.replaceAll("lt;", "");
		retval = retval.replaceAll("gt;", "");
		
		return retval;
	}
}
