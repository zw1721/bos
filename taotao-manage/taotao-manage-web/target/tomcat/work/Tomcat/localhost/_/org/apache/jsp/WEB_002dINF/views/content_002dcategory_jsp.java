/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2017-09-26 08:08:21 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class content_002dcategory_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<div>\r\n");
      out.write("\t <ul id=\"contentCategory\" class=\"easyui-tree\">\r\n");
      out.write("    </ul>\r\n");
      out.write("</div>\r\n");
      out.write("<div id=\"contentCategoryMenu\" class=\"easyui-menu\" style=\"width:120px;\" data-options=\"onClick:menuHandler\">\r\n");
      out.write("    <div data-options=\"iconCls:'icon-add',name:'add'\">添加</div>\r\n");
      out.write("    <div data-options=\"iconCls:'icon-edit',name:'rename'\">重命名</div>\r\n");
      out.write("    <div class=\"menu-sep\"></div>\r\n");
      out.write("    <div data-options=\"iconCls:'icon-remove',name:'delete'\">删除</div>\r\n");
      out.write("</div>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("$(function(){\r\n");
      out.write("\t$(\"#contentCategory\").tree({\r\n");
      out.write("\t\turl : '/rest/content/category',\r\n");
      out.write("\t\tanimate: true,\r\n");
      out.write("\t\tmethod : \"GET\",\r\n");
      out.write("\t\t// 右键菜单事件\r\n");
      out.write("\t\tonContextMenu: function(e,node){\r\n");
      out.write("            e.preventDefault(); //阻止默认事件执行\r\n");
      out.write("            $(this).tree('select',node.target); //选中执行右键事件的行\r\n");
      out.write("            $('#contentCategoryMenu').menu('show',{\r\n");
      out.write("                left: e.pageX,\r\n");
      out.write("                top: e.pageY\r\n");
      out.write("            });\r\n");
      out.write("        },\r\n");
      out.write("        // 节点结束编辑后触发的事件\r\n");
      out.write("        onAfterEdit : function(node){\r\n");
      out.write("        \tvar _tree = $(this);\r\n");
      out.write("        \tif(node.id == 0){\r\n");
      out.write("        \t\t// 新增节点\r\n");
      out.write("        \t\t$.post(\"/rest/content/category\",{parentId:node.parentId,name:node.text},function(data){\r\n");
      out.write("        \t\t\t// 在新增节点之后，获取数据库中的ID。回填到页面节点ID\r\n");
      out.write("        \t\t\t_tree.tree(\"update\",{\r\n");
      out.write("        \t\t\t\ttarget : node.target,\r\n");
      out.write("        \t\t\t\tid : data.id\r\n");
      out.write("        \t\t\t});\r\n");
      out.write("        \t\t});\r\n");
      out.write("        \t}else{\r\n");
      out.write("        \t\t// 重命名\r\n");
      out.write("        \t\t$.ajax({\r\n");
      out.write("        \t\t\t   type: \"PUT\",\r\n");
      out.write("        \t\t\t   url: \"/rest/content/category\",\r\n");
      out.write("        \t\t\t   data: {id:node.id,name:node.text},\r\n");
      out.write("        \t\t\t   success: function(msg){\r\n");
      out.write("        \t\t\t\t   //$.messager.alert('提示','新增商品成功!');\r\n");
      out.write("        \t\t\t   },\r\n");
      out.write("        \t\t\t   error: function(){\r\n");
      out.write("        \t\t\t\t   $.messager.alert('提示','重命名失败!');\r\n");
      out.write("        \t\t\t   }\r\n");
      out.write("        \t\t\t});\r\n");
      out.write("        \t}\r\n");
      out.write("        }\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("// 菜单的处理事件\r\n");
      out.write("function menuHandler(item){\r\n");
      out.write("\t// 获取树\r\n");
      out.write("\tvar tree = $(\"#contentCategory\");\r\n");
      out.write("\t// 获取树中被选择的节点\r\n");
      out.write("\tvar node = tree.tree(\"getSelected\");\r\n");
      out.write("\t// 判断选择的是 add\\delete\\rename\r\n");
      out.write("\tif(item.name === \"add\"){\r\n");
      out.write("\t\t// 给树追加子节点\r\n");
      out.write("\t\ttree.tree('append', {\r\n");
      out.write("            parent: (node?node.target:null),// 指定父节点\r\n");
      out.write("            data: [{\r\n");
      out.write("                text: '新建分类',// 子节点名字\r\n");
      out.write("                id : 0,// 子节点的ID，给出的是初始化ID。\r\n");
      out.write("                parentId : node.id// 父节点ID\r\n");
      out.write("            }]\r\n");
      out.write("        });\r\n");
      out.write("\t\t// 获取新增的节点\r\n");
      out.write("\t\tvar _node = tree.tree('find',0);\r\n");
      out.write("\t\t// 先选中新增的节点，打开编辑窗口\r\n");
      out.write("\t\ttree.tree(\"select\",_node.target).tree('beginEdit',_node.target);\r\n");
      out.write("\t}else if(item.name === \"rename\"){\r\n");
      out.write("\t\ttree.tree('beginEdit',node.target);\r\n");
      out.write("\t}else if(item.name === \"delete\"){\r\n");
      out.write("\t\t$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){\r\n");
      out.write("\t\t\t// 无论确认还是取消都会执行\r\n");
      out.write("\t\t\tif(r){\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("     \t\t\t   type: \"POST\",\r\n");
      out.write("     \t\t\t   url: \"/rest/content/category\",\r\n");
      out.write("     \t\t\t   data : {parentId:node.parentId,id:node.id,\"_method\":\"DELETE\"},\r\n");
      out.write("     \t\t\t   success: function(msg){\r\n");
      out.write("     \t\t\t\t   //$.messager.alert('提示','新增商品成功!');\r\n");
      out.write("     \t\t\t\t  tree.tree(\"remove\",node.target);\r\n");
      out.write("     \t\t\t   },\r\n");
      out.write("     \t\t\t   error: function(){\r\n");
      out.write("     \t\t\t\t   $.messager.alert('提示','删除失败!');\r\n");
      out.write("     \t\t\t   }\r\n");
      out.write("     \t\t\t});\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("</script>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
