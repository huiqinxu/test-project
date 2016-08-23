package cn.pinming.reportforms.service;

import cn.pinming.common.result.Result;
import cn.pinming.reportforms.entity.DateInterval;
import cn.pinming.reportforms.entity.WorkDate;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 2016/7/13.
 */
public interface WorkDateService {
    /**
     * 自动生成工作日列表
     * @param year
     * @param reversDay，调休的日期，周末上班或者非周末放假
     */
    void generateWorkDate(int year, List<Calendar> reversDay);

    /**
     * 年度区间
     * @param year
     * @param inteval 0全年;1上半年;2下半年
     * @return
     */
    DateInterval getDateIntervalByYear(int year, int inteval);

    /**
     * 获取季度区间
     * @param year
     * @param quarter 季度，1,2,3,4
     * @return
     */
    DateInterval getDateIntervalByQuarter(int year, int quarter);

    /**
     * 获取月度区间
     * @param year
     * @param month 1,2,3,4,5,6,7,8,9,10,11,12
     * @return
     */
    DateInterval getDateIntervalByMonth(int year, int month);


    /**
     * 获取环比区间
     * @return
     */
    DateInterval getChainDateInterval(DateInterval dateInterval);

    /**
     * 获取同比区间
     * @param dateInterval
     * @return
     */
    DateInterval getYearDateInterval(DateInterval dateInterval);

    /**
     * 查询两个时间之间的分钟数
     * @param startDate
     * @param endDate
     * @return
     */
    int getIntervalMinutes(Date startDate, Date endDate);
    /**
     * 查询某两天之间的工作日天数
     * @param startDate
     * @param endDate
     * @return
     */
    int getWorkMinutes(Date startDate, Date endDate);

    /**
     * 产生时间维度的id
     * @param date
     * @return
     */
    long generateTimeId(Date date);

    /**
     * 工作日小时数
     */
    static final double WordDayHours = 8D;

    /**
     * 当前时间获取当月日列表
     * @param year
     * @param month
     * @return
     */
    Map<Long,WorkDate> getCurrentMonthDateByCurrentDate(Integer year, Integer month);



     Result changeworkday(Long currentDay) throws Exception;

}
