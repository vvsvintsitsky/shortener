var prefix = 'service';

	var RestGet = function() {
		$.ajax({
			type : 'GET',
			url : prefix + '/' + Date.now() + '.info',
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
			longUrl : "http://localhost:8087/shortener-webapp-1.0.0/",
			description : "client PUT request",
			tagDescriptions : new Array("tgDs0", "tgDs1", "tgDs2")
		};
		
		$.ajax({
			type : 'PUT',
			url : prefix,
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(JSONObject),
			dataType : 'json',
			async : true,
			success : function(result) {
				alert('longUrl: ' + result.longUrl + ', description: '
						+ result.description + 'tagDescriptions: ' + result.tagDescriptions);
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