if (window.console) {
  //alert("hi");
  console.log("Welcome to your Play application's JavaScript!");
  

  var autoLoad = setInterval(
  function ()
  {
	  //alert("hi");
	  $('#load_post').load('next').fadeIn("slow");
  }, 10000); // refresh page every 10 seconds
}
