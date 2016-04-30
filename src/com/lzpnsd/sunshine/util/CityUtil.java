package com.lzpnsd.sunshine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lzpnsd.sunshine.bean.CityBean;

import android.content.Context;
import android.util.Log;

public class CityUtil {

	public List<CityBean> parseExcel(Context context){
		List<CityBean> cityBeans = null;
		try {
			InputStream inputStream = context.getAssets().open("areaid_v.xls");
			cityBeans = readXls(inputStream);
			Log.i("ParseExcelUtil", cityBeans.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cityBeans;
	}
    /**
     * 读取xls文件内容
     * 
     * @return List<XlsDto>对象
     * @throws IOException
     *             输入/输出(i/o)异常
     */
    private List<CityBean> readXls(InputStream inputStream) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        CityBean cityBean = null;
        List<CityBean> list = new ArrayList<CityBean>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                cityBean = new CityBean();
                // 循环列Cell
                HSSFCell areaId = hssfRow.getCell(0);
                if (areaId == null) {
                    continue;
                }
                cityBean.setAreaId(getValue(areaId));
                
                HSSFCell nameEn = hssfRow.getCell(1);
                if (nameEn == null) {
                    continue;
                }
                cityBean.setNameEn(getValue(nameEn));
                HSSFCell nameCn = hssfRow.getCell(2);
                if (nameCn == null) {
                    continue;
                }
                cityBean.setNameCn(getValue(nameCn));
                HSSFCell districtEn = hssfRow.getCell(3);
                if (districtEn == null) {
                    continue;
                }
                cityBean.setDistrictEn(getValue(districtEn));
                HSSFCell districtCn = hssfRow.getCell(4);
                if (districtCn == null) {
                    continue;
                }
                cityBean.setDistrictCn(getValue(districtCn));
                HSSFCell provEn = hssfRow.getCell(5);
                if (provEn == null) {
                	continue;
                }
                cityBean.setProvEn(getValue(provEn));
                HSSFCell provCn = hssfRow.getCell(6);
                if (provCn == null) {
                	continue;
                }
                cityBean.setProvCn(getValue(provCn));
                HSSFCell nationEn = hssfRow.getCell(7);
                if (nationEn == null) {
                	continue;
                }
                cityBean.setNationEn(getValue(nationEn));
                HSSFCell nationCn = hssfRow.getCell(8);
                if (nationCn == null) {
                	continue;
                }
                cityBean.setNationCn(getValue(nationCn));
                list.add(cityBean);
            }
        }
        return list;
    }
    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
        	 Double tmpDou = Double.parseDouble(String.valueOf(hssfCell.getNumericCellValue()));//转换单元格值的类型为double类型
             
             Long tmpMno = tmpDou.longValue();//把单元格的值1.2222的值转换成正常输入的数字
             
            return tmpMno.toString().trim();//删除字符串两端的空格
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}
