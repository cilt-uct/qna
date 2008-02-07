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
    
    // Used in questions list
    function init_questions_toggle(element_id,entry_id) {
    	var element = document.getElementById(element_id);
    	element.onclick = function() { toggle_questions(entry_id);};
    }
    
    function  toggle_add_questions(link_id,icon_id,div_id) {
	   	toggle_visibility(link_id);
    	toggle_visibility(icon_id);
    	toggle_visibility(div_id);
    }
    
    function init_add_question_toggle(link_id,icon_id,div_id) {
    	var link = document.getElementById(link_id);
    	link.href= "#";
    	link.onclick = function() { toggle_add_questions(link_id,icon_id,div_id);};
    	var icon = document.getElementById(link_id);
    	icon.onclick = function() { toggle_add_questions(link_id,icon_id,div_id);};
    }