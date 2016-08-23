package cn.pinming.reportforms.service;

import cn.pinming.common.result.Result;
import cn.pinming.reportforms.entity.DateInterval;
import cn.pinming.reportforms.entity.WorkDate;
import cn.pinming.reportforms.mapping.WorkDateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangyu on 2016/7/13.
 */
@Service
public class WorkDateServiceImpl implements WorkDateService {
    @Autowired
    private WorkDateMapper workDateMapper;

    private boolean checkHoliday(Calendar src, List<Calendar> holidayList) {
        boolean result = false;
        //先检查是否是周六周日(有些国家是周五周六)
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        //这里的检查逻辑需要修改
        if (holidayList != null)
            for (Calendar c : holidayList) {
                if (src.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                        && src.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                    result = true;
                }
            }
        return result;
    }

    /**
     * @param year
     * @param reversDay 周末变工作日，非周末变工作日，也就是调休列表,格式：
     * @param reversDay 周末变工作日，非周末变工作日，也就是调休列表,格式：yyyyMMdd
     */
    @Override
    public void generateWorkDate(int year, List<Calendar> reversDay) {
        workDateMapper.deleteByYear(year);
        Calendar day = Calendar.getInstance();
        day.set(Calendar.YEAR, year);
        day.set(Calendar.DAY_OF_YEAR,1);
        for (int i = 0; i < 366; i++) {
            if(day.get(Calendar.YEAR)>year){
               break;
            }
            //把源日期加一天
            boolean holidayFlag = checkHoliday(day, reversDay);
            WorkDate WorkDate = createWorkDate(day, holidayFlag);
            workDateMapper.insert(WorkDate);

            day.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private WorkDate createWorkDate(Calendar day, boolean holidayFlag) {
        WorkDate WorkDate = new WorkDate();
        WorkDate.setDayofweek(day.get(Calendar.DAY_OF_WEEK));
        WorkDate.setId(caledar2Day(day));
        WorkDate.setMonth(day.get(Calendar.MONTH)+1);
        WorkDate.setQuarter(getQuarterByMonth(day.get(Calendar.MONTH)+1));
        WorkDate.setWeek(day.get(Calendar.WEEK_OF_YEAR));
        if (holidayFlag)
            WorkDate.setWorkday(0);
        else
            WorkDate.setWorkday(1);
        WorkDate.setYear(day.get(Calendar.YEAR));
        return WorkDate;
    }

    private int getQuarterByMonth(int month) {
        if (month <= 3)
            return 1;
        if (month <= 6)
            return 2;
        if (month <= 9)
            return 3;
        return 4;

    }


    /**
     * 获取年度区间
     * @param year
     * @param inteval 0全能，1上半年，2下半年
     * @return
     */
    @Override
    public DateInterval getDateIntervalByYear(int year, int inteval){
        Calendar day = Calendar.getInstance();
        day.set(Calendar.YEAR, year);
        long startDay,endDay;
        if(inteval!=2) {
            day.set(Calendar.DAY_OF_YEAR, 1);
            startDay = caledar2Day(day);
        }else{
            //七月一日
            day.set(Calendar.MONTH, 6);
            day.set(Calendar.DATE, 1);
            startDay = caledar2Day(day);
        }
        if(inteval!=1){
            //12月31日
            day.set(Calendar.MONTH, 11);
            day.set(Calendar.DATE, 31);
            endDay= caledar2Day(day);;
        }else{
            //六月30日
            day.set(Calendar.MONTH, 5);
            day.set(Calendar.DATE, 30);
            endDay=caledar2Day(day);
        }
        DateInterval dateInterval = new DateInterval(startDay,endDay);

        return dateInterval;
    }


    private long caledar2Day(Calendar day) {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return Long.parseLong(format1.format(day.getTime()));
    }

    /**
     * 获取季度区间
     * @param year
     * @param quarter 季度，1,2,3,4
     * @return
     */
    @Override
    public DateInterval getDateIntervalByQuarter(int year, int quarter){
        long startDay,endDay;
        Calendar c = Calendar.getInstance();
       //根据季度，算第一个月的第一天，然后往后推三个月，再剪一天就好;
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,(quarter-1)*3);
        c.set(Calendar.DATE, 1);
        startDay = caledar2Day(c);
        //往后推4个月
        c.add(Calendar.MONTH, 3);
        //设置为抢一个月的最后一天
        c.add(Calendar.DATE, -1);
        endDay = caledar2Day(c);
        DateInterval dateInterval = new DateInterval(startDay,endDay);
        return dateInterval;
    }
    /**
     * 获取月度区间
     * @param year
     * @param month 1,2,3,4,5,6,7,8,9,10,11,12
     * @return
     */
    @Override
    public DateInterval getDateIntervalByMonth(int year, int month){
        long startDay,endDay;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month-1);
        //当月的第一天
        c.set(Calendar.DATE, 1);
        startDay = caledar2Day(c);
        //往后推一个月
        c.add(Calendar.MONTH, 1);
        //设置为抢一个月的最后一天
        c.add(Calendar.DATE, -1);
        endDay = caledar2Day(c);
        DateInterval dateInterval = new DateInterval(startDay, endDay);

        return dateInterval;
    }

