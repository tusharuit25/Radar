package mr_immortalz.com.modelqq.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by comp 2 on 9/3/2016.
 */
public class DateUtils {
    public static String UTCDateToLocalTime(String UtcDateFormat) {
        try
        {

            DateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date  = utcDateFormat.parse(UtcDateFormat);
            SimpleDateFormat istDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            TimeZone tz = TimeZone.getDefault();
            istDateFormat.setTimeZone(TimeZone.getTimeZone(tz.getID()));
            String convertedDate = istDateFormat.format(date);
            return  convertedDate;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static String UTCDateToLocalTimeWithoutT(String UtcDateFormat) {
        try
        {

            DateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date  = utcDateFormat.parse(UtcDateFormat);
            SimpleDateFormat istDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            TimeZone tz = TimeZone.getDefault();
            istDateFormat.setTimeZone(TimeZone.getTimeZone(tz.getID()));
            String convertedDate = istDateFormat.format(date);


            return  convertedDate;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static String LocalTimeToUTCDate(String UtcDateFormat) {
        try
        {
            TimeZone tz = TimeZone.getDefault();
            String TimeZoneId=tz.getID();
           // String _strDate=  convertLocalTimeToUTC(TimeZoneId,UtcDateFormat);





            DateFormat istDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault());

            istDateFormat.setTimeZone(TimeZone.getDefault());
            Date date  = istDateFormat.parse(UtcDateFormat);
            SimpleDateFormat utcDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String convertedDate = utcDateFormat.format(date);
            return  convertedDate;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static String convertLocalTimeToUTC(String saleTimeZone, String p_localDateTime) throws Exception {

        String dateFormateInUTC="";
        Date localDate = null;
        String localTimeZone ="";
        SimpleDateFormat formatter;
        SimpleDateFormat parser;
        localTimeZone = saleTimeZone;

        //create a new Date object using the timezone of the specified city
        parser = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        parser.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        localDate = parser.parse(p_localDateTime);
        formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        System.out.println("convertLocalTimeToUTC: "+saleTimeZone+": "+" The Date in the local time zone " +   formatter.format(localDate));

        //Convert the date from the local timezone to UTC timezone
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormateInUTC = formatter.format(localDate);
        System.out.println("convertLocalTimeToUTC: "+saleTimeZone+": "+" The Date in the UTC time zone " +  dateFormateInUTC);

        return dateFormateInUTC;
    }


}
