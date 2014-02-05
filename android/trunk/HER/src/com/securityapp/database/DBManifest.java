package com.securityapp.database;

/*import com.makemytrip.calendar.database.BreakOutTable;
import com.makemytrip.calendar.database.DateTable;
import com.makemytrip.calendar.database.ImageTable;*/



public interface DBManifest
{
	
	final String DATABASE_NAME = "mmt_calendar_db";
	final int DATABASE_VERSION = 1;

//Table Names....
	String[] TABLE_NAMES = new String[] 
	{
			/*DateTable.DATE_TABLE,
			ImageTable.IMAGE_TABLE,
			BreakOutTable.BREAKOUT_TABLE*/
	};

	//Table create queries...
    String[] CREATE_QUERIES = new String[]
    {
    		/*DateTable.CREATE_DATE_TABLE,
    		ImageTable.CREATE_IMAGE_TABLE,
    		BreakOutTable.CREATE_BREAKOUT_TABLE*/
    };
}
