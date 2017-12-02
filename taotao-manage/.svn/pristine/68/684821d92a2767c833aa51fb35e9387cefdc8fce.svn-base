<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-edit',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/rest/content/category',
		animate: true,
		method : "GET",
		// 右键菜单事件
		onContextMenu: function(e,node){
            e.preventDefault(); //阻止默认事件执行
            $(this).tree('select',node.target); //选中执行右键事件的行
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        // 节点结束编辑后触发的事件
        onAfterEdit : function(node){
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/rest/content/category",{parentId:node.parentId,name:node.text},function(data){
        			// 在新增节点之后，获取数据库中的ID。回填到页面节点ID
        			_tree.tree("update",{
        				target : node.target,
        				id : data.id
        			});
        		});
        	}else{
        		// 重命名
        		$.ajax({
        			   type: "PUT",
        			   url: "/rest/content/category",
        			   data: {id:node.id,name:node.text},
        			   success: function(msg){
        				   //$.messager.alert('提示','新增商品成功!');
        			   },
        			   error: function(){
        				   $.messager.alert('提示','重命名失败!');
        			   }
        			});
        	}
        }
	});
});
// 菜单的处理事件
function menuHandler(item){
	// 获取树
	var tree = $("#contentCategory");
	// 获取树中被选择的节点
	var node = tree.tree("getSelected");
	// 判断选择的是 add\delete\rename
	if(item.name === "add"){
		// 给树追加子节点
		tree.tree('append', {
            parent: (node?node.target:null),// 指定父节点
            data: [{
                text: '新建分类',// 子节点名字
                id : 0,// 子节点的ID，给出的是初始化ID。
                parentId : node.id// 父节点ID
            }]
        });
		// 获取新增的节点
		var _node = tree.tree('find',0);
		// 先选中新增的节点，打开编辑窗口
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			// 无论确认还是取消都会执行
			if(r){
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category",
     			   data : {parentId:node.parentId,id:node.id,"_method":"DELETE"},
     			   success: function(msg){
     				   //$.messager.alert('提示','新增商品成功!');
     				  tree.tree("remove",node.target);
     			   },
     			   error: function(){
     				   $.messager.alert('提示','删除失败!');
     			   }
     			});
			}
		});
	}
}
</script>