package org.antframework.configcenter.biz.provider.topology;

import com.google.common.collect.Maps;
import org.antframework.configcenter.biz.util.WorkOrderDingdingUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zongzheng
 * @date 2024/7/11 10:56 AM
 * @project configcenter
 */
@Configuration
public class BeScheduledTask {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 每周五下午2点启动
     */
    @Scheduled(cron = "0 0 15 ? * FRI")
    private void allNotice() {
        String message = "[喇叭]又是周五了，大家抖抖小手，基于实事求是的原则，提醒下团队里的同学，记得今天把蓝效平台填一下^-^";
        WorkOrderDingdingUtils.sendMessageAtAll(message.toString());
    }

    /**
     * 每周五和每周六下午5点启动
     */
    @Scheduled(cron = "0 0 17 ? * FRI,SAT")
    private void noticeLeader() {
        System.err.println("start check lanxiao: " + LocalDateTime.now());
        checkLanXiao(DateFormatUtils.format(getMondayDate(),"yyyy-MM-dd"),
                DateFormatUtils.format(DateUtils.addDays(getMondayDate(),5),"yyyy-MM-dd"));
    }

    private void checkLanXiao(String startTime, String endTime){
        System.err.println("begin check LanXiao, startTime= " + startTime + ",endTime=" + endTime);

        String checkSql = "SELECT\n" +
                "bbb.teamName,\n" +
                "ROUND(AVG(fillNum),2) as teamFillNum\n" +
                "from\n" +
                "(select\n" +
                "  workDate,\n" +
                "  teamName,\n" +
                "  avg(employeeNum),\n" +
                "  ROUND(sum(manDay),2),\n" +
                "  avg(employeeNum)*1,\n" +
                "  ROUND(sum(manDay)/(avg(employeeNum)*1),2) as fillNum,\n" +
                "  ROUND(sum(aManDay),2),\n" +
                "  ROUND(sum(wManDay),2)\n" +
                "from\n" +
                "  (\n" +
                "    select\n" +
                "      t1.teamName,\n" +
                "      t1.id,\n" +
                "      t2.manDay,\n" +
                "      date (t2.workDate) workDate,\n" +
                "      case\n" +
                "        t2.workOrderStatus\n" +
                "        when 'APPROVED' THEN t2.manDay\n" +
                "        ElSE 0\n" +
                "      end as aManDay,\n" +
                "      case\n" +
                "        t2.workOrderStatus\n" +
                "        when 'WAIT_APPROVE' THEN t2.manDay\n" +
                "        ElSE 0\n" +
                "      end as wManDay,\n" +
                "      ben.employeeNum\n" +
                "    from\n" +
                "      BeEmployee t1\n" +
                "      left join BeWorkOrder t2 on t1.id=t2.employeeId\n" +
                "      left join BeProject t3 on t2.projectId=t3.id\n" +
                "      left join BeEmployee be on t1.superiorId=be.id\n" +
                "      left join (\n" +
                "        select\n" +
                "          be.teamName,\n" +
                "          count(1) employeeNum\n" +
                "        from\n" +
                "          BeEmployee be\n" +
                "        where\n" +
                "          serviceStatus='ON_LINE'\n" +
                "        group by\n" +
                "          be.teamName\n" +
                "      ) ben on t1.teamName=ben.teamName\n" +
                "    where\n" +
                "      t1.serviceStatus='ON_LINE'\n" +
                "      and t1.superiorId is not null\n" +
                "      and t2.id is not null\n" +
                "  ) as aaa\n" +
                "  where workDate >= '"+startTime+"' and workDate <= '"+endTime+"'\n" +
                "group by\n" +
                "  teamName,\n" +
                "  workDate) bbb group by teamName";

        System.err.println(checkSql);

        List<Map<String, Object>> lstData = jdbcTemplate.queryForList(checkSql);
        for (Map<String, Object> map : lstData) {
            String teamName = map.get("teamName").toString();
            double fillNum = Double.parseDouble(map.get("teamFillNum").toString());
            NumberFormat nf = NumberFormat.getPercentInstance();
            String percent = nf.format(fillNum);
            System.err.println("teamName=" + teamName + ",fillNum=" + percent);
            if(fillNum < 0.8d){
                String message = teamName + "在"+startTime+"~"+endTime+"的蓝效填报率为" + percent + "，请敦促团队同学及时填报^-^";
                WorkOrderDingdingUtils.sendMessageAtChosePerson(message,namePhoneMapping.get(teanNameMapping.get(teamName)));
            }
        }

        System.err.println("finish check LanXiao");
    }

    public static Date getMondayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -6);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }

        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.err.println( DateFormatUtils.format(getMondayDate(),"yyyy-MM-dd"));
        System.err.println( DateFormatUtils.format(DateUtils.addDays(getMondayDate(),5),"yyyy-MM-dd"));
        System.err.println( namePhoneMapping.get(teanNameMapping.get("GAIA平台研发组")));
    }

    private static Map<String,String> namePhoneMapping = Maps.newHashMap();
    static{
        namePhoneMapping.put("云风","18551238687");
        namePhoneMapping.put("宗政","18968080085");
        namePhoneMapping.put("一帝","13777831295");
        namePhoneMapping.put("才龙","15838342507");
        namePhoneMapping.put("子辰","18606510130");
        namePhoneMapping.put("人觉","17348518864");
        namePhoneMapping.put("玄微","15658196677");
        namePhoneMapping.put("飘絮","13816213469");
        namePhoneMapping.put("九河","13501399720");
        namePhoneMapping.put("严慎","15394225205");
    }
    private static Map<String,String> teanNameMapping = Maps.newHashMap();
    static{
        teanNameMapping.put("GAIA平台研发组","云风");
        teanNameMapping.put("PM","宗政");
        teanNameMapping.put("产品组","一帝");
        teanNameMapping.put("前端组","才龙");
        teanNameMapping.put("密码计算引擎组","子辰");
        teanNameMapping.put("政务行业研发组","人觉");
        teanNameMapping.put("数据科学组","玄微");
        teanNameMapping.put("测试组","飘絮");
        teanNameMapping.put("解决方案部","九河");
        teanNameMapping.put("运维&交付组","宗政");
        teanNameMapping.put("金融&数据运营研发组","严慎");
    }


}
