<html>
<head>
<title>Index Page</title>
</head>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
	var prefix = 'myservice';

	var RestGet = function() {
		$.ajax({
			type : 'GET',
			url : prefix + '/' + Date.now(),
			dataType : 'json',
			async : true,
			success : function(result) {
				alert('Time ' + result.time + ', message:'
						+ result.message);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.status + ' ' + jqXHR.responseText);
			}
		});
	}

	var RestPut = function() {
		var JSONObject = {
			"time" : Date.now(),
			"message" : "PUT method usage example"
		};

		$.ajax({
			type : 'PUT',
			url : prefix,
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(JSONObject),
			dataType : 'json',
			async : true,
			success : function(result) {
				alert('Time ' + result.time + ', message:'
						+ result.message);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.status + ' ' + jqXHR.responseText);
			}
		});
	}

	var RestPost = function() {
		$.ajax({
			type : 'POST',
			url : prefix,
			dataType : 'json',
			async : true,
			success : function(result) {
				alert('Time ' + result.time + ', message:'
						+ result.message);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.status + ' ' + jqXHR.responseText);
			}
		});
	}

	var RestDelete = function() {
		$.ajax({
			type : 'DELETE',
			url : prefix + '/' + Date.now(),
			dataType : 'json',
			async : true,
			success : function(result) {
				alert('Time ' + result.time + ', message:'
						+ result.message);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.status + ' ' + jqXHR.responseText);
			}
		});
	}
</script>
<body>
 
    <button type="button" onclick="RestGet()">Method GET</button>
    <button type="button" onclick="RestPost()">Method POST</button>
    <button type="button" onclick="RestDelete()">Method DELETE</button>
    <button type="button" onclick="RestPut()">Method PUT</button>
 
</body>
</html>