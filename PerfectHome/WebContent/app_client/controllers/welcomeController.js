// welcomeController.js

var app = angular.module('ngApp');

app.controller('welcomeController', function($scope, $rootScope){
	console.log('Welcome Controller');
	$rootScope.bodyClass = 'cd-fixed-bg cd-bg-2';

});
