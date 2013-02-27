

$(document).ready(function() {
	applyStyles();
	setupPagination();
	setupInputChoices();
	
	$(".ui-widget-header").addClass("rounded-top");
	$(".ui-widget-content").addClass("rounded-bottom");
	$("#heading").removeClass("rounded-top");
	$("button").addClass("rounded-corners");
	
	$( "#helpDialog" ).dialog({ autoOpen: false,  title: 'Contextual Help', modal: true});
	$(".context_help").click(function() {
		$("#helpDialog").css("background-color", "#F0F0F0");
		$("#helpDialog").load("help?helpKey=" + $(this).attr("data-help-key"));
		var h = $(document).height();
		var w = $(document).width();
		$("#helpDialog").dialog( "option", "width", w*0.75 );
		$("#helpDialog").dialog( "option", "height", h*0.60 );
		$("#helpDialog").dialog('open');
	});
});

function setupInputChoices() {
	
	$("#seq1IdOption").click ( function () { 
		$("#seq1").fadeIn(1000);
		$("#seq1_chars").hide();
	} );
	$("#seq1CharsOption").click ( function () { 
		$("#seq1").hide();
		$("#seq1_chars").fadeIn(1000);
	} );
	
	$("#seq2IdOption").click ( function () { 
		$("#seq2").fadeIn(1000);
		$("#seq2_chars").hide();
		$("#seq2_homologs").hide();
	} );
	$("#seq2CharsOption").click ( function () { 
		$("#seq2").hide();
		$("#seq2_chars").fadeIn(1000);
		$("#seq2_homologs").hide();
	} );
	$("#seq2HomologOption").click ( function () { 
		$("#seq2").hide();
		$("#seq2_chars").hide();
		$("#seq2_homologs").fadeIn(1000);
	} );
	
	$(".runPairButton").click( function() {
		var p = $(this).parent().siblings(".principle").attr("data-accession");
		var c = $(this).parent().siblings(".comparison").attr("data-accession");
		$("#seq1").val(p);
		$("#seq2").val(c);
		
		$("#radio1A").click();
		$("#radio1B").click();
		$("#startForm").delay(100).submit();
	});
	
	$(".multiselect").multiselect();
	
}

function textToSpans(jqElement) {
	var character = 0;
	var content = jqElement.html();
	jqElement.html("");
	
	for(i=0; i<content.length; i++) {
		$("<span/>").html(content.charAt(i)).appendTo(jqElement);
	}
}

$(document).ready ( function() {

	$(".qgrsSlice").each (function() {
		var numTetrads = parseInt($(this).attr("data-nt"));
		var t1 = parseInt($(this).attr("data-t1"))-1;
		var t2 = parseInt($(this).attr("data-t2"))-1;
		var t3 = parseInt($(this).attr("data-t3"))-1;
		var t4 = parseInt($(this).attr("data-t4"))-1;
		textToSpans($(this));
		$(this).children("span").slice(t1, t1+numTetrads).addClass("tetrad");
		$(this).children("span").slice(t2, t2+numTetrads).addClass("tetrad");
		$(this).children("span").slice(t3, t3+numTetrads).addClass("tetrad");
		$(this).children("span").slice(t4, t4+numTetrads).addClass("tetrad");
	});
});

