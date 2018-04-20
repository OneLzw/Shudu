function valueChange(obj) {
		var value = obj.value;
		if (value.length > 0) {
			value = value.substring(value.length-1 , value.length);
			if (value == "0") {
			value = "";
		}
		obj.value = value;
	}
	checkValue(obj);
}

function checkValue (obj) {
	var commonLine = checkLine(obj);
	var commonRow = checkRow(obj);
	var littleNine = checkLittleNine(obj);
	if (commonLine == 1 || commonRow == 1 || littleNine == 1) {
		obj.style.backgroundColor="rgb(254, 112, 120)";
	}
	
//	if (xhasCommon == 0) {
//		curxObj.style.backgroundColor="#fff";
//	}
//	if (yhasCommon == 0) {
//		curyObj.style.backgroundColor="#fff";
//	}
//	if (x == y && x == i) {
//		if (xhasCommon == 1 || yhasCommon == 1) {
//			curxObj.style.backgroundColor="rgb(254, 112, 120)";
//		}
//	}
// 		curObj.style.backgroundColor="rgb(254, 112, 120)";
}

function checkLittleNine (x , y) {
	var xStart = 1;
	var xEnd = 1;
	var yStart = 1;
	var yEnd = 1;
	if (x <= 3) {
		xEnd = 3;
    }else if (x >= 4 && x <= 6) {
    	xStart = 4;
    	xEnd = 6;
    } else {
    	xStart = 7;
    	xEnd = 9;
    }
	
	if (y <= 3) {
		yEnd = 3;
    }else if (y >= 4 && y <= 6) {
    	yStart = 4;
    	yEnd = 6;
    } else {
    	yStart = 7;
    	yEnd = 9;
    }
	var curObj;
	var nextObj;
	var xhasCommon = 0;
	var specialCommon = 0;
	for (var i = xStart ; i <=xEnd ; i++) {
		for (var j = yStart ; j <=yEnd ; j++ ) {
			curObj = document.getElementById("" + i + j);
			var hasCommon = 0;
			if (curObj.value == 0) {
				continue;
			}
			for (var m = xStart ; m <=xEnd ; m++) {
				for (var n = yStart ; n <=yEnd ; n++ ) {
					if (i == m && j == n) {
						continue;
					}
					nextObj = document.getElementById("" + m + n);
					if (nextObj.value == 0) {
						
					}
					if (curObj.value == nextObj.value) {
						curObj.style.backgroundColor="rgb(254, 112, 120)";
						hasCommon = 1;
					}
				}
			}
			if (i == x && j == y) {
				specialCommon = hasCommon;
			}
			if (hasCommon == 0) {
				curObj.style.backgroundColor="#fff";
			}
		}
	}	
	return specialCommon;
}

function jisuan () {
    var rows = "";
    var longnumber = "";
    var haveNumber = null;
    for (var i = 1 ; i < 10 ; i++) {
        rows =  document.getElementsByName(i);
        for (var j = 0; j < rows.length ; j++) {
            var number = rows[j].value;
            if (number == "") {
                number = 0;
            } else {
                haveNumber = document.getElementById(""+ i + (j + 1));
                haveNumber.disabled = "disabled";
                haveNumber.style.backgroundColor  = "#3fad";
            }
            if (j == rows.length - 1) {
                longnumber += number;
            } else {
                longnumber += number + ",";
            }
        }

        longnumber += ";";
    }

    $.ajax({
        type:'post',
        url:'countnine',
        // data:$("#myform").serialize(),
        data: {
            "longnumber" : longnumber
        },
        cache:false,
        dataType:'json',
        success:function(data){
            alert(data.msg);
            if (data.state) {
            	var dataArray = data.data;
            	for (var i = 0 ; i < 9 ; i++) {
            		for (var j = 0 ; j < 9 ; j++) {
            			document.getElementById(""+ (i+1) + (j+1)).value = dataArray[i][j];
            		}
            	}
            }
        }
    });
}
function revert () {
    for (var i = 1 ; i < 10 ; i++) {
        var rows =  document.getElementsByName(i);
        for (var j = 0; j < rows.length ; j++) {
            var haveNumber = rows[j];
            //haveNumber.value = "";
            haveNumber.disabled = "";
            haveNumber.style.backgroundColor  = "#fff";
        }
    }
}

function clean () {
	 for (var i = 1 ; i < 10 ; i++) {
         var rows =  document.getElementsByName(i);
         for (var j = 0; j < rows.length ; j++) {
             var haveNumber = rows[j];
             haveNumber.value = "";
             haveNumber.disabled = "";
             haveNumber.style.backgroundColor  = "#fff";
         }
     }
}

//该点在该行，是否有相同的值
function checkLine (obj) {
	var id = obj.id;
	var x = id.substring(0,1);
	var y = id.substring(1,2);
	var value = obj.value;
	var hasCommon = 0;
	var curObj;
	for (var i = 1 ; i < 10 ; i++ ) {
		if (i == y) {
			continue;
		}
		curObj = document.getElementById("" + x + i);
		if (curObj.value == 0) {
			continue;
		}
		if (curObj.value == value) {
			hasCommon == 1;
			break;
		}
	}
	return hasCommon;
}

function checkRow (obj) {
	var id = obj.id;
	var x = id.substring(0,1);
	var y = id.substring(1,2);
	var value = obj.value;
	var hasCommon = 0;
	var curObj;
	for (var i = 1 ; i < 10 ; i++ ) {
		if (i == x) {
			continue;
		}
		curObj = document.getElementById("" + i + y);
		if (curObj.value == 0) {
			continue;
		}
		if (curObj.value == value) {
			hasCommon == 1;
			break;
		}
	}
	return hasCommon;
}

function checkLittleNine (obj) {
	var id = obj.id;
	var x = id.substring(0,1);
	var y = id.substring(1,2);
	var value = obj.value;
	var xStart = 1;
	var xEnd = 1;
	var yStart = 1;
	var yEnd = 1;
	if (x <= 3) {
		xEnd = 3;
    }else if (x >= 4 && x <= 6) {
    	xStart = 4;
    	xEnd = 6;
    } else {
    	xStart = 7;
    	xEnd = 9;
    }
	
	if (y <= 3) {
		yEnd = 3;
    }else if (y >= 4 && y <= 6) {
    	yStart = 4;
    	yEnd = 6;
    } else {
    	yStart = 7;
    	yEnd = 9;
    }
	
	var curObj;
	var hasCommon = 0;
	for (var i = xStart ; i <=xEnd ; i++) {
		for (var j = yStart ; j <=yEnd ; j++ ) {
			if (i == x && j == y) {
				continue;
			}
			curObj = document.getElementById("" + i + j);
			if (curObj.value == 0) {
				continue;
			}
			if (curObj.value == value) {
				hasCommon = 1;
				break;
			}
		}
	}
	return hasCommon;
}


















