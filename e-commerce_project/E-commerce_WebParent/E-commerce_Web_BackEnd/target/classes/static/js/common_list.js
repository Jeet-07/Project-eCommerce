function clearFilter() {
	window.location = moduleURL;	
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	$("#yesButton").attr("href", link.attr("href"));	
	$("#modalBody").text("Are you sure you want to delete this "
							 + entityName + " ID " + entityId + "?");
	$("#confirmDeleteModal").modal('show');
}