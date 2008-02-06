	// Toggle visibility of an element
    function toggle_visibility(id) {
       if (document.getElementById(id).style.display == '') 
       	document.getElementById(id).style.display = 'none';
       else 
       	document.getElementById(id).style.display = '';
	}
	
	// Toggle visibility of questions of a certain entry id 
	function toggle_questions(entry_id) {
    	var element = document.getElementById(entry_id);
 		var rows = document.getElementsByTagName("tr");
				
		var expr = new RegExp(element.id +"question-entry:([0-9]*):$"); 
 				
		for (i=0;i<rows.length;i++) {
			if (!(rows[i].id == "")) {
				if(rows[i].id.match(expr)) {
					toggle_visibility(rows[i].id);
				}
			}
		}
    }
    
    function init_questions_toggle(element_id,entry_id) {
    	var element = document.getElementById(element_id);
    	element.onclick = function() { toggle_questions(entry_id);};
    }