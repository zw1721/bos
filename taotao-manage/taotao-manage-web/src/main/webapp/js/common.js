Date.prototype.format = function(format){ 
    var o =  { 
    "M+" : this.getMonth()+1, //month 
    "d+" : this.getDate(), //day 
    "h+" : this.getHours(), //hour 
    "m+" : this.getMinutes(), //minute 
    "s+" : this.getSeconds(), //second 
    "q+" : Math.floor((this.getMonth()+3)/3), //quarter 
    "S" : this.getMilliseconds() //millisecond 
    };
    if(/(y+)/.test(format)){ 
    	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
    for(var k in o)  { 
	    if(new RegExp("("+ k +")").test(format)){ 
	    	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	    } 
    } 
    return format; 
};

var TT = TAOTAO = {
	// 编辑器参数
	kingEditorParams : {
		filePostName  : "uploadFile",// 指定上传时的参数名
		uploadJson : '/rest/pic/upload',// 指定处理文件上传的服务器路径
		dir : "image", // 上传的文件类型
		wellFormatMode : false // 是否美化生成的HTML代码。false：精简生成的HTML代码
	},
	// 格式化时间
	formatDateTime : function(val,row){
		var now = new Date(val);
    	return now.format("yyyy-MM-dd hh:mm:ss");
	},
	// 格式化连接
	formatUrl : function(val,row){
		if(val){
			return "<a href='"+val+"' target='_blank'>查看</a>";			
		}
		return "";
	},
	// 格式化价格
	formatPrice : function(val,row){
		return (val/100).toFixed(2);
	},
	// 格式化商品的状态
	formatItemStatus : function formatStatus(val,row){
        if (val == 1){
            return '正常';
        } else if(val == 2){
        	return '<span style="color:red;">下架</span>';
        } else {
        	return '未知';
        }
    },
    
    init : function(data){
    	// 初始化图片上传
    	this.initPicUpload(data);
    	// 初始化商品类目
    	this.initItemCat(data);
    },
    // 初始化图片上传组件
    initPicUpload : function(data){
    	$(".picFileUpload").each(function(i,e){
    		var _ele = $(e);
    		// 先清空以前的div
    		_ele.siblings("div.pics").remove();
    		// 准备一个div，用来回显图片
    		_ele.after('\
    			<div class="pics">\
        			<ul></ul>\
        		</div>');
    		// 编辑页面的图片回显
        	if(data && data.pics){
        		var imgs = data.pics.split(",");
        		for(var i in imgs){
        			if($.trim(imgs[i]).length > 0){
        				_ele.siblings(".pics").find("ul").append("<li><a href='"+imgs[i]+"' target='_blank'><img src='"+imgs[i]+"' width='65' height='65' /></a></li>");
        			}
        		}
        	}
        	// 给上传图片的按钮，绑定点击事件
        	$(e).unbind('click').click(function(){
        		// 找距离当前元素最近的form元素
        		var form = $(this).parentsUntil("form").parent("form");
        		// 初始化图片上传组件，并且指定上传的参数
        		KindEditor.editor(TT.kingEditorParams).loadPlugin('multiimage',function(){
        			var editor = this;
        			editor.plugin.multiImageDialog({
						clickFn : function(urlList) {// 点击“全部插入”触发的事件。参数：上传的所有图片的集合
							// 定义一个数组
							var imgArray = [];
							// 遍历图片的集合
							KindEditor.each(urlList, function(i, data) {
								// 把图片地址添加到数组
								imgArray.push(data.url);
								// 把图片回填到页面
								form.find(".pics ul").append("<li><a href='"+imgArray[i]+"' target='_blank'><img src='"+imgArray[i]+"' width='65' height='65' /></a></li>");
							});
							// 把图片地址回填到 隐藏域，用于提交
							form.find("[name=image]").val(imgArray.join(","));
							// 关闭上传窗口
							editor.hideDialog();
						}
					});
        		});
        	});
    	});
    },
    
    // 初始化选择类目组件
    initItemCat : function(data){
    	// 选中所有类名为selectItemCat的标签
    	$(".selectItemCat").each(function(i,e){
    		// 把dom元素变为JQuery对象
    		var _ele = $(e);
    		// 回填数据（先不管）
    		if(data && data.cid){
    			_ele.after("<span style='margin-left:10px;'>"+data.cid+"</span>");
    		}else{
    			// 准备一个空的span标签，将来回填数据用
    			_ele.after("<span style='margin-left:10px;'></span>");
    		}
    		// 先解绑在绑定，避免事件重复
    		_ele.unbind('click').click(function(){
    			// 创建一个div标签，调用EasyUI的弹窗事件，形成窗口
    			$("<div>").css({padding:"5px"}).html("<ul>")
    			.window({
    				width:'500',
    			    height:"450",
    			    modal:true,
    			    closed:true,
    			    iconCls:'icon-save',
    			    title:'选择类目',
    			    onOpen : function(){// 窗口打开后触发事件
    			    	// 获取窗口对象
    			    	var _win = this;
    			    	// 选中新建窗口中的UL标签，生成一棵树
    			    	$("ul",_win).tree({
    			    		url:'/rest/item/cat',
    			    		method : "GET",
    			    		animate:true,
    			    		onClick : function(node){
    			    			// 先判断点击的是否是叶子节点
    			    			if($(this).tree("isLeaf",node.target)){
    			    				// 填写到cid中
    			    				_ele.parent().find("[name=cid]").val(node.id);
    			    				// 实现把节点的名称回填到页面
    			    				_ele.next().text(node.text).attr("cid",node.id);
    			    				// 关闭窗口
    			    				$(_win).window('close');
    			    				
    			    				if(data && data.fun){
    			    					// 通过 call方法可以执行某个对象中的方法，本例中，执行了data对象中的fun方法。
    			    					// 第一个参数：设置函数的this指向，如果没有用到this，那么可以设置为null
    			    					// 从第二个参数开始就是方法所需要的实际参数
    			    					data.fun.call(null,node);
    			    				}
    			    			}
    			    		}
    			    	});
    			    },
    			    onClose : function(){
    			    	$(this).window("destroy");
    			    }
    			}).window('open');
    		});
    	});
    },
    
    createEditor : function(select){
    	return KindEditor.create(select, TT.kingEditorParams);
    },
    
    /**
     * 创建一个窗口，关闭窗口后销毁该窗口对象。<br/>
     * 
     * 默认：<br/>
     * width : 80% <br/>
     * height : 80% <br/>
     * title : (空字符串) <br/>
     * 
     * 参数：<br/>
     * width : <br/>
     * height : <br/>
     * title : <br/>
     * url : 必填参数 <br/>
     * onLoad : function 加载完窗口内容后执行<br/>
     * 
     * 
     */
    createWindow : function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : params.width || "80%",
    		height : params.height || "80%",
    		modal:true,
    		title : params.title || " ",
    		href : params.url,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onLoad : function(){
		    	if(params.onLoad){
		    		params.onLoad.call(this);
		    	}
		    }
    	}).window("open");
    },
    
    closeCurrentWindow : function(){
    	$(".panel-tool-close").click();
    },
    
    changeItemParam : function(node,formId){
    	$.ajax({
    		url : "/rest/item/param/" + node.id,
    		type : "GET",
    		dataType : "json",
    		statusCode : {
    			200 : function(data){
    				$("#"+formId+" .params").show();
	   				 var paramData = JSON.parse(data.paramData);
	   				 var html = "<ul>";
	   				 for(var i in paramData){
	   					 var pd = paramData[i];
	   					 html+="<li><table>";
	   					 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
	   					 
	   					 for(var j in pd.params){
	   						 var ps = pd.params[j];
	   						 html+="<tr><td class=\"param\"><span>"+ps+"</span>: </td><td><span class='textbox'><input autocomplete=\"off\" type=\"text\"/></span></td></tr>";
	   					 }
	   					 
	   					 html+="</li></table>";
	   				 }
	   				 html+= "</ul>";
	   				 $("#"+formId+" .params td").eq(1).html(html);
    			},
    			404 : function(){
    				$("#"+formId+" .params").hide();
   				  $("#"+formId+" .params td").eq(1).empty();
    			},
    			500 : function(){
    				$.messager.alert('提示','查询规格参数模板失败!');
    			}
    		}
    	});
    },
    getSelectionsIds : function (select){
    	var list = $(select);
    	var sels = list.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    },
    
    /**
     * 初始化单图片上传组件 <br/>
     * 选择器为：.onePicUpload <br/>
     * 上传完成后会设置input内容以及在input后面追加<img> 
     */
    initOnePicUpload : function(){
    	$(".onePicUpload").click(function(){
			var _self = $(this);
			KindEditor.editor(TT.kingEditorParams).loadPlugin('image', function() {
				this.plugin.imageDialog({
					showRemote : false,
					clickFn : function(url, title, width, height, border, align) {
						var input = _self.siblings("input");
						input.parent().find("img").remove();
						input.val(url);
						input.after("<a href='"+url+"' target='_blank'><img src='"+url+"' width='80' height='50'/></a>");
						this.hideDialog();
					}
				});
			});
		});
    }
};

function in_image(_li){
	$(_li).find("div").css("display","block");
}
function out_image(_li){
	$(_li).find("div").css("display","none");
}
function _do_left(_node){
	var _li = $(_node).parent().parent();
	var prev_li = _li.prev();
	if(prev_li){
		prev_li.before(_li);
	}
}
function _do_right(_node){
	var _li = $(_node).parent().parent();
	var next_li = _li.next();
	if(next_li){
		next_li.after(_li);
	}
}
function _do_del(_node){
	$(_node).parent().parent().remove();
}
