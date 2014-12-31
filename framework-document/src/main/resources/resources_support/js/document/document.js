String.prototype.formaturl = function() {
			var args = arguments;
			return this.replace(/\{(\w+)\}/g, function(m, i) {
				//alert("m:" + m + "i:" + i);
				//return args[0]['' + i];
				if (document.getElementsByName(i).length == 0) {
					return m;
				}
				return document.getElementsByName(i)[0].value;
			});
		};

		//alert("{dd}adsf{d}".format({dd:1, d:2}));

		// prepare the form when the DOM is ready 
		$(document).ready(function() {
			
			//GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
			var requrl = document.getElementById('req_url').value.formaturl();
			var reqmethod = document.getElementById('req_method').value;
			var type = 'post';
			if (reqmethod.indexOf('GET') >= 0) {
				type = 'get';
			}else if(reqmethod.indexOf('HEAD') >= 0){
				type = 'head';
			}else if(reqmethod.indexOf('POST') >= 0){
				type = 'post';
			}else if(reqmethod.indexOf('PUT') >= 0){
				type = 'put';
			}else if(reqmethod.indexOf('PATCH') >= 0){
				type = 'patch';
			}else if(reqmethod.indexOf('DELETE') >= 0){
				type = 'delete';
			}else if(reqmethod.indexOf('OPTIONS') >= 0){
				type = 'options';
			}else if(reqmethod.indexOf('TRACE') >= 0){
				type = 'trace';
			}
			var options = {
				target : '#output1', // target element(s) to be updated with server response 
				beforeSubmit : showRequest, // pre-submit callback 
				success : showResponse, // post-submit callback 
				dataType : 'text',
				url : requrl,
				type : type,
				timeout : 200000

			// other available options: 
			//dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
			//clearForm: true        // clear all form fields after successful submit 
			//resetForm: true        // reset the form after successful submit 

			// $.ajax options can be used here too, for example: 
			};

			// bind form using 'ajaxForm' 
			$('#form1').submit(function() {
				$(this).ajaxSubmit(options);
				return false;
			});
		});

		// pre-submit callback 
		function showRequest(formData, jqForm, options) {
			var queryString = $.param(formData);
			$('#request_param').html(queryString);
			return true;
		}

		// post-submit callback 
		function showResponse(responseText, statusText, xhr, $form) {
			$('#RawJson').val(responseText);
			Process();
			//alert(responseText.toString());
			//alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
			//'\n\nThe output div should have already been updated with the responseText.'); 
		}

		function clearNoNum(event, obj) {
			//响应鼠标事件，允许左右方向键移动 
			event = window.event || event;
			if (event.keyCode == 37 | event.keyCode == 39) {
				return;
			}
			//先把非数字的都替换掉，除了数字和. 
			obj.value = obj.value.replace(/[^\d.]/g, "");
			//必须保证第一个为数字而不是. 
			obj.value = obj.value.replace(/^\./g, "");
			//保证只有出现一个.而没有多个. 
			obj.value = obj.value.replace(/\.{2,}/g, ".");
			//保证.只出现一次，而不能出现两次以上 
			obj.value = obj.value.replace(".", "$#$").replace(/\./g, "")
					.replace("$#$", ".");
		}
		function checkNum(obj) {
			//为了去除最后一个. 
			obj.value = obj.value.replace(/\.$/g, "");
		}

		function DigitInput(obj, event) {
			//响应鼠标事件，允许左右方向键移动 
			event = window.event || event;
			if (event.keyCode == 37 | event.keyCode == 39) {
				return;
			}
			obj.value = obj.value.replace(/\D/g, "");
		}