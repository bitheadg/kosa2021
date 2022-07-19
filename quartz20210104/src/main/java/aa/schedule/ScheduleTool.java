package aa.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;

public class ScheduleTool {

	private final Date mDate;
	private final Calendar mCal;
	private final String mSeconds = "0";
	private final String mDaysOfWeek = "?";
	
	private String mMins;
	private String mHours;
	private String mDaysOfMonth;
	private String mMonths;
	private String mYears;
	
	private static Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <V> V fromJson(String json, Class<V> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    public static String generateRandomUUID(String idPrefix) {
        return idPrefix + UUID.randomUUID().toString();
    }
    
	public ScheduleTool(Date pDate) {
		this.mDate = pDate;
		mCal = Calendar.getInstance();
		this.generateCronExpression();
	}
	
	public String generateCronExpression() {
		mCal.setTime(mDate);
		
		String hours = String.valueOf(mCal.get(Calendar.HOUR_OF_DAY));
		this.mHours = hours;
		
		String mins = String.valueOf(mCal.get(Calendar.MINUTE));
		this.mMins = mins;
		
		String days = String.valueOf(mCal.get(Calendar.DAY_OF_MONTH));
		this.mDaysOfMonth = days;
		
		String months = new java.text.SimpleDateFormat("MM").format(mCal.getTime());
		this.mMonths = months;
		
		String years = String.valueOf(mCal.get(Calendar.YEAR));
		this.mYears = years;
		
		//cron; Seconds        Minutes        Hours        Day-of-Month        Month        Day-of-Week        Year (optional field)
		//A question mark ( ? ) is allowed in the day-of-month and day-of-week fields. e.g.) "0 0/5 * * * ?"
		return getSeconds()+" "+getMins()+" "+getHours()+" "+getDaysOfMonth()+" "+getMonths()+" "+getDaysOfWeek()+" "+getYears();
	}
	
	
	public Date getDate() {
	return mDate;
	}
	
	public String getSeconds() {
	return mSeconds;
	}
	
	public String getMins() {
	return mMins;
	}
	
	public String getDaysOfWeek() {
	return mDaysOfWeek;
	}
	
	public String getHours() {
	return mHours;
	}
	
	public String getDaysOfMonth() {
	return mDaysOfMonth;
	}
	
	public String getMonths() {
	return mMonths;
	}
	
	public String getYears() {
	return mYears;
	}
	
}