var isUpdateFormOpen = false;
var isReanswerFormOpen = false;


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
			$(".qna-comment-slipp-articles").append(template);
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
	

	$(document).on('click','.link-answer-modify-article', updateAnswer)
	
	function updateAnswer(e) {
		e.preventDefault();
		cancelReAnswer();
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
				if(isUpdateFormOpen) {
					cancelUpdateAnswer();
				}
				if(!isUpdateFormOpen) {
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
				$('#editContent').focus();
				isUpdateFormOpen = true;
			}
		});
	}

		$(document).on('click', '#cancelUpdateForm', cancelUpdateAnswer);

		//댓글 업데이트 취소 시
		function cancelUpdateAnswer() {
			if(!isUpdateFormOpen) {
				return false;
			}
			var content = $(".qna-comment-slipp-articles .input-group .answerUpdateForm").val();
			var questionId = tempAnswerData.question.id;
			var answerId = tempAnswerData.id;
		
					var original = '';
					original += '<div class="article-main" id="answer-main">'
					original +=	'<div class="article-doc comment-doc">'+content+'</div>'
					original += '<div class="article-util">'
					original += '<ul class="article-util-list">'
					original += '<li><a class="link-answer-modify-article" href="/api/questions/'+questionId+'/answers/'+answerId+'/updateForm">수정</a></li>'
					original +=	'<li><a class="link-delete-article" href="/api/questions/'+questionId+'/answers/'+answerId+'">삭제</a></li>'
					original += '<li><a class="link-reanswer-article" id="reanswer-article{4}" href="/api/questions/'+questionId+'/answers/'+answerId+'/reanswers">답글</a>';
                    original += '</li>'
					original += '</ul></div>'
					$('.qna-comment-slipp-articles .article'+answerId+' .input-group').html(original);
					isUpdateFormOpen = false;	
		}

		$(document).on('click','#updateAction', updateAnswerAction);
		
		//댓글 업데이트 반영
		function updateAnswerAction(e) {
			e.preventDefault();
			var updatActionBtn = $(this);
			var url = updatActionBtn.attr("href");

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
					var questionId = data.question.id;
					var answerId = data.id;
					updatedAnswerUrl += '<div class="article-main" id="answer-main">'
					updatedAnswerUrl +=	'<div class="article-doc comment-doc">'+data.contents+'</div>'
					updatedAnswerUrl += '<div class="article-util">'
					updatedAnswerUrl += '<ul class="article-util-list">'
					updatedAnswerUrl += '<li><a class="link-answer-modify-article" href="/api/questions/'+questionId+'/answers/'+answerId+'/updateForm">수정</a></li>'
					updatedAnswerUrl +=	'<li><a class="link-delete-article" href="/api/questions/'+questionId+'/answers/'+answerId+'">삭제</a></li>'
					updatedAnswerUrl += '<li><a class="link-reanswer-article" id="reanswer-article{4}" href="/api/questions/'+questionId+'/answers/'+answerId+'/reanswers">답글</a>';
                    updatedAnswerUrl += '</li>'
					updatedAnswerUrl += '</ul></div>'

					$('.qna-comment-slipp-articles .article'+data.id+' .input-group').html(updatedAnswerUrl);
				}
			}); 
		}

	$(document).on('click', '.link-reanswer-article', reanswerForm);


	function reanswerForm(e) {
		e.preventDefault();
		var reanswerBtn = $(this);
		var url = reanswerBtn.attr("href");
		console.log(url);
		$.ajax({
			type: 'get',
			dataType: 'json',
			url: url,
			success: function(data) {
				openAnswerForm(data);
			},
			error: function(status) {
				alert(status);
			}
		});

	}

	function openAnswerForm(data) {
		if(isReanswerFormOpen) {
			cancelReAnswer();
		}
		if(isUpdateFormOpen) {
			cancelUpdateAnswer();
		}
		var answerForm = '';
		answerForm += '<form class="reanswer-write" method="post" action="/api/questions/'+data.question.id+'/answers/">';
		answerForm += '<div class="form-group" style="padding: 14px;">';
		answerForm += '<input type="hidden" name="id" value="'+data.id+'" />'
		answerForm += '<textarea class="form-control" placeholder="내용을 입력하세요." id="answer-contents" name="contents"></textarea>';
		answerForm += '</div>';
		answerForm += '<div><input type="submit" class="btn btn-success pull-right" id="reAnswerBtn" value="댓글등록">';
		answerForm += '<a style="cursor:pointer;" onClick="cancelReAnswer()">취소</a></div>'
		answerForm += '<div class="clearfix" />';	
		answerForm += '</form>';
		isReanswerFormOpen = true;
		$('.article'+data.id).after(answerForm);
		$('#answer-contents').focus();
	}

	function cancelReAnswer() {
		$('.reanswer-write').remove();
		isReanswerFormOpen = false;
		
	}

	$(document).on('click', "#reAnswerBtn", addReAnswer);

	function addReAnswer(e) {
		e.preventDefault();
		var url = $('.reanswer-write').attr('action');
		var queryString = $('.reanswer-write').serialize();
		$.ajax({
			type: 'post',
			data: queryString,
			dataType: 'json',
			url: url,
			success: function(data) {
				var reanswerTemplate = $('#answerTemplate').html();
				var template = reanswerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents,
					data.question.id, data.id);
				$('.article'+data.reparentNo).after(template);
			}, error: function() {
				alert('error');
			}
		});
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