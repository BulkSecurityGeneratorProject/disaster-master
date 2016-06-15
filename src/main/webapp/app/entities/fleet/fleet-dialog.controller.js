(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('FleetDialogController', FleetDialogController);

    FleetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fleet', 'Unit', 'Service'];

    function FleetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fleet, Unit, Service) {
        var vm = this;

        vm.fleet = entity;
        vm.clear = clear;
        vm.save = save;
        vm.units = Unit.query();
        vm.services = Service.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fleet.id !== null) {
                Fleet.update(vm.fleet, onSaveSuccess, onSaveError);
            } else {
                Fleet.save(vm.fleet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:fleetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
