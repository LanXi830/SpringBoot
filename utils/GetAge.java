package com.qf.CJDX_MANAGER.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Version 1.0
 * @Author:cyl
 * @Date:2022/7/18
 * @Content:
 */
public class GetAge
{
    public static int getAge(Date birthDay) throws Exception
    {
        Calendar cal = Calendar.getInstance();

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth)
        {
            if (monthNow == monthBirth)
            {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            }
            else
            {
                age--;
            }
        }
        return age;
    }
}
