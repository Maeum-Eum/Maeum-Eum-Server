package com.five.Maeum_Eum.common;
import java.util.List;

public class WorkCalculator {

    public static int calculateSalary(int weeklyWorkTime, int wage){
        /*
            근무시간이 15시간 이상이라면 주휴수당 적용 :
            (근무시간/40) * 8 * wage 적용하여 주휴수당 추가.
            월급 = 주급 * 4.35
         */
        double weeklySalary = wage * weeklyWorkTime;
        System.out.println("주급 : "+ weeklySalary);
        if(weeklyWorkTime >= 15) weeklySalary += (weeklyWorkTime/40.0) * 8 * wage;
        System.out.println("주휴수당 : "+ (weeklyWorkTime/40.0) * 8 * wage);
        System.out.println("월급 : "+ (int)(4.35*weeklySalary));
        return (int)(4.35 * weeklySalary);
    }

    public static int getWorkDayTime(List<String> workTimeSlot){

        int hours = 0;

        for (String workTime : workTimeSlot) {
            switch (workTime) {
                case "MORNING", "EVENING" -> hours += 3;
                case "AFTERNOON" -> hours += 6;
            }
        }

        System.out.println("하루 근무시간 : " + hours);
        return hours;
    }
}