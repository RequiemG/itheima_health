package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.http.HttpResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    public ReportController() {
    }

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {

        // 前端使用的是get请求，我们返回的数据要和前端指定的一致，这里用map
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 前端要的数据为month和membercount，这两个数据要用列表
        List<String> months = new ArrayList<>();



        // 计算过去12个月，要借助calendar对象
        Calendar calendar = Calendar.getInstance();// 获得日历对象，默认时间为当前时间

        // 往前推12个月
        calendar.add(Calendar.MONTH,-12);   // 获得当前时间往前推12个月的时间

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",months);

        List<Integer> memberCount = memberService.findMemberCountByMonth(months);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // setmealNames setmealCount
        HashMap<String, Object> map = new HashMap<>();
        // 查找setmealName到t_setmeal表中得到id和name
        List<Map<String, Object>> setmealInfos = setmealService.findSetmealCount();
        map.put("setmealCount",setmealInfos);


        ArrayList<Object> setmealName = new ArrayList<>();
        for (Map<String, Object> setmealInfo : setmealInfos) {
            String Name = (String) setmealInfo.get("name");
            setmealName.add(Name);
        }
        map.put("setmealName",setmealName);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }


    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> businessReport = reportService.getBusinessReport();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessReport);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        // 这里声明了request和response，springMVC就会自动帮我们将其注进来
        // 服务器响应的应该是一个输出流，将excel文件以流的形式写到客户端供用户下载，就不能使用异步请求
        // 将数据写入excel，再将excel传给用户
        // 写的时候应该在内存中写，而不是写到模(mú)板中，模板应该是干净的
        try {
            Map<String, Object> data = reportService.getBusinessReport();
            // 写excel文件
            String reportDate = (String) data.get("reportDate");
            Integer todayNewMember = (Integer) data.get("todayNewMember");
            Integer totalMember = (Integer) data.get("totalMember");
            Integer thisWeekNewMember = (Integer) data.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) data.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) data.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) data.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) data.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) data.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) data.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) data.get("thisMonthVisitsNumber");
            List<Map<String,Object>> hotSetmeal = (List<Map<String, Object>>) data.get("hotSetmeal");


            // 读取模板文件到内存,要查找模板的路径就要用到HttpServletRequest
            // 得到template的绝对路径，然后加上分隔符，然后加上模板文件名
            String templateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator +"report_template.xlsx";
            // 基于模板文件，在内存中创建新的模板文件
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheet = excel.getSheetAt(0);

            //日期填充位置
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);


            row = sheet.getRow(4);
            //新增会员数(本日) 填充位置
            row.getCell(5).setCellValue(todayNewMember);
            //总会员数 填充位置
            row.getCell(7).setCellValue(totalMember);



            row = sheet.getRow(5);
            //本周新增会员数 填充位置
            row.getCell(5).setCellValue(thisWeekNewMember);
            //本月新增会员数 填充位置
            row.getCell(7).setCellValue(thisMonthNewMember);



            row = sheet.getRow(7);
            //今日预约数 填充位置
            row.getCell(5).setCellValue(todayOrderNumber);
            //今日到诊数 填充位置
            row.getCell(7).setCellValue(todayVisitsNumber);



            row = sheet.getRow(8);
            //本周预约数 填充位置
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            //本周到诊数 填充位置
            row.getCell(7).setCellValue(thisWeekVisitsNumber);



            row = sheet.getRow(9);
            //本月预约数 填充位置
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            //本月到诊数 填充位置
            row.getCell(7).setCellValue(thisMonthVisitsNumber);


            int rowNum = 12;
            for (Map<String, Object> map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                System.out.println("----------------------------------没++之前的rowNum"+rowNum);
                row = sheet.getRow(rowNum);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());
                rowNum+=1;
                System.out.println("rouNum+=1->"+rowNum);
            }


            // 基于浏览器作为客户端来下载，要用response
            // 使用输出流让用户下载表格
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置响应信息 两个头一个流
//            response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/wps-office.xlsx'");
            // 指定以附件的形式进行下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

//            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(outputStream);
            outputStream.flush();
            outputStream.close();
            excel.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
