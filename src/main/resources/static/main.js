$(function() {
        assign_input_events();
});

function assign_input_events() {
  $("#send-files").off().on('click',send_files);
}

function folder_change(e) {
}

// The count of current uploads
var count;

// The async function doing the uploading
function do_the_sending(corpus_files,file_exts,resolve) {
  for (var i = 0 ; i <corpus_files.length; i++) {
    file_exts.forEach(function(e) {
      if (corpus_files[i].name.endsWith(e)) {
        var reader = new FileReader();
        reader.addEventListener("load", ((name) => { return () => {
          var content = reader.result;
          --count;
          $.ajax({
    	    url: "send",
            type: "POST",
	        data: JSON.stringify({'name':name,'data':content}),
    	    cache: false,
            contentType: false,
	        processData: false,
    	    timeout: 300000,
            enctype: 'multipart/form-data',
          });
	      if (count == 0)
	        return resolve('done')
        }})(corpus_files[i].name), false);
        ++count;
        reader.readAsDataURL(corpus_files[i]);
      }
    });
  }
}

function send_files() {
  // Get files from the file chooser
  var corpus_files = $("#corpus-files")[0].files;
  // Split file extensions on comma
  var file_exts =  $("#file-exts").val().split(',');
  // send all the files that match the extensions
  // count of active uploads
  count = 0;
  new Promise(function(resolve,reject) {
    do_the_sending(corpus_files,file_exts,resolve)
  }).then(() => {
    // Get all the functions
    var functions = []
    $("#corpus_checker_form input:checked").each((index,element) => functions.push(element.value));
    var url = "check_corpus?input=tmp&output=tmp&functions=" + functions.join(",") + "&token=tmp&callback="
    $.ajax({
      url: url,
      type: "GET",
      cache: false,
      contentType: false,
      processData: false,
      timeout: 300000,
      success: function() { $('#check_in_progress_dialogue').modal('show')}
      })
  })

}