function applyStyles() {
	$(".container").addClass("rounded-corners");
	$(".insert").addClass("rounded-corners");
	$(".alert").addClass("rounded-corners");
	
	$(".container").addClass("shadow");
	$(".ui-widget").addClass("shadow");
	
	$("#heading").addClass("shadow");
	$("#infoTable").addClass("rounded-corners");
	
	$('.outputContainer table tbody tr:even').addClass('even');
	$('.outputContainer table tbody tr:odd').addClass('odd');
	
	$('.analysisTable tbody tr:even').addClass('even');
	$('.analysisTable tbody tr:odd').addClass('odd');
	
	$('#infoTable tbody tr:even').addClass('odd');
	$('#infoTable tbody tr:odd').addClass('even');
	
	$('#geneDetailsTable tbody tr:even').addClass('odd');
	$('#geneDetailsTable tbody tr:odd').addClass('even');
	
	$('#statsTable tbody tr:even').addClass('odd');
	$('#statsTable tbody tr:odd').addClass('even');
	
	$('.pairTable tbody tr:even').addClass('odd');
	$('.pairTable tbody tr:odd').addClass('even');
	
	$( "#check" ).button();
	$("#check").click(function() { $("#infoDetails").slideToggle();});
	
	function setFilterLinkText() {
		if( $('#criteriaDetails').is(':visible') ) {
			$("#dbFilterState").val("display");
			$("#checkCriteria").text("Hide Details");
		}
		else {
			$("#dbFilterState").val("none");
			$("#checkCriteria").text("Show Details");
		}
	}
	setFilterLinkText();
	$("#refreshButton" ).button();
	$("#checkCriteria").click(function() { 
		$("#criteriaDetails").slideToggle('slow', function() {
			setFilterLinkText();
		});
	});
	
	$(".container h1 a").click(function () {
		$(this).parent().siblings(".collapsable").slideToggle();
	});

	$("button").button();
	$(".pageNavButton").css("padding", "0").css("font-size", "8pt").css("margin-left", "0.3em").css("position", "relative").css("top", "-5px");
	
	$(".pairTable tbody td").hover(
		function() {
			$(this).siblings().addClass("highlight");
			$(this).addClass("highlight");
		},
		function() {
			$(this).siblings().removeClass("highlight");
			$(this).removeClass("highlight");
		}
	)
	
	$( ".radioContainer" ).buttonset();
	
	
	
	var dbMinAlignmentScore = $("#dbMinAlignmentScore").val();
	$("#dbMinAlignmentScoreSlider").slider({
		value:dbMinAlignmentScore,
		min: 0,
		max: 100,
		step:5,
		slide: function( event, ui ) {
			$( "#dbMinAlignmentScore" ).val( ui.value );
			$( "#dbMinAlignmentScoreValue").html(ui.value.toFixed(0)+"%");
		}
		});
	
	var dbMinNumConservedVal = $("#dbMinNumConserved").val();
	$("#dbMinNumConservedSlider").slider({
		value:dbMinNumConservedVal,
		min: 0,
		max: 10,
		step:1,
		slide: function( event, ui ) {
			$( "#dbMinNumConserved" ).val( ui.value );
			$( "#dbMinNumConservedValue").html(ui.value);
		}
		});
	
	var dbOverallSimilarity = $("#dbOverallSimilarity").val();
	$("#dbOverallSimilaritySlider").slider({
		value:dbOverallSimilarity,
		min: 0.9,
		max: 1,
		step:0.01,
		slide: function( event, ui ) {
			$( "#dbOverallSimilarity" ).val( ui.value );
			$( "#dbOverallSimilarityValue").html(ui.value.toFixed(2));
		}
		});
	
	var dbTotalLengthSimilarity = $("#dbTotalLengthSimilarity").val();
	$("#dbTotalLengthSimilaritySlider").slider({
		value:dbTotalLengthSimilarity,
		min: 0,
		max: 1,
		step:0.05,
		slide: function( event, ui ) {
			$( "#dbTotalLengthSimilarity" ).val( ui.value );
			$( "#dbTotalLengthSimilarityValue").html(ui.value.toFixed(2));
		}
		});
	
	var dbOverlapSimilarity = $("#dbOverlapSimilarity").val();
	$("#dbOverlapSimilaritySlider").slider({
		value:dbOverlapSimilarity,
		min: 0,
		max: 1,
		step:0.05,
		slide: function( event, ui ) {
			$( "#dbOverlapSimilarity" ).val( ui.value );
			$( "#dbOverlapSimilarityValue").html(ui.value.toFixed(2));
		}
		});
	
	var dbTetradSimilarity = $("#dbTetradSimilarity").val();
	$("#dbTetradSimilaritySlider").slider({
		value:dbTetradSimilarity,
		min: 0,
		max: 1,
		step:0.05,
		slide: function( event, ui ) {
			$( "#dbTetradSimilarity" ).val( ui.value );
			$( "#dbTetradSimilarityValue").html(ui.value.toFixed(2));
		}
		});
	
	var dbLoopSimilarity = $("#dbLoopSimilarity").val();
	$("#dbLoopSimilaritySlider").slider({
		value:dbLoopSimilarity,
		min: 0,
		max: 1,
		step:0.05,
		slide: function( event, ui ) {
			$( "#dbLoopSimilarity" ).val( ui.value );
			$( "#dbLoopSimilarityValue").html(ui.value.toFixed(2));
		}
		});
	
	
	var dbGScore1 = $("#dbGScore1").val();
	$("#dbGScore1Slider").slider({
		value:dbGScore1,
		min: 35,
		max: 100,
		step:1,
		slide: function( event, ui ) {
			$( "#dbGScore1" ).val( ui.value );
			$( "#dbGScore1Value").html(ui.value.toFixed(0));
		}
		});
	var dbGScore2 = $("#dbGScore2").val();
	$("#dbGScore2Slider").slider({
		value:dbGScore2,
		min: 35,
		max: 100,
		step:1,
		slide: function( event, ui ) {
			$( "#dbGScore2" ).val( ui.value );
			$( "#dbGScore2Value").html(ui.value.toFixed(0));
		}
		});
	
	var dbMinTetrads1 = $("#dbMinTetrads1").val();
	$("#dbTetrads1Slider").slider({
		value:dbMinTetrads1,
		min: 2,
		max: 5,
		step:1,
		slide: function( event, ui ) {
			$( "#dbMinTetrads1" ).val( ui.value );
			$( "#dbTetrads1Value").html(ui.value.toFixed(0));
		}
		});
	var dbMinTetrads2 = $("#dbMinTetrads2").val();
	$("#dbTetrads2Slider").slider({
		value:dbMinTetrads2,
		min: 2,
		max: 5,
		step:1,
		slide: function( event, ui ) {
			$( "#dbMinTetrads2" ).val( ui.value );
			$( "#dbTetrads2Value").html(ui.value.toFixed(0));
		}
		});
	
	
	
	
	$(".regionButtonSet").buttonset();
	$(".sliderContainer").css("margin", "0.25em");
}

