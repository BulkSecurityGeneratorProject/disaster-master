(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('VehicleDialogController', VehicleDialogController);

    VehicleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vehicle', 'Unit'];

    function VehicleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vehicle, Unit) {
        var vm = this;

        vm.vehicle = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.units = Unit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vehicle.id !== null) {
                Vehicle.update(vm.vehicle, onSaveSuccess, onSaveError);
            } else {
                Vehicle.save(vm.vehicle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:vehicleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreated = false;
        vm.datePickerOpenStatus.dateLastAccessed = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
