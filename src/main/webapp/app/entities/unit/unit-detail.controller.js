(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UnitDetailController', UnitDetailController);

    UnitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Unit', 'Fleet', 'Vehicle'];

    function UnitDetailController($scope, $rootScope, $stateParams, entity, Unit, Fleet, Vehicle) {
        var vm = this;

        vm.unit = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:unitUpdate', function(event, result) {
            vm.unit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
