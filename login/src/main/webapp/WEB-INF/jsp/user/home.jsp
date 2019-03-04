<!DOCTYPE html>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<head>
    <title>Admin Page</title>
    <link rel="stylesheet" type="text/css" href="/css/home.css}" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">

    <form method="get" action="/logout">
        <button class="btn btn-md btn-danger btn-block" name="registration"
                type="Submit">Logout
        </button>
    </form>

    <div class="panel-group" style="margin-top:40px">
     <div class="panel-heading" style="text-align: right;">
                <span  th:utext="selectedStockList" align="center" style="solid white;font-size:large">${userName}</span>
            </div>
        <div class="panel panel-primary">
       
            <div class="panel-heading" style="text-align: center;">
                <span th:utext="${userName}" th:utext="selectedStockList" align="center" style="solid white;font-size:xx-large">Selected Stocks</span>
            </div>
            <div class="panel-body">
                <!-- <img th:src="@{/images/tiger.jpg}" class="img-responsive center-block" width="400" height="400"
                     alt="Beer"/> -->
            </div>
            <p class="admin-message-text text-center" th:utext="${adminMessage}"></p>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading"  style="text-align: center;">
                <span th:utext="stockList" align="center" style="solid white;font-size: -webkit-xxx-large"> Current Market</span>
            </div>
            <div class="panel-body">
            	<div class="table-responsive">
				
				 <table class="table">
				 <tr>
                        <th>Stock Symbol</th>
                        <th>Previous Close</th>
                        <th>Price </th>
                        <th>Low</th>
                        <th>High</th>
                        <th>Volume</th>
                        <th>52 WK Low} </th>
                        <th>52 WK High} </th>
                        <th>Open Price</th>
                        <th>PE</th>
                        <th>EPS</th>
                        <th>Purchase</th>
                        
                        <%-- <th>${stock} </th> --%>
                    </tr>
                <c:forEach items="${stockList}" var="stock">
                    <tr>
                        <td>${stock.stockSymbol}</td>
                        <td>${stock.prevClose}</td>
                        <td>${stock.price} </td>
                        <td>${stock.low} </td>
                        <td>${stock.high} </td>
                        <td>${stock.volume} </td>
                        <td>${stock.wkLow} </td>
                        <td>${stock.wkHigh} </td>
                        <td>${stock.openPrice} </td>
                        <td>${stock.pe} </td>
                        <td>${stock.eps} </td>
                        <td>
                   <button type="button" class="btn btn-primary" onclick="hello('${stock.stockSymbol}',${stock.price})">Purchase</button>      
                         </td>
                        
                        <%-- <td>${stock} </td> --%>
                    </tr>
                </c:forEach>

            </table>
            </div>
            </div>
        </div>
    </div>

</div>
<script>
function hello(stockID, lockedPrice ){

	
	if (confirm("Are you sure to purchase this stock!")) {
		  
		  var investment ;
		  while(true){
			  investment= prompt("Please enter you amount", "00.00");
		  if(isNaN(investment)){
			  alert("Sorry that was not a number.")
		  }else{
			  break;
		  }
		  }
		  if(investment>0){
			  
			//  console.log("Going to make ajax call: "+ '{"investment":'+ investment +',"stockID":"STOCK::'+stockID+'", "lockedPrice": '+lockedPrice+'}');
			  
			  $.ajax({
			        url: "/user/purchase",
			        method: "POST",
			        data: '{"investment":'+ investment +',"stockID":"STOCK::'+stockID+'", "lockedPrice": '+lockedPrice+'}',//JSON.stringify(jsonObj),
			        dataType: 'json',
			        contentType: "application/json",
			         success: function(result,status,jqXHR ){
			              alert("Post was succefull with result: "+ result);
			         },
			         error(jqXHR, textStatus, errorThrown){
			             alert("ajax call was failed with error: "+ textStatus);
			         }
			    }); 
		}else
			
		  alert("Invalid price: "+ investment);
		  
	
	} else {
		  console.log("User cancelled purchase");
		} 
	/* //stockID, //lockedPrice,// currentPrice , investment
	var jsonObj='{"bid": 100.33,"stockId":"StockID"}';
	 $.ajax({
	        url: "/user/purchase",
	        method: "POST",
	        data: '{"bid": 100.33,"stockId":"StockID"}',//JSON.stringify(jsonObj),
	        dataType: 'json',
	        contentType: "application/json",
	         success: function(result,status,jqXHR ){
	              alert("Post was succefull with result: "+ result);
	         },
	         error(jqXHR, textStatus, errorThrown){
	             alert("ajax call was failed with error: "+ textStatus);
	         }
	    }); 
} */
}
</script>
</body>
</html>

