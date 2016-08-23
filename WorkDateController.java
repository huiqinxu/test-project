package cn.pinming.reportforms.web;

import cn.pinming.common.result.Result;
import cn.pinming.reportforms.entity.WorkDate;
import cn.pinming.reportforms.service.WorkDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/9.
 */
@Controller
public class WorkDateController {

    @Autowired
    private WorkDateService workDateService;

    /**
     * 工作日首页
     * @return
     */
    @RequestMapping("/reportforms/workDaateIndex")
    public ModelAndView workDaateIndex(){
        return new ModelAndView("/reportforms/workdate/workdate");
    }


    /**
     * 当前时间获取当月日列表
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("/reportforms/getCurrentMonthDateByCurrentDate")
    @ResponseBody
    public Map<Long,WorkDate> getCurrentMonthDateByCurrentDate(Integer year, Integer month){
        Map<Long,WorkDate> map = workDateService.getCurrentMonthDateByCurrentDate(year,month);
        return map;
    }


    /**
     * 更改工作休息日
     * @param currentDay
     * @return
     * @throws Exception
     */
    @RequestMapping("/reportforms/changeworkday")
    @ResponseBody
    public Result changeworkday(Long currentDay) throws Exception {
        return workDateService.changeworkday(currentDay);
    }


}



