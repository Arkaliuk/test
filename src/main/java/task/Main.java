package task;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static Set<Date> dates;
    private static Set<String> names;
    private static String[][] workHours;

    public static void main(String[] args) throws Exception {

        List<Employee> employees = new ArrayList<>();

        reader(employees);

        workHours = process(employees);

        writer();
    }

    private static void reader(List<Employee> employees) throws IOException {
        ICsvBeanReader csvBeanReader = new CsvBeanReader(new FileReader("src\\main\\resources\\acme_worksheet.csv"), CsvPreference.STANDARD_PREFERENCE);
        CellProcessor[] procs = getProcessors();
        // indicate how we will map
        String[] mapping = new String[]{"employeeName", "date", "workHours"};

        // we go through the entire csv file to the end
        Employee employee;
        while ((employee = csvBeanReader.read(Employee.class, mapping, procs)) != null) {
            employees.add(employee);
        }
        employees.remove(0);

        csvBeanReader.close();
    }

    private static String[][] process(List<Employee> employees) {
        dates = new TreeSet<>();
        names = new TreeSet<>();
        for (Employee employee : employees) {
            dates.add(employee.getDateTyped());
            names.add(employee.getEmployeeName());
        }
        System.out.println(dates);
        System.out.println(names);

        return putArrayWorkHours(employees);
    }

    private static String[][] putArrayWorkHours(List<Employee> employees) {
        String[][] workHours = new String[names.size()][dates.size()];
        for (String[] workHour : workHours) {
            Arrays.fill(workHour, "0");
        }
        for (Employee employee : employees) {
            int i = 0;
            for (String name : names) {
                if (name.equals(employee.getEmployeeName()))
                    break;
                i++;
            }
            int j = 0;
            for (Date date : dates) {
                if (date.equals(employee.getDateTyped()))
                    break;
                j++;
            }
            workHours[i][j] = employee.getWorkHours();
        }
        return workHours;
    }

    private static void writer() throws IOException {
        String filename = "src\\main\\resources\\output.csv";
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(filename), CsvPreference.STANDARD_PREFERENCE);

        StringBuilder header = new StringBuilder("Employee name/Date");
        for (Date date : dates) {
            header.append(",").append(new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).format(date));
        }
        csvBeanWriter.writeComment(header.toString());
        int i = 0;
        for (String name : names) {
            StringBuilder line = new StringBuilder(name);
            for (int j = 0; j < workHours[i].length; j++) {
                line.append(",").append(workHours[i][j]);
            }
            i++;
            csvBeanWriter.writeComment(line.toString());
        }
        csvBeanWriter.close();
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull(), // Employee name must not be null
                new NotNull(), // Date must not be null
                new NotNull()  // Work hours must not be null
        };
    }
}


