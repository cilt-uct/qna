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
    
    function change_view(select,form,current_selected) {
    	document.location=form.action+"?viewtype="+select.options[select.selectedIndex].value;			    	
    }

    // View select on question list screen
    function init_view_select(select_id, form_id, options_size, current_selected) {
    	var select = document.getElementById(select_id);
    	var form = document.getElementById(form_id);
		select.onchange = function () { change_view(select,form,current_selected);};
    }

    function  toggle_add_questions(link_id,icon_id,div_id) {
	   	toggle_visibility(link_id);
    	toggle_visibility(icon_id);
    	toggle_visibility(div_id);
    }
    
    // Add an answer in answers screen 
    function init_add_question_toggle(link_id,icon_id,div_id) {
    	var link = document.getElementById(link_id);
    	link.href= "#";
    	link.onclick = function() { toggle_add_questions(link_id,icon_id,div_id);};
    	var icon = document.getElementById(link_id);
    	icon.onclick = function() { toggle_add_questions(link_id,icon_id,div_id);};
    }
    

    