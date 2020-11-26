package task;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Employee implements Serializable {

    private String employeeName;
    private String date;
    private String workHours;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDate() {
        return date;
    }

    public Date getDateTyped() {
        try {
            return new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    @Override
    public String toString() {
        return "\n Employee name = " + getEmployeeName() + "; Date = "
                + getDate() + "; Work hours = " + getWorkHours();
    }
}