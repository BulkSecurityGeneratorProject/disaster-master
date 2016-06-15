(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ServiceController', ServiceController);

    ServiceController.$inject = ['$scope', '$state', 'Service'];

    function ServiceController ($scope, $state, Service) {
        var vm = this;
        
        vm.services = [];

        loadAll();

        function loadAll() {
            Service.query(function(result) {
                vm.services = result;
            });
        }
    }
})();
