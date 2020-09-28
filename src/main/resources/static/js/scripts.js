$(document).on('click', '.answer-write input[type=submit]', addAnswer);

function addAnswer(e) {
	e.preventDefault();
	console.log("click me");
	
	var queryString = $(".answer-write").serialize();
	console.log("query : " + queryString);
	
	var url = $(".answer-write").attr("action");
	console.log(url);
	
	$.ajax({
		type: 'post',
		url: url,
		data: queryString,
		dataType: 'json',
		error: onError,
		success: function(data, status) {
			console.log("통과");
			var answerTemplate = $("#answerTemplate").html();
			var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents,
				data.question.id, data.id);
			$(".qna-comment-slipp-articles").prepend(template);
			$(".comment-count").text(data.question.countOfAnswer);
			$(".answer-write textarea").val("");
		}
	});
	
	function onError(jqXHR, data) {
    	console.log(jqXHR.status);
   }
   

}
	$(document).on('click', '.link-delete-article', deleteAnswer);

	
	function deleteAnswer(e) {
		e.preventDefault();
		var deletBtn = $(this);
		var url = deletBtn.attr("href");
		
		
		$.ajax({
			type: 'delete',
			url: url,
			dataType: 'json',
			error: function(xhr, status) {				
				console.log("error");
			},
			success: function(data, status) {
				console.log(data);
				if(data.valid) {
					deletBtn.closest("article").remove();
					$(".comment-count").text();
				}else {
					alert(data.errorMessage);
				}
			}
		});
	}

	var tempAnswerData = null;
	var check = 0;

	$(document).on('click','.link-answer-modify-article', updateAnswer)
	
	function updateAnswer(e) {
		e.preventDefault();
		var updateBtn = $(this);
		var url = updateBtn.attr("href");
		console.log(url);

		$.ajax({
			type: 'get',
			url: url,
			dataType: 'json',
			error: function (xhr, status) {
				console.log(status);
			},
			success: function (data, status) {
				if(check==1) {
					cancelUpdateAnswer();
					check=0;
				}
				var answerId = data.id;
				var content = data.contents;
				var questionId = data.question.id;
				var a = '';
				a += '<div class="input-group"  style="width: 100%">';
				a += '<textarea class="form-control answerUpdateForm" name="content_"'+answerId+'>';
				a += content;
				a += '</textarea>';
				a += '<ul class="article-util-list">';
				a += '<li class="update-confirm-list"><a href="/api/questions/'+questionId+'/answers/'+answerId+'/update">확인</a></li>'
				a += '<li><a href="">취소</a></li></ul>'
				a += '</div>';
				$('.qna-comment-slipp-articles .article'+answerId).children('.article-main').html(a);
				check = 1;
			}
		});

		function cancelUpdateAnswer() {
			var content = $(".qna-comment-slipp-articles .input-group .answerUpdateForm").val();
			var questionId = $(".update-confirm-list a").attr("href").split("/")[3];
			var answerId = $(".update-confirm-list a").attr("href").split("/")[5];
		
					var original = '';
					original += '<div class="article-main" id="answer-main">'
					original +=	'<div class="article-doc comment-doc">'+content+'</div>'
					original += '<div class="article-util">'
					original += '<ul class="article-util-list">'
					original += '<li><a class="link-answer-modify-article" href="/api/questions/'+questionId+'/answers/'+answerId+'/updateForm">수정</a></li>'
					original +=	'<li><a class="link-delete-article href="/api/questions/{3}/answers/{4}">삭제</a></li>"'
					original += '</ul></div>'
					$('.qna-comment-slipp-articles .article'+answerId+' .input-group').html(original);
				
		}
	
	}

	
	String.prototype.format = function() {
		var args = arguments;
		return this.replace(/{(\d+)}/g, function(match, number) {
		  return typeof args[number] != 'undefined'
			  ? args[number]
			  : match
			  ;
		});
	};