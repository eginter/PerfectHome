// userController.js

var app = angular.module('ngApp');

app.controller('userController', function($scope, $location, userService, $rootScope, authenticationService, zillowService){
	console.log('User controller');
	$rootScope.bodyClass = 'container';
	$scope.user = {};
	$scope.activeHome = {};
	$scope.currentHomeUserId = null;
	
	$scope.remove = function(homeUserId) {
		console.log(homeUserId)
		userService.deleteHome(homeUserId)
		.then(function(){
			userService.getUser(authenticationService.currentUser().id)
			.then(function(response){
				$scope.user =  response;
				console.log($scope.user)
			})
		})
	}
	
	$scope.addHome = function(home){
		console.log(home)
		userService.addHome(authenticationService.currentUser().id, home)
		.then(function(){
			userService.getUser(authenticationService.currentUser().id)
			.then(function(response){
				$scope.user =  response;
				console.log($scope.user)
			})
		})
	}
	
	
	$scope.click = function(zillowId, HomeUserId){
		zillowService.getZillowInfo(zillowId)
		.then(function(response){
			console.log(response)
			$scope.activeHome = response;
			$scope.currentHomeUserId = HomeUserId;
		})
	}
	
	
    if (authenticationService.isLoggedIn()){
       userService.getUser(authenticationService.currentUser().id)
       .then(function(response){
           $scope.user =  response;
           console.log($scope.user)
         });
        
       }
	
	var placeSearch, autocomplete, address, zillowSearchAddress;
    
	var componentForm = {
      street_number: 'short_name',
      route: 'long_name',
      locality: 'long_name',
      administrative_area_level_1: 'short_name',
      country: 'long_name',
      postal_code: 'short_name'
    };
	
    
    function fillInAddress() {
        // Get the place details from the autocomplete object.
        var place = $scope.autocomplete.getPlace();
        // Parse address compenents out of place.
        
        console.log(place);
        
        address = place.address_components;

        var addressNum = address[0].long_name;
        var street = address[1].long_name;
        var city = address[2].long_name;
        var state = address[4].short_name;
        if (state.length > 2) {
        	state = address[5].short_name;
        }

        zillowSearchAddress = makeApiString(addressNum, street, city, state);

    }
    
    // Bias the autocomplete object to the user's geographical location,
    // as supplied by the browser's 'navigator.geolocation' object.
    $scope.geolocate = function() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var geolocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
          var circle = new google.maps.Circle({
            center: geolocation,
            radius: position.coords.accuracy
          });
          $scope.autocomplete.setBounds(circle.getBounds());
        });
      }
    }

    var makeApiString = function(number, street, city, state) {
      number = number.replace(/ /g , '+');
      state = state.replace(/ /g , '+');
      street = street.replace(/ /g , '+');
      city = city.replace(/ /g , '+');
      var string = 'http://www.zillow.com/webservice/GetDeepSearchResults' +
      '.htm?zws-id=X1-ZWz1fhzcpm7ymj_4zss3&address=' + number + '+' +
      street + '&citystatezip='+ city +'%2C+'+ state;
      console.log(string);
      userService.getHomeZpid(string)
      		.then(function(response){
      			console.log(response)
      			$scope.zillowResult = response;
      			$scope.activeHome = response;
      			$scope.currentHomeUserId = null;
      			});
    }	
	
    $scope.autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),{types: ['geocode']});
    $scope.autocomplete.addListener('place_changed', fillInAddress);

});
