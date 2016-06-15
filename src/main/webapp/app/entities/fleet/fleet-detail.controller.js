(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('FleetDetailController', FleetDetailController);

    FleetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fleet', 'Unit', 'Service'];

    function FleetDetailController($scope, $rootScope, $stateParams, entity, Fleet, Unit, Service) {
        var vm = this;

        vm.fleet = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:fleetUpdate', function(event, result) {
            vm.fleet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
