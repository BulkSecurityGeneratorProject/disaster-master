(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UnitDialogController', UnitDialogController);

    UnitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Unit', 'Fleet', 'Vehicle'];

    function UnitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Unit, Fleet, Vehicle) {
        var vm = this;

        vm.unit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fleets = Fleet.query();
        vm.vehicles = Vehicle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.unit.id !== null) {
                Unit.update(vm.unit, onSaveSuccess, onSaveError);
            } else {
                Unit.save(vm.unit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:unitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