    /**
     * 获取环比区间
     * @return
     */
    @Override
    public DateInterval getChainDateInterval(DateInterval dateInterval){
        long startDay,endDay;
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            Date startdate = format1.parse(String.valueOf(dateInterval.getStartDate()));
            c.setTime(startdate);
            c.set(Calendar.DATE, c.get(Calendar.DAY_OF_MONTH)-1);
            endDay = caledar2Day(c);
            Date enddate = format1.parse(String.valueOf(dateInterval.getEndDate()));
            long gap = (enddate.getTime()-startdate.getTime())/(1000*3600*24);

            c.set(Calendar.DATE, c.get(Calendar.DAY_OF_MONTH)-(int) gap+1);
            startDay = caledar2Day(c);
            return new DateInterval(startDay, endDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        DateInterval interval = new DateInterval(20160101L,20161231L);
        WorkDateServiceImpl service = new WorkDateServiceImpl();
        System.out.println(service.getChainDateInterval(interval));
        System.out.println(service.getYearDateInterval(interval));
        System.out.println(service.getDateIntervalByYear(2016,0));
        System.out.println(service.getDateIntervalByYear(2016,1));
        System.out.println(service.getDateIntervalByYear(2016,2));
        System.out.println("J="+service.getDateIntervalByQuarter(2016,1));
        System.out.println("J="+service.getDateIntervalByQuarter(2016,2));
        System.out.println("J="+service.getDateIntervalByQuarter(2016,3));
        System.out.println("J="+service.getDateIntervalByQuarter(2016,4));
        System.out.println(service.getDateIntervalByMonth(2016,1));
        System.out.println(service.getDateIntervalByMonth(2016,2));
        System.out.println(service.getDateIntervalByMonth(2015,2));
        System.out.println("================================================");

        System.out.println("getChainDateInterval=="+service.getChainDateInterval(interval));

        System.out.println("3824="+WorkDateTools.formateWorkDays(3824));
        System.out.println("60="+WorkDateTools.formateWorkDays(60));
        System.out.println("61="+WorkDateTools.formateWorkDays(61));
        System.out.println("20="+WorkDateTools.formateWorkDays(20));
        System.out.println("0="+WorkDateTools.formateWorkDays(0));
    }

    /**
     * 获取同比区间
     * @param dateInterval
     * @return
     */
    @Override
    public DateInterval getYearDateInterval(DateInterval dateInterval){
        long startDay = dateInterval.getStartDate()-10000;
        long endDay = dateInterval.getEndDate()-10000;
        return new DateInterval(startDay, endDay);
    }

    /**
     * 查询某两天之间的工作日天数
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public int getWorkMinutes(Date startDate, Date endDate){
        //24小时制度，中间的时差要处理一下的
        //int notWordDays = workDateMapper.countNotWorkDays(generateTimeId(startDate),generateTimeId(endDate));
        //第一天18点前的时间
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal2.setTime(endDate);
        if(WorkDateTools.isSameDate(cal1,cal2)){
            long longChar=Math.abs((endDate.getTime()-startDate.getTime())/(1000*60));
            return (int) longChar;
        }
        cal1.set(Calendar.HOUR_OF_DAY, WorkDateTools.CLOSE_WORK_TIME);
        cal1.set(Calendar.MINUTE,0);
        int startDayMinutes = (int) (cal1.getTime().getTime()-startDate.getTime())/(1000*60);
        if(startDayMinutes<0) startDayMinutes = 0;
        //半截日期8点后的时间
        cal2.set(Calendar.HOUR_OF_DAY, WorkDateTools.OPEN_WORK_TIME);
        cal2.set(Calendar.MINUTE,0);
        int endDayMinutes = (int) (endDate.getTime() - cal2.getTime().getTime())/(1000*60);
        if(endDayMinutes<0) endDayMinutes = 0 ;

        int workDays = workDateMapper.countWorkingDays(generateTimeId(startDate),generateTimeId(endDate));

        return startDayMinutes+endDayMinutes+workDays*WorkDateTools.DAY_WORK_TIME*60;
    }


    @Override
    public int getIntervalMinutes(Date startDate, Date endDate){
        long longChar=Math.abs((endDate.getTime()-startDate.getTime())/(1000*60));
        //24小时制度，中间的时差要处理一下的
        int notWordDays = workDateMapper.countNotWorkDays(generateTimeId(startDate),generateTimeId(endDate));
        return (int)longChar-notWordDays*24*60;
    }

    @Override
    public long generateTimeId(Date date) {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return Long.parseLong(format1.format(date));
    }

    @Override
    public Map<Long,WorkDate> getCurrentMonthDateByCurrentDate(Integer year, Integer month) {
        Map<Long,WorkDate> map = new HashMap<Long,WorkDate>();
        List<WorkDate> list = workDateMapper.getWorkDateByYearAndMonth(year,month);
        for(WorkDate date:list){
            map.put(date.getId(),date);
        }
        return map;
    }



    public Result changeworkday(Long currentDay) throws Exception {
        Result result = new Result();
        if(currentDay != null){
            WorkDate workDate = workDateMapper.selectByPrimaryKey(currentDay);
            if(workDate != null){
                if(workDate.getWorkday() == 1){
                    workDate.setWorkday(0);
                } else if(workDate.getWorkday() == 0){
                    workDate.setWorkday(1);
                }
                workDateMapper.updateByPrimaryKey(workDate);
            }else{
                //处理日期
                Calendar calendar = new GregorianCalendar();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = df.parse(currentDay.toString());
                calendar.setTime(date);
                workDate = new WorkDate();
                workDate.setId(currentDay);
                workDate.setYear(calendar.get(Calendar.YEAR));
                workDate.setMonth(calendar.get(Calendar.MONTH)+1);
                workDate.setQuarter(getSeason(calendar));
                workDate.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
                workDate.setDayofweek(calendar.get(Calendar.DAY_OF_WEEK));
                workDate.setWorkday(1);
                workDateMapper.insert(workDate);
            }
        }
        return result;
    }


    /**
     *
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @return
     */
    public static int getSeason(Calendar c) {
        int season = 0;
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }
}
