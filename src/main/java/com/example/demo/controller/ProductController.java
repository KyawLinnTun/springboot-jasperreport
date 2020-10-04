package com.example.demo.controller;

import com.example.demo.repo.ProductRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;


    @GetMapping
    public String products(){
        return  "product";
    }


    @GetMapping("/report")
    public void report(HttpServletResponse response) throws Exception {
      //  response.setContentType("text/html");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productsreport",new JRBeanCollectionDataSource(productRepo.findAll()));

        InputStream inputStream = this.getClass().getResourceAsStream("/reports/productsreport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
        String filePath="D:/ANGULAR-TEST";

        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath + "\\Employee_report.pdf");

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"users.pdf\""));

        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }
}
