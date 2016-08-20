<?php
$databasehost = "localhost";
$databasename = "smokesig_mobiletrolley";
$databaseusername ="smokesig_mobtrol";
$databasepassword = "9zUAF06J]i}4";

$con = mysql_connect($databasehost,$databaseusername,$databasepassword) or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

$images = array();

//--------------------------------------OUTLETS BRANDS-----------------------------------------------------//
$query_outlets = "SELECT logo FROM hdjgf_shops_brands";
$results_outlets = mysql_query($query_outlets);



//Get rows
while($r = mysql_fetch_assoc($results_outlets)) {
	$images[] = $r;
}


//--------------------------------------Product Image-----------------------------------------------------//

$query_outlet = "SELECT image FROM hdjgf_shops_stocks_brands";
$results_outlet = mysql_query($query_outlet);
//Get rows
while($r = mysql_fetch_assoc($results_outlet)) {
	$images[] = $r;
}

print json_encode($images);
mysql_close();

?>