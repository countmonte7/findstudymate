$(document).on('click', '.answer-write input[type=submit]', addAnswer);

function addAnswer(e) {
	e.preventDefault();
	var queryString = $(".answer-write").serialize();
	var url = $(".answer-write").attr("action");
	
	$.ajax({
		type: 'post',
		url: url,
		data: queryString,
		dataType: 'json',
		error: onError,
		success: function(data, status) {
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
		var answerId=url.split("/")[5];
		
		$.ajax({
			type: 'delete',
			url: url,
			dataType: 'json',
			error: function(xhr, status) {				
				console.log("error");
			},
			success: function(data, status) {
				if(data[0].valid) {
					$(".article"+answerId).remove();
					$(".comment-count").text(data[1].countOfAnswer);
				}else {
					alert(data[0].errorMessage);
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
				var answerId = data.id;
				var content = data.contents;
				var questionId = data.question.id;
				if(check==1) {
					cancelUpdateAnswer();
				}
				if(check==0) {
					tempAnswerData = data;
				}
				var a = '';
				a += '<div class="input-group"  style="width: 100%">';
				a += '<textarea class="form-control answerUpdateForm" id="editContent">';
				a += content;
				a += '</textarea>';
				a += '<ul class="article-util-list">';
				a += '<li class="update-confirm-list"><a id="updateAction" href="/api/questions/'+questionId+'/answers/'+answerId+'/update">확인</a></li>'
				a += '<li><a id="cancelUpdateForm" style="cursor:pointer">취소</a></li></ul>'
				a += '</div>';
				$('.qna-comment-slipp-articles .article'+answerId).children('.article-main').html(a);
				check = 1;
			}
		});

		$(document).on('click', '#cancelUpdateForm', cancelUpdateAnswer);

		//댓글 업데이트 취소 시
		function cancelUpdateAnswer() {
			if(check==0) {
				return false;
			}
			
			var content = $(".qna-comment-slipp-articles .input-group .answerUpdateForm").val();
			var questionId = tempAnswerData.question.id;
			var answerId = tempAnswerData.id;
			console.log("questionId : "+questionId+" answerId : "+answerId);
		
					var original = '';
					original += '<div class="article-main" id="answer-main">'
					original +=	'<div class="article-doc comment-doc">'+content+'</div>'
					original += '<div class="article-util">'
					original += '<ul class="article-util-list">'
					original += '<li><a class="link-answer-modify-article" href="/api/questions/'+questionId+'/answers/'+answerId+'/updateForm">수정</a></li>'
					original +=	'<li><a class="link-delete-article" href="/api/questions/'+questionId+'/answers/'+answerId+'">삭제</a></li>"'
					original += '</ul></div>'
					$('.qna-comment-slipp-articles .article'+answerId+' .input-group').html(original);
			check=0;	
		}

		$(document).on('click','#updateAction', updateAnswerAction);
		
		//댓글 업데이트 반영
		function updateAnswerAction(e) {
			e.preventDefault();
			var updatActionBtn = $(this);
			url = updatActionBtn.attr("href");

			var contents = $('#editContent').val();
			$.ajax({
				type: 'post',
				dataType: 'json',
				data: {'contents': contents},
				url: url,
				error: function(status) {
					alert(status);
				},
				success: function(data, status) {
					$('#answer-create-time').text(data.formattedCreateDate);
					var updatedAnswerUrl = '';
					updatedAnswerUrl += '<div class="article-main" id="answer-main">'
					updatedAnswerUrl +=	'<div class="article-doc comment-doc">'+data.contents+'</div>'
					updatedAnswerUrl += '<div class="article-util">'
					updatedAnswerUrl += '<ul class="article-util-list">'
					updatedAnswerUrl += '<li><a class="link-answer-modify-article" href="/api/questions/'+data.question.id+'/answers/'+data.id+'/updateForm">수정</a></li>'
					updatedAnswerUrl +=	'<li><a class="link-delete-article" href="/api/questions/'+data.question.id+'/answers/'+data.id+'">삭제</a></li>'
					updatedAnswerUrl += '</ul></div>'

					$('.qna-comment-slipp-articles .article'+data.id+' .input-group').html(updatedAnswerUrl);
				}
			}); 
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