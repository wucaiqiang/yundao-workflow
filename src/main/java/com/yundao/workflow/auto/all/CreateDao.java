package com.yundao.workflow.auto.all;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * myBatis模板代码生成工具dao生成类
 * @author 
 * 创建时间:2012-07-03
 */
public class CreateDao {

	public List<String> colClassList;//类中字段名-类型
	public String className;//类名
	public String classPackageName;//类所在包名
	public String parentPackageName;//上层类名
	public String pk = "";//主键名
	public String dbpk = "";//数据库中主键名
	public String classNameMapper;
	public String classNameModel;
	public String classNameService;
	public String classNameServiceImpl;
	public CreateDao(List<String> colClassList,String className,String classPackageName,String parentPackageName,String pk,String dbpk) {
		this.colClassList = colClassList;
		this.className = className;
		this.classPackageName = classPackageName;
		this.parentPackageName = parentPackageName;
		this.pk = pk;
		this.dbpk = dbpk;
		this.classNameModel = className + "Model";
		this.classNameService = className + "Service";
		this.classNameServiceImpl = className + "ServiceImpl";
		this.classNameMapper = className + "Mapper";
	}
	
	public void createIDAOJava(Util util) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> fileStrList = new ArrayList<String>();
		
		fileStrList.add("package " + classPackageName+".mapper."+util.toLower(className)+";");
		fileStrList.add("");
//		fileStrList.add("import java.sql.SQLException;");
//		fileStrList.add("import java.util.List;");
//		fileStrList.add("import "+util.getUserPackageName()+".model."+util.toLower(className)+"."+classNameModel+";");
////		fileStrList.add("import "+classPackageName+".mapper.IBaseMapper;");
//		fileStrList.add("import java.util.Map;");
//		fileStrList.add("import org.apache.ibatis.annotations.Mapper;");
//		fileStrList.add("");
////		fileStrList.add("public interface "+className+"Mapper extends IBaseMapper<"+classNameModel+">{");
//		fileStrList.add("@Mapper");
		fileStrList.add("public interface "+className+"Mapper{");
		fileStrList.add("");
		
		
		
//		if(!pk.equals("")){
//			fileStrList.add("    public int insert"+className+"("+classNameModel+" object) throws SQLException;");
//			fileStrList.add("");
//			fileStrList.add("    public int update"+className+"ByPK("+classNameModel+" object) throws SQLException;");
//			fileStrList.add("");
//		}
//
//		fileStrList.add("    public int delete"+className+"ByPK("+classNameModel+" object) throws SQLException;");
//		fileStrList.add("");
//
//		if(!pk.equals("")){
//			fileStrList.add("    public "+classNameModel+" get"+className+"ByPK("+classNameModel+" object) throws SQLException;");
//			fileStrList.add("");
//		}
//
//		fileStrList.add("    public List<"+classNameModel+"> get"+className+"ListByCond("+classNameModel+" object) throws SQLException;");
//		fileStrList.add("");
//
//		fileStrList.add("    public List<"+classNameModel+"> queryPageInfo(Map<String, Object> params) throws SQLException;");
//		fileStrList.add("");
//
//		fileStrList.add("    public Integer queryPageCount(Map<String, Object> params) throws SQLException;");
//		fileStrList.add("");
		
		fileStrList.add("}");
		util.writeFile(fileStrList, "mapper/"+util.toLower(className)+"/", className+"Mapper.java");
	}

}