function setupPagination() {
//	var curPage = parseInt($("#originalPageNumber").val());
//	if ( curPage >= parseInt($("#numPages").val())) {
//		$("#next").attr('disabled', true);
//	}
//	if ( curPage == 1 ) {
//		$("#back").attr('disabled', true);
//	}
//	$("#back").click(function () {
//		var pageNumber = parseInt($("#originalPageNumber").val());
//		$("#pageNumber").val(pageNumber-1);
//		$("#paginationForm").submit();
//	});
//	
//	$("#next").click(function () {
//		var pageNumber = parseInt($("#originalPageNumber").val());
//		$("#pageNumber").val(pageNumber+1);
//		$("#paginationForm").submit();
//	});
//	$("#pageNumber").blur(function() {
//		if ($("#pageNumber").val() != $("#originalPageNumber").val()) {
//			$("#paginationForm").submit();
//		}
//	});
	
	
	var totalPages = $("#dbTotalPages").val();
	var pageNumber = $("#dbPageNumber").val();
	if ( totalPages < 2 ) {
		$(".pageNavButton").hide();
	}
	if ( pageNumber == totalPages ) {
		$("#nextPageButton").hide();
		$("#lastPageButton").hide();
	}
	if ( pageNumber == 1 ) {
		$("#previousPageButton").hide();
		$("#firstPageButton").hide();
	}
	
	$("#nextPageButton").click(  function() {
		$("#dbPageNumber").val(parseInt(pageNumber)+1);
		$("#navigationFilterForm").submit();
	});
	$("#lastPageButton").click(  function() {
		$("#dbPageNumber").val(totalPages);
		$("#navigationFilterForm").submit();
	});
	$("#previousPageButton").click(  function() {
		$("#dbPageNumber").val(parseInt(pageNumber)-1);
		$("#navigationFilterForm").submit();
	});
	$("#firstPageButton").click(  function() {
		$("#dbPageNumber").val(1);
		$("#navigationFilterForm").submit();
	});
}


function insertTetradSpan(text, pos, length) {
	var end = pos+length;
	text = text.splice(end, 0, "</span>");
	text = text.splice(pos, 0, "<span class='.tetrad'>");
	return text;
}



