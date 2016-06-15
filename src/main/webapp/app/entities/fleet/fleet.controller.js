(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('FleetController', FleetController);

    FleetController.$inject = ['$scope', '$state', 'Fleet'];

    function FleetController ($scope, $state, Fleet) {
        var vm = this;
        
        vm.fleets = [];

        loadAll();

        function loadAll() {
            Fleet.query(function(result) {
                vm.fleets = result;
            });
        }
    }
})();
