//for ck editor...
$(function () {
	var editor = CKEDITOR.replace('editor1',
		      {
		        toolbar :
		            [
		                { name: 'document', items : [ 'Preview' ] },
		                { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
		                { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },
		                { name: 'insert', items : [ 'Table','HorizontalRule','Smiley','SpecialChar','PageBreak'] },
		                        '/',
		                { name: 'styles', items : [ 'Styles','Format' ] },
		                { name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] },
		                { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote' ] },
		                { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
		                { name: 'tools', items : [ 'Maximize','-' ] }

		            ],
		        });
	
	editor.resize( '100%', '500' )
	
  });

$(function() {
	$("#example").dataTable();
});




