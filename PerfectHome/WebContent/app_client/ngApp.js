// ngApp.js

var app = angular.module('ngApp', ['ngRoute','ngAnimate','ui.bootstrap'])
.config(function($routeProvider){
  $routeProvider
    .when('/', {
       templateUrl: 'public/views/home.view.html',
       controller: 'welcomeController'
    })
//    .when('/', {
//    	templateUrl: 'public/views/login.view.html',
//    	controller: 'loginController'
//    })
    .when('/login', {
      templateUrl: 'public/views/login.view.html',
      controller: 'loginController'
    })
    .when('/register', {
      templateUrl: 'public/views/register.view.html',
      controller: 'registerController'
    })
    .when('/user', {
      templateUrl: 'public/views/user.view.html',
      controller: 'userController'
    })
    .otherwise({
      templateUrl: 'public/views/_404_error.html'
    })
});
