package com.gmcc.msb.msbsystem.controller;


import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.UserService;
import com.gmcc.msb.msbsystem.util.CommonStaticUtils;
import io.swagger.annotations.Api;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.gmcc.msb.msbsystem.util.CommonStaticUtils.toDefaultValue;

@Api(value = "DownloadController", tags = {"下载管理管理"})
@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private UserService service;

    @GetMapping("/user")
    public void user(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单,参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet();

        //创建表头
        setTitle(workbook, sheet);
//        List<Answer> answers = answerService.findAll();
        List<User> all = service.findAll();
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        if(all != null){
            for (User a: all
                    ) {
                HSSFRow row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(toDefaultValue(a.getId(),0).toString());
                row.createCell(1).setCellValue(toDefaultValue(a.getLoginId(),"").toString());
                row.createCell(2).setCellValue(toDefaultValue(a.getName(),"").toString());
                row.createCell(3).setCellValue(toDefaultValue(a.getOrgId(),"").toString());
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue(toDefaultValue(a.getMobile(),"").toString());
                row.createCell(6).setCellValue(toDefaultValue(a.getStatus(),"").toString());
                row.createCell(7).setCellValue(toDefaultValue(a.getIsLock(),"").toString());
                if(a.getUpdateTime() != null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat(CommonStaticUtils.dateFormat);
                    row.createCell(8).setCellValue(dateFormat.format(a.getUpdateTime()));
                }
                rowNum++;
            }
        }
        String fileName = "user.csv";
        //清空response
        response.reset();
        //设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
    }



    private void setTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("id");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("账号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("所在组织架构");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("所属角色");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("手机");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("状态");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("是否锁定");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("最后一次成功登陆");
        cell.setCellStyle(style);
    }
}
