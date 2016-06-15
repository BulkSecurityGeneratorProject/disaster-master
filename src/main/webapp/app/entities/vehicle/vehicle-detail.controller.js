(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('VehicleDetailController', VehicleDetailController);

    VehicleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Vehicle', 'Unit'];

    function VehicleDetailController($scope, $rootScope, $stateParams, entity, Vehicle, Unit) {
        var vm = this;

        vm.vehicle = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:vehicleUpdate', function(event, result) {
            vm.vehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